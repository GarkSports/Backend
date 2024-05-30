package com.gark.garksport.controller;

import com.gark.garksport.dto.request.EntrainementRequest;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.ConvocationEntrainement;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.service.IDisciplineService;
import com.gark.garksport.service.IEntrainementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entrainement")
@RequiredArgsConstructor
public class EntrainementController {
    @Autowired
    private IEntrainementService entrainementService;

    @PostMapping("/addEntrainement/{idEquipe}")
    public ConvocationEntrainement addEntrainement(@RequestBody EntrainementRequest entrainementRequest, @PathVariable Integer idEquipe) {
        ConvocationEntrainement convocationEntrainement = entrainementRequest.getConvocationEntrainement();
        entrainementService.addEntrainement(entrainementRequest, idEquipe);
        return convocationEntrainement;
    }

    @GetMapping("/getEntrainements")
    public List<Equipe> getEntrainements() {
        return entrainementService.getEntrainements();
    }

    @DeleteMapping("/deleteEntrainement/{idConvocation}")
    public void deleteEntrainement(@PathVariable Integer idConvocation) {
        entrainementService.deleteEntrainement(idConvocation);
    }

    @GetMapping("/getAdherentsByConvocation/{idConvocation}")
    public List<Adherent> getAdherentsByConvocation(@PathVariable Integer idConvocation) {
        return entrainementService.getAdherentsByConvocation(idConvocation);
    }

    @PutMapping("/updateConvocationEntrainement/{idConvocation}")
    public Equipe updateConvocationEntrainement(@RequestBody EntrainementRequest entrainementRequest, @PathVariable Integer idConvocation) {
        return entrainementService.updateConvocationEntrainement(entrainementRequest, idConvocation);
    }



}
