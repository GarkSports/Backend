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

    @PostMapping("/add-test-with-categories")
    @ResponseStatus
    public ResponseEntity<Test> addTestWithCategories(@RequestBody Test testRequest, Principal connectedUser) {

        return entraineurService.addTestWithCategories(testRequest, connectedUser);
    }

    @PostMapping("/add-test")
    @ResponseStatus
    public ResponseEntity<Test> addTest(@RequestParam Integer academieId, @RequestBody Test testRequest) {

        return entraineurService.addTest(academieId, testRequest);
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

    @GetMapping("/hello1")
    public String getHello1(){
        return "hello";
    }

    @DeleteMapping("/tests/{testId}")
    public ResponseEntity<Void> deleteTest(@PathVariable Integer testId) {
        entraineurService.deleteTest(testId);
        return ResponseEntity.noContent().build(); // Return no content (204) on successful deletion
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategorie(@PathVariable Integer categoryId) {
        entraineurService.deleteCategorie(categoryId);
        return ResponseEntity.noContent().build(); // Return no content (204) on successful deletion
    }
}
