package com.gark.garksport.service;

import com.gark.garksport.dto.request.EquipeHoraireDTO;
import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.modal.Evenement;
import com.gark.garksport.modal.enums.EvenementType;
import com.gark.garksport.modal.enums.StatutEvenenement;
import com.gark.garksport.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public Integer getEquipesMatchAmical(Integer managerId) {
        // Retrieve all events as a list
        Set<Evenement> allEventsSet = evenementRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        List<Evenement> allEventsList = new ArrayList<>(allEventsSet);

        // Convert the list to a set
        Set<Evenement> allEvents = new HashSet<>(allEventsList);

        // Retrieve teams assigned to Match Amical events
        Set<Equipe> assignedEquipes = allEvents.stream()
                .filter(e -> e.getType() == EvenementType.MATCH_AMICAL)
                .flatMap(e -> e.getConvocationEquipesMatchAmical().stream())
                .collect(Collectors.toSet());

        // Retrieve all teams
        Set<Equipe> allEquipes = equipeRepository.findAll().stream()
                .collect(Collectors.toSet());

        // Filter out teams that are assigned to Match Amical events
        Set<Equipe> filteredEquipes = allEquipes.stream()
                .filter(equipe -> !assignedEquipes.contains(equipe))
                .collect(Collectors.toSet());

        Set<Equipe> filteredEquipes2 = filteredEquipes.stream()
                .filter(equipe -> equipe.getAcademie().getId() == managerRepository.findById(managerId).get().getAcademie().getId())
                .collect(Collectors.toSet());

        return filteredEquipes2.size();
    }

    @Override
    public Integer getEquipesTest(Integer managerId) {
        // Retrieve all events as a list
        Set<Evenement> allEventsSet = evenementRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        List<Evenement> allEventsList = new ArrayList<>(allEventsSet);

        // Convert the list to a set
        Set<Evenement> allEvents = new HashSet<>(allEventsList);

        // Retrieve teams assigned to test events
        Set<Equipe> assignedEquipes = allEvents.stream()
                .filter(e -> e.getType() == EvenementType.TEST_EVALUATION)
                .flatMap(e -> e.getConvocationEquipesTest().stream())
                .collect(Collectors.toSet());

        // Retrieve all teams
        Set<Equipe> allEquipes = equipeRepository.findAll().stream()
                .collect(Collectors.toSet());

        // Filter out teams that are assigned to test events
        Set<Equipe> filteredEquipes = allEquipes.stream()
                .filter(equipe -> !assignedEquipes.contains(equipe))
                .collect(Collectors.toSet());

        Set<Equipe> filteredEquipes2 = filteredEquipes.stream()
                .filter(equipe -> equipe.getAcademie().getId() == managerRepository.findById(managerId).get().getAcademie().getId())
                .collect(Collectors.toSet());

        return filteredEquipes2.size();
    }

    @Override
    public Integer getMembersTest(Integer managerId) {
        // Retrieve all events as a list
        Set<Evenement> allEventsSet = evenementRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        List<Evenement> allEventsList = new ArrayList<>(allEventsSet);

        // Convert the list to a set
        Set<Evenement> allEvents = new HashSet<>(allEventsList);

        // Retrieve members assigned to test events
        Set<Adherent> assignedMembers = allEvents.stream()
                .filter(e -> e.getType() == EvenementType.TEST_EVALUATION)
                .flatMap(e -> e.getConvocationMembresTest().stream())
                .collect(Collectors.toSet());

        // Retrieve all members
        Set<Adherent> allMembers = adherentRepository.findAll().stream()
                .collect(Collectors.toSet());

        // Filter out members that are assigned to test events
        Set<Adherent> filteredMembers = allMembers.stream()
                .filter(member -> !assignedMembers.contains(member))
                .collect(Collectors.toSet());

        Set<Adherent> filteredMembers2 = filteredMembers.stream()
                .filter(member -> member.getAcademie().getId() == managerRepository.findById(managerId).get().getAcademie().getId())
                .collect(Collectors.toSet());

        return filteredMembers2.size();
    }

    @Override
    public Integer getMembersPersonnalise(Integer managerId) {
        // Retrieve all events as a list
        Set<Evenement> allEventsSet = evenementRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        List<Evenement> allEventsList = new ArrayList<>(allEventsSet);

        // Convert the list to a set
        Set<Evenement> allEvents = new HashSet<>(allEventsList);

        // Retrieve members assigned to personnalise events
        Set<Adherent> assignedMembers = allEvents.stream()
                .filter(e -> e.getType() == EvenementType.EVENEMENT_PERSONNALISE)
                .flatMap(e -> e.getConvocationMembresPersonnalise().stream())
                .collect(Collectors.toSet());

        // Retrieve all members
        Set<Adherent> allMembers = adherentRepository.findAll().stream()
                .collect(Collectors.toSet());

        // Filter out members that are assigned to personnalise events
        Set<Adherent> filteredMembers = allMembers.stream()
                .filter(member -> !assignedMembers.contains(member))
                .collect(Collectors.toSet());

        Set<Adherent> filteredMembers2 = filteredMembers.stream()
                .filter(member -> member.getAcademie().getId() == managerRepository.findById(managerId).get().getAcademie().getId())
                .collect(Collectors.toSet());

        return filteredMembers2.size();
    }

    @Override
    public Integer getEquipesPersonnalise(Integer managerId) {
        // Retrieve all events as a list
        Set<Evenement> allEventsSet = evenementRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        List<Evenement> allEventsList = new ArrayList<>(allEventsSet);

        // Convert the list to a set
        Set<Evenement> allEvents = new HashSet<>(allEventsList);

        // Retrieve teams assigned to personalized events
        Set<Equipe> assignedEquipes = allEvents.stream()
                .filter(e -> e.getType() == EvenementType.EVENEMENT_PERSONNALISE)
                .map(Evenement::getConvocationEquipePersonnalise)
                .collect(Collectors.toSet());

        // Retrieve all teams
        Set<Equipe> allEquipes = equipeRepository.findAll().stream()
                .collect(Collectors.toSet());

        // Filter out teams that are assigned to test events
        Set<Equipe> filteredEquipes = allEquipes.stream()
                .filter(equipe -> !assignedEquipes.contains(equipe))
                .collect(Collectors.toSet());

        Set<Equipe> filteredEquipes2 = filteredEquipes.stream()
                .filter(equipe -> equipe.getAcademie().getId() == managerRepository.findById(managerId).get().getAcademie().getId())
                .collect(Collectors.toSet());

        return filteredEquipes2.size();
    }


    @Override
    public Evenement addCompetition(Evenement evenement, Integer managerId) {
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());
        evenement.setType(EvenementType.COMPETITION);
        // Save the evenement
        evenement = evenementRepository.save(evenement);
        return evenement;
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
            evenement.setConvocationEquipePersonnalise(equipe);

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
            evenement.setConvocationMembresPersonnalise(membres);

            // Set the type of the evenement
            evenement.setType(EvenementType.EVENEMENT_PERSONNALISE);

            // Save the evenement
            return evenementRepository.save(evenement);
        }

        // If neither idEquipe nor idMembres are provided, throw IllegalArgumentException
        throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
    }

    @Override
    public Evenement addTest(Evenement evenement, List<Integer> idEquipes, List<Integer> idMembres, Integer managerId) {
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());
        // Check if both idEquipes and idMembres are provided
        if ((idEquipes != null && !idEquipes.isEmpty()) && (idMembres != null && !idMembres.isEmpty())) {
            throw new IllegalArgumentException("Both idEquipes and idMembres cannot be provided simultaneously.");
        }

        // Check if idEquipes is provided
        if (idEquipes != null && !idEquipes.isEmpty()) {
            // Retrieve the Equipes from the repository using idEquipes
            List<Equipe> equipes = equipeRepository.findByIdIn(idEquipes);
            // Set the convocationEquipes of the evenement
            evenement.setConvocationEquipesTest(new HashSet<>(equipes));

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
            evenement.setConvocationMembresTest(membres);

            // Set the type of the evenement
            evenement.setType(EvenementType.TEST_EVALUATION);

            // Save the evenement
            return evenementRepository.save(evenement);
        }

        // If neither idEquipes nor idMembres are provided, throw IllegalArgumentException
        throw new IllegalArgumentException("Either idEquipes or idMembres must be provided.");
    }

    @Override
    public Evenement addMatchAmical(Evenement evenement, List<EquipeHoraireDTO> equipeHoraires, Integer managerId) {
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());
        evenement.setType(EvenementType.MATCH_AMICAL);

        Set<Equipe> equipes = new HashSet<>();

        for (EquipeHoraireDTO equipeHoraire : equipeHoraires) {
            Optional<Equipe> equipeOptional = equipeRepository.findById(equipeHoraire.getEquipeId());
            equipeOptional.ifPresent(equipe -> {
                equipe.setDateMatchAmical(equipeHoraire.getHoraire());
                equipes.add(equipe);
            });
        }
        evenement.setConvocationEquipesMatchAmical(equipes);
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


}
