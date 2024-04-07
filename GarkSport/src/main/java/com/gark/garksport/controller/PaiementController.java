package com.gark.garksport.controller;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Paiement;
import com.gark.garksport.modal.PaiementHistory;
import com.gark.garksport.modal.enums.StatutAdherent;
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

    @PutMapping("updatePaiement/{idPaiement}")
    public Paiement updatePaiement(@RequestBody Paiement updatedPaiement, @PathVariable Integer idPaiement){
        return paiementService.updatePaiement(updatedPaiement, idPaiement);
    }

    @GetMapping("getPaiementHistory/{adherentId}")
    public Set<PaiementHistory> getPaiementHistoryByAdherent(@PathVariable Integer adherentId){
        return paiementService.getPaiementHistoryByAdherent(adherentId);
    }

    @GetMapping("getAdherent/{adherentId}")
    public Adherent getAdherentById(@PathVariable Integer adherentId){
        return paiementService.getAdherentById(adherentId);
    }

    @PutMapping("changeStatutAdherent/{adherentId}")
    public Adherent changeStatutAdherent(@PathVariable Integer adherentId, @RequestBody StatutAdherent statutAdherent){
        return paiementService.changeStatutAdherent(adherentId, statutAdherent);
    }

    @DeleteMapping("deletePaiement/{idPaiement}")
    public void deletePaiement(@PathVariable Integer idPaiement){
        paiementService.deletePaiement(idPaiement);
    }

    @GetMapping("getAdherents/{academieId}")
    public Set<Adherent> getAdherentsByAcademie(@PathVariable Integer academieId){
        return paiementService.getAdherentsByAcademie(academieId);
    }
}
