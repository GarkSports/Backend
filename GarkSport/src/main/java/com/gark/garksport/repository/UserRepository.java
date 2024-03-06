package com.gark.garksport.repository;

import com.gark.garksport.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findAllByIdNot(Integer id);
    List<User> findByBlocked(boolean blocked);

}
