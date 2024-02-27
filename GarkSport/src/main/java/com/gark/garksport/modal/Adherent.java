package com.gark.garksport.modal;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Adherent extends User{
    private String informationParent;

    @ManyToOne(cascade = CascadeType.ALL)
    private Discipline discipline;

    @ManyToOne
    private Entraineur entraineur;

    @OneToOne
    private InformationMedicales informationMedicales;

    @OneToOne
    private InformationSportives informationSportives;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Parent> parents;

    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL)
    private Set<Paiement> paiements;

    @ManyToOne
    private Equipe equipe;
}
