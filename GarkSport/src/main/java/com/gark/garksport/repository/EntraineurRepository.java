package com.gark.garksport.repository;

import com.gark.garksport.modal.Entraineur;
import com.gark.garksport.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EntraineurRepository extends JpaRepository<Entraineur, Integer> {
    public Set<Entraineur> findByAcademieId(Integer academieId);

    List<Entraineur> findAllByAcademieId(Integer id);

}
