package com.gark.garksport.repository;

import com.gark.garksport.modal.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Integer> {
    public Set<Equipe> findByAcademieId(Integer academieId);
}
