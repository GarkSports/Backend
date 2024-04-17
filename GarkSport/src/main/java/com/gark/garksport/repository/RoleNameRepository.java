package com.gark.garksport.repository;

import com.gark.garksport.modal.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleNameRepository extends JpaRepository<RoleName, Integer> {
    RoleName findByRoleName(String roleName);

}
