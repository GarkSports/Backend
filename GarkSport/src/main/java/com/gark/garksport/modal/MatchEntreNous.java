package com.gark.garksport.modal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class MatchEntreNous extends Evenement {
    private String nom;
    private String description;
    private String colorEquipe1;
    private String colorEquipe2;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Adherent> equipe1;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Adherent> equipe2;



}
