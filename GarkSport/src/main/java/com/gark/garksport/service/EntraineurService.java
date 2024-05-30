package com.gark.garksport.service;

import com.gark.garksport.modal.*;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.KpiRepository;
import com.gark.garksport.repository.EquipeRepository;
import com.gark.garksport.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntraineurService {
    @Autowired
    private AdherentRepository adherentRepository;

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

    public ResponseEntity<Evaluation> removeDynamicFieldFromEvaluation(Long evaluationId, Integer kpiId) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
        if (evaluationOptional.isPresent()) {
            Evaluation evaluation = evaluationOptional.get();
            Optional<Kpi> kpiOptional = kpiRepository.findById(kpiId);
            if (kpiOptional.isPresent()) {
                Kpi dynamicField = kpiOptional.get();
                if (evaluation.getKpis().remove(dynamicField)) {
                    kpiRepository.delete(dynamicField);
                    Evaluation updatedEvaluation = evaluationRepository.save(evaluation);
                    return ResponseEntity.ok(updatedEvaluation);
                } else {
                    return ResponseEntity.badRequest().body(null);
                }
            } else {
                return ResponseEntity.notFound().build();
            }
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

                Evaluation updatedEvaluation = evaluationRepository.save(evaluation);
                kpiRepository.save(dynamicField);
                return ResponseEntity.ok(updatedEvaluation);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> fillEvaluationFormAndSetForAdherent(Long evaluationId, Integer adherentId, Evaluation formData) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
        Optional<Adherent> adherentOptional = adherentRepository.findById(adherentId);

        if (evaluationOptional.isPresent() && adherentOptional.isPresent()) {
            Evaluation evaluationTemplate = evaluationOptional.get();
            Adherent adherent = adherentOptional.get();

            // Create a new Evaluation object to store the filled data
            Evaluation filledEvaluation = new Evaluation();
            filledEvaluation.setAdherent(adherent);
            filledEvaluation.setEvaluationName(evaluationTemplate.getEvaluationName());
            filledEvaluation.setKpis(new ArrayList<>());

            // Fill the dynamic fields with the data from the form
//            for (Kpi fieldData : formData.getDynamicFields()) {
//                Kpi dynamicField = new Kpi();
//                dynamicField.setFieldName(fieldData.getFieldName());
//                dynamicField.setUnit(fieldData.getUnit());
//                dynamicField.setValue(fieldData.getValue());
//                filledEvaluation.getDynamicFields().add(dynamicField);
//            }

            // Save the filled evaluation
            Evaluation savedEvaluation = evaluationRepository.save(filledEvaluation);

            // Add the filled evaluation to the adherent's list of evaluations
            adherent.getEvaluations().add(savedEvaluation);
            adherentRepository.save(adherent);

            return ResponseEntity.ok(savedEvaluation);
        }

        return ResponseEntity.badRequest().build();
    }


//    public ResponseEntity<Evaluation> updateEvaluation(Integer evaluationId, Evaluation evaluation){
//
//    }

    public Kpi addDynamicFieldToEvaluation(Long evaluationId, Kpi dynamicField) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
        if (evaluationOptional.isPresent()) {
            Evaluation evaluation = evaluationOptional.get();
            dynamicField.setEvaluation(evaluation);
            return kpiRepository.save(dynamicField);
        } else {
            throw new RuntimeException("Evaluation not found");
        }
    }
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