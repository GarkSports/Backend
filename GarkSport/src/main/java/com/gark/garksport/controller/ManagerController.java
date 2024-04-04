package com.gark.garksport.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.Staff;
import com.gark.garksport.modal.User;
import com.gark.garksport.modal.enums.Permission;
import com.gark.garksport.repository.AcademieRepository;
import com.gark.garksport.repository.ManagerRepository;
import com.gark.garksport.repository.UserRepository;
import com.gark.garksport.service.AdminService;
import com.gark.garksport.service.ManagerService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final UserRepository repository;
    private final ManagerRepository managerRepository;
    private final ManagerService managerService;
    private final AdminService adminService;

    @Autowired
    private AcademieRepository academieRepository;


    @GetMapping("/hello")
    public String getHello(){
        return "hello manager";
    }

    @GetMapping("/get-profil")
    @ResponseBody
    public User getProfil(
            Principal connectedUser
    ) {
        return managerService.getProfil(connectedUser);
    }

    @GetMapping("/get-role-names")
    public ResponseEntity<Set<String>> getRoleNames(Principal connectedUser, Integer managerId) {
        User user = managerService.getProfil(connectedUser);
        Academie academie = academieRepository.findByManagerId(user.getId());
        if (academie != null) {
            return ResponseEntity.ok(academie.getRoleNames());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-permissions")
    public ResponseEntity<List<String>> getStaffPermissions() {
        return ResponseEntity.ok(Arrays.stream(Permission.values())
                .filter(permission -> permission.name().startsWith("STAFF"))
                .map(Enum::name)
                .collect(Collectors.toList()));
    }

    @PostMapping("/add-role-name")
    public ResponseEntity<Set<String>> addRoleName(@RequestBody JsonNode requestBody, Principal connectedUser) {
        User user = getProfil(connectedUser);
        Academie academie = academieRepository.findByManagerId(user.getId());

        if (academie != null) {
            Set<String> roleNames = academie.getRoleNames();
            JsonNode roleNamesNode = requestBody.get("roleNames");
            if (roleNamesNode != null && roleNamesNode.isArray()) {
                for (JsonNode roleNameNode : roleNamesNode) {
                    roleNames.add(roleNameNode.asText());
                }
                academieRepository.save(academie);
                return ResponseEntity.ok(roleNames);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add-staff")
    public Staff addStaff(
            @RequestBody Staff staff
    ) throws MessagingException {
        Set<Permission> permissions = new HashSet<>(); // Initialize permissions set if needed
        return managerService.addStaff(staff, permissions);
    }


    @GetMapping("/get-all-users")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers(Principal connectedUser) {
        var user = getProfil(connectedUser);
        List<User> userList = repository.findAllByIdNot(user.getId());
        return ResponseEntity.ok(userList);
    }
    @GetMapping("/get-academie/{manager_id}")
    public ResponseEntity<?> getAcademie(@PathVariable("manager_id") Integer managerId) {
        Academie academie = academieRepository.findByManagerId(managerId);
        if (academie != null) {
            return ResponseEntity.ok(academie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-academie")
    public ResponseEntity<?> getAcademie(Principal connectedUser){
        User user = managerService.getProfil(connectedUser);
        Academie academie = academieRepository.findByManagerId(user.getId());
        if (academie != null) {
            return ResponseEntity.ok(academie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/block-user")
    public String blockUser(@RequestParam Integer id) {
        return managerService.blockUser(id);
    }

    @PutMapping("/unblock-user")
    public String unblockUser(@RequestParam Integer id) {
        return managerService.unblockUser(id);
    }

    @DeleteMapping("/archive-user")
    public String archiveUser(@RequestParam Integer id){
        return managerService.archiveUser(id);
    }


//    @GetMapping("/get-role-names")
//    public ResponseEntity<List<String>> getRoleNames(Principal connectedUser) {
//        User user = managerService.getProfil(connectedUser);
//        if (user instanceof Manager) {
//            Manager manager = (Manager) user;
//            Academie academie = manager.getAcademie();
//            if (academie != null) {
//                List<String> roleNames = (List<String>) academie.getRoleNames();
//                return roleNames != null ? ResponseEntity.ok(roleNames) : ResponseEntity.notFound().build();
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }

//    @PostMapping("/add-role-names")
//    public String

}
