package com.gark.garksport.repository;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Adherent;
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
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    //List<User> findAllByAcademieId(Integer id);

//    @Query("SELECT u FROM User u WHERE u.academie.id = :academieId")
//    List<User> findAllByAcademieId(Integer academieId);

    //List<User> findAllByIdNot(Integer id);
    //@Query("SELECT u FROM User u WHERE u.role IN :roles AND u.academie.id = :academieId")
    //List<User> findByRolesInAcademie(@Param("roles") List<Role> roles, @Param("academieId") Integer academieId);
    //Set<User> findByAcademieId(Integer academieId);
    //List<User> findByRolesAndAcademieIdIn(List<Role> roles, Integer academieId);

    //public Set<User> findByAcademieId(Integer academieId);

    List<User> findByBlocked(boolean blocked);
    List<User> findByRoleName(String existingRoleName);
}
