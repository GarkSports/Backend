package com.gark.garksport.repository;

import com.gark.garksport.modal.Categorie;
import com.gark.garksport.modal.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
}
