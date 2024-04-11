package com.gark.garksport.service;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Paiement;
import com.gark.garksport.modal.PaiementHistory;
import com.gark.garksport.modal.enums.StatutAdherent;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.PaiementHistoryRepository;
import com.gark.garksport.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaiementService implements IPaiementService {
    @Autowired
    private PaiementRepository paiementRepository;
    @Autowired
    private AdherentRepository adherentRepository;
    @Autowired
    private PaiementHistoryRepository paiementHistoryRepository;

    @Override
    public Set<Paiement> getAllPaiementsByAcademie(Integer academieId) {
        return paiementRepository.findByAdherentAcademieId(academieId);
    }

    @Override
    public Paiement addPaiement(Paiement paiement, Integer idAdherent) {
        if (adherentRepository.existsById(idAdherent)) {
            Adherent adherent = adherentRepository.findById(idAdherent)
                    .orElseThrow(() -> new IllegalArgumentException("Adherent not found"));

            // Check if there is an existing payment for the same adherent
            Paiement existingPaiement = paiementRepository.findByAdherent(adherent);

            // If there is an existing payment, delete it
            if (existingPaiement != null) {
                paiementRepository.delete(existingPaiement);
            }

            // Extract relevant fields from newPaiement
            Date datePaiement = paiement.getDatePaiement();
            Date dateFin = paiement.getDateFin(); // Assuming dateFin is set

            // Calculate retardPaiement
            int retardPaiement = 0;
            if (datePaiement.after(dateFin)) {
                long differenceMillis = datePaiement.getTime() - dateFin.getTime();
                long daysDifference = differenceMillis / (1000 * 60 * 60 * 24); // Convert milliseconds to days
                retardPaiement = (int) daysDifference;
            }

            // Set retardPaiement in the newPaiement
            paiement.setRetardPaiement(retardPaiement);

            if (paiement.getReste() == null) {
                paiement.setReste(0f);
            }

            if (paiement.getReste() != 0) {
                adherent.setStatutAdherent(StatutAdherent.Payé_Partiellement);
            } else {
                adherent.setStatutAdherent(StatutAdherent.Payé);
            }

            // Set the adherent for the newPaiement
            paiement.setAdherent(adherent);

            // Save the new paiement
            Paiement savedPaiement = paiementRepository.save(paiement);

            // Update paiementDate of the adherent to dateFin
            adherent.setPaiementDate(paiement.getDateFin());
            // Save the updated adherent
            adherentRepository.save(adherent);

            // Create a new paiement history entry
            PaiementHistory paiementHistory = PaiementHistory.builder()
                    .dateDebut(paiement.getDateDebut())
                    .dateFin(paiement.getDateFin())
                    .datePaiement(datePaiement)
                    .montant(paiement.getMontant())
                    .reste(paiement.getReste())
                    .retardPaiement(paiement.getRetardPaiement())
                    .adherent(adherent)
                    .build();

            // Save the paiement history
            paiementHistoryRepository.save(paiementHistory);

            return savedPaiement;
        }
        return null;
    }


    @Override
    public Paiement updatePaiement(Paiement updatedPaiement, Integer idPaiement) {
        // Check if the Paiement with the given ID exists
        Optional<Paiement> optionalPaiement = paiementRepository.findById(idPaiement);
        if (optionalPaiement.isPresent()) {
            Paiement existingPaiement = optionalPaiement.get();

            // Update the specific fields if they are not null in the updatedPaiement
            if (updatedPaiement.getTypeAbonnement() != null) {
                existingPaiement.setTypeAbonnement(updatedPaiement.getTypeAbonnement());
            }
            if (updatedPaiement.getDateDebut() != null) {
                existingPaiement.setDateDebut(updatedPaiement.getDateDebut());
            }
            if (updatedPaiement.getDateFin() != null) {
                existingPaiement.setDateFin(updatedPaiement.getDateFin());
            }
            if (updatedPaiement.getDatePaiement() != null) {
                // Calculate the new retardPaiement
                long differenceMillis = updatedPaiement.getDatePaiement().getTime() - existingPaiement.getDateFin().getTime();
                long daysDifference = differenceMillis / (1000 * 60 * 60 * 24); // Convert milliseconds to days
                int retardPaiement = (int) daysDifference;
                existingPaiement.setRetardPaiement(Math.max(0, retardPaiement)); // Set the new retardPaiement, ensuring it's non-negative
                existingPaiement.setDatePaiement(updatedPaiement.getDatePaiement());
            }
            if (updatedPaiement.getMontant() != null) {
                existingPaiement.setMontant(updatedPaiement.getMontant());
            }
            if (updatedPaiement.getReste() == null || updatedPaiement.getReste() == 0f) {
                updatedPaiement.setReste(0f);
                // Update the adherent status based on the updated reste
                Adherent adherent = existingPaiement.getAdherent();
                adherent.setStatutAdherent(StatutAdherent.Payé);
            } else {
                // Update the adherent status based on the updated reste
                Adherent adherent = existingPaiement.getAdherent();
                adherent.setStatutAdherent(StatutAdherent.Payé_Partiellement);
            }
            if (updatedPaiement.getReste() != null) {
                existingPaiement.setReste(updatedPaiement.getReste());
            }

            if (updatedPaiement.getRemarque() != null) {
                existingPaiement.setRemarque(updatedPaiement.getRemarque());
            }

            // Save the updated Paiement
            Paiement updatedPaiementEntity = paiementRepository.save(existingPaiement);

            Adherent adherent = existingPaiement.getAdherent();
            adherent.setPaiementDate(existingPaiement.getDateFin());
            adherentRepository.save(adherent);

            // Create a new paiement history entry for the updated paiement
            PaiementHistory paiementHistory = PaiementHistory.builder()
                    .dateDebut(updatedPaiement.getDateDebut())
                    .dateFin(updatedPaiement.getDateFin())
                    .datePaiement(updatedPaiement.getDatePaiement()) // Use updated datePaiement
                    .montant(updatedPaiement.getMontant())
                    .reste(updatedPaiement.getReste())
                    .retardPaiement(updatedPaiement.getRetardPaiement())
                    .adherent(existingPaiement.getAdherent()) // Assuming adherent remains same after update
                    .build();

            // Save the paiement history for the updated paiement
            paiementHistoryRepository.save(paiementHistory);

            return updatedPaiementEntity;
        } else {
            throw new IllegalArgumentException("Paiement not found with ID: " + idPaiement);
        }
    }

    @Override
    public Set<PaiementHistory> getPaiementHistoryByAdherent(Integer adherentId) {
        if (adherentRepository.existsById(adherentId)) {
            Adherent adherent = adherentRepository.findById(adherentId).orElseThrow(() -> new IllegalArgumentException("Adherent not found"));
            return paiementHistoryRepository.findByAdherent(adherent);
        }
        return null;
    }

    @Override
    public Adherent getAdherentById(Integer adherentId) {
        if (adherentRepository.existsById(adherentId)) {
            return adherentRepository.findById(adherentId).orElseThrow(() -> new IllegalArgumentException("Adherent not found"));
        }
        return null;
    }

    @Override
    public Adherent changeStatutAdherent(Integer adherentId, StatutAdherent statutAdherent) {
        if (adherentRepository.existsById(adherentId)) {
            Adherent adherent = adherentRepository.findById(adherentId).orElseThrow(() -> new IllegalArgumentException("Adherent not found"));
            adherent.setStatutAdherent(statutAdherent);
            return adherentRepository.save(adherent);
        }
        return null;
    }

    @Override
    public void deletePaiement(Integer idPaiement) {
        paiementRepository.deleteById(idPaiement);
    }

    @Override
    public Set<Adherent> getAdherentsByAcademie(Integer academieId) {
        return adherentRepository.findByAcademieId(academieId);
    }


}
