package com.gark.garksport.repository;

import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
}
