package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Adherent extends User {
    private String informationParent;

    @ManyToOne(cascade = CascadeType.ALL)
    private Discipline discipline;

    @JsonIgnoreProperties("adherents")
    @ManyToOne
    private Entraineur entraineur;

    @OneToOne
    private InformationMedicales informationMedicales;

    @OneToOne
    private InformationSportives informationSportives;

    @JsonIgnoreProperties("adherents")
    @ManyToOne
    private Academie academie;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Parent> parents;

    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL)
    private Set<Paiement> paiements;

    @JsonIgnoreProperties("membres")
    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "membres")
    private Set<Evenement> evenements;
}
