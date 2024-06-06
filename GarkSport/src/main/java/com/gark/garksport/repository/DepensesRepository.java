package com.gark.garksport.repository;

import com.gark.garksport.modal.Depenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepensesRepository extends JpaRepository<Depenses,Integer> {

    List<Depenses> findByAcademieId(Integer academieID);
}
