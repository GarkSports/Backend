package com.gark.garksport.controller;

import com.gark.garksport.dto.request.MatchAmicalRequest;
import com.gark.garksport.modal.Evenement;
import com.gark.garksport.service.IEvenementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/evenement")
public class EvenementController {
    @Autowired
    private IEvenementService evenementService;

    @PostMapping("/addMatchAmical")
    public Evenement addMatchAmical(@RequestBody MatchAmicalRequest request) {
        return evenementService.addMatchAmical(request.getEvenement(), request.getEquipes());
    }



}
