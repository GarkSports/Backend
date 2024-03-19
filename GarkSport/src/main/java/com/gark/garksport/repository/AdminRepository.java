package com.gark.garksport.repository;

import com.gark.garksport.modal.Admin;
import com.gark.garksport.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<User, Integer> {
}
