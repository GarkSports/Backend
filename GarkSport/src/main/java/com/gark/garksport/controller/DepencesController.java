package com.gark.garksport.controller;


import com.gark.garksport.modal.Depenses;
import com.gark.garksport.service.DepensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comptabilite/depences")
public class DepencesController {

    @Autowired
    private DepensesService depensesService;
    //    @PreAuthorize("hasAuthority('ajouter_comptabilite')")

    @PostMapping("/add")
    public ResponseEntity<Depenses> saveDepenses(Principal connectedUser,@RequestBody Depenses depenses) {
        if (connectedUser == null || depenses.getBeneficiaire().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Depenses savedDepenses = depensesService.saveDepenses(connectedUser,depenses);
        return new ResponseEntity<>(savedDepenses, HttpStatus.CREATED);
    }
    //    @PreAuthorize("hasAuthority('lire_comptabilite')")

    @GetMapping("/all")
    public ResponseEntity<List<Depenses>> getAllDepenses(Principal connectedUser) {
        if (connectedUser == null ) {
            return ResponseEntity.badRequest().build();
        }
        List<Depenses> allDepenses = depensesService.getAllDepenses(connectedUser);
        return new ResponseEntity<>(allDepenses, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAuthority('lire_comptabilite')")

    @GetMapping("/{id}")
    public ResponseEntity<Depenses> getDepensesById(Principal connectedUser,@PathVariable Integer id) {
        if (connectedUser == null ) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Depenses> depensesOptional = depensesService.getDepensesById(connectedUser,id);
        return depensesOptional.map(depenses -> new ResponseEntity<>(depenses, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
//    @PreAuthorize("hasAuthority('modifier_comptabilite')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Depenses> updateDepenses(Principal connectedUser,@PathVariable Integer id, @RequestBody Depenses newDepenses) {
        if (connectedUser == null ) {
            return ResponseEntity.badRequest().build();
        }
        Depenses updatedDepenses = depensesService.updateDepenses(connectedUser,id, newDepenses);
        if (updatedDepenses != null) {
            return new ResponseEntity<>(updatedDepenses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

  //  @PreAuthorize("hasAuthority('supprimer_comptabilite')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDepenses(Principal connectedUser,@PathVariable Integer id) {
        if (connectedUser == null ) {
            return ResponseEntity.badRequest().build();
        }
        depensesService.deleteDepenses(connectedUser,id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
