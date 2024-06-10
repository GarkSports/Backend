package com.gark.garksport.repository;

import com.gark.garksport.modal.InformationsParent;
import com.gark.garksport.modal.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationsParentRepository extends JpaRepository<InformationsParent, Integer> {
}
