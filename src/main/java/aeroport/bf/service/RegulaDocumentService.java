package aeroport.bf.service;

import org.bouncycastle.asn1.isismtt.x509.ProfessionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.regula.documentreader.webclient.ApiException;
import com.regula.documentreader.webclient.api.DocumentReaderApi;
import com.regula.documentreader.webclient.model.ImageData;
import com.regula.documentreader.webclient.model.ProcessParams;
import com.regula.documentreader.webclient.model.ProcessRequest;
import com.regula.documentreader.webclient.model.ProcessRequestImage;
import com.regula.documentreader.webclient.model.ProcessSystemInfo;
import com.regula.documentreader.webclient.model.AuthParams;
import com.regula.documentreader.webclient.model.ext.RecognitionResponse;

import aeroport.bf.dto.DocumentData;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

@Service
public class RegulaDocumentService {

    private final DocumentReaderApi documentReaderApi;
    private String regulaLicense;

    public RegulaDocumentService(DocumentReaderApi documentReaderApi,
                                 @Value("${regula.license.path}") Resource licenseResource) {
        this.documentReaderApi = documentReaderApi;
        
        // Charger la licence depuis le fichier
        try {
            if (licenseResource.exists()) {
                byte[] licenseBytes = licenseResource.getInputStream().readAllBytes();
                this.regulaLicense = new String(licenseBytes, java.nio.charset.StandardCharsets.UTF_8).trim();
                System.out.println("✓ Licence Regula chargée depuis le fichier (longueur: " + regulaLicense.length() + ")");
            } else {
                System.err.println("⚠️ ATTENTION: Fichier de licence Regula introuvable : " + licenseResource.getDescription());
            }
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement de la licence Regula: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Traite un document d'identité uploadé
     * 
     * @param file Le fichier image du document
     * @return RecognitionResponse contenant les données extraites
     * @throws IOException Si erreur de lecture du fichier
     * @throws ApiException Si erreur de l'API Regula
     */
    public RecognitionResponse processDocument(MultipartFile file) throws IOException, ApiException {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Traitement du document: " + file.getOriginalFilename());
        System.out.println("-----------------------------------------------------------------");

        // Convertir le fichier en bytes puis en Base64
        byte[] fileBytes = file.getBytes();
        String base64File = Base64.getEncoder().encodeToString(fileBytes);

        // Créer l'objet ImageData
        ImageData imageData = new ImageData();
        imageData.setImage(base64File);

        // Créer l'image du document
        ProcessRequestImage processImage = new ProcessRequestImage();
        processImage.setImageData(imageData);
        processImage.setLight(6); // WHITE light
        processImage.setPageIdx(0);

        // Créer les paramètres de traitement
        ProcessParams processParams = new ProcessParams();
        processParams.setScenario("FullProcess");
        processParams.setAlreadyCropped(true);
        
        // Paramètres d'authentification
        AuthParams authParams = new AuthParams();
        authParams.setCheckLiveness(false);
        processParams.setAuthParams(authParams);
        
        // Ajouter tous les types de résultats nécessaires
        processParams.addResultTypeOutputItem(3);   // TEXT
        processParams.addResultTypeOutputItem(5);   // IMAGES
        processParams.addResultTypeOutputItem(8);   // VISUAL_OCR_EXTENDED
        processParams.addResultTypeOutputItem(15);  // STATUS
        processParams.addResultTypeOutputItem(17);  // TEXT_AVAILABLE
        processParams.addResultTypeOutputItem(30);  // DOCUMENT_TYPE
        processParams.addResultTypeOutputItem(33);  // DOCUMENT_POSITION
        processParams.addResultTypeOutputItem(36);  // BARCODES
        processParams.addResultTypeOutputItem(37);  // MRZ_POSITION

        // Créer systemInfo avec la licence
        ProcessSystemInfo systemInfo = new ProcessSystemInfo();
        if (regulaLicense != null && !regulaLicense.isEmpty()) {
           // systemInfo.setLicense(regulaLicense);
            System.out.println("✓ Licence Regula configurée (longueur: " + regulaLicense.length() + ")");
        } else {
            System.err.println("⚠️ ATTENTION: Aucune licence Regula configurée !");
            System.err.println("   Ajoutez 'regula.license=VOTRE_LICENCE' dans application.properties");
        }

        // Créer le ProcessRequest
        ProcessRequest request = new ProcessRequest();
        request.setSystemInfo(systemInfo);
        request.setProcessParam(processParams);
        request.addListItem(processImage);

        // Appeler l'API Regula
        try {
            RecognitionResponse response = documentReaderApi.process(request);
            
            // Log des informations extraites
            logExtractedInformation(response);
            
            return response;
        } catch (ApiException e) {
            System.err.println("❌ Erreur lors de l'appel API Regula: " + e.getMessage());
            System.err.println("Code d'erreur: " + e.getCode());
            throw e;
        }
    }


   public DocumentData findDonneeBiometriquue(MultipartFile file) {
    try {
        logHeader("Début de la vérification", file);

        //  Vérification du fichier
        byte[] fileBytes = validateFile(file);

        //  Vérification des dimensions de l'image
        validateImageDimensions(fileBytes);

        // 📤 Appel au service de reconnaissance Regula
        RecognitionResponse response = processDocument(file);

        //  Vérifier si le document est valide
        if (!isDocumentValid(response)) {
            System.err.println("⚠️ Document non valide selon Regula");
        }

        // 📄 Afficher type de document
        System.out.println("Type de document détecté : " + getDocumentTypeInfo(response));

        // 🧾 Debug : afficher tout
        printAllFields(response);

        // 📌 Extraction des données biographiques
        DocumentData data = extractDocumentData(response);
        logExtractedData(data);

        // 🖼 Extraction des images
       // extractImages(response);

        return data;

    } catch (ApiException e) {
        System.err.println("❌ Erreur Regula : " + e.getMessage());
        System.err.println("Code HTTP: " + e.getCode());
        System.err.println("Response: " + e.getResponseBody());

        if (e.getCode() == 403) {
            System.err.println("➡️ Cause probable : licence Regula invalide / non acceptée");
        }
        return null;

    } catch (Exception e) {
        System.err.println("❌ Erreur inattendue : " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}


private void logHeader(String title, MultipartFile file) {
    System.out.println("=================Laurent=============================");
    System.out.println(title);
    System.out.println("Fichier: " + file.getOriginalFilename());
    System.out.println("================Laurent==============================");
}


private byte[] validateFile(MultipartFile file) throws IOException {
    if (file.isEmpty()) {
        throw new IOException("Le fichier est vide ou inexistant.");
    }

    byte[] bytes = file.getBytes();
    System.out.println("Taille fichier: " + bytes.length + " bytes (" + bytes.length / 1024 + " KB)");

    if (bytes.length < 10_000) {
        System.err.println("⚠️ Fichier trop petit (<10KB) : risque d'échec OCR");
    }

    return bytes;
}


private void validateImageDimensions(byte[] fileBytes) {
    try {
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(fileBytes));

        if (img == null) {
            System.err.println("❌ Format d'image inconnu / illisible");
            return;
        }

        System.out.println("Dimensions image: " + img.getWidth() + " x " + img.getHeight());

        if (img.getWidth() < 600 || img.getHeight() < 400) {
            System.err.println("⚠️ Dimensions trop faibles (min recommandé : 600×400)");
        }

    } catch (Exception e) {
        System.err.println("❌ Erreur lecture image : " + e.getMessage());
    }
}

private DocumentData extractDocumentData(RecognitionResponse response) {

    DocumentData data = new DocumentData();
    data.setLastName(getSurname(response));
    data.setFirstName(getGivenNames(response));
    data.setDateOfBirth(getDateOfBirth(response));
    data.setDocumentNumber(getDocumentNumber(response));
    data.setNationality(getNationality(response));
    data.setExpiryDate(getDateOfExpiry(response));
    data.setIssueDate(getIssueDate(response));
    data.setLieuNaissance(getPlaceOfBirth(response));
    data.setSexe(getSex(response));
    data.setProfession(getProfession(response));
    data.setNip(getNip(response));
    data.setDateIssue(getDateIssue(response));
    

    return data;
}

private void logExtractedData(DocumentData data) {
    System.out.println("============ DONNÉES EXTRAITES ============");
    System.out.println("Nom: " + data.getLastName());
    System.out.println("Prénom: " + data.getFirstName());
    System.out.println("Date naissance: " + data.getDateOfBirth());
    System.out.println("Numéro Document: " + data.getDocumentNumber());
    System.out.println("Nationalité: " + data.getNationality());
    System.out.println("Délivrance: " + data.getDateIssue());
    System.out.println("Expiration: " + data.getExpiryDate());
    System.out.println("Lieu de naissance: "+data.getPlaceOfBirth());
    System.out.println("Sexe: "+data.getSexe());
    System.out.println("date delivrance:"+data.getIssueDate());
    System.out.println("NIP: "+data.getNip());

    System.out.println("=============Laurent===============================");

}


private void extractImages(RecognitionResponse response) throws IOException {
    byte[] docImage = getDocumentImage(response);
    if (docImage != null) {
        System.out.println("📄 Image document extraite: " + docImage.length + " bytes");
        // Files.write(Paths.get("document.jpg"), docImage);
    }

    byte[] portrait = getPortraitImage(response);
    if (portrait != null) {
        System.out.println("🧑 Photo portrait extraite: " + portrait.length + " bytes");
        // Files.write(Paths.get("portrait.jpg"), portrait);
    }
}

    /**
     * Affiche les informations extraites du document
     */
    private void logExtractedInformation(RecognitionResponse response) {
        System.out.println();
        System.out.println("-----------------------------------------------------------------");
        
        // Type de document
        if (response.documentType() != null) {
            System.out.println("Type de Document: " + response.documentType().getDocumentName());
        }
        
        // Statut
        if (response.status() != null) {
            var overallStatus = response.status().getOverallStatus();
            System.out.println("Statut Global: " + (overallStatus == 1 ? "VALIDE" : "NON VALIDE"));
        }
        
        // Champs de texte
        if (response.text() != null && response.text().getFieldList() != null) {
            System.out.println();
            System.out.println("Champs extraits:");
            
            for (var field : response.text().getFieldList()) {
                if (field.getValueList() != null && !field.getValueList().isEmpty()) {
                    var firstValue = field.getValueList().get(0);
                    System.out.format("  %s: %s (Source: %s)%n",
                            field.getFieldName(),
                            firstValue.getValue(),
                            firstValue.getSource());
                }
            }
        }
        
        System.out.println("-----------------------------------------------------------------");
        System.out.println();
    }

    /**
     * Extrait une valeur de champ par nom
     */
    public String getFieldValueByName(RecognitionResponse response, String fieldName) {
        if (response.text() != null && response.text().getFieldList() != null) {
            for (var field : response.text().getFieldList()) {
                if (fieldName.equals(field.getFieldName())) {
                    if (field.getValueList() != null && !field.getValueList().isEmpty()) {
                        return field.getValueList().get(0).getValue();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Extrait le numéro de document
     */
    public String getDocumentNumber(RecognitionResponse response) {
        return getFieldValueByName(response, "Document Number");
    }

    /**
     * Extrait le nom de famille
     */
    public String getSurname(RecognitionResponse response) {
        return getFieldValueByName(response, "Surname");
    }

    /**
     * Extrait le prénom
     */
    public String getGivenNames(RecognitionResponse response) {
        return getFieldValueByName(response, "Given Names");
    }

    /**
     * Extrait la date de naissance
     */
    public String getDateOfBirth(RecognitionResponse response) {
        return getFieldValueByName(response, "Date of Birth");
    }
    public String getDateIssue(RecognitionResponse response) {
        return getFieldValueByName(response, "Date of Issue");
    }

    public String getPlaceOfBirth(RecognitionResponse response) {
        return getFieldValueByName(response, "Place of Birth");
    }
    public String getNip(RecognitionResponse response) {
        return getFieldValueByName(response, "Personal Number");
    }
    /**
     * Extrait la date d'expiration
     */
    public String getDateOfExpiry(RecognitionResponse response) {
        return getFieldValueByName(response, "Date of Expiry");
    }

    public String getIssueDate(RecognitionResponse response){
        return getFieldValueByName(response,"Date of Issue"); 
    }


      public String getIssueState(RecognitionResponse response){
        return getFieldValueByName(response,"Issuing State Name"); 
    }
    /**
     * Extrait la nationalité
     */
    public String getNationality(RecognitionResponse response) {
        return getFieldValueByName(response, "Nationality");
    }

    /**
     * Extrait le sexe
     */
    public String getSex(RecognitionResponse response) {
        return getFieldValueByName(response, "Sex");
    }

    public String getProfession(RecognitionResponse response) {
        return getFieldValueByName(response, "Profession");
    }

     /**
     * Extrait le profession
     */
   

    /**
     * Extrait l'image du document
     */
    public byte[] getDocumentImage(RecognitionResponse response) {
        if (response.images() != null && response.images().getFieldList() != null) {
            for (var field : response.images().getFieldList()) {
                // Type 10 correspond généralement à DOCUMENT_FRONT
                if (field.getFieldType() == 10) {
                    if (field.getValueList() != null && !field.getValueList().isEmpty()) {
                        var value = field.getValueList().get(0).getValue();
                        if (value != null) {
                            try {
                                return Base64.getDecoder().decode(value);
                            } catch (Exception e) {
                                System.err.println("Erreur décodage image document: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Extrait la photo du portrait
     */
    public byte[] getPortraitImage(RecognitionResponse response) {
        if (response.images() != null && response.images().getFieldList() != null) {
            for (var field : response.images().getFieldList()) {
                // Type 201 correspond généralement à PORTRAIT
                if (field.getFieldType() == 201) {
                    if (field.getValueList() != null && !field.getValueList().isEmpty()) {
                        var value = field.getValueList().get(0).getValue();
                        if (value != null) {
                            try {
                                return Base64.getDecoder().decode(value);
                            } catch (Exception e) {
                                System.err.println("Erreur décodage photo portrait: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Vérifie si le document est valide
     */
    public boolean isDocumentValid(RecognitionResponse response) {
        return response.status() != null && response.status().getOverallStatus() == 1;
    }

    /**
     * Affiche tous les champs extraits (utile pour le debug)
     */
    public void printAllFields(RecognitionResponse response) {
        if (response.text() != null && response.text().getFieldList() != null) {
            System.out.println("=== Tous les champs extraits ===");
            for (var field : response.text().getFieldList()) {
                if (field.getValueList() != null && !field.getValueList().isEmpty()) {
                    var firstValue = field.getValueList().get(0);
                    System.out.format("%s: %s (Source: %s)%n",
                            field.getFieldName(),
                            firstValue.getValue(),
                            firstValue.getSource());
                }
            }
            System.out.println("================================");
        }
    }

    /**
     * Obtient le type de document complet
     */
    public String getDocumentTypeInfo(RecognitionResponse response) {
        if (response.documentType() != null) {
            return response.documentType().getDocumentName();
        }
        return "Type inconnu";
    }
}