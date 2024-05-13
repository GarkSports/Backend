package com.gark.garksport.controller;

import com.gark.garksport.dto.authentication.RegisterRequest;
import com.gark.garksport.modal.*;
import com.gark.garksport.repository.AcademieRepository;
import com.gark.garksport.repository.ManagerRepository;
import com.gark.garksport.repository.UserRepository;
import com.gark.garksport.service.AdminService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserRepository repository;
    private final ManagerRepository managerRepository;
    private AcademieRepository academieRepository;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/hello")
    public String get() {

            return "Hello from Admin controller";

    }

    @GetMapping("/hello2")
    public String gethello() {

        return "Hello from Admin controller";

    }
    @PostMapping("/add-manager")
    public Manager addManager(
            @RequestBody Manager manager
    ) throws MessagingException {
        return adminService.addManager(manager);
    }


    @GetMapping("/get-profil")
    @ResponseBody
    public User getProfil(
            Principal connectedUser
    ) {
        return adminService.getProfil(connectedUser);
    }


    @GetMapping("/get-managers")
    @ResponseBody
    public ResponseEntity<List<Manager>> getManagers() {
        List<Manager> userList = managerRepository.findAll();
        return ResponseEntity.ok(userList);
    }

//    @GetMapping("/get-all-users")
//    @ResponseBody
//    public ResponseEntity<List<User>> getAllUsers(Principal connectedUser) {
//        var user = getProfil(connectedUser);
//        Integer managerId = user.getId();
//        Academie academie = user.getAcademie();
//        List<User> userList = academieRepository.findAdherentsByAcademieAndManagerIdNot(academie, managerId);
//        return ResponseEntity.ok(userList);
//    }

    @PutMapping("/update-manager")
    public ResponseEntity<Manager> updateManager(@RequestParam Integer id, @RequestBody Manager manager) {
        try {
            Optional<Manager> existingManager = managerRepository.findById(id);
            if (existingManager.isPresent()) {
                Manager updatedManager = adminService.updateManager(id, manager);
                return ResponseEntity.ok(updatedManager);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/block-user")
    public ResponseEntity<String> blockUser(@RequestParam Integer id) {
        try {
            String blocked = adminService.blockUser(id);
            if (blocked.equals("User blocked successfully")) {
                return ResponseEntity.ok(blocked);
            } else {
                return ResponseEntity.badRequest().body(blocked);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/unblock-user")
    public ResponseEntity<String> unblockUser(@RequestParam Integer id) {
        try {
            String unblocked = adminService.unblockUser(id);
            if (unblocked.equals("User unblocked successfully")) {
                return ResponseEntity.ok(unblocked);
            } else {
                return ResponseEntity.badRequest().body(unblocked);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("/archive-user")
    public String archiveUser(@RequestParam Integer id){
        return adminService.archiveUser(id);
    }

}
