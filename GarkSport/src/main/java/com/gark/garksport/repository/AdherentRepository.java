package com.gark.garksport.repository;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AdherentRepository extends JpaRepository<Adherent,Integer> {
    public Set<Adherent> findByAcademieId(Integer academieId);



    Set<Adherent> findByIdIn(List<Integer> idMembres);
    //@Query("SELECT u ,STRING_AGG(e.nom, ', ') AS equipe_names FROM User u LEFT JOIN equipe_adherents ue ON u.id = ue.adherents_id LEFT JOIN Equipe e ON ue.equipe_id = e.id GROUP BY u.id")
    //Set<?> findAdherentWithEquipe();
}
