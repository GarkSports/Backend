package com.gark.garksport.controller;


import com.gark.garksport.dto.request.EquipeRequest;
import com.gark.garksport.modal.*;
import com.gark.garksport.service.IRandomService;
import com.gark.garksport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/random")
@RequiredArgsConstructor
public class RandomController {
    @Autowired
    private IRandomService randomService;
    private final UserService userService;


    @PostMapping("/addManager")
    public Manager addManager(@RequestBody Manager manager) {
        return randomService.addManager(manager);
    }

    @PostMapping("/addAndAffectAdherentToAcademie/{academieId}")
    public Adherent addAndAffectAdherentToAcademie(@RequestBody Adherent adherent, @PathVariable Integer academieId) {
        return randomService.addAndAffectAdherentToAcademie(adherent, academieId);
    }

    @PostMapping("/addAdherent/{codeequipe}")
    public Adherent addAdherent(@RequestBody Adherent adherent, @PathVariable String codeequipe) {
        return randomService.addAdherent(adherent, codeequipe);
    }

    @GetMapping("/getManagers/{academieId}")
    public Set<Manager> getManagers(@PathVariable Integer academieId) {
        return randomService.getManagersExceptAssigned(academieId);
    }

    @GetMapping("/getManagersNotAssigned")
    public Set<Manager> getManagers() {
        return randomService.getManagersNotAssigned();
    }

    @PostMapping("/addEquipe")
    public Equipe addEquipe(@RequestBody EquipeRequest equipeRequest, Principal connectedUser) {
        return randomService.addEquipe(equipeRequest.getEquipe(), userService.getUserId(connectedUser.getName()), equipeRequest.getDisciplineId());
    }

    @PostMapping("/addEntraineur")
    public Entraineur addEntraineur(@RequestBody Entraineur entraineur) {
        return randomService.addEntraineur(entraineur);
    }

    @GetMapping("/getEquipes")
    public Set<Equipe> getEquipes(Principal connectedUser) {
        return randomService.getEquipesByAcademie(userService.getUserId(connectedUser.getName()));
    }

    @GetMapping("/getAdherents")
    public Set<Adherent> getAdherents(Principal connectedUser) {
        return randomService.getAdherentsByAcademie(userService.getUserId(connectedUser.getName()));
    }

    @GetMapping("/getEntraineurs")
    public Set<Entraineur> getEntraineurs(Principal connectedUser) {
        return randomService.getEntraineursByAcademie(userService.getUserId(connectedUser.getName()));
    }

    @DeleteMapping("/deleteEquipe/{equipeId}")
    public void deleteEquipe(@PathVariable Integer equipeId) {
        randomService.deleteEquipe(equipeId);
    }

    @PostMapping("/affectAdherentsToEquipe/{equipeId}")
    public Equipe affectAdherentsToEquipe(@PathVariable Integer equipeId, @RequestBody List<Integer> adherentIds) {
        return randomService.affectAdherentToEquipe(equipeId, adherentIds);
    }

    @PostMapping("/affectEntraineursToEquipe/{equipeId}")
    public Equipe affectEntraineursToEquipe(@PathVariable Integer equipeId, @RequestBody List<Integer> entraineurIds) {
        return randomService.affectEntraineurToEquipe(equipeId, entraineurIds);
    }

    @PutMapping("/updateAcademie")
    public Academie updateAcademie(@RequestBody Academie academie, Principal connectedUser) {
        return randomService.updateAcademie(academie, userService.getUserId(connectedUser.getName()));
    }

    @PutMapping("/updateAcademieBackground/{academieId}")
    public ResponseEntity<String> updateAcademieBackground(@PathVariable Integer academieId, @RequestBody String background) {
        randomService.updateAcademieBackground(academieId, background);
        return ResponseEntity.ok("Academie background updated successfully");
    }

    @GetMapping("/getEntraineursByEquipe/{equipeId}")
    public Set<Entraineur> getEntraineursByEquipe(@PathVariable Integer equipeId) {
        return randomService.getEntraineursByEquipe(equipeId);
    }

    @GetMapping("/getMembersByAcademie/{academieId}")
    public Set<Adherent> getMembersByAcademie(@PathVariable Integer academieId) {
        return randomService.getMembersByAcademie(academieId);
    }


}
