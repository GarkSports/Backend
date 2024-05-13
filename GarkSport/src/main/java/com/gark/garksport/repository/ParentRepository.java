package com.gark.garksport.repository;

import com.gark.garksport.modal.Entraineur;
import com.gark.garksport.modal.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ParentRepository extends JpaRepository<Entraineur, Integer> {
    public Set<Parent> findByAcademieId(Integer academieId);

}
