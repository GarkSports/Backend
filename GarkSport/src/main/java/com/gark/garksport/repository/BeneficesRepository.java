package com.gark.garksport.repository;

import com.gark.garksport.modal.Benefices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficesRepository extends JpaRepository<Benefices,Integer> {

    List<Benefices> findByAcademieId(Integer academieID);

}
