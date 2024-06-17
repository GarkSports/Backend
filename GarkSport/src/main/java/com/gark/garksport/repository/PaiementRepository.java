package com.gark.garksport.repository;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {
    Set<Paiement> findByAdherentAcademieId(Integer academieId);

    Paiement findByAdherent(Adherent adherent);
    @Query("SELECT p FROM Paiement p WHERE MONTH(p.datePaiement) = :month AND YEAR(p.datePaiement) = :year AND p.adherent.academie.id = :adherentAcademieId And p.adherent.statutAdherent = 'Payé'")
    List<Paiement> findAllByDatePaiementInCurrentMonth(
            @Param("month") int month,
            @Param("year") int year,
            @Param("adherentAcademieId") Integer adherentAcademieId
    );


}
