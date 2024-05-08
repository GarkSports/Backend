package com.gark.garksport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gark.garksport.dto.authentication.AuthenticationRequest;
import com.gark.garksport.dto.authentication.AuthenticationResponse;
import com.gark.garksport.dto.authentication.RegisterRequest;
import com.gark.garksport.modal.Admin;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.User;
import com.gark.garksport.modal.enums.Role;
import com.gark.garksport.repository.AdminRepository;
import com.gark.garksport.repository.UserRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
//import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    @Autowired
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${application.security.jwt.expiration}")
    private long cookieExpiry;

    public Admin registerAsAdmin(Admin admin) {
        admin.setRole(Role.ADMIN);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public AuthenticationResponse register(RegisterRequest request){
        var user = Admin.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        repository.save(user);
        return AuthenticationResponse.builder()
                .build();
    }
    public User registerAsUser(User user) {

        user.setRole(Role.ADHERENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        System.out.println("User authorities: " + user.getAuthorities());
        System.out.println("User role: " + user.getRole());

        if (user.isBlocked()) {
            throw new LockedException("User is blocked");
        }
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        ResponseCookie cookie = ResponseCookie.from("accessToken", jwtToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(cookieExpiry)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }
    /*private void storeRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("authToken", refreshToken)
                .domain("localhost")
                .maxAge(Duration.of(14, ChronoUnit.DAYS)) // Set your desired expiration time
                .httpOnly(true)
                .secure(true) // Only sent over HTTPS
                .path("/")
                .build();

        // Add the cookie to the response
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }*/

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                var authReponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authReponse);
            }
        }
    }


}