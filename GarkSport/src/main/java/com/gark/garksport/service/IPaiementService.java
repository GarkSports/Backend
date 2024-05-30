package com.gark.garksport.service;


import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Paiement;
import com.gark.garksport.modal.PaiementHistory;
import com.gark.garksport.modal.enums.StatutAdherent;

import java.util.Set;

public interface IPaiementService {
    public Set<Paiement> getAllPaiementsByAcademie(Integer managerId);

    public Paiement addPaiement(Paiement paiement, Integer idAdherent);

    public Paiement updatePaiement(Paiement updatedPaiement, Integer idPaiement);

    public Set<PaiementHistory> getPaiementHistoryByAdherent(Integer adherentId);

    public Adherent getAdherentById(Integer adherentId);

    public Adherent changeStatutAdherent(Integer adherentId, StatutAdherent statutAdherent);

    public void deletePaiement(Integer idPaiement);

    public Set<Adherent> getAdherentsByAcademie(Integer managerId);

    public Paiement getPaiementById(Integer idPaiement);

}
