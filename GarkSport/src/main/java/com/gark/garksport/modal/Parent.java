package com.gark.garksport.modal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class Parent extends User {
    private String informationParent;

    @OneToMany(mappedBy = "parent")
    private Set<Reclamation> reclamations;

    @ManyToMany(mappedBy = "parents", cascade = CascadeType.ALL)
    private Set<Adherent> adherents;


}
