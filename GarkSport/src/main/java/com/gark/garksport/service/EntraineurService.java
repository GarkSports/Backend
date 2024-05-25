package com.gark.garksport.service;

import com.gark.garksport.modal.*;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.DynamicFieldRepository;
import com.gark.garksport.repository.EquipeRepository;
import com.gark.garksport.repository.EvaluationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private DynamicFieldRepository dynamicFieldRepository;

    //////////////////// EVALUATION PART ///////////////////////////////////


    public ResponseEntity<Evaluation> addFieldsToEvaluation(Long evaluationId, DynamicField request) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
        if (evaluationOptional.isPresent()) {
            Evaluation evaluation = evaluationOptional.get();
            DynamicField dynamicField = new DynamicField();
            dynamicField.setFieldName(request.getFieldName());
            dynamicField.setUnit(request.getUnit());
            evaluation.getDynamicFields().add(dynamicField);

            Evaluation savedEvaluation = evaluationRepository.save(evaluation);
            dynamicField.setEvaluation(savedEvaluation);
            dynamicFieldRepository.save(dynamicField);
            return ResponseEntity.ok(savedEvaluation);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Evaluation> removeDynamicFieldFromEvaluation(Long evaluationId, Integer dynamicFieldId) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
        if (evaluationOptional.isPresent()) {
            Evaluation evaluation = evaluationOptional.get();
            Optional<DynamicField> dynamicFieldOptional = dynamicFieldRepository.findById(dynamicFieldId);
            if (dynamicFieldOptional.isPresent()) {
                DynamicField dynamicField = dynamicFieldOptional.get();
                if (evaluation.getDynamicFields().remove(dynamicField)) {
                    dynamicFieldRepository.delete(dynamicField);
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
                                                                     DynamicField request) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
        if (evaluationOptional.isPresent()) {
            Evaluation evaluation = evaluationOptional.get();
            Optional<DynamicField> dynamicFieldOptional = dynamicFieldRepository.findById(dynamicFieldId);
            if (dynamicFieldOptional.isPresent()) {
                DynamicField dynamicField = dynamicFieldOptional.get();
                dynamicField.setFieldName(request.getFieldName());
                dynamicField.setUnit(request.getUnit());
                dynamicField.setValue(request.getValue());

                Evaluation updatedEvaluation = evaluationRepository.save(evaluation);
                dynamicFieldRepository.save(dynamicField);
                return ResponseEntity.ok(updatedEvaluation);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

//    public ResponseEntity<Evaluation> updateEvaluation(Integer evaluationId, Evaluation evaluation){
//
//    }

    public DynamicField addDynamicFieldToEvaluation(Long evaluationId, DynamicField dynamicField) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluationId);
        if (evaluationOptional.isPresent()) {
            Evaluation evaluation = evaluationOptional.get();
            dynamicField.setEvaluation(evaluation);
            return dynamicFieldRepository.save(dynamicField);
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