package com.gark.garksport.controller;

import com.gark.garksport.modal.*;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.service.EntraineurService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Categorie> addFieldsToCategory(@RequestParam Integer categorieId,
                                                         @RequestBody Kpi kpis){
        Categorie newCategorie = entraineurService.addFieldsToCategory(categorieId, kpis).getBody();
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

    @PostMapping("/add-categorie")
    public ResponseEntity<Categorie> addCategorie(Principal connectedUser,
                                                  @RequestParam Integer testId,
                                                  @RequestBody Categorie requestCategorie
                                                  ){
        return entraineurService.addCategorie(connectedUser,testId,requestCategorie);
    }

    @PostMapping("/adherents/{adherentId}/tests/{testId}/categories/{categoryId}/assignKpiValues")
    public ResponseEntity<Void> assignKpiValues(
            @PathVariable Integer adherentId,
            @PathVariable Integer testId,
            @PathVariable Integer categoryId,
            @RequestBody Map<String, String> kpiValuesMap) {

        List<Integer> kpiIds = new ArrayList<>();
        List<String> kpiValues = new ArrayList<>();

        kpiValuesMap.forEach((key, value) -> {
            kpiIds.add(Integer.parseInt(key));
            kpiValues.add(value);
        });

        entraineurService.assignKpiValues(adherentId, testId, categoryId, kpiIds, kpiValues);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/academies/{academieId}/tests")
    public ResponseEntity<List<Test>> getTestsByAcademieId(@PathVariable Integer academieId) {
        List<Test> tests = entraineurService.getTestsByAcademieId(academieId);
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<Optional<Test>> getTestById(@PathVariable Integer testId) {
        Optional<Test> test = entraineurService.getTestById(testId);
        return ResponseEntity.ok(test);
    }

    @GetMapping("/{testId}/categories")
    public ResponseEntity<List<Categorie>> getCategoriesByTestId(@PathVariable Integer testId) {
        List<Categorie> categories = entraineurService.getCategoriesByTestId(testId);
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/valKpi/{valKpiId}")
    public ResponseEntity<Void> deleteKpiValue(@PathVariable Integer valKpiId) {
        entraineurService.deleteKpiValue(valKpiId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/kpi/{kpiId}")
    public ResponseEntity<Void> deleteKpi(@PathVariable Integer kpiId) {
        entraineurService.deleteKpi(kpiId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/kpi/{kpiId}")
    public ResponseEntity<Kpi> updateKpi(@PathVariable Integer kpiId, @RequestBody Map<String, String> updates) {
        String newValue = updates.get("kpiType");
        Kpi updatedKpi = entraineurService.updateKpi(kpiId, newValue);
        return ResponseEntity.ok(updatedKpi);
    }
    @PutMapping("/valKpi/{valKpiId}")
    public ResponseEntity<ValKpis> updateValKpi(@PathVariable Integer valKpiId, @RequestBody Map<String, String> updates) {
        String newValue = updates.get("valKpi");
        ValKpis updatedValKpi = entraineurService.updateValKpi(valKpiId, newValue);
        return ResponseEntity.ok(updatedValKpi);
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
