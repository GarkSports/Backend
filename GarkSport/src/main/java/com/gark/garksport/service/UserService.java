package com.gark.garksport.service;

import com.gark.garksport.modal.User;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Autowired
    private JavaMailSender mailSender;

    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Integer getUserId(String email){
        return repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found")).getId();
    }


    public String getUserFullName(String email){
        User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFirstname() + " " + user.getLastname();
    }





}
