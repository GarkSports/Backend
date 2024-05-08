package com.gark.garksport.repository;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AcademieRepository extends JpaRepository<Academie, Integer> {
    Academie findByManagerId(Integer managerId);

    Set<Academie> findByIsArchivedFalse();

    Set<Academie> findByIsArchivedTrue();


}
