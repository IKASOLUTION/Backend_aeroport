package aeroport.bf.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Service
public class RegulaWebService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // Configurations possibles pour Regula
    private static final String[][] POSSIBLE_CONFIGS = {
        {"http://localhost:5000", "/api/v1/process"},
        {"http://localhost:5000", "/api/process"},
        {"http://localhost:5000", "/process"},
        {"http://localhost:5000", "/v1/process"},
        {"http://localhost:5000", "/"},
        {"http://localhost:5000", "/api"},
        {"http://localhost:8080", "/api/v1/process"},
        {"http://localhost:8080", "/process"}
    };
    
    private String workingBaseUrl;
    private String workingEndpoint;

    @PostConstruct
    public void init() {
        detectWorkingConfiguration();
    }

    /**
     * Détecte automatiquement la configuration qui fonctionne
     */
    private void detectWorkingConfiguration() {
        System.out.println("=== Détection de la configuration Regula ===");
        
        for (String[] config : POSSIBLE_CONFIGS) {
            String baseUrl = config[0];
            String endpoint = config[1];
            String fullUrl = baseUrl + endpoint;
            
            try {
                System.out.println("Test: " + fullUrl);
                
                // Test avec OPTIONS pour voir si l'endpoint existe
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                
                // Payload minimal pour tester
                Map<String, Object> testBody = new HashMap<>();
                List<Map<String, Object>> images = new ArrayList<>();
                testBody.put("images", images);
                
                HttpEntity<Map<String, Object>> request = new HttpEntity<>(testBody, headers);
                
                try {
                    restTemplate.postForEntity(fullUrl, request, String.class);
                    // Si pas d'exception, c'est bon
                    this.workingBaseUrl = baseUrl;
                    this.workingEndpoint = endpoint;
                    System.out.println("✓ Configuration trouvée: " + fullUrl);
                    return;
                } catch (HttpClientErrorException e) {
                    // 400 = endpoint existe mais payload invalide (c'est bon)
                    if (e.getStatusCode().value() == 400) {
                        this.workingBaseUrl = baseUrl;
                        this.workingEndpoint = endpoint;
                        System.out.println("✓ Configuration trouvée (400 OK): " + fullUrl);
                        return;
                    }
                    // 404 = endpoint n'existe pas
                    System.out.println("  → 404 Not Found");
                }
                
            } catch (ResourceAccessException e) {
                System.out.println("  → Connexion refusée (serveur non démarré?)");
            } catch (Exception e) {
                System.out.println("  → Erreur: " + e.getMessage());
            }
        }
        
        System.err.println("✗ ERREUR: Aucune configuration Regula valide trouvée!");
        System.err.println("Vérifiez que le serveur Regula est démarré.");
        System.err.println("Ports testés: 5000, 8080");
    }

    /**
     * Vérifie un document d'identité
     */
    public String verifyDocument(byte[] imageBytes) {
        if (workingBaseUrl == null || workingEndpoint == null) {
            throw new RuntimeException(
                "API Regula non disponible. " +
                "Vérifiez que le serveur est démarré sur le port 5000 ou 8080. " +
                "Commande Docker: docker run -p 8080:8080 regulaforensics/docreader"
            );
        }
        
        String url = workingBaseUrl + workingEndpoint;
       
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        
        // Construction du payload Regula
        List<Map<String, Object>> images = new ArrayList<>();
        Map<String, Object> imageData = new HashMap<>();
        imageData.put("data", Base64.getEncoder().encodeToString(imageBytes));
        imageData.put("light", 0); // 0=White, 6=IR, etc.
        images.add(imageData);
        
        body.put("images", images);
        
        // Paramètres de traitement
        Map<String, Object> processParam = new HashMap<>();
        processParam.put("scenario", "FullProcess");
        body.put("processParam", processParam);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String errorBody = e.getResponseBodyAsString();
            
            // Gestion spécifique de l'erreur de licence
            if (e.getStatusCode().value() == 403 && errorBody.contains("Bad license")) {
                throw new RuntimeException(
                    "Licence Regula invalide ou manquante. " +
                    "Solutions: 1) Obtenir une licence d'essai sur regulaforensics.com, " +
                    "2) Vérifier la configuration du serveur Regula, " +
                    "3) Monter le fichier regula.license dans le conteneur Docker. " +
                    "Erreur: " + errorBody,
                    e
                );
            }
            
            String errorMsg = String.format(
                "Erreur API Regula [%s]: %s",
                e.getStatusCode(),
                errorBody
            );
            throw new RuntimeException(errorMsg, e);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de communication avec Regula: " + e.getMessage(), e);
        }
    }

    /**
     * Vérifie un document avec scénario personnalisé
     */
    public String verifyDocument(byte[] imageBytes, String scenario) {
        if (workingBaseUrl == null || workingEndpoint == null) {
            throw new RuntimeException("API Regula non disponible");
        }
        
        String url = workingBaseUrl + workingEndpoint;
       
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        
        List<Map<String, Object>> images = new ArrayList<>();
        Map<String, Object> imageData = new HashMap<>();
        imageData.put("data", Base64.getEncoder().encodeToString(imageBytes));
        imageData.put("light", 0);
        images.add(imageData);
        
        body.put("images", images);
        
        Map<String, Object> processParam = new HashMap<>();
        processParam.put("scenario", scenario);
        body.put("processParam", processParam);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Erreur: " + e.getMessage(), e);
        }
    }

    /**
     * Vérifie si l'API est disponible
     */
    public boolean isApiAvailable() {
        return workingBaseUrl != null && workingEndpoint != null;
    }

    /**
     * Obtient l'URL complète de l'API
     */
    public String getApiUrl() {
        if (workingBaseUrl == null || workingEndpoint == null) {
            return "Non configuré";
        }
        return workingBaseUrl + workingEndpoint;
    }

    /**
     * Diagnostics complets
     */
    public String getDiagnostics() {
        StringBuilder diag = new StringBuilder();
        diag.append("=== Diagnostics Regula API ===\n\n");
        
        if (isApiAvailable()) {
            diag.append("✓ API disponible\n");
            diag.append("URL: ").append(getApiUrl()).append("\n");
        } else {
            diag.append("✗ API non disponible\n\n");
            diag.append("Configurations testées:\n");
            for (String[] config : POSSIBLE_CONFIGS) {
                diag.append("  - ").append(config[0]).append(config[1]).append("\n");
            }
            diag.append("\nSolutions possibles:\n");
            diag.append("1. Vérifier que Regula est démarré\n");
            diag.append("2. Si Docker: docker run -p 8080:8080 regulaforensics/docreader\n");
            diag.append("3. Vérifier les logs du serveur Regula\n");
        }
        
        return diag.toString();
    }

    /**
     * Force la redétection de la configuration
     */
    public void refreshConfiguration() {
        detectWorkingConfiguration();
    }
}