package com.gark.garksport.repository;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AdherentRepository extends JpaRepository<Adherent,Integer> {
    public Set<Adherent> findByAcademieId(Integer academieId);

    public Integer findAcademieIdById(Integer adherentId);

}
