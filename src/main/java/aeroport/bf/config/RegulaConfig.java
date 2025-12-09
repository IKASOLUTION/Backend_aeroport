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
        // Crée un ApiClient et configure l'URL de l'API en ligne
        ApiClient client = new ApiClient();
        client.setBasePath(regulaApiUrl);
        client.setDebugging(false);
        client.setVerifyingSsl(true);

        // Crée l'API avec le client configuré
        DocumentReaderApi api = new DocumentReaderApi(client);

        // Lire le fichier de licence et définir la licence
        byte[] licenseBytes = Files.readAllBytes(licenseResource.getFile().toPath());
        api.setLicense(licenseBytes); // ou api.withLicense(Base64) si tu veux en string

        System.out.println("Licence Regula initialisée : " + (api.getLicense() != null));

        return api;
    }
}
