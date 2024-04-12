package com.gark.garksport.service;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Evenement;
import com.gark.garksport.repository.AcademieRepository;
import com.gark.garksport.repository.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvenementService implements IEvenementService {
    @Autowired
    private EvenementRepository evenementRepository;
    @Autowired
    private AcademieRepository academieRepository;

    @Override
    public Evenement addAndAffectEvenementToAcademie(Evenement evenement, Integer idAcademie) {
        Academie academie = academieRepository.findById(idAcademie).get();
        evenement.setAcademie(academie);
        return evenementRepository.save(evenement);
    }
}
