package com.gark.garksport.controller;

import com.gark.garksport.dto.request.AcademieRequest;
import com.gark.garksport.dto.request.EtatRequest;
import com.gark.garksport.exception.InvalidEtatException;
import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.service.IAcademieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/academie")
public class AcademieController {
    @Autowired
    private IAcademieService academieService;

    @PostMapping("/addAcademie/{managerId}")
    public Academie addAcademie(@RequestBody AcademieRequest academieRequest, @PathVariable Integer managerId) {
        return academieService.addAcademie(academieRequest.getAcademie(), academieRequest.getDisciplineIds(), managerId);
    }

    @GetMapping("/getAcademies")
    public Set<Academie> getAcademies() {
        return academieService.getAcademies();
    }

    @PutMapping("/updateAcademie/{academieId}")
    public Academie updateAcademie(@RequestBody Academie academie, @PathVariable Integer academieId) {
        return academieService.updateAcademie(academie, academieId);
    }

    @PutMapping("/changeEtat/{academieId}")
    public Academie changeEtat(@PathVariable Integer academieId, @RequestBody EtatRequest etatRequest) {
        try {
            return academieService.chanegeEtatAcademie(academieId, etatRequest.getEtat());
        } catch (IllegalArgumentException e) {
            throw new InvalidEtatException("Etat non valide");
        }
    }

    @PutMapping("/deleteAcademie/{academieId}")
    public ResponseEntity<String> deleteAcademie(@PathVariable Integer academieId) {
        academieService.deleteAcademie(academieId);
        return ResponseEntity.ok("Academie deleted successfully");
    }

    @GetMapping("/getManagerDetails/{academieId}")
    public Manager getManagerDetails(@PathVariable Integer academieId) {
        return academieService.getManagerDetails(academieId);
    }

    @GetMapping("/getDisciplinesByAcademie/{academieId}")
    public Set<String> getDisciplinesByAcademie(@PathVariable Integer academieId) {
        return academieService.getDisciplinesByAcademie(academieId);
    }







}
