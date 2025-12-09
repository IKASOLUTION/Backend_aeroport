package aeroport.bf.controller;

import aeroport.bf.dto.DocumentData;
import aeroport.bf.service.RegulaDocumentService;
import com.regula.documentreader.webclient.model.ext.RecognitionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final RegulaDocumentService regulaDocumentService;

    @Autowired
    public DocumentController(RegulaDocumentService regulaDocumentService) {
        this.regulaDocumentService = regulaDocumentService;
    }

    /**
     * Endpoint pour traiter un document biométrique.
     *
     * @param file le fichier à traiter
     * @return le résultat de reconnaissance ou une erreur
     */
    @PostMapping("/process")
    public ResponseEntity<DocumentData> findDonneeIdentite(@RequestParam("file") MultipartFile file) {
       return ResponseEntity.ok(regulaDocumentService.findDonneeBiometriquue(file));
    }
}
