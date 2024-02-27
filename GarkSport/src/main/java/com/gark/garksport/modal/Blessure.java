package com.gark.garksport.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Blessure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;
    @Temporal(TemporalType.DATE)
    private Date dateBlessure;
    private Integer dureRepos;
    @Temporal(TemporalType.DATE)
    private Date dateRetablissement;

    @ManyToOne
    private InformationMedicales informationMedicales;
}
