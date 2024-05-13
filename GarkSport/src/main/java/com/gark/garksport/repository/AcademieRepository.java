package com.gark.garksport.repository;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.RoleName;
import com.gark.garksport.modal.User;
import com.gark.garksport.modal.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AcademieRepository extends JpaRepository<Academie, Integer> {
    Academie findByManagerId(Integer managerId);

    Academie findByAdherentsId(Integer adherentId);

    Set<Academie> findByIsArchivedFalse();

    Set<Academie> findByIsArchivedTrue();

//    @Query("SELECT u FROM User u WHERE u.role IN :roles")
//    List<User> findByRoles(@Param("roles") List<Role> roles);

    //Optional<RoleName> findByRoleNameAndAcademieId(String roleName, Integer academieId);

}
