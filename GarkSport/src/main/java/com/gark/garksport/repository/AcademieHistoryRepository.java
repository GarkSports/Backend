package com.gark.garksport.repository;

import com.gark.garksport.modal.AcademieHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface AcademieHistoryRepository extends JpaRepository<AcademieHistory, Integer> {
    Set<AcademieHistory> findByAcademie_IdOrderByChangeDateDesc(Integer academieId);
}
