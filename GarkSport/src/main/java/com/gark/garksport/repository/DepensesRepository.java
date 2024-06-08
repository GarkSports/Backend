package com.gark.garksport.repository;

import com.gark.garksport.modal.Depenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepensesRepository extends JpaRepository<Depenses,Integer> {

    List<Depenses> findByAcademieIdOrderByDateDesc(Integer academieID);

    @Query("SELECT SUM(d.total) FROM Depenses d WHERE d.date BETWEEN :startDate AND :endDate AND d.academie.id = :academieId")
    BigDecimal sumByMonthAndAcademie(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("academieId") Integer academieId);


}
