package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Kpi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String kpiType;

    @OneToOne
    private ValKpis valkpi;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    public Kpi(String kpiType) {
        this.kpiType = kpiType;
    }
}
