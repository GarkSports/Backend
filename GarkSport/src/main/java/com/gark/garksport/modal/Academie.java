package com.gark.garksport.modal;

import com.gark.garksport.modal.enums.AcademieType;
import com.gark.garksport.modal.enums.Etat;
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
public class Academie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    @Enumerated(EnumType.STRING)
    private AcademieType type;
    private Float fraisAdhesion;
    private String logo;
    private String affiliation;
    @Enumerated(EnumType.STRING)
    private Etat etat;
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Discipline> disciplines;

    @OneToMany(mappedBy = "academie", cascade = CascadeType.ALL)
    private Set<Adherent> adherents;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Adresse> adresses;

    @OneToOne
    private Manager manager;

    @OneToMany(mappedBy = "academie", cascade = CascadeType.ALL)
    private Set<Evenement> evenements;
}
