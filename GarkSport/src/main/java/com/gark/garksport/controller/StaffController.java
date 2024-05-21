package com.gark.garksport.controller;

import com.gark.garksport.modal.*;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.service.EntraineurService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {
    @Autowired
    private EntraineurService entraineurService;

    @Autowired
    private AdherentRepository adherentRepository;


    //@PreAuthorize("hasAuthority('ROLE_STAFF')")
    @PreAuthorize("hasAnyAuthority('STAFF_READ','management:read')")
    @GetMapping("/hello")
    public String getHello(){
        return "first hello from staff controller";
    }

    @PreAuthorize("hasAuthority('STAFF_SECOND_READ')")
    @GetMapping("/hello2")
    public String getHello2(){
        return "second hello from staff controller";
    }


    @PostMapping("/save-evaluation")
    public ResponseEntity<Evaluation> saveEvaluation(@RequestParam Integer adherentId, @RequestBody Evaluation evaluation,
                                                     @RequestParam boolean clearOldFields) {
        Evaluation savedEvaluation = entraineurService.saveEvaluation(adherentId, evaluation, clearOldFields);
        return ResponseEntity.ok(savedEvaluation);
    }


    @GetMapping("/get-evaluations")
    public ResponseEntity<List<Evaluation>> getEvaluations(@RequestParam Integer adherentId) {
        Optional<Adherent> adherentOptional = adherentRepository.findById(adherentId);
        if (adherentOptional.isPresent()) {
            Adherent adherent = adherentOptional.get();
            return ResponseEntity.ok(adherent.getEvaluations());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
