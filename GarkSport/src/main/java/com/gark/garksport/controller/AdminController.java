package com.gark.garksport.controller;

import com.gark.garksport.dto.authentication.RegisterRequest;
import com.gark.garksport.modal.User;
import com.gark.garksport.repository.UserRepository;
import com.gark.garksport.service.AdminService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserRepository repository;

    @GetMapping("/hello")

    public String get() {

            return "Hello from Admin controller";

    }
    @PostMapping("/add-manager")
    public String addManager(
            @RequestBody RegisterRequest request
    ) throws MessagingException {
        return adminService.addManager(request);
    }

    @GetMapping("/get-profil")
    @ResponseBody
    public User getProfil(
            Principal connectedUser
    ) {
        return adminService.getProfil(connectedUser);
    }

//    @GetMapping("/get-all-users")
//    @ResponseBody
//    public ResponseEntity<List<User>> getAllUsers(Principal connectedUser) {
//        var user = getProfil(connectedUser);
//        List<User> userList = repository.findAllByIdNot(user.getId());
//
//        return ResponseEntity.ok(userList);
//    }
    @GetMapping("/get-all-users")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = repository.findAll();
        return ResponseEntity.ok(userList);
    }

    @PutMapping("/block-user")
    public String blockUser(@RequestParam Integer id) {
        return adminService.blockUser(id);
    }

    @PutMapping("/unblock-user")
    public String unblockUser(@RequestParam Integer id) {
        return adminService.unblockUser(id);
    }

    @DeleteMapping("/archive-user")
    public String archiveUser(@RequestParam Integer id){
        return adminService.archiveUser(id);
    }

}
