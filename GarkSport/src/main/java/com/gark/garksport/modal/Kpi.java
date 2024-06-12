package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(mappedBy = "kpi", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("kpi")
    private ValKpis valkpi;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    @JsonIgnore
    private Categorie categorie;
    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    public Kpi(String kpiType) {
        this.kpiType = kpiType;
    }
}
