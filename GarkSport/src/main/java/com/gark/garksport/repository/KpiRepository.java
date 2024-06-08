package com.gark.garksport.repository;

import com.gark.garksport.modal.Kpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface KpiRepository extends JpaRepository<Kpi, Integer> {
    List<Kpi> findByValkpi_Id(Integer valKpiId);
}
