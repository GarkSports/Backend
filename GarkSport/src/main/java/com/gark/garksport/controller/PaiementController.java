package com.gark.garksport.controller;

import com.gark.garksport.modal.Paiement;
import com.gark.garksport.service.IPaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/paiement")
public class PaiementController {
    @Autowired
    private IPaiementService paiementService;

    @GetMapping("getAllPaiements/{academieId}")
    public Set<Paiement> getAllPaiementsByAcademie(@PathVariable Integer academieId){
        return paiementService.getAllPaiementsByAcademie(academieId);
    }

    @PostMapping("addPaiement/{idAdherent}")
    public Paiement addPaiement(@RequestBody Paiement paiement,@PathVariable Integer idAdherent){
        return paiementService.addPaiement(paiement, idAdherent);
    }


}
