
package aeroport.bf.service;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

@Configuration
class JavaMail {

    @Bean
	public JavaMailSender getJavaMailSender()
	{
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);

	    // mailSender.setUsername("volta1528@gmail.com");
	   // mailSender.setPassword("Volt@2024#");
        

	    Properties props = mailSender.getJavaMailProperties();
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("volta1528@gmail.com", "Volt@2024#");
            }
        });
        
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
       mailSender.setSession(session);

	    return mailSender;
	}

	@Bean
	public SimpleMailMessage emailTemplate()
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("somebody@gmail.com");
		message.setFrom("admin@gmail.com");
	    message.setText("FATAL - Application crash. Save your job !!");
	    return message;
	}
}
