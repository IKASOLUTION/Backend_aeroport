package aeroport.bf.config.event;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import aeroport.bf.domain.User ;
import aeroport.bf.service.EmailService ;
import aeroport.bf.service.VerificationTokenService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Email Verification";
        String confirmationUrl = "http://localhost:8092/verify-email?token=" + token;
        String message = "Click the link to verify your email: " + confirmationUrl;

        emailService.sendNewMail(recipientAddress, subject, message);
    }
}