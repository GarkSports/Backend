package com.gark.garksport.repository;

import com.gark.garksport.modal.Benefices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BeneficesRepository extends JpaRepository<Benefices,Integer> {

    List<Benefices> findByAcademieIdOrderByDateDesc(Integer academieID);

    @Query("SELECT SUM(b.total) FROM Benefices b WHERE b.date BETWEEN :startDate AND :endDate AND b.academie.id = :academieId")
    BigDecimal sumByMonthAndAcademie(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("academieId") Integer academieId);


}
