package com.gark.garksport.service;

import com.gark.garksport.modal.Evenement;

public interface IEvenementService {
    public Evenement addAndAffectEvenementToAcademie(Evenement evenement, Integer idAcademie);
}
