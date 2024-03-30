package com.gark.garksport.repository;

import com.gark.garksport.modal.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {
    Set<Paiement> findByAdherentAcademieId(Integer academieId);
}
