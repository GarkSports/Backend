package com.gark.garksport.repository;

import com.gark.garksport.modal.ConvocationEntrainement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvocationEntrainementRepository extends JpaRepository<ConvocationEntrainement, Integer> {
}
