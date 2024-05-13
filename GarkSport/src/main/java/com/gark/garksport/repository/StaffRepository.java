package com.gark.garksport.repository;

import com.gark.garksport.modal.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findByRoleName(String existingRoleName);
    public Set<Staff> findByAcademieId(Integer academieId);

}
