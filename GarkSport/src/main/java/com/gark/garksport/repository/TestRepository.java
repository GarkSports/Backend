package com.gark.garksport.repository;

import com.gark.garksport.modal.Evaluation;
import com.gark.garksport.modal.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Integer> {
}