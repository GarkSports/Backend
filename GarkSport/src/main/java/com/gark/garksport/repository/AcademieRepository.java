package com.gark.garksport.repository;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AcademieRepository extends JpaRepository<Academie, Integer> {
    Academie findByManagerId(Integer managerId);

    Academie findByAdherentsId(Integer adherentId);

    Set<Academie> findByIsArchivedFalse();

    Set<Academie> findByIsArchivedTrue();

    //Optional<RoleName> findByRoleNameAndAcademieId(String roleName, Integer academieId);

}
