package aeroport.bf.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import aeroport.bf.service.UserService;

@Controller
public class VerificationController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/users/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        String result = userService.validateVerificationToken(token);
        
        model.addAttribute("message", result);
        
        if (result.equals("Compte activé avec succès")) {
            model.addAttribute("status", "success");
        } else if (result.equals("Lien expiré")) {
            model.addAttribute("status", "expired");
        } else {
            model.addAttribute("status", "invalid");
        }
        
        return "verification-result";
    }
}