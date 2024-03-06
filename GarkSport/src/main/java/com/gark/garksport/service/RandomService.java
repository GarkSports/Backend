package com.gark.garksport.service;


import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.repository.AcademieRepository;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RandomService implements IRandomService {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private AcademieRepository academieRepository;
    @Autowired
    private AdherentRepository adherentRepository;
    @Override
    public Manager addManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public Adherent addAndAffectAdherentToAcademie(Adherent adherent, Integer academieId) {
        Academie academie = academieRepository.findById(academieId).get();
        academie.getAdherents().add(adherent);
        adherent.setAcademie(academie);
        adherentRepository.save(adherent);
        academieRepository.save(academie);
        return adherent;
    }

}
