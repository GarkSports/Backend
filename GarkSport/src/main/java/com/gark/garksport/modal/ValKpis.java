package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class ValKpis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String valKpi;

    @OneToOne
    @JoinColumn(name = "kpi_id")
    @JsonIgnore
    private Kpi kpi;

}
