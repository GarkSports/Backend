package com.gark.garksport.repository;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.ConvocationEntrainement;
import com.gark.garksport.modal.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Integer> {
    public Set<Equipe> findByAcademieId(Integer academieId);
    Equipe findByCodeEquipe(String codeEquipe);

    List<Equipe> findByIdIn(List<Integer> idEquipes);

    Equipe findByNom(String nomEquipe);

   // List<Equipe> findByNoms(List<String> equipeNoms);

    Optional<Equipe> findByConvocationsContains(ConvocationEntrainement convocation);

    //Equipe findByAdherentId(Integer adherentId);

    List<Equipe> findUsersById(Integer id);

    @Query("SELECT e FROM Equipe e JOIN e.adherents a WHERE a.id = :id")
    List<Equipe> findEquipesByAdherentId(@Param("id") Integer id);

    @Query("SELECT e FROM Equipe e JOIN e.entraineurs a WHERE a.id = :id")
    List<Equipe> findEquipesByEntraineurId(@Param("id") Integer id);
    boolean existsByCodeEquipe(String randomCode);

    @Query("SELECT e.adherents FROM Equipe e WHERE e.id = :id")
    Set<Adherent> findAdherentsByEquipeId(@Param("id") Integer id);
}
