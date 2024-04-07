package com.gark.garksport.repository;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.PaiementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PaiementHistoryRepository extends JpaRepository<PaiementHistory, Integer> {
    Set<PaiementHistory> findByAdherent(Adherent adherent);
}
