package com.gark.garksport.controller;

import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.Staff;
import com.gark.garksport.modal.enums.Permission;
import com.gark.garksport.modal.enums.Role;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AdherentRepository adherentRepository;

    @PreAuthorize("hasAuthority('management:read')")
    @GetMapping("/hello")
        public String sayHello(){
            return "Hello from secured endpoint";
        }

//    @GetMapping("/test")
//    public Set<?> getAdherantByEquipe() {
//        return adherentRepository.findAdherentWithEquipe();
//    }
}
