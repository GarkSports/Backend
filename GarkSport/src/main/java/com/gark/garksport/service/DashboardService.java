package com.gark.garksport.service;

import com.gark.garksport.dto.request.AcademieEtatRequest;
import com.gark.garksport.dto.request.ClubAcademieRequest;
import com.gark.garksport.dto.request.EventTypeRequest;
import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.enums.AcademieType;
import com.gark.garksport.modal.enums.Etat;
import com.gark.garksport.modal.enums.EvenementType;
import com.gark.garksport.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService implements IDashboardService {
    @Autowired
    private AcademieRepository academieRepository;
    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private AdherentRepository adherentRepository;
    @Autowired
    private EntraineurRepository entraineurRepository;
    @Autowired
    private EvenementRepository evenementRepository;
    @Override
    public long countAcademies() {
        return academieRepository.count();
    }
    @Override
    public long countDisciplines() {
        return disciplineRepository.count();
    }

    @Override
    public long countManagers() {
        return managerRepository.count();
    }

    @Override
    public long countAdherents() {
        return adherentRepository.count();
    }

    @Override
    public long countEntraineurs() {
        return entraineurRepository.count();
    }

    @Override
    public long countEvenements() {
        return evenementRepository.count();
    }

    @Override
    public ClubAcademieRequest getClubAcademieCountsAndPercentages() {
        // Get all academies
        List<Academie> academies = academieRepository.findAll();

        // Count variables
        long academieCount = 0;
        long clubCount = 0;

        // Count academies and clubs based on the 'type' attribute
        for (Academie academie : academies) {
            if (AcademieType.CLUB.equals(academie.getType())) {
                clubCount++;
            } else {
                academieCount++;
            }
        }

        // Total count
        long totalCount = academieCount + clubCount;

        // Calculate percentages
        long academiePercentage = (academieCount * 100) / totalCount;
        long clubPercentage = (clubCount * 100) / totalCount;

        // Create and return the ClubAcademieRequest object
        return new ClubAcademieRequest(academieCount, clubCount, academiePercentage, clubPercentage);
    }

    @Override
    public List<AcademieEtatRequest> countAcademiesByEtat() {
        // Get all academies
        List<Academie> academies = academieRepository.findAll();

        // Initialize a map to store counts for all possible states
        Map<Etat, Long> etatCounts = new EnumMap<>(Etat.class);

        // Initialize counts for all states with 0
        for (Etat etat : Etat.values()) {
            etatCounts.put(etat, 0L);
        }

        // Update counts based on actual academies
        for (Academie academie : academies) {
            Etat etat = academie.getEtat();
            etatCounts.put(etat, etatCounts.get(etat) + 1);
        }

        // Convert the map to a list of AcademieEtatRequest objects
        List<AcademieEtatRequest> academieEtatRequests = etatCounts.entrySet().stream()
                .map(entry -> new AcademieEtatRequest(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return academieEtatRequests;
    }

    @Override
    public List<EventTypeRequest> countEventsWithType() {
        // Create a map to store the count of events for each event type
        Map<EvenementType, Long> eventTypeCountMap = new EnumMap<>(EvenementType.class);

        // Initialize counts for all event types with 0
        for (EvenementType type : EvenementType.values()) {
            eventTypeCountMap.put(type, 0L);
        }

        // Get the count of events for each event type
        List<Object[]> counts = evenementRepository.countEventsByType();
        for (Object[] row : counts) {
            EvenementType type = (EvenementType) row[0];
            Long count = (Long) row[1];
            eventTypeCountMap.put(type, count);
        }

        // Create EventTypeRequest objects from the map
        List<EventTypeRequest> eventTypeRequests = new ArrayList<>();
        for (Map.Entry<EvenementType, Long> entry : eventTypeCountMap.entrySet()) {
            eventTypeRequests.add(new EventTypeRequest(entry.getKey(), entry.getValue()));
        }

        return eventTypeRequests;
    }

    @Override
    public List<Academie> getAcademiesWithMostEvents() {
        // Retrieve all academies
        List<Academie> academies = academieRepository.findAll();

        // Sort academies based on the number of events they have
        List<Academie> sortedAcademies = academies.stream()
                .sorted(Comparator.comparingLong(academie -> {
                    long eventCount = evenementRepository.countByAcademie(academie);
                    return -eventCount; // Sort in descending order
                }))
                .collect(Collectors.toList());

        // Return the top three academies with the most events
        return sortedAcademies.stream().limit(3).collect(Collectors.toList());
    }



}
