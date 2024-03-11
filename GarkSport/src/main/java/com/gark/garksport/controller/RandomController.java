package com.gark.garksport.controller;


import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.service.IRandomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/random")
public class RandomController {
    @Autowired
    private IRandomService randomService;


    @PostMapping("/addManager")
    public Manager addManager(@RequestBody Manager manager) {
        return randomService.addManager(manager);
    }

    @PostMapping("/addAndAffectAdherentToAcademie/{academieId}")
    public Adherent addAndAffectAdherentToAcademie(@RequestBody Adherent adherent, @PathVariable Integer academieId) {
        return randomService.addAndAffectAdherentToAcademie(adherent, academieId);
    }

    @GetMapping("/getManagers")
    public Set<Manager> getManagers() {
        return randomService.getManagers();
    }


}
