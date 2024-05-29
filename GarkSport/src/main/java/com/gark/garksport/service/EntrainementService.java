package com.gark.garksport.service;

import com.gark.garksport.dto.request.EntrainementRequest;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.ConvocationEntrainement;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.ConvocationEntrainementRepository;
import com.gark.garksport.repository.EquipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EntrainementService implements IEntrainementService{
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private ConvocationEntrainementRepository convocationEntrainementRepository;

    @Override
    public Equipe addEntrainement(EntrainementRequest entrainementRequest, Integer idEquipe) {
        Optional<Equipe> optionalEquipe = equipeRepository.findById(idEquipe);
        if (optionalEquipe.isPresent()) {
            Equipe equipe = optionalEquipe.get();

            ConvocationEntrainement convocationEntrainement = entrainementRequest.getConvocationEntrainement();

            // Fetch adherents by IDs
            List<Adherent> adherents = entrainementRequest.getIdAdherents().stream()
                    .map(id -> adherentRepository.findById(id).orElseThrow(() -> new RuntimeException("Adherent not found with ID: " + id)))
                    .collect(Collectors.toList());

            // Set the fetched adherents to the convocationEntrainement
            convocationEntrainement.setAdherents(adherents);

            // Save the convocationEntrainement first to persist it
            ConvocationEntrainement savedConvocationEntrainement = convocationEntrainementRepository.save(convocationEntrainement);

            // Add the savedConvocationEntrainement to the equipe
            equipe.getConvocations().add(savedConvocationEntrainement);

            // Save the equipe
            equipeRepository.save(equipe);
            return equipe;
        }
        return null;
    }

    @Override
    public List<Equipe> getEntrainements() {
        return equipeRepository.findAll();
    }

    @Override
    public void deleteEntrainement(Integer idConvocation) {
        // Find the convocation
        ConvocationEntrainement convocation = convocationEntrainementRepository.findById(idConvocation)
                .orElseThrow(() -> new EntityNotFoundException("Convocation not found"));

        // Find the equipe associated with this convocation
        Equipe equipe = equipeRepository.findByConvocationsContains(convocation)
                .orElseThrow(() -> new EntityNotFoundException("Equipe not found for this convocation"));

        // Remove the convocation from the equipe
        equipe.getConvocations().remove(convocation);

        // Save the equipe
        equipeRepository.save(equipe);

        // Delete the convocation
        convocationEntrainementRepository.deleteById(idConvocation);
    }

    @Override
    public List<Adherent> getAdherentsByConvocation(Integer idConvocation) {
        ConvocationEntrainement convocation = convocationEntrainementRepository.findById(idConvocation)
                .orElseThrow(() -> new EntityNotFoundException("Convocation not found"));
        return convocation.getAdherents();
    }

    @Override
    public Equipe updateConvocationEntrainement(EntrainementRequest entrainementRequest, Integer idConvocation) {
        ConvocationEntrainement convocationEntrainement = entrainementRequest.getConvocationEntrainement();
        ConvocationEntrainement convocation = convocationEntrainementRepository.findById(idConvocation)
                .orElseThrow(() -> new EntityNotFoundException("Convocation not found"));

        List<Adherent> adherents = entrainementRequest.getIdAdherents().stream()
                .map(id -> adherentRepository.findById(id).orElseThrow(() -> new RuntimeException("Adherent not found with ID: " + id)))
                .collect(Collectors.toList());
        convocation.setHeure(convocationEntrainement.getHeure());
        convocation.setAdherents(adherents);
        convocationEntrainementRepository.save(convocation);
        return equipeRepository.findByConvocationsContains(convocation)
                .orElseThrow(() -> new EntityNotFoundException("Equipe not found for this convocation"));
    }




}
