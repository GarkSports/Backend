package com.gark.garksport.service;

import com.gark.garksport.dto.authentication.RegisterRequest;
import com.gark.garksport.modal.User;
import com.gark.garksport.repository.UserRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public String generateRandomPassword(){
        // Define the character set for the password
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Set the length of the password
        int length = 20;

        StringBuilder randomPassword = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            // Generate a random index to pick a character from the set
            int randomIndex = secureRandom.nextInt(characters.length());

            // Append the randomly picked character to the password
            randomPassword.append(characters.charAt(randomIndex));
        }

        return randomPassword.toString();
    }

    public String addManager(RegisterRequest request) {
        try {
            String generatedPWD = generateRandomPassword();

            var user = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(generatedPWD))
                    .role(request.getRole())
                    //.permissions(request.getPermissions())
                    .build();
            repository.save(user);

            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress("${spring.mail.username}"));
            message.setRecipients(MimeMessage.RecipientType.TO, request.getEmail());
            message.setSubject(request.getRole() + " Login");
            message.setText("<div> Login using your email and this password: " + request.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

            mailSender.send(message);
            return request.getRole() + " added successfully";
        } catch(
                Exception e)

        {
            // Handle the exception here
            e.printStackTrace(); // Print the stack trace for debugging
            return "An error occurred while processing the request";
        }

    }
}