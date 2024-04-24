package com.gark.garksport.controller;

import com.gark.garksport.dto.authentication.AuthenticationRequest;
import com.gark.garksport.dto.authentication.AuthenticationResponse;
import com.gark.garksport.dto.authentication.RegisterRequest;
import com.gark.garksport.modal.Admin;
import com.gark.garksport.modal.User;
import com.gark.garksport.repository.UserRepository;
import com.gark.garksport.service.AdminService;
import com.gark.garksport.service.AuthenticationService;
import com.gark.garksport.service.JwtService;
import com.gark.garksport.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private User user;
    private final UserRepository repository;
    private final AuthenticationService service;
    public final UserService userService;
    private final AdminService adminService;
    //private final LogoutService logoutService;


    @Autowired
    private JwtService jwtService;

    @PostMapping("/registerAsAdmin")
    public ResponseEntity<Admin> registerAsAdmin(
            @RequestBody Admin admin
            ){
        return ResponseEntity.ok(service.registerAsAdmin(admin));
    }

    @PostMapping("/registerAsUser")
    public ResponseEntity<User> registerAsUser(
            @RequestBody User user
    ){
        return ResponseEntity.ok(service.registerAsUser(user));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request, HttpServletResponse response
    )
    {
        return ResponseEntity.ok(service.authenticate(request, response));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request ,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/hello")
    public String getHello(){
        return "hey from user controller";
    }

}