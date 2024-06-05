package com.gark.garksport.controller;

import com.gark.garksport.modal.*;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.TestRepository;
import com.gark.garksport.service.EntraineurService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.PrinterInfo;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Categorie> addFieldsToCategory(@RequestParam Integer evaluationId,
                                                      @RequestBody Kpi kpis){
        Categorie newCategorie = entraineurService.addFieldsToCategory(evaluationId, kpis).getBody();
        return ResponseEntity.ok(newCategorie);
    }

    @GetMapping("/getTest")
    public Test getTest(@RequestParam Integer testId){

        return entraineurService.getTest(testId);
    }

    @PostMapping("/add-test")
    @ResponseStatus
    public ResponseEntity<Test> addTest(Principal connectedUser){
        return entraineurService.addTest(connectedUser);
    }

    @PostMapping("/adherents/{adherentId}/tests/{testId}/assign")
    public ResponseEntity<?> assignKpiValue(@PathVariable Integer adherentId, @PathVariable Integer testId) {
        try {Integer testId, Integer categoryId, Integer kpiId, Integer adherentId, String kpiValue
            entraineurService.assignKpiValue(adherentId, testId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


//    @DeleteMapping("/{evaluationId}/dynamicFields/{dynamicFieldId}")
//    public ResponseEntity<Evaluation> removeDynamicFieldFromEvaluation(
//            @PathVariable Long evaluationId,
//            @PathVariable Integer dynamicFieldId) {
//        return entraineurService.removeDynamicFieldFromEvaluation(evaluationId, dynamicFieldId);
//    }
//
//    @PutMapping("/{evaluationId}/dynamicFields/{dynamicFieldId}")
//    public ResponseEntity<Evaluation> updateDynamicFieldInEvaluation(
//            @PathVariable Long evaluationId,
//            @PathVariable Integer dynamicFieldId,
//            @RequestBody Kpi request) {
//        return entraineurService.updateDynamicFieldInEvaluation(evaluationId, dynamicFieldId, request);
//    }
//
//    @PutMapping("/evaluation/{evaluationId}/adherent/{adherentId}")
//    public ResponseEntity<?> fillEvaluationFormAndSetForAdherent(@PathVariable Long evaluationId,
//                                                                 @PathVariable Integer adherentId,
//                                                                 @RequestBody Evaluation formData) {
//        return entraineurService.fillEvaluationFormAndSetForAdherent(evaluationId, adherentId, formData);
//    }

//    @PostMapping("/save-evaluation")
//    public ResponseEntity<Evaluation> createEvaluation(@RequestParam Integer equipeId) {
//        Evaluation createdEvaluation = entraineurService.createEvaluation(equipeId);
//        return ResponseEntity.ok(createdEvaluation);
//    }

    @GetMapping("/hello1")
    public String getHello1(){
        return "hello";
    }

//    @PostMapping("/evaluations/{evaluationId}/dynamic-fields")
//    public ResponseEntity<Kpi> addDynamicField(@PathVariable Long evaluationId, @RequestBody Kpi dynamicField) {
//        Kpi createdDynamicField = entraineurService.addDynamicFieldToEvaluation(evaluationId, dynamicField);
//        return ResponseEntity.ok(createdDynamicField);
//    }

//    @GetMapping("/get-evaluations")
//    public ResponseEntity<List<Evaluation>> getEvaluations(@RequestParam Integer adherentId) {
//        Optional<Adherent> adherentOptional = adherentRepository.findById(adherentId);
//        if (adherentOptional.isPresent()) {
//            Adherent adherent = adherentOptional.get();
//            return ResponseEntity.ok(adherent.getEvaluations());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
