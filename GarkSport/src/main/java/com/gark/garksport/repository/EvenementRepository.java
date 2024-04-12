package com.gark.garksport.repository;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Integer> {
    @Query("SELECT e.type, COUNT(e) FROM Evenement e GROUP BY e.type")
    List<Object[]> countEventsByType();

    int countByAcademie(Academie academie);
}
