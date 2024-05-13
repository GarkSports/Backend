package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Parent extends User {
    private String informationParent;

    @JsonIgnoreProperties("parents")
    @ManyToOne
    private Academie academie;

    @OneToMany(mappedBy = "parent")
    private Set<Reclamation> reclamations;

    @ManyToMany(mappedBy = "parents", cascade = CascadeType.ALL)
    private Set<Adherent> adherents;


}
