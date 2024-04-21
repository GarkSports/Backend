package com.gark.garksport.controller;


import com.gark.garksport.dto.request.EquipeRequest;
import com.gark.garksport.modal.*;
import com.gark.garksport.service.IRandomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/random")
public class RandomController {
    @Autowired
    private IRandomService randomService;


    @PostMapping("/addManager")
    public Manager addManager(@RequestBody Manager manager) {
        return randomService.addManager(manager);
    }

    @PostMapping("/addAndAffectAdherentToAcademie/{academieId}")
    public Adherent addAndAffectAdherentToAcademie(@RequestBody Adherent adherent, @PathVariable Integer academieId) {
        return randomService.addAndAffectAdherentToAcademie(adherent, academieId);
    }

    @GetMapping("/getManagers/{academieId}")
    public Set<Manager> getManagers(@PathVariable Integer academieId) {
        return randomService.getManagersExceptAssigned(academieId);
    }

    @GetMapping("/getManagersNotAssigned")
    public Set<Manager> getManagers() {
        return randomService.getManagersNotAssigned();
    }

    @PostMapping("/addEquipe/{academieId}")
    public Equipe addEquipe(@RequestBody EquipeRequest equipeRequest, @PathVariable Integer academieId) {
        return randomService.addEquipe(equipeRequest.getEquipe(), academieId, equipeRequest.getDisciplineId());
    }


    @PostMapping("/addEntraineur")
    public Entraineur addEntraineur(@RequestBody Entraineur entraineur) {
        return randomService.addEntraineur(entraineur);
    }

    @GetMapping("/getEquipes/{academieId}")
    public Set<Equipe> getEquipes(@PathVariable Integer academieId) {
        return randomService.getEquipesByAcademie(academieId);
    }

    @GetMapping("/getAdherents/{academieId}")
    public Set<Adherent> getAdherents(@PathVariable Integer academieId) {
        return randomService.getAdherentsByAcademie(academieId);
    }

    @GetMapping("/getEntraineurs/{academieId}")
    public Set<Entraineur> getEntraineurs(@PathVariable Integer academieId) {
        return randomService.getEntraineursByAcademie(academieId);
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

    @PutMapping("/updateAcademie/{academieId}")
    public Academie updateAcademie(@RequestBody Academie academie, @PathVariable Integer academieId) {
        return randomService.updateAcademie(academie, academieId);
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
