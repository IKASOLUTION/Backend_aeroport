package aeroport.bf.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import aeroport.bf.service.RegulaWebService;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/regula")
public class RegulaController {

    @Autowired
    private RegulaWebService regulaWebService;

    @PostMapping("/verify")
    public String verifyDocument(@RequestParam("file") MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        return regulaWebService.verifyDocument(bytes);
    }
}

