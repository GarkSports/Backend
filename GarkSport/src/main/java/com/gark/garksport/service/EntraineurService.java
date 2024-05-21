package com.gark.garksport.service;

import com.gark.garksport.modal.*;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.EvaluationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    //////////////////// EVALUATION PART ///////////////////////////////////
    public Evaluation saveEvaluation(Integer adherentId, Evaluation evaluation, boolean clearOldFields) {
        Adherent adherent = adherentRepository.findById(adherentId)
                .orElseThrow(() -> new RuntimeException("Adherent not found"));

        Optional<Evaluation> existingEvaluationOpt = adherent.getEvaluations().stream()
                .filter(e -> e.getId().equals(evaluation.getId()))
                .findFirst();

        if (existingEvaluationOpt.isPresent()) {
            Evaluation existingEvaluation = existingEvaluationOpt.get();

            existingEvaluation.setPoids(evaluation.getPoids());
            existingEvaluation.setTaille(evaluation.getTaille());
            existingEvaluation.setImc(evaluation.getImc());
            existingEvaluation.setVitesse(evaluation.getVitesse());
            existingEvaluation.setEndurance(evaluation.getEndurance());
            existingEvaluation.setNote(evaluation.getNote());

            if (clearOldFields) {
                existingEvaluation.getDynamicFields().clear();
            }

            List<DynamicField> dynamicFields = evaluation.getDynamicFields();
            Map<Long, DynamicField> existingFieldsMap = existingEvaluation.getDynamicFields().stream()
                    .collect(Collectors.toMap(DynamicField::getId, field -> field));

            dynamicFields.forEach(field -> {
                if (field.getId() != null && existingFieldsMap.containsKey(field.getId())) {
                    DynamicField existingField = existingFieldsMap.get(field.getId());
                    existingField.setName(field.getName());
                    existingField.setValue(field.getValue());
                    existingField.setUnit(field.getUnit());
                } else {
                    field.setEvaluation(existingEvaluation);
                    existingEvaluation.getDynamicFields().add(field);
                }
            });

            existingEvaluation.getDynamicFields().removeIf(field ->
                    dynamicFields.stream().noneMatch(newField ->
                            newField.getId() != null && newField.getId().equals(field.getId())));

            evaluationRepository.save(existingEvaluation);
            return existingEvaluation;
        } else {
            evaluation.setAdherent(adherent);
            Evaluation savedEvaluation = evaluationRepository.save(evaluation);
            adherent.getEvaluations().add(savedEvaluation);
            adherentRepository.save(adherent);
            return savedEvaluation;
        }
    }

}