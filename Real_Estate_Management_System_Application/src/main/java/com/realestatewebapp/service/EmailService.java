package com.realestatewebapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendTokenEmail(String to, String token) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Password Reset Request");
		message.setText("To reset your password, use the following token: " + token);
		javaMailSender.send(message);
		
	}
	
	 public void sendHireAgentEmail(String agentEmail, String sellerDetails) throws MessagingException {
	        MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        // Set email parameters
	        helper.setTo(agentEmail);
	        helper.setSubject("Seller Hire Request");
	        helper.setText(sellerDetails, true);  // The second parameter is for HTML content
	        
	        // Send the email
	        javaMailSender.send(message);
	    }
	 
	 public void sendBuyPropertyAgentEmail(String agentEmail, String buyerDetails) throws MessagingException {
	        MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        // Set email parameters
	        helper.setTo(agentEmail);
	        helper.setSubject("Buyer Chat Request");
	        helper.setText(buyerDetails, true);  // The second parameter is for HTML content
	        
	        // Send the email
	        javaMailSender.send(message);
	    }

}
