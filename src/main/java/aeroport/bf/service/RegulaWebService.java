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
    
    // Configurations possibles pour Regula - MISE À JOUR
    private static final String[][] POSSIBLE_CONFIGS = {
        {"http://localhost:5000", "/process"},
        {"http://localhost:5000", "/api/v1/process"},
        {"http://localhost:5000", "/v1/process"},
        {"http://localhost:5000", "/api/v2/process"},
        {"http://localhost:5000", "/v2/process"},
        {"http://localhost:5000", "/api/process"},
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
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                
                // Payload minimal pour tester
                Map<String, Object> testBody = new HashMap<>();
                List<Map<String, Object>> images = new ArrayList<>();
                testBody.put("images", images);
                
                HttpEntity<Map<String, Object>> request = new HttpEntity<>(testBody, headers);
                
                try {
                    ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, request, String.class);
                    // Si 200, c'est bon
                    this.workingBaseUrl = baseUrl;
                    this.workingEndpoint = endpoint;
                    System.out.println("✓ Configuration trouvée (200): " + fullUrl);
                    return;
                } catch (HttpClientErrorException e) {
                    int statusCode = e.getStatusCode().value();
                    
                    // 400 = endpoint existe mais payload invalide (c'est acceptable)
                    if (statusCode == 400) {
                        this.workingBaseUrl = baseUrl;
                        this.workingEndpoint = endpoint;
                        System.out.println("✓ Configuration trouvée (400 - endpoint valide): " + fullUrl);
                        return;
                    }
                    
                    // 403 = problème de licence mais endpoint existe
                    if (statusCode == 403) {
                        this.workingBaseUrl = baseUrl;
                        this.workingEndpoint = endpoint;
                        System.out.println("⚠ Configuration trouvée (403 - vérifier licence): " + fullUrl);
                        return;
                    }
                    
                    // 404 = endpoint n'existe pas
                    if (statusCode == 404) {
                        System.out.println("  → 404 Not Found");
                    } else {
                        System.out.println("  → HTTP " + statusCode);
                    }
                }
                
            } catch (ResourceAccessException e) {
                System.out.println("  → Connexion refusée");
            } catch (Exception e) {
                System.out.println("  → Erreur: " + e.getMessage());
            }
        }
        
        System.err.println("✗ ERREUR: Aucune configuration Regula valide trouvée!");
        System.err.println("Le serveur Regula semble tourner sur http://localhost:5000 mais aucun endpoint API n'a été trouvé.");
        System.err.println("Vérifiez la documentation de votre version de Regula.");
    }

    /**
     * Vérifie un document d'identité
     */
    public String verifyDocument(byte[] imageBytes) {
        if (workingBaseUrl == null || workingEndpoint == null) {
            throw new RuntimeException(
                "API Regula non disponible. " +
                "Vérifiez que le serveur est démarré sur le port 5000. " +
                "URL testée: http://localhost:5000"
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
                    "La licence doit être placée dans: C:\\Program Files (x86)\\Regula\\Document Reader Web API\\extBin\\win_x86\\regula.license " +
                    "Puis redémarrez le service: .\regdocreadersvc.exe restart " +
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
            diag.append("Le serveur Regula tourne sur http://localhost:5000\n");
            diag.append("mais aucun endpoint API valide n'a été trouvé.\n\n");
            diag.append("Configurations testées:\n");
            for (String[] config : POSSIBLE_CONFIGS) {
                diag.append("  - ").append(config[0]).append(config[1]).append("\n");
            }
            diag.append("\nSolutions possibles:\n");
            diag.append("1. Vérifier la documentation de votre version de Regula\n");
            diag.append("2. Chercher dans les logs le chemin de l'API\n");
            diag.append("3. Ouvrir http://localhost:5000 dans un navigateur\n");
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