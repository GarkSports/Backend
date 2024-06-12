package com.gark.garksport.repository;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {
    Set<Paiement> findByAdherentAcademieId(Integer academieId);

    Paiement findByAdherent(Adherent adherent);

    List<Paiement> findByAdherentAcademieIdAndDatePaiement(Integer academieId, LocalDate datepaiement);
}
