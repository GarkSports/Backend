package com.gark.garksport.controller;

import com.gark.garksport.dto.request.AcademieRequest;
import com.gark.garksport.dto.request.EtatRequest;
import com.gark.garksport.exception.InvalidEtatException;
import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.AcademieHistory;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.User;
import com.gark.garksport.service.AdminService;
import com.gark.garksport.service.IAcademieService;
import com.gark.garksport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;


@RestController
@RequestMapping("/academie")
@RequiredArgsConstructor
public class AcademieController {
    @Autowired
    private IAcademieService academieService;
    private final AdminService adminService;
    private final UserService userService;

    @PostMapping("/addAcademie/{managerId}")
    public Academie addAcademie(@RequestBody AcademieRequest academieRequest, @PathVariable Integer managerId) {
        return academieService.addAcademie(academieRequest.getAcademie(), managerId);
    }

    @GetMapping("/getAcademies")
    public Set<Academie> getAcademies() {
        return academieService.getAcademies();
    }

    @PutMapping("/updateAcademie/{academieId}/{managerId}")
    public Academie updateAcademie(@RequestBody AcademieRequest academieRequest, @PathVariable Integer managerId, @PathVariable Integer academieId) {
        return academieService.updateAcademie(academieRequest.getAcademie(), academieId, managerId);
    }

    @PutMapping("/changeEtat/{academieId}")
    public Academie changeEtat(@PathVariable Integer academieId, @RequestBody EtatRequest etatRequest) {
        try {
            return academieService.changeEtatAcademie(academieId, etatRequest.getEtat(), etatRequest.getChangeReason());
        } catch (IllegalArgumentException e) {
            throw new InvalidEtatException("Etat non valide");
        }
    }

    @GetMapping("/getAcademieHistory/{academieId}")
    public Set<AcademieHistory> getAcademieHistory(@PathVariable Integer academieId) {
        return academieService.getAcademieHistory(academieId);
    }

    @PutMapping("/archiveAcademie/{academieId}")
    public ResponseEntity<String> archiveAcademie(@PathVariable Integer academieId) {
        academieService.deleteAcademie(academieId);
        return ResponseEntity.ok("Academie deleted successfully");
    }

    @GetMapping("/getManagerDetails/{academieId}")
    public Manager getManagerDetails(@PathVariable Integer academieId) {
        return academieService.getManagerDetails(academieId);
    }

    @GetMapping("/getAcademieById")
    public Academie getAcademieById(Principal connectedUser) {
        return academieService.getAcademieById(userService.getUserId(connectedUser.getName()));
    }

    @GetMapping("/getArchivedAcademies")
    public Set<Academie> getArchivedAcademies() {
        return academieService.getArchivedAcademies();
    }

    @DeleteMapping("/deleteArchivedAcademie/{academieId}")
    public ResponseEntity<String> deleteArchivedAcademie(@PathVariable Integer academieId) {
        academieService.deleteArchivedAcademie(academieId);
        return ResponseEntity.ok("Archived Academie deleted successfully");
    }

    @PutMapping("/restoreArchivedAcademie/{academieId}")
    public ResponseEntity<String> restoreArchivedAcademie(@PathVariable Integer academieId) {
        academieService.restoreArchivedAcademie(academieId);
        return ResponseEntity.ok("Archived Academie restored successfully");
    }

    @GetMapping("/checkIfManager")
    public boolean checkIfManager(Principal connectedUser) {
        Integer userId = userService.getUserId(connectedUser.getName());
        return academieService.isManager(userId);
    }

    @GetMapping("/checkIfAdmin")
    public boolean checkIfAdmin(Principal connectedUser) {
        Integer userId = userService.getUserId(connectedUser.getName());
        return academieService.isAdmin(userId);
    }

    @GetMapping("/get-profil")
    @ResponseBody
    public User getProfil(
            Principal connectedUser
    ) {
        return adminService.getProfil(connectedUser);
    }
}
