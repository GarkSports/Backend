package com.gark.garksport.controller;

import com.gark.garksport.modal.*;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.service.EntraineurService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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


    @PutMapping("/add-fields-evaluation")
    @ResponseStatus
    public ResponseEntity<Evaluation> addFieldsToEvaluation(@RequestParam Long evaluationId,
                                                            @RequestBody DynamicField dynamicFields){
        Evaluation newEvaluation = entraineurService.addFieldsToEvaluation(evaluationId, dynamicFields).getBody();
        return ResponseEntity.ok(newEvaluation);
    }

    @DeleteMapping("/{evaluationId}/dynamicFields/{dynamicFieldId}")
    public ResponseEntity<Evaluation> removeDynamicFieldFromEvaluation(
            @PathVariable Long evaluationId,
            @PathVariable Integer dynamicFieldId) {
        return entraineurService.removeDynamicFieldFromEvaluation(evaluationId, dynamicFieldId);
    }

    @PutMapping("/{evaluationId}/dynamicFields/{dynamicFieldId}")
    public ResponseEntity<Evaluation> updateDynamicFieldInEvaluation(
            @PathVariable Long evaluationId,
            @PathVariable Integer dynamicFieldId,
            @RequestBody DynamicField request) {
        return entraineurService.updateDynamicFieldInEvaluation(evaluationId, dynamicFieldId, request);
    }
//    @PostMapping("/save-evaluation")
//    public ResponseEntity<Evaluation> createEvaluation(@RequestParam Integer equipeId) {
//        Evaluation createdEvaluation = entraineurService.createEvaluation(equipeId);
//        return ResponseEntity.ok(createdEvaluation);
//    }

    @GetMapping("/hello1")
    public String getHello1(){
        return "hello";
    }

    @PostMapping("/evaluations/{evaluationId}/dynamic-fields")
    public ResponseEntity<DynamicField> addDynamicField(@PathVariable Long evaluationId, @RequestBody DynamicField dynamicField) {
        DynamicField createdDynamicField = entraineurService.addDynamicFieldToEvaluation(evaluationId, dynamicField);
        return ResponseEntity.ok(createdDynamicField);
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
