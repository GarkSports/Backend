package com.gark.garksport.modal;

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

    @ManyToOne
    @JoinColumn(name = "kpi_id")
    private Kpi kpi;

    private String valKpi;
}
