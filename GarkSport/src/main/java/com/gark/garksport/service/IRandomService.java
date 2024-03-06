package com.gark.garksport.service;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Manager;

public interface IRandomService {
    public Manager addManager(Manager manager);

    public Adherent addAndAffectAdherentToAcademie(Adherent adherent, Integer academieId);
}
