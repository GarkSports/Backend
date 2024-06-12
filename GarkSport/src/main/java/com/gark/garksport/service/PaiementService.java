package com.gark.garksport.service;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.NotificationMessage;
import com.gark.garksport.modal.Paiement;
import com.gark.garksport.modal.PaiementHistory;
import com.gark.garksport.modal.enums.StatutAdherent;
import com.gark.garksport.modal.enums.TypeAbonnement;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.ManagerRepository;
import com.gark.garksport.repository.PaiementHistoryRepository;
import com.gark.garksport.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Set<Paiement> getAllPaiementsByAcademie(Integer managerId) {
        return paiementRepository.findByAdherentAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());
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
            Date todayDate = new Date();

            // Calculate retardPaiement
            int retardPaiement = 0;
            if (todayDate.after(dateFin)) {
                long differenceMillis = todayDate.getTime() - dateFin.getTime();
                long daysDifference = differenceMillis / (1000 * 60 * 60 * 24); // Convert milliseconds to days
                retardPaiement = (int) daysDifference;
            }


            // Set retardPaiement in the newPaiement
            paiement.setRetardPaiement(retardPaiement);

            if (paiement.getReste() == null) {
                paiement.setReste(0f);
            }

            if (paiement.getReste() != 0 && paiement.getMontant() != 0) {
                adherent.setStatutAdherent(StatutAdherent.Payé_Partiellement);
            } else if (paiement.getReste() == 0 && paiement.getMontant() != 0) {
                adherent.setStatutAdherent(StatutAdherent.Payé);
            } else if (paiement.getMontant() == 0 ) {
                adherent.setStatutAdherent(StatutAdherent.Non_Payé);
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
                    .statutAdherent(adherent.getStatutAdherent())
                    .adherent(adherent)
                    .build();

            // Save the paiement history
            paiementHistoryRepository.save(paiementHistory);

            //send notification
            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setTitle("GarkSport:"+adherent.getFirstname());
            notificationMessage.setBody("Le règlement de votre abonnement a été effectué avec succès.");
            notificationMessage.setImage("https://cdn-icons-png.flaticon.com/512/1019/1019607.png");
            notificationService.sendNotificationToUser(
                    idAdherent,
                    notificationMessage
            );

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
                existingPaiement.setDatePaiement(updatedPaiement.getDatePaiement());
            } else if (updatedPaiement.getMontant() != null && updatedPaiement.getMontant() != 0f) {
                // Set datePaiement to today's date if montant is not null and different from 0
                existingPaiement.setDatePaiement(new Date());
            } else if (updatedPaiement.getMontant() != null && updatedPaiement.getMontant() == 0f) {
                // Set datePaiement to null if montant is 0
                existingPaiement.setDatePaiement(null);
            }
            if (updatedPaiement.getMontant() != null) {
                existingPaiement.setMontant(updatedPaiement.getMontant());
            }
            if (updatedPaiement.getReste() == null && updatedPaiement.getMontant() != 0f || updatedPaiement.getReste() == 0f && updatedPaiement.getMontant() != 0f) {
                updatedPaiement.setReste(0f);
                // Update the adherent status based on the updated reste
                Adherent adherent = existingPaiement.getAdherent();
                adherent.setStatutAdherent(StatutAdherent.Payé);
            } else if (updatedPaiement.getReste() != 0f && updatedPaiement.getMontant() != 0f) {
                // Update the adherent status based on the updated reste
                Adherent adherent = existingPaiement.getAdherent();
                adherent.setStatutAdherent(StatutAdherent.Payé_Partiellement);
            } else if (updatedPaiement.getMontant() == 0f) {
                // Update the adherent status based on the updated reste
                Adherent adherent = existingPaiement.getAdherent();
                adherent.setStatutAdherent(StatutAdherent.Non_Payé);
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
                    .statutAdherent(existingPaiement.getAdherent().getStatutAdherent()) // Assuming statutAdherent remains same after update
                    .adherent(existingPaiement.getAdherent()) // Assuming adherent remains same after update
                    .build();

            // Save the paiement history for the updated paiement
            paiementHistoryRepository.save(paiementHistory);

            //send notification
            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setTitle("GarkSport:"+adherent.getFirstname());
            notificationMessage.setBody("Le règlement de votre abonnement a été effectué avec succès.");
            notificationMessage.setImage("https://cdn-icons-png.flaticon.com/512/1019/1019607.png");
            notificationService.sendNotificationToUser(
                    adherent.getId(),
                    notificationMessage
            );

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
    public Set<Adherent> getAdherentsByAcademie(Integer managerId) {
        return adherentRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());
    }

    @Override
    public Paiement getPaiementById(Integer idPaiement) {
        return paiementRepository.findById(idPaiement).orElseThrow(() -> new IllegalArgumentException("Paiement not found"));
    }

    @Override
    public void deletePaiementHistory(Integer idPaiementHistory) {
        paiementHistoryRepository.deleteById(idPaiementHistory);
    }

    @Scheduled(cron = "*/15 * * * * *")
    public void checkPaiementDate() {
        List<Paiement> paiements = paiementRepository.findAll();
        Date today = new Date();
        for (Paiement paiement : paiements) {
            if (paiement.getDateFin().before(today)) {
                long differenceMillis = today.getTime()-paiement.getDateFin().getTime();
                long daysDifference = differenceMillis / (1000 * 60 * 60 * 24);
                paiement.setRetardPaiement((int) daysDifference);
                paiement.setDateDebut(today);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                if (paiement.getTypeAbonnement().equals(TypeAbonnement.Mensuel)) {
                    calendar.add(Calendar.MONTH, 1);
                } else if (paiement.getTypeAbonnement().equals(TypeAbonnement.Trimestriel)) {
                    calendar.add(Calendar.MONTH, 3);
                } else if (paiement.getTypeAbonnement().equals(TypeAbonnement.Annuel)) {
                    calendar.add(Calendar.YEAR, 1);
                }
                paiement.setDateFin(calendar.getTime());
                paiement.setMontant(0f);
                paiement.setDatePaiement(null);
                paiement.getAdherent().setStatutAdherent(StatutAdherent.Non_Payé);

                adherentRepository.save(paiement.getAdherent());
                if (paiement.getTypeAbonnement().equals(TypeAbonnement.Mensuel)) {
                    paiement.setReste(paiement.getAdherent().getAcademie().getFraisAdhesion());
                } else if (paiement.getTypeAbonnement().equals(TypeAbonnement.Trimestriel)) {
                    paiement.setReste(paiement.getAdherent().getAcademie().getFraisAdhesion() * 3);
                } else if (paiement.getTypeAbonnement().equals(TypeAbonnement.Annuel)) {
                    paiement.setReste(paiement.getAdherent().getAcademie().getFraisAdhesion() * 12);
                }
                paiementRepository.save(paiement);
            }else {
                paiement.setRetardPaiement(0);
                paiementRepository.save(paiement);
            }
        }
    }






}
