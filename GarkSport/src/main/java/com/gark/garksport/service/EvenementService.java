package com.gark.garksport.service;

import com.gark.garksport.dto.request.EquipeHorraireDTO;
import com.gark.garksport.dto.request.MatchAmicalRequest;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.modal.Evenement;
import com.gark.garksport.modal.NotificationMessage;
import com.gark.garksport.modal.enums.EvenementType;
import com.gark.garksport.modal.enums.StatutEvenenement;
import com.gark.garksport.modal.enums.TypeRepetition;
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

    @Autowired
    private NotificationService notificationService;

    @Override
    public Evenement addCompetition(Evenement evenement, Integer idEquipe, List<Integer> idMembres, Integer managerId) {
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTitle("GarkSport");
        notificationMessage.setBody("vous avez une nouvelle competition");
        notificationMessage.setImage("https://cdn-icons-png.flaticon.com/512/3176/3176237.png");

        // Check if both idEquipe and idMembres are provided
        if ((idEquipe != null && idEquipe != 0) && (idMembres != null && !idMembres.isEmpty())) {
            throw new IllegalArgumentException("Both idEquipe and idMembres cannot be provided simultaneously.");
        }

        // Check if idEquipe is provided
        if (idEquipe != null && idEquipe != 0) {
            Equipe equipe = equipeRepository.findById(idEquipe)
                    .orElseThrow(() -> new NoSuchElementException("Equipe not found with id: " + idEquipe));

            evenement.setConvocationEquipe(equipe);
            evenement.setConvocationMembres(new HashSet<>(equipe.getAdherents()));
            evenement.setType(EvenementType.COMPETITION);
            notificationService.sendNotificationToTeam(idEquipe,notificationMessage);

        } else if (idMembres != null && !idMembres.isEmpty()) {
            Set<Adherent> membres = adherentRepository.findByIdIn(idMembres);
            evenement.setConvocationMembres(new HashSet<>(membres));
            Adherent firstMember = membres.iterator().next();
            Equipe equipe = equipeRepository.findById(firstMember.getEquipeId()).get();
            evenement.setConvocationEquipe(equipe);
            evenement.setType(EvenementType.COMPETITION);
            //send notification members

            notificationService.sendNotificationToMembers(idMembres,notificationMessage);

        } else {
            throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
        }

        // Save the initial evenement
        Evenement savedEvenement = evenementRepository.save(evenement);






        // Handle repetition logic
        if (Boolean.TRUE.equals(evenement.getRepetition())) {
            handleRepetition(savedEvenement);
        }

        return savedEvenement;
    }

    private void handleRepetition(Evenement evenement) {
        if (Boolean.TRUE.equals(evenement.getRepetition())) {
            TypeRepetition typeRepetition = evenement.getTypeRepetition();
            Integer nbRepetition = evenement.getNbRepetition();
            if (typeRepetition != null && nbRepetition != null) {
                for (int i = 1; i <= nbRepetition-1; i++) {
                    Evenement repeatedEvent = new Evenement();
                    repeatedEvent.setType(evenement.getType());
                    repeatedEvent.setNomEvent(evenement.getNomEvent());
                    repeatedEvent.setLieu(evenement.getLieu());
                    repeatedEvent.setDate(calculateNewDate(evenement.getDate(), typeRepetition, i));
                    repeatedEvent.setHeure(evenement.getHeure());
                    repeatedEvent.setDescription(evenement.getDescription());
                    repeatedEvent.setStatut(evenement.getStatut());
                    repeatedEvent.setRepetition(evenement.getRepetition());
                    repeatedEvent.setTypeRepetition(evenement.getTypeRepetition());
                    repeatedEvent.setNbRepetition(evenement.getNbRepetition());
                    repeatedEvent.setConvocationEquipe(evenement.getConvocationEquipe());
                    repeatedEvent.setConvocationMembres(new HashSet<>(evenement.getConvocationMembres()));
                    repeatedEvent.setAcademie(evenement.getAcademie());

                    evenementRepository.save(repeatedEvent);
                }
            }
        }
    }

    private Date calculateNewDate(Date originalDate, TypeRepetition typeRepetition, int increment) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalDate);
        switch (typeRepetition) {
            case MOIS:
                calendar.add(Calendar.MONTH, increment);
                break;
            case SEMAINE:
                calendar.add(Calendar.WEEK_OF_YEAR, increment);
                break;
            default:
                throw new IllegalArgumentException("Unknown TypeRepetition: " + typeRepetition);
        }
        return calendar.getTime();
    }



    @Override
    public Evenement addPersonnalisé(Evenement evenement, Integer idEquipe, List<Integer> idMembres, Integer managerId) {
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTitle("GarkSport");
        notificationMessage.setBody("vous avez un nouveau événement");
        notificationMessage.setImage("https://cdn-icons-png.flaticon.com/512/3176/3176237.png");


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
            evenement = evenementRepository.save(evenement);

            // Handle repetition logic
            handleRepetition(evenement);

            notificationService.sendNotificationToTeam(idEquipe,notificationMessage);

            return evenement;
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
            evenement = evenementRepository.save(evenement);

            // Handle repetition logic
            handleRepetition(evenement);
            notificationService.sendNotificationToMembers(idMembres,notificationMessage);

            return evenement;
        }

        // If neither idEquipe nor idMembres are provided, throw IllegalArgumentException
        throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
    }

    @Override
    public Evenement addTest(Evenement evenement, Integer idEquipe, List<Integer> idMembres, Integer managerId) {
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTitle("GarkSport");
        notificationMessage.setBody("vous avez un nouveau Test");
        notificationMessage.setImage("https://cdn-icons-png.flaticon.com/512/3176/3176237.png");

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
            evenement = evenementRepository.save(evenement);

            // Handle repetition logic
            handleRepetition(evenement);
            notificationService.sendNotificationToTeam(idEquipe,notificationMessage);

            return evenement;
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
            evenement = evenementRepository.save(evenement);

            // Handle repetition logic
            handleRepetition(evenement);
            notificationService.sendNotificationToMembers(idMembres,notificationMessage);

            return evenement;
        }

        // If neither idEquipe nor idMembres are provided, throw IllegalArgumentException
        throw new IllegalArgumentException("Either idEquipe or idMembres must be provided.");
    }

    @Override
    public Evenement addMatchAmical(MatchAmicalRequest request, Integer managerId) {
        Evenement evenement = request.getEvenement();
        List<EquipeHorraireDTO> equipeHorraireDTOs = request.getEquipesHorraires();

        // Validate input
        if (equipeHorraireDTOs == null || equipeHorraireDTOs.isEmpty()) {
            throw new IllegalArgumentException("The list of equipeHorraireDTOs must not be null or empty");
        }

        // Set the academie and other properties for the event
        evenement.setAcademie(managerRepository.findById(managerId).get().getAcademie());
        evenement.setType(EvenementType.MATCH_AMICAL);

        // Set convocationEquipesMatchAmical and convocationMembres
        Set<Equipe> equipes = new HashSet<>();
        Set<Adherent> allAdherents = new HashSet<>();

        for (EquipeHorraireDTO dto : equipeHorraireDTOs) {
            Integer equipeId = dto.getEquipeId();
            LocalTime horraire = dto.getHorraire();

            Equipe equipe = equipeRepository.findById(equipeId)
                    .orElseThrow(() -> new NoSuchElementException("Equipe not found with id: " + equipeId));

            // Each equipe gets its own horraire
            equipe.setDateMatchAmical(horraire);
            equipeRepository.save(equipe);

            equipes.add(equipe);
            allAdherents.addAll(equipe.getAdherents());
        }

        evenement.setConvocationEquipesMatchAmical(equipes);
        evenement.setConvocationMembres(allAdherents);

        // Save the event
        evenement = evenementRepository.save(evenement);

        // Handle repetition logic
        if (Boolean.TRUE.equals(evenement.getRepetition())) {
            handleRepetition(evenement);
        }

        // Send notifications
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTitle("GarkSport");
        notificationMessage.setBody("Vous avez un nouveau match amical");
        notificationMessage.setImage("https://cdn-icons-png.flaticon.com/512/3176/3176237.png");

        for (EquipeHorraireDTO dto : equipeHorraireDTOs) {
            notificationService.sendNotificationToTeam(dto.getEquipeId(), notificationMessage);
        }

        return evenement;
    }


    @Override
    public Set<Evenement> getAllEvenements(Integer managerId) {
        return evenementRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());
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

    @Override
    public List<Adherent> getMembersByEvenement(Integer idEvenement) {
        Evenement evenement = evenementRepository.findById(idEvenement)
                .orElseThrow(() -> new NoSuchElementException("Evenement not found with id: " + idEvenement));
        Set<Adherent> members =  evenement.getConvocationEquipe().getAdherents();
        return new ArrayList<>(members);
    }

    @Override
    public Evenement updateEvenement(Evenement evenement, List<Integer> idMembres, Integer evenementId) {
        Evenement existingEvenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new NoSuchElementException("Evenement not found with id: " + evenementId));

        existingEvenement.setNomEvent(evenement.getNomEvent());
        existingEvenement.setLieu(evenement.getLieu());
        existingEvenement.setDate(evenement.getDate());
        existingEvenement.setHeure(evenement.getHeure());
        existingEvenement.setConvocationMembres(new HashSet<>(adherentRepository.findByIdIn(idMembres)));


        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTitle("GarkSport");
        notificationMessage.setBody("vous avez un evenement modifié");
        notificationMessage.setImage("https://cdn-icons-png.flaticon.com/512/3176/3176237.png");

        notificationService.sendNotificationToMembers(idMembres,notificationMessage);

        return evenementRepository.save(existingEvenement);
    }

    @Override
    public Evenement updateEvenementMatchAmical(Evenement evenement, Integer evenementId) {
        Evenement existingEvenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new NoSuchElementException("Evenement not found with id: " + evenementId));

        existingEvenement.setNomEvent(evenement.getNomEvent());
        existingEvenement.setLieu(evenement.getLieu());
        existingEvenement.setDate(evenement.getDate());


        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTitle("GarkSport");
        notificationMessage.setBody("vous avez un evenement modifié");
        notificationMessage.setImage("https://cdn-icons-png.flaticon.com/512/3176/3176237.png");

        return evenementRepository.save(existingEvenement);
    }

    @Override
    public List<Equipe> getEquipesByEvenementMatchAmical(Integer idEvenement) {
        Evenement evenement = evenementRepository.findById(idEvenement).orElseThrow(() -> new NoSuchElementException("Evenement not found with id: " + idEvenement));
        return new ArrayList<>(evenement.getConvocationEquipesMatchAmical());
    }




}
