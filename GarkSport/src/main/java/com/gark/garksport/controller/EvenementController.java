package com.gark.garksport.controller;

import com.gark.garksport.dto.request.*;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.modal.Evenement;
import com.gark.garksport.modal.enums.StatutEvenenement;
import com.gark.garksport.service.IEvenementService;
import com.gark.garksport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/evenement")
public class EvenementController {
    @Autowired
    private IEvenementService evenementService;
    @Autowired
    private UserService userService;


    @PostMapping("/addCompetition")
    public Evenement addCompetition(@RequestBody Evenement evenement, Principal connectedUser) {
        return evenementService.addCompetition(evenement, userService.getUserId(connectedUser.getName()));
    }

    @PostMapping("/addPersonnalise")
    public Evenement addPersonnalise(@RequestBody PersonnaliseRequest request, Principal connectedUser) {
        Evenement evenement = request.getEvenement();
        Integer idEquipe = request.getIdEquipe();
        List<Integer> idMembres = request.getIdMembres();

        if (idEquipe != null) {
            // If idEquipe is provided, call the service method with idEquipe
            return evenementService.addPersonnalisé(evenement, idEquipe, Collections.emptyList(), userService.getUserId(connectedUser.getName()));
        } else if (idMembres != null && !idMembres.isEmpty()) {
            // If idMembres are provided, call the service method with idMembres
            return evenementService.addPersonnalisé(evenement, null, idMembres, userService.getUserId(connectedUser.getName()));
        } else {
            // Handle the situation where neither idEquipe nor idMembres are provided
            throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
        }
    }

    @PostMapping("/addTest")
    public Evenement addTest(@RequestBody TestRequest request, Principal connectedUser) {
        Evenement evenement = request.getEvenement();
        Integer idEquipe = request.getIdEquipe();
        List<Integer> idMembres = request.getIdMembres();

        if (idEquipe != null) {
            // If idEquipe is provided, call the service method with idEquipe
            return evenementService.addPersonnalisé(evenement, idEquipe, Collections.emptyList(), userService.getUserId(connectedUser.getName()));
        } else if (idMembres != null && !idMembres.isEmpty()) {
            // If idMembres are provided, call the service method with idMembres
            return evenementService.addPersonnalisé(evenement, null, idMembres, userService.getUserId(connectedUser.getName()));
        } else {
            // Handle the situation where neither idEquipe nor idMembres are provided
            throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
        }
    }

//    @PostMapping("/addMatchAmical")
//    public Evenement addMatchAmical(@RequestBody MatchAmicalRequest request, Principal connectedUser) {
//        Evenement evenement = request.getEvenement();
//        List<EquipeHoraireDTO> equipesHoraire = request.getEquipesHoraire();
//
//        if (equipesHoraire != null && !equipesHoraire.isEmpty()) {
//            return evenementService.addMatchAmical(evenement, equipesHoraire, userService.getUserId(connectedUser.getName()));
//        } else {
//            throw new IllegalArgumentException("EquipesHoraire must be provided.");
//        }
//    }

    @GetMapping("/getAllEvenements")
    public List<Evenement> getAllEvenements() {
        return evenementService.getAllEvenements();
    }

    @DeleteMapping("/deleteEvenement/{id}")
    public void deleteEvenement(@PathVariable Integer id) {
        evenementService.deleteEvenement(id);
    }

    @PutMapping("/changeStatutEvenement/{id}")
    public Evenement changeStatutEvenement(@PathVariable Integer id, @RequestBody StatutEvenenement statut) {
        return evenementService.changeStatutEvenement(id, statut);
    }

    @GetMapping("/getMembersByEquipe/{idEquipe}")
    public List<Adherent> getMembersByEquipe(@PathVariable Integer idEquipe) {
        return evenementService.getMembersByEquipe(idEquipe);
    }
}
