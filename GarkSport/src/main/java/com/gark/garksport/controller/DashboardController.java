package com.gark.garksport.controller;

import com.gark.garksport.dto.request.AcademieEtatRequest;
import com.gark.garksport.dto.request.ClubAcademieRequest;
import com.gark.garksport.dto.request.EventTypeRequest;
import com.gark.garksport.modal.Academie;
import com.gark.garksport.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private IDashboardService dashboardService;

    @GetMapping("/countAcademies")
    public long countAcademies() {
        return dashboardService.countAcademies();
    }

    @GetMapping("/countDisciplines")
    public long countDisciplines() {
        return dashboardService.countDisciplines();
    }

    @GetMapping("/countManagers")
    public long countManagers() {
        return dashboardService.countManagers();
    }

    @GetMapping("/countAdherents")
    public long countAdherents() {
        return dashboardService.countAdherents();
    }

    @GetMapping("/countEntraineurs")
    public long countEntraineurs() {
        return dashboardService.countEntraineurs();
    }

    @GetMapping("/countEvenements")
    public long countEvenements() {
        return dashboardService.countEvenements();
    }

    @GetMapping("/countClubAcademie")
    public ClubAcademieRequest countClubAcademie() {
        return dashboardService.getClubAcademieCountsAndPercentages();
    }

    @GetMapping("/countAcademiesByEtat")
    public List<AcademieEtatRequest> countAcademiesByEtat() {
        return dashboardService.countAcademiesByEtat();
    }

    @GetMapping("/countEventsWithType")
    public List<EventTypeRequest> countEventsWithType() {
        return dashboardService.countEventsWithType();
    }

    @GetMapping("/getAcademiesWithMostEvents")
    public List<Academie> getAcademiesWithMostEvents() {
        return dashboardService.getAcademiesWithMostEvents();
    }
}
