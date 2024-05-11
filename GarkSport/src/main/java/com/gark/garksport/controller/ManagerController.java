package com.gark.garksport.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.gark.garksport.dto.request.AddRoleNameRequest;
import com.gark.garksport.modal.*;
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
    public ResponseEntity<Set<RoleName>> getRoleNames(Principal connectedUser) {
        User user = managerService.getProfil(connectedUser);
        Academie academie = academieRepository.findByManagerId(user.getId());
        if (academie != null) {
            return ResponseEntity.ok(academie.getRoleNames());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add-role-name")
    public ResponseEntity<Set<RoleName>> addRoleName(@RequestBody AddRoleNameRequest request, Principal connectedUser) {
        User user = getProfil(connectedUser);
        Academie academie = academieRepository.findByManagerId(user.getId());

        if (academie != null) {
            RoleName roleName = new RoleName();
            roleName.setName(request.getRoleName());
            roleName.setPermissions(request.getPermissions().stream()
                    .map(Permission::valueOf)
                    .collect(Collectors.toSet()));
            roleName.setAcademie(academie);
            academie.getRoleNames().add(roleName);
            academieRepository.save(academie);
            return ResponseEntity.ok(academie.getRoleNames());
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get-permissions")
    public ResponseEntity<List<String>> getPermissions() {
        List<String> permissionNames = Arrays.stream(Permission.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(permissionNames);
    }




//    @PostMapping("/add-staff")
//    public Staff addStaff(
//            @RequestBody Staff staff
//    ) throws MessagingException {
//        return managerService.addStaff(staff);
//    }

    @PostMapping("/add-staff")
    public Staff addStaff(@RequestBody Staff request) throws MessagingException {
        return managerService.addStaff(request);
    }
    @PostMapping("/add-coach")
    public Entraineur addCoach(
            @RequestBody Entraineur entraineur
    ) throws MessagingException {
        return managerService.addCoach(entraineur);
    }

    @PostMapping("/add-parent")
    public Parent addParent(
            @RequestBody Parent parent
    ) throws MessagingException {
        return managerService.addParent(parent);
    }

    @PostMapping("/add-adherent")
    public Adherent addAdherent(
            @RequestBody Adherent adherent
    ) throws MessagingException {
        return managerService.addAdherent(adherent);
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