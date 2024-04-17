package com.gark.garksport.controller;

import com.gark.garksport.modal.*;
import com.gark.garksport.modal.enums.Permission;
import com.gark.garksport.repository.AcademieRepository;
import com.gark.garksport.repository.ManagerRepository;
import com.gark.garksport.repository.StaffRepository;
import com.gark.garksport.repository.UserRepository;
import com.gark.garksport.service.AdminService;
import com.gark.garksport.service.ManagerService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private final StaffRepository staffRepository;

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

    @GetMapping("/get-profil-by-id")
    public User getProfilById(
            @RequestParam Integer id
    ) {
        return managerService.getProfilById(id);
    }

    @GetMapping("/get-role-names")
    public ResponseEntity<List<RoleName>> getRoleNames(Principal connectedUser) {
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

        User user = getProfil(connectedUser);
        if (user instanceof Manager) {
            Manager manager = (Manager) user;
            Academie academie = academieRepository.findByManagerId(manager.getId());

            if (academie != null) {
                RoleName roleName = new RoleName();
                roleName.setRoleName(request.getRoleName());
                roleName.setPermissions(request.getPermissions());
                roleName.setAcademie(academie);
                academie.getRoleNames().add(roleName);

                academieRepository.save(academie);
                return ResponseEntity.ok(roleName);
            }
        }

        return ResponseEntity.badRequest().build();
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



    @GetMapping("/get-permissions")
    public ResponseEntity<List<String>> getPermissions() {
        List<String> permissionNames = Arrays.stream(Permission.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(permissionNames);
    }


    @PostMapping("/add-staff")
    public Staff addStaff(@RequestBody Staff request,
                          Principal connectedUser
    ) throws MessagingException {
        return managerService.addStaff(request, connectedUser);
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
    public ResponseEntity<Academie> getAcademie(Principal connectedUser){
        User user = managerService.getProfil(connectedUser);
        return managerService.getAcademie(user.getId());

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
