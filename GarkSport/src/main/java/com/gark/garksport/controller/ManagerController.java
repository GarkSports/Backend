package com.gark.garksport.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.gark.garksport.dto.request.AddRoleNameRequest;
import com.gark.garksport.dto.request.ResetPasswordRequest;
import com.gark.garksport.modal.*;
import com.gark.garksport.modal.enums.Permission;
import com.gark.garksport.modal.enums.Role;
import com.gark.garksport.repository.*;
import com.gark.garksport.service.AdminService;
import com.gark.garksport.service.ManagerService;
import com.gark.garksport.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    private final StaffRepository staffRepository;
    private final EntraineurRepository entraineurRepository;
    private final AdherentRepository adherentRepository;
    private final ParentRepository parentRepository;
    private final UserService userService;

    @Autowired
    private AcademieRepository academieRepository;



    @GetMapping("/hello")
    public String getHello(){
        return "hello manager";
    }

    @GetMapping("/get-profil")
    @JsonIgnoreProperties
    @ResponseBody
    public User getProfil(
            Principal connectedUser
    ) {
        return managerService.getProfil(connectedUser);
    }

    @GetMapping("/get-manager-profil")
    @JsonIgnoreProperties
    @ResponseBody
    public Manager getManagerProfil(
            Principal connectedUser
    ) {
        return managerService.getManagerProfil(connectedUser);
    }

    @GetMapping("/get-only-role-names")
    public ResponseEntity<Set<String>> getOnlyRoleNames(Principal connectedUser) {
        User user = managerService.getProfil(connectedUser);
        Academie academie = academieRepository.findByManagerId(user.getId());
        if (academie != null) {
            Set<String> roleNames = academie.getRoleNames().stream()
                    .map(RoleName::getName) // Map RoleName objects to roleName strings
                    .collect(Collectors.toSet()); // Collect the roleName strings into a set
            return ResponseEntity.ok(roleNames);
        } else {
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<RoleName> addRoleName(@RequestBody RoleName request, Principal connectedUser) {
        return managerService.addRoleName(request, connectedUser);
    }

    @GetMapping("/get-permissions")
    public ResponseEntity<List<String>> getPermissions() {
        List<String> permissionNames = Arrays.stream(Permission.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(permissionNames);
    }

    @GetMapping("/getFormManagerById/{id}")
    public ResponseEntity<User> getManagerById(@PathVariable Integer id) {
        Optional<User> user = managerService.getManagerById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/update-role-name")
    public ResponseEntity<RoleName> updateRoleName(@RequestParam Integer id, @RequestBody RoleName request, Principal connectedUser) {
        try {
            User user = getProfil(connectedUser);
            if (user instanceof Manager) {
                Manager manager = (Manager) user;
                RoleName updatedRoleName = managerService.updateRoleName(id, request, manager);
                return ResponseEntity.ok(updatedRoleName);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update-manager")
    public ResponseEntity<Manager> updateManager(Principal principal, @RequestBody Manager request) {
        try {
            Manager updatedManager = managerService.updateManager(principal, request);
            return ResponseEntity.ok(updatedManager);
        } catch (RuntimeException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/add-staff")
    public Staff addStaff(
            @RequestBody Staff request,
            Principal connectedUser) throws MessagingException {
        return managerService.addStaff(request, connectedUser);
    }

    @PostMapping("/add-coach")
    public Entraineur addCoach(
            @RequestBody Entraineur entraineur,
            Principal connectedUser
    ) throws MessagingException {
        return managerService.addCoach(entraineur, connectedUser);
    }


    @PostMapping("/add-parent")
    public Parent addParent(
            @RequestBody Parent parent,
            Principal connectedUser
    ) throws MessagingException {
        return managerService.addParent(parent, connectedUser);
    }

    @PostMapping("/add-adherent")
    public Adherent addAdherent(
            @RequestBody Adherent adherent,
            Principal connectedUser
    ) throws MessagingException {

        return managerService.addAdherent(adherent, connectedUser);
    }

    @GetMapping("/get-adherents")
    @ResponseBody
    public Set<Adherent> getAdherents(Principal connectedUser) {
        return managerService.getAdherentsByAcademie(userService.getUserId(connectedUser.getName()));
    }
    @GetMapping("/get-staff")
    @ResponseBody
    public Set<Staff> getStaff(Principal connectedUser) {
        return managerService.getStaffByAcademie(userService.getUserId(connectedUser.getName()));
    }

    @GetMapping("/get-entraineur")
    @ResponseBody
    public Set<Entraineur> getEntraineur(Principal connectedUser) {
        return managerService.getEntraineurByAcademie(userService.getUserId(connectedUser.getName()));
    }

    @GetMapping("/get-parent")
    @ResponseBody
    public Set<Parent> getParent(Principal connectedUser) {
        return managerService.getParentByAcademie(userService.getUserId(connectedUser.getName()));
    }

    @GetMapping("/get-users")
    @ResponseBody
    public List<User> getUsersByAcademie(Principal connectedUser) {
        return managerService.getUsersByAcademie(userService.getUserId(connectedUser.getName()));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(Principal connectedUser, @RequestBody ResetPasswordRequest request) {
        try {
            String result = managerService.resetPassword(connectedUser, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-user")
    public String deleteUser(@RequestParam Integer id){
        return managerService.deleteUser(id);
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

    @PutMapping("/update-staff")
    public ResponseEntity<Staff> updateStaff(@RequestParam Integer id, @RequestBody Staff request) throws MessagingException {
        try {
            Optional<Staff> existingStaff = staffRepository.findById(id);
            if (existingStaff.isPresent()) {
                Staff updateStaff = managerService.updateStaff(id, request);
                return ResponseEntity.ok(updateStaff);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update-coach")
    public ResponseEntity<Entraineur> updateCoach(@RequestParam Integer id, @RequestBody Entraineur request) throws MessagingException {
        try {
            Optional<Entraineur> existingEntraineur = entraineurRepository.findById(id);
            if (existingEntraineur.isPresent()) {
                Entraineur updateCoach = managerService.updateCoach(id, request);
                return ResponseEntity.ok(updateCoach);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update-adherent")
    public ResponseEntity<Adherent> updateAdherent(@RequestParam Integer id, @RequestBody Adherent request) throws MessagingException {


                Adherent updateAdherent = managerService.updateAdherent(id, request);
                return ResponseEntity.ok(updateAdherent);


    }

    @GetMapping("/get-by-id")
    public Adherent getById(@RequestParam Integer id) {
        Optional<Adherent> existingAdherent = adherentRepository.findById(id);
        if (existingAdherent.isPresent()) {
            return existingAdherent.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Adherent not found");
        }
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