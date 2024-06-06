package com.gark.garksport.controller;

import com.gark.garksport.modal.Benefices;
import com.gark.garksport.service.BeneficesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comptabilite/benefices")
public class BeneficesController {

    @Autowired
    private BeneficesService beneficesService;

    // Endpoint pour enregistrer un bénéfice
    @PostMapping("/add")
    public ResponseEntity<Benefices> saveBenefices(Principal connectedUser,@RequestBody Benefices benefices) {
        if (connectedUser == null ) {
            return ResponseEntity.badRequest().build();
        }
        Benefices savedBenefices = beneficesService.saveBenefices(connectedUser,benefices);
        return new ResponseEntity<>(savedBenefices, HttpStatus.CREATED);
    }

    // Endpoint pour récupérer tous les bénéfices
    @GetMapping("/all")
    public ResponseEntity<List<Benefices>> getAllBenefices(Principal connectedUser) {
        if (connectedUser == null ) {
            return ResponseEntity.badRequest().build();
        }
        List<Benefices> beneficesList = beneficesService.getAllBenefices(connectedUser);
        return new ResponseEntity<>(beneficesList, HttpStatus.OK);
    }

    // Endpoint pour récupérer un bénéfice par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Benefices> getBeneficesById(Principal connectedUser,@PathVariable Integer id) {
        if (connectedUser == null ) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Benefices> beneficesOptional = beneficesService.getBeneficesById(connectedUser,id);
        return beneficesOptional.map(benefices -> new ResponseEntity<>(benefices, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint pour mettre à jour un bénéfice par son ID
    @PutMapping("/update/{id}")
    public ResponseEntity<Benefices> updateBenefices(Principal connectedUser,@PathVariable Integer id, @RequestBody Benefices newBenefices) {
        if (connectedUser == null ) {
            return ResponseEntity.badRequest().build();
        }
        Benefices updatedBenefices = beneficesService.updateBenefices(connectedUser,id, newBenefices);
        return updatedBenefices != null ?
                new ResponseEntity<>(updatedBenefices, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint pour supprimer un bénéfice par son ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBenefices(Principal connectedUser,@PathVariable Integer id) {
        if (connectedUser == null ) {
            return ResponseEntity.badRequest().build();
        }
        beneficesService.deleteBenefices(connectedUser,id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
