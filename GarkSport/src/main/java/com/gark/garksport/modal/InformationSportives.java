package com.gark.garksport.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InformationSportives {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dateAdhesion;
    private String numeroLicence;

    @OneToOne(mappedBy = "informationSportives")
    private Adherent adherent;

    @OneToOne
    private NoteAssiduite noteAssiduite;

    @OneToMany(mappedBy = "informationSportives", cascade = CascadeType.ALL)
    private Set<Performance> performances;
}
