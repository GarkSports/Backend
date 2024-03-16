package com.gark.garksport.service;

import com.gark.garksport.dto.authentication.RegisterRequest;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.User;
import com.gark.garksport.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

//    public Manager addManager(Manager manager) {
//        return managerRepository.save(manager);
//    }

    public String addManager(RegisterRequest request)throws MessagingException {

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


    }

    public User getProfil(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<User> userOptional = repository.findById(user.getId());
        return userOptional.orElse(null);
    }

    public String blockUser(Integer id) {
        var userOptional = repository.findById(id);
        System.out.println("id is : "+id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Set the blocked status and other relevant information
            user.setBlocked(true);
            user.setBlockedTimestamp(Instant.now());
            user.setBlockedDuration(Duration.ofDays(7)); // Example: Block for 7 days

            repository.save(user);

            return "User blocked successfully";
        } else {
            return "User not found";
        }

    }

    public String unblockUser(Integer id){
        var userOptional = repository.findById(id);
        System.out.println("id is : "+id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Set the blocked status and other relevant information
            user.setBlocked(false);

            repository.save(user);

            return "User unblocked successfully";
        } else {
            return "User not found";
        }

    }

    public String archiveUser(Integer id) {
        var user = repository.findById(id);
        System.out.println("id is : "+id);
        if (user.isPresent()) {

            repository.delete(user.get());
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }
//    @Scheduled(fixedRate = 60 * 1000) // 1 min
//    public void unblockBlockedUsers() {
//        List<User> blockedUsers = repository.findByBlocked(true);
//        System.out.println("1 minute has passed");
//        for (User user : blockedUsers) {
//            Instant expirationTime = user.getBlockedTimestamp().plus(user.getBlockedDuration());
//            if (Instant.now().isAfter(expirationTime)) {
//                // Unlock the user
//                user.setBlocked(false);
//                repository.save(user);
//            }
//        }
//    }
}