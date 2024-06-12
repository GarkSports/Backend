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
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/evenement")
public class EvenementController {
    @Autowired
    private IEvenementService evenementService;
    @Autowired
    private UserService userService;


    @PostMapping("/addCompetition")
    public Evenement addCompetition(@RequestBody CompetitionRequest request, Principal connectedUser) {
        Evenement evenement = request.getEvenement();
        Integer idEquipe = request.getIdEquipe();
        List<Integer> idMembres = request.getIdMembres();

        if (idEquipe != null) {
            // If idEquipe is provided, call the service method with idEquipe
            return evenementService.addCompetition(evenement, idEquipe, Collections.emptyList(), userService.getUserId(connectedUser.getName()));
        } else if (idMembres != null && !idMembres.isEmpty()) {
            // If idMembres are provided, call the service method with idMembres
            return evenementService.addCompetition(evenement, null, idMembres, userService.getUserId(connectedUser.getName()));
        } else {
            // Handle the situation where neither idEquipe nor idMembres are provided
            throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
        }
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
            return evenementService.addTest(evenement, idEquipe, Collections.emptyList(), userService.getUserId(connectedUser.getName()));
        } else if (idMembres != null && !idMembres.isEmpty()) {
            // If idMembres are provided, call the service method with idMembres
            return evenementService.addTest(evenement, null, idMembres, userService.getUserId(connectedUser.getName()));
        } else {
            // Handle the situation where neither idEquipe nor idMembres are provided
            throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
        }
    }

    @PostMapping("/addMatchAmical")
    public Evenement addMatchAmical(@RequestBody MatchAmicalRequest request, Principal connectedUser) {
        Integer managerId = userService.getUserId(connectedUser.getName());
        return evenementService.addMatchAmical(request, managerId);
    }


    @GetMapping("/getAllEvenements")
    public Set<Evenement> getAllEvenements(Principal connectedUser) {
        return evenementService.getAllEvenements(userService.getUserId(connectedUser.getName()));
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

    @GetMapping("/getMembersByEvenement/{idEvenement}")
    public List<Adherent> getMembersByEvenement(@PathVariable Integer idEvenement) {
        return evenementService.getMembersByEvenement(idEvenement);
    }

    @PutMapping("/updateEvenement/{evenementId}")
    public Evenement updateEvenement(@RequestBody UpdateEvenementRequest request, @PathVariable Integer evenementId) {
        Evenement evenement = request.getEvenement();
        List<Integer> idMembres = request.getIdMembres();
        return evenementService.updateEvenement(evenement, idMembres, evenementId);
    }

    @PutMapping("/updateEvenementMatchAmical/{evenementId}")
    public Evenement updateEvenementMatchAmical(@RequestBody Evenement evenement, @PathVariable Integer evenementId) {
        return evenementService.updateEvenementMatchAmical(evenement, evenementId);
    }

    @GetMapping("/getEquipesByEvenementMatchAmical/{idEvenement}")
    public List<Equipe> getEquipesByEvenementMatchAmical(@PathVariable Integer idEvenement) {
        return evenementService.getEquipesByEvenementMatchAmical(idEvenement);
    }
}
