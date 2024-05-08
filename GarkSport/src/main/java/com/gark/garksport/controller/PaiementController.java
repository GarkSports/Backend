package com.gark.garksport.controller;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Paiement;
import com.gark.garksport.modal.PaiementHistory;
import com.gark.garksport.modal.User;
import com.gark.garksport.modal.enums.StatutAdherent;
import com.gark.garksport.service.AdminService;
import com.gark.garksport.service.IPaiementService;
import com.gark.garksport.service.JwtService;
import com.gark.garksport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;


@RestController
@RequestMapping("/paiement")
@RequiredArgsConstructor
public class PaiementController {
    @Autowired
    private IPaiementService paiementService;
    private final UserService userService;

    @GetMapping("getAllPaiements")
    public Set<Paiement> getAllPaiementsByAcademie(Principal connectedUser){
        return paiementService.getAllPaiementsByAcademie(userService.getUserId(connectedUser.getName()));
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

    @GetMapping("getAdherents")
    public Set<Adherent> getAdherentsByAcademie(Principal connectedUser){
        return paiementService.getAdherentsByAcademie(userService.getUserId(connectedUser.getName()));
    }
}
