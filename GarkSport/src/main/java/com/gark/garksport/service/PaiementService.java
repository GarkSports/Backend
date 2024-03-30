package com.gark.garksport.service;

import com.gark.garksport.modal.Paiement;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PaiementService implements IPaiementService{
    @Autowired
    private PaiementRepository paiementRepository;
    @Autowired
    private AdherentRepository adherentRepository;

    @Override
    public Set<Paiement> getAllPaiementsByAcademie(Integer academieId) {
        return paiementRepository.findByAdherentAcademieId(academieId);
    }

    @Override
    public Paiement addPaiement(Paiement paiement, Integer idAdherent) {
        if(adherentRepository.existsById(idAdherent)){
            paiement.setAdherent(adherentRepository.findById(idAdherent).get());
            return paiementRepository.save(paiement);
        }
        return null;
    }


}
