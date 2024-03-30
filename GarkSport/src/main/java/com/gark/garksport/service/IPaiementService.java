package com.gark.garksport.service;


import com.gark.garksport.modal.Paiement;

import java.util.Set;

public interface IPaiementService {
    public Set<Paiement> getAllPaiementsByAcademie(Integer academieId);

    public Paiement addPaiement(Paiement paiement, Integer idAdherent);

}
