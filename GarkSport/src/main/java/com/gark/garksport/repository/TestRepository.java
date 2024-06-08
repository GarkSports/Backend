package com.gark.garksport.repository;

import com.gark.garksport.modal.Evaluation;
import com.gark.garksport.modal.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findByAcademieId(Integer academieId);
}
