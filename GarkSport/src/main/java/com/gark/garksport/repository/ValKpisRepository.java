package com.gark.garksport.repository;

import com.gark.garksport.modal.Kpi;
import com.gark.garksport.modal.ValKpis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ValKpisRepository extends JpaRepository<ValKpis, Integer> {
}
