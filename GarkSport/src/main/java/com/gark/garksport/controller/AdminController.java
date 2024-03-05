package com.gark.garksport.controller;

import com.gark.garksport.dto.authentication.RegisterRequest;
import com.gark.garksport.service.AdminService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/hello")

    public String hello() {

            return "Hello from Admin controller";

    }
    @PostMapping("/add-manager")
    public String addManager(
            @RequestBody RegisterRequest request
    ) throws MessagingException {
        return adminService.addManager(request);
    }

}
