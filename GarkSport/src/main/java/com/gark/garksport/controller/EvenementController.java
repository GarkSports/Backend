package com.gark.garksport.controller;

import com.gark.garksport.modal.Evenement;
import com.gark.garksport.service.IEvenementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evenement")
public class EvenementController {
    @Autowired
    private IEvenementService evenementService;

    @PostMapping("/addAndAffectEvenementToAcademie/{idAcademie}")
    public Evenement addAndAffectEvenementToAcademie(@RequestBody Evenement evenement, @PathVariable Integer idAcademie) {
        return evenementService.addAndAffectEvenementToAcademie(evenement, idAcademie);
    }

}
