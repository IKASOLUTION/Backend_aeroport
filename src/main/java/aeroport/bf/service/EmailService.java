package aeroport.bf.service;



import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// @Transactional
public class EmailService {
   // private final JavaMail javaMailSenderImpl;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SimpleMailMessage preConfiguredMessage;


    @Async
    public void sendNewMail(String to, String subject, String body)
    {
    final String username = "dambrelaurent82@gmail.com";
    final String password = "wubursdavbwueylb"; 
        Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true"); //TLS

    // Create session with authentication
    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    try {
        // Create message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(subject);
        
        message.setContent(body, "text/html");

        // Send message
        Transport.send(message);

        System.out.println("Email sent successfully.");

    } catch (MessagingException e) {
        throw new RuntimeException(e);
    }
}


@Async
public void sendNewMailing(Set<String> to, String subject, String body)
{
final String username = "dambrelaurent82@gmail.com";
final String password = "wubursdavbwueylb"; 
    Properties props = new Properties();
props.put("mail.smtp.host", "smtp.gmail.com");
props.put("mail.smtp.port", "587");
props.put("mail.smtp.auth", "true");
props.put("mail.smtp.starttls.enable", "true"); //TLS

// Create session with authentication
Session session = Session.getInstance(props, new Authenticator() {
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
});

try {
    // Create message
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(username));
    InternetAddress[] iAdressArray = InternetAddress.parse(to.toString()
    .replaceAll("[\\[\\]]", ""));
    message.setRecipients(Message.RecipientType.TO,
    iAdressArray);
    message.setSubject(subject);
    
    message.setContent(body, "text/html");

    // Send message
    Transport.send(message);

    System.out.println("Email sent successfully.");

} catch (MessagingException e) {
    throw new RuntimeException(e);
}
}
    

   

    /* @Async
    public void sendSimpleEmail(final String to, final String subjet, final String message) {
        
        try {

            Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("dambrelaurent82@gmail.com", "wubursdavbwueylb");
            }
        });

            

             Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("volta1528@gmail.com", true));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject(subjet);
        msg.setContent(message, "text/html");

        msg.setSentDate(new Date());

           Transport.send(msg);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    } */


    

}
