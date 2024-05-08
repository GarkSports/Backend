package com.gark.garksport.repository;

import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.RoleName;
import com.gark.garksport.modal.Staff;
import com.gark.garksport.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findByRoleName(String existingRoleName);
}
