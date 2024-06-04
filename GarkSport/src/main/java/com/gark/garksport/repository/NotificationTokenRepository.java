package com.gark.garksport.repository;

import com.gark.garksport.modal.NotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Integer> {

    NotificationToken findByUserId(Integer userId);

    @Query("SELECT nt.token FROM NotificationToken nt WHERE nt.academieId = :academieId")

    List<String> findTokensByAcademieId(Integer academieId);

    @Query("SELECT nt.token FROM NotificationToken nt WHERE nt.EquipeId = :codeEquipe")


    List<String> findTokensByCodeEquipe(Integer codeEquipe);
}
