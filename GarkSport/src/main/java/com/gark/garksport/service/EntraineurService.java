package com.gark.garksport.service;

import com.gark.garksport.modal.*;
import com.gark.garksport.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntraineurService {
    private final UserRepository repository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private AcademieRepository academieRepository;
    @Autowired
    private AcademieService academieService;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private AdherentRepository adherentRepository;

    //////////////////// EVALUATION PART ///////////////////////////////////

    public User getProfil(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<User> userOptional = repository.findById(user.getId());

        return userOptional.orElse(null);
    }

    public ResponseEntity<Categorie> addFieldsToCategory(Integer categorieId, Kpi request) {
        Optional<Categorie> categorieOptional = categorieRepository.findById(categorieId);
        if (categorieOptional.isPresent()) {
            Categorie categorie = categorieOptional.get();
            Kpi kpi = new Kpi(request.getKpiType());
            kpi.setCategorie(categorie); // Set the category reference in the Kpi instance
            categorie.getKpis().add(kpi); // Add the Kpi to the category

            Categorie savedCategorie = categorieRepository.save(categorie); // Save the category
            return ResponseEntity.ok(savedCategorie);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Test> addTest(Principal connectedUser) {
        User user = getProfil(connectedUser);

        Manager manager = (Manager) user;
        Academie academie = academieRepository.findByManagerId(manager.getId());

        academieService.addTest(academie);
        return ResponseEntity.ok(academie.getTests().get(academie.getTests().size() - 1));
    }

    public Test getTest(Integer testId){
        Test test = testRepository.findById(testId).orElseThrow(() -> new IllegalArgumentException("Test not found"));
        return test;
    }

    public void assignKpiValue(Integer testId, Integer categoryId, Integer kpiId, Integer adherentId, String kpiValue) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new IllegalArgumentException("Test not found"));

        // Find the category in the test
        Optional<Categorie> optionalCategory = test.getCategories().stream()
                .filter(category -> category.getId().equals(categoryId))
                .findFirst();

        if (optionalCategory.isPresent()) {
            Categorie category = optionalCategory.get();

            // Find the KPI in the category
            Optional<Kpi> optionalKpi = category.getKpis().stream()
                    .filter(kpi -> kpi.getId().equals(kpiId))
                    .findFirst();

            if (optionalKpi.isPresent()) {
                Kpi kpi = optionalKpi.get();

                // Create a ValKpi and set its value
                ValKpis valKpis = new ValKpis();
                valKpis.setValKpi(kpiValue);
                valKpis.setKpi(kpi);

                // Find the adherent
                Adherent adherent = adherentRepository.findById(adherentId)
                        .orElseThrow(() -> new IllegalArgumentException("Adherent not found"));

                adherent.getTests().add(test);
                adherentRepository.save(adherent);

                // Add the ValKpi to the KPI and save the test
                kpi.setValkpi(valKpis);
                testRepository.save(test);
            } else {
                throw new IllegalArgumentException("KPI not found in category");
            }
        } else {
            throw new IllegalArgumentException("Category not found in test");
        }
    }



//    public ResponseEntity<Evaluation> removeDynamicFieldFromEvaluation(Long evaluationId, Integer kpiId) {
//        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
//        if (evaluationOptional.isPresent()) {
//            Evaluation evaluation = evaluationOptional.get();
//            Optional<Kpi> kpiOptional = kpiRepository.findById(kpiId);
//            if (kpiOptional.isPresent()) {
//                Kpi dynamicField = kpiOptional.get();
//                if (evaluation.getKpis().remove(dynamicField)) {
//                    kpiRepository.delete(dynamicField);
//                    Evaluation updatedEvaluation = evaluationRepository.save(evaluation);
//                    return ResponseEntity.ok(updatedEvaluation);
//                } else {
//                    return ResponseEntity.badRequest().body(null);
//                }
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    public ResponseEntity<Evaluation> updateDynamicFieldInEvaluation(Long evaluationId, Integer dynamicFieldId,
//                                                                     Kpi request) {
//        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
//        if (evaluationOptional.isPresent()) {
//            Evaluation evaluation = evaluationOptional.get();
//            Optional<Kpi> dynamicFieldOptional = kpiRepository.findById(dynamicFieldId);
//            if (dynamicFieldOptional.isPresent()) {
//                Kpi dynamicField = dynamicFieldOptional.get();
//                dynamicField.setKpiType(request.getKpiType());
//
//                Evaluation updatedEvaluation = evaluationRepository.save(evaluation);
//                kpiRepository.save(dynamicField);
//                return ResponseEntity.ok(updatedEvaluation);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    public ResponseEntity<?> fillEvaluationFormAndSetForAdherent(Long evaluationId, Integer adherentId, Evaluation formData) {
//        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
//        Optional<Adherent> adherentOptional = adherentRepository.findById(adherentId);
//
//        if (evaluationOptional.isPresent() && adherentOptional.isPresent()) {
//            Evaluation evaluationTemplate = evaluationOptional.get();
//            Adherent adherent = adherentOptional.get();
//
//            // Create a new Evaluation object to store the filled data
//            Evaluation filledEvaluation = new Evaluation();
//            filledEvaluation.setAdherent(adherent);
//            filledEvaluation.setEvaluationName(evaluationTemplate.getEvaluationName());
//            filledEvaluation.setKpis(new ArrayList<>());

            // Fill the dynamic fields with the data from the form
//            for (Kpi fieldData : formData.getDynamicFields()) {
//                Kpi dynamicField = new Kpi();
//                dynamicField.setFieldName(fieldData.getFieldName());
//                dynamicField.setUnit(fieldData.getUnit());
//                dynamicField.setValue(fieldData.getValue());
//                filledEvaluation.getDynamicFields().add(dynamicField);
//            }

            // Save the filled evaluation
//            Evaluation savedEvaluation = evaluationRepository.save(filledEvaluation);
//
//            // Add the filled evaluation to the adherent's list of evaluations
//            adherent.getEvaluations().add(savedEvaluation);
//            adherentRepository.save(adherent);
//
//            return ResponseEntity.ok(savedEvaluation);
//        }
//
//        return ResponseEntity.badRequest().build();
//    }


//    public Kpi addDynamicFieldToEvaluation(Long evaluationId, Kpi dynamicField) {
//        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
//        if (evaluationOptional.isPresent()) {
//            Evaluation evaluation = evaluationOptional.get();
//            dynamicField.setEvaluation(evaluation);
//            return kpiRepository.save(dynamicField);
//        } else {
//            throw new RuntimeException("Evaluation not found");
//        }
//    }
//    public ResponseEntity<RoleName> addRoleName(RoleName request, Principal connectedUser) {
//        User user = getProfil(connectedUser);
//        if (user instanceof Manager) {
//            Manager manager = (Manager) user;
//            Academie academie = academieRepository.findByManagerId(manager.getId());
//            if (academie != null) {
//                RoleName roleName = new RoleName();
//                roleName.setName(request.getName());
//                roleName.setPermissions(request.getPermissions());
//                roleName.setAcademie(academie);
//                academie.getRoleNames().add(roleName);
//                academieRepository.save(academie);
//                return ResponseEntity.ok(roleName);
//            }
//        }
//        return ResponseEntity.badRequest().build();
//    }

    //    public Evaluation createEvaluation(Integer equipeId) {
//        Optional<Equipe> equipeOptional = equipeRepository.findById(equipeId);
//        if (equipeOptional.isPresent()) {
//            Evaluation evaluation = new Evaluation();
//            Equipe equipe = equipeOptional.get();
//            evaluation.setEquipe(equipe); // Associate the evaluation with the equipe
//            return evaluationRepository.save(evaluation);
//        } else {
//            throw new RuntimeException("Equipe not found with id " + equipeId);
//        }
//    }


}