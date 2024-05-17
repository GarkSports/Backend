package com.gark.garksport.service;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.modal.Evenement;
import com.gark.garksport.modal.enums.EvenementType;
import com.gark.garksport.modal.enums.StatutEvenenement;
import com.gark.garksport.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class EvenementService implements IEvenementService {
    @Autowired
    private EvenementRepository evenementRepository;
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private AdherentRepository adherentRepository;
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public Evenement addCompetition(Evenement evenement, Integer idEquipe, List<Integer> idMembres, Integer managerId) {
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());
        // Check if both idEquipe and idMembres are provided
        if ((idEquipe != null && idEquipe != 0) && (idMembres != null && !idMembres.isEmpty())) {
            throw new IllegalArgumentException("Both idEquipe and idMembres cannot be provided simultaneously.");
        }

        // Check if idEquipe is provided
        if (idEquipe != null && idEquipe != 0) {
            // Retrieve the Equipe from the repository using idEquipe
            Equipe equipe = equipeRepository.findById(idEquipe)
                    .orElseThrow(() -> new NoSuchElementException("Equipe not found with id: " + idEquipe));

            // Set the convocationEquipe of the evenement
            evenement.setConvocationEquipe(equipe);

            Set<Adherent> membres = new HashSet<>(equipe.getAdherents());
            evenement.setConvocationMembres(membres);

            // Set the type of the evenement
            evenement.setType(EvenementType.COMPETITION);

            // Save the evenement
            return evenementRepository.save(evenement);
        }

        // Check if idMembres is provided
        if (idMembres != null && !idMembres.isEmpty()) {
            // Retrieve the Adherents from the repository using idMembres
            Set<Adherent> membres = adherentRepository.findByIdIn(idMembres);
            // Set the convocationMembres of the evenement
            evenement.setConvocationMembres(membres);
            Adherent firstMember = membres.iterator().next();
            Equipe equipe = equipeRepository.findById(firstMember.getEquipeId()).get();
            evenement.setConvocationEquipe(equipe);

            // Set the type of the evenement
            evenement.setType(EvenementType.COMPETITION);

            // Save the evenement
            return evenementRepository.save(evenement);
        }

        // If neither idEquipe nor idMembres are provided, throw IllegalArgumentException
        throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
    }

    @Override
    public Evenement addPersonnalisé(Evenement evenement, Integer idEquipe, List<Integer> idMembres, Integer managerId) {
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());
        // Check if both idEquipe and idMembres are provided
        if ((idEquipe != null && idEquipe != 0) && (idMembres != null && !idMembres.isEmpty())) {
            throw new IllegalArgumentException("Both idEquipe and idMembres cannot be provided simultaneously.");
        }

        // Check if idEquipe is provided
        if (idEquipe != null && idEquipe != 0) {
            // Retrieve the Equipe from the repository using idEquipe
            Equipe equipe = equipeRepository.findById(idEquipe)
                    .orElseThrow(() -> new NoSuchElementException("Equipe not found with id: " + idEquipe));

            // Set the convocationEquipe of the evenement
            evenement.setConvocationEquipe(equipe);

            Set<Adherent> membres = new HashSet<>(equipe.getAdherents());
            evenement.setConvocationMembres(membres);

            // Set the type of the evenement
            evenement.setType(EvenementType.EVENEMENT_PERSONNALISE);

            // Save the evenement
            return evenementRepository.save(evenement);
        }

        // Check if idMembres is provided
        if (idMembres != null && !idMembres.isEmpty()) {
            // Retrieve the Adherents from the repository using idMembres
            Set<Adherent> membres = adherentRepository.findByIdIn(idMembres);
            // Set the convocationMembres of the evenement
            evenement.setConvocationMembres(membres);
            Adherent firstMember = membres.iterator().next();
            Equipe equipe = equipeRepository.findById(firstMember.getEquipeId()).get();
            evenement.setConvocationEquipe(equipe);

            // Set the type of the evenement
            evenement.setType(EvenementType.EVENEMENT_PERSONNALISE);

            // Save the evenement
            return evenementRepository.save(evenement);
        }

        // If neither idEquipe nor idMembres are provided, throw IllegalArgumentException
        throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
    }

    @Override
    public Evenement addTest(Evenement evenement, Integer idEquipe, List<Integer> idMembres, Integer managerId) {
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());
        // Check if both idEquipe and idMembres are provided
        if ((idEquipe != null && idEquipe != 0) && (idMembres != null && !idMembres.isEmpty())) {
            throw new IllegalArgumentException("Both idEquipe and idMembres cannot be provided simultaneously.");
        }

        // Check if idEquipe is provided
        if (idEquipe != null && idEquipe != 0) {
            // Retrieve the Equipe from the repository using idEquipe
            Equipe equipe = equipeRepository.findById(idEquipe)
                    .orElseThrow(() -> new NoSuchElementException("Equipe not found with id: " + idEquipe));

            // Set the convocationEquipe of the evenement
            evenement.setConvocationEquipe(equipe);

            Set<Adherent> membres = new HashSet<>(equipe.getAdherents());
            evenement.setConvocationMembres(membres);

            // Set the type of the evenement
            evenement.setType(EvenementType.TEST_EVALUATION);

            // Save the evenement
            return evenementRepository.save(evenement);
        }

        // Check if idMembres is provided
        if (idMembres != null && !idMembres.isEmpty()) {
            // Retrieve the Adherents from the repository using idMembres
            Set<Adherent> membres = adherentRepository.findByIdIn(idMembres);
            // Set the convocationMembres of the evenement
            evenement.setConvocationMembres(membres);
            Adherent firstMember = membres.iterator().next();
            Equipe equipe = equipeRepository.findById(firstMember.getEquipeId()).get();
            evenement.setConvocationEquipe(equipe);

            // Set the type of the evenement
            evenement.setType(EvenementType.TEST_EVALUATION);

            // Save the evenement
            return evenementRepository.save(evenement);
        }

        // If neither idEquipe nor idMembres are provided, throw IllegalArgumentException
        throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
    }

    @Override
    public Evenement addMatchAmical(Evenement evenement, Integer equipeId, LocalTime horraire, Integer managerId) {
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());
        evenement.setType(EvenementType.MATCH_AMICAL);
        evenement.setHeure(horraire);

        evenement.setConvocationEquipe(equipeRepository.findById(equipeId).get());
        Equipe equipe = equipeRepository.findById(equipeId).get();

        Set<Adherent> membres = new HashSet<>(equipe.getAdherents());
        evenement.setConvocationMembres(membres);

        equipe.setDateMatchAmical(horraire);
        equipeRepository.save(equipe);

        evenement = evenementRepository.save(evenement);
        return evenement;
    }

    @Override
    public List<Evenement> getAllEvenements() {
        return evenementRepository.findAll();
    }

    @Override
    public void deleteEvenement(Integer id) {
        evenementRepository.deleteById(id);
    }

    @Override
    public Evenement changeStatutEvenement(Integer id, StatutEvenenement statutEvenenement) {
        Evenement evenement = evenementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evenement not found with id: " + id));
        evenement.setStatut(statutEvenenement);
        return evenementRepository.save(evenement);
    }

    @Override
    public List<Adherent> getMembersByEquipe(Integer idEquipe) {
        Equipe equipe = equipeRepository.findById(idEquipe)
                .orElseThrow(() -> new NoSuchElementException("Equipe not found with id: " + idEquipe));
        return new ArrayList<>(equipe.getAdherents());
    }


}
