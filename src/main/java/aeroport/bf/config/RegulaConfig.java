package aeroport.bf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.regula.documentreader.webclient.ApiClient;
import com.regula.documentreader.webclient.api.DocumentReaderApi;

import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class RegulaConfig {

    @Value("${regula.api.url}")
    private String regulaApiUrl;

    @Value("${regula.license.path}")
    private Resource licenseResource;

    @Bean
    public DocumentReaderApi documentReaderApi() throws IOException {
        // Créer le client avec l'URL configurée
        ApiClient client = new ApiClient();
        client.setBasePath(regulaApiUrl);
        client.setDebugging(false);
        client.setVerifyingSsl(true);

        // Créer l'API
        DocumentReaderApi api = new DocumentReaderApi(client);

        // Charger la licence depuis le fichier
        if (licenseResource.exists()) {
            byte[] licenseBytes = Files.readAllBytes(licenseResource.getFile().toPath());
            api.setLicense(licenseBytes);
            System.out.println("✓ Licence Regula initialisée et API configurée avec URL : " + regulaApiUrl);
        } else {
            System.err.println("⚠️ Fichier de licence introuvable : " + licenseResource.getDescription());
        }

        return api;
    }
}
