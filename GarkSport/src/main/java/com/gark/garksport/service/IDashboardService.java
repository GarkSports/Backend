package com.gark.garksport.service;

import com.gark.garksport.dto.request.AcademieEtatRequest;
import com.gark.garksport.dto.request.ClubAcademieRequest;
import com.gark.garksport.dto.request.EventTypeRequest;
import com.gark.garksport.modal.Academie;

import java.util.List;
import java.util.Set;

public interface IDashboardService {
    public long countAcademies();
    public long countDisciplines();
    public long countManagers();
    public long countAdherents();
    public long countEntraineurs();
    public long countEvenements();
    public ClubAcademieRequest getClubAcademieCountsAndPercentages();
    public List<AcademieEtatRequest> countAcademiesByEtat();
    public List<EventTypeRequest> countEventsWithType();
    public List<Academie> getAcademiesWithMostEvents();
}
