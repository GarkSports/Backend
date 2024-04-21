package com.gark.garksport.repository;

import com.gark.garksport.modal.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Integer> {
    List<Discipline> findByProtectedDisciplineTrue();
}
