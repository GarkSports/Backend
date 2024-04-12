package com.gark.garksport.controller;

import com.gark.garksport.dto.authentication.RegisterRequest;
import com.gark.garksport.modal.Admin;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.Staff;
import com.gark.garksport.modal.User;
import com.gark.garksport.repository.ManagerRepository;
import com.gark.garksport.repository.UserRepository;
import com.gark.garksport.service.AdminService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<User>> getAllUsers(Principal connectedUser) {
        var user = getProfil(connectedUser);
        List<User> userList = repository.findAllByIdNot(user.getId());
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
