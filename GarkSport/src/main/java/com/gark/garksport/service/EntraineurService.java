package com.gark.garksport.service;

import com.gark.garksport.modal.*;
import com.gark.garksport.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntraineurService {
    @Autowired
    private AdherentRepository adherentRepository;
    @Autowired
    private ValKpisRepository valKpisRepository;
    @Autowired
    private KpiRepository kpiRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private KpiRepository kpiRepository;

    //////////////////// EVALUATION PART ///////////////////////////////////


    public ResponseEntity<Evaluation> addFieldsToEvaluation(Long evaluationId, Kpi request) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
        if (evaluationOptional.isPresent()) {
            Evaluation evaluation = evaluationOptional.get();
            Kpi kpi = new Kpi(request.getKpiType());
            kpi.setKpiType(request.getKpiType());
            kpi.setEvaluation(evaluation); // Set the evaluation reference in the Kpi instance
            evaluation.getKpis().add(kpi);

            Evaluation savedEvaluation = evaluationRepository.save(evaluation);
            return ResponseEntity.ok(savedEvaluation);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Test> addTest(Integer academieId, Test testRequest){
        Optional<Academie> academieOpt = academieRepository.findById(academieId);

        if (!academieOpt.isPresent()) {
            return ResponseEntity.badRequest().body(null); // Or handle this case appropriately
        }

        Academie academie = academieOpt.get();
        Test newTest = new Test();
        newTest.setTestName(testRequest.getTestName());
        newTest.setAcademie(academie); // Associate the test with the academie


        for (Categorie categorieRequest : testRequest.getCategories()) {
            Categorie newCategorie = new Categorie(categorieRequest.getCategorieName());
            newCategorie.setTest(newTest); // Associate the category with the test
            newTest.getCategories().add(newCategorie);
        }

        Test savedTest = testRepository.save(newTest); // Save the category along with its KPIs

        return ResponseEntity.ok(savedTest);
    }

    public ResponseEntity<Test> addTestWithCategories(Test testRequest, Principal connectedUser) {
        User user = getProfil(connectedUser);

        Manager manager = (Manager) user;
        Academie academie = academieRepository.findByManagerId(manager.getId());

        if (academie == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Test newTest = new Test();
        newTest.setTestName(testRequest.getTestName());
        newTest.setAcademie(academie); // Associate the test with the academie

        // Add categories
        for (Categorie categorieRequest : testRequest.getCategories()) {
            Categorie newCategorie = new Categorie(categorieRequest.getCategorieName());
            newCategorie.setTest(newTest); // Associate the category with the test
            newTest.getCategories().add(newCategorie);

            for (Kpi kpiRequest : categorieRequest.getKpis()) {
                Kpi kpi = new Kpi(kpiRequest.getKpiType());
                kpi.setCategorie(newCategorie); // Set the category reference in the KPI instance
                newCategorie.getKpis().add(kpi); // Add the KPI to the category
            }
        }

        Test savedTest = testRepository.save(newTest); // Save the test along with its categories and KPIs

        return ResponseEntity.ok(savedTest);
    }

    //TODO duplicate test

    public Test getTest(Integer testId){
        Test test = testRepository.findById(testId).orElseThrow(() -> new IllegalArgumentException("Test not found"));
        return test;
    }

    public void deleteKpi(Integer kpiId) {
        Kpi kpi = kpiRepository.findById(kpiId)
                .orElseThrow(() -> new ResourceNotFoundException("ValKpi not found with id: " + kpiId));

        kpiRepository.delete(kpi);
    }

    public ValKpis updateValKpi(Integer valKpiId, String newValue) {
        ValKpis valKpi = valKpisRepository.findById(valKpiId)
                .orElseThrow(() -> new ResourceNotFoundException("ValKpi not found with id: " + valKpiId));

        valKpi.setValKpi(newValue);
        return valKpisRepository.save(valKpi);
    }

    public Kpi updateKpi(Integer kpiId, String newValue) {
        Kpi kpi = kpiRepository.findById(kpiId)
                .orElseThrow(() -> new ResourceNotFoundException("Kpi not found with id: " + kpiId));

        kpi.setKpiType(newValue);
        return kpiRepository.save(kpi);
    }

    public List<Test> getTestsByAcademieId(Integer academieId) {
        return testRepository.findByAcademieId(academieId);
    }

    public Optional<Test> getTestById(Integer testId) {
        return testRepository.findById(testId);
    }

    public List<Categorie> getCategoriesByTestId(Integer testId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new ResourceNotFoundException("Test not found"));
        return test.getCategories();
    }

    public ResponseEntity<Categorie> addCategorie(Principal connectedUser, Integer testId, Categorie requestCategorie){
        User user = getProfil(connectedUser);

        Manager manager = (Manager) user;
        Academie academie = academieRepository.findByManagerId(manager.getId());


        Optional<Test> testOptional = testRepository.findById(testId);
        if (!testOptional.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        Test test = testOptional.get();

        Categorie newCategorie = new Categorie(requestCategorie.getCategorieName());
        newCategorie.setTest(test); // Associate the category with the test

        for (Kpi kpiRequest : requestCategorie.getKpis()) {
            Kpi kpi = new Kpi(kpiRequest.getKpiType());
            kpi.setCategorie(newCategorie); // Set the category reference in the KPI instance
            newCategorie.getKpis().add(kpi); // Add the KPI to the category
        }

        Categorie savedCategorie = categorieRepository.save(newCategorie); // Save the category along with its KPIs

        return ResponseEntity.ok(savedCategorie);
    }

    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Evaluation> updateDynamicFieldInEvaluation(Long evaluationId, Integer dynamicFieldId,
                                                                     Kpi request) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
        if (evaluationOptional.isPresent()) {
            Evaluation evaluation = evaluationOptional.get();
            Optional<Kpi> dynamicFieldOptional = kpiRepository.findById(dynamicFieldId);
            if (dynamicFieldOptional.isPresent()) {
                Kpi dynamicField = dynamicFieldOptional.get();
                dynamicField.setKpiType(request.getKpiType());

    @Transactional
    public void assignKpiValues(Integer adherentId, Integer testId, Integer categoryId, List<Integer> kpiIds, List<String> kpiValues) {
        if (kpiIds.size() != kpiValues.size()) {
            throw new IllegalArgumentException("The number of KPI IDs must match the number of KPI values.");
        }

        Adherent adherent = adherentRepository.findById(adherentId)
                .orElseThrow(() -> new ResourceNotFoundException("Adherent not found with id " + adherentId));

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found with id " + testId));



        Categorie category = categorieRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        if (!category.getTest().getId().equals(test.getId())) {
            throw new IllegalArgumentException("Category does not belong to the specified test.");
        }

        for (int i = 0; i < kpiIds.size(); i++) {
            Integer kpiId = kpiIds.get(i);
            String kpiValue = kpiValues.get(i);

            Kpi kpi = kpiRepository.findById(kpiId)
                    .orElseThrow(() -> new ResourceNotFoundException("KPI not found with id " + kpiId));

            if (!kpi.getCategorie().getId().equals(category.getId())) {
                throw new IllegalArgumentException("KPI does not belong to the specified category.");
            }

            ValKpis valKpi = kpi.getValkpi();
            if (valKpi == null) {
                valKpi = new ValKpis();
                valKpi.setKpi(kpi);
                kpi.setValkpi(valKpi);
            }
            valKpi.setValKpi(kpiValue);
            valKpisRepository.save(valKpi);
        }

        adherentRepository.save(adherent);
    }

    public void deleteKpiValue(Integer valKpiId) {
        ValKpis valKpi = valKpisRepository.findById(valKpiId)
                .orElseThrow(() -> new ResourceNotFoundException("ValKpi not found with id: " + valKpiId));

        // Dissociate the ValKpis from any Kpi that references it
        List<Kpi> kpis = kpiRepository.findByValkpi_Id(valKpiId);
        for (Kpi kpi : kpis) {
            kpi.setValkpi(null);
            kpiRepository.save(kpi);
        }

        valKpisRepository.delete(valKpi);
    }
    @Transactional
    public void deleteTest(Integer testId) {
        testRepository.deleteById(testId);
    }

    @Transactional
    public void deleteCategorie(Integer categoryId) {
        categorieRepository.deleteById(categoryId);
    }
}