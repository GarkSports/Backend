package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Entraineur extends User {
    @ManyToOne(cascade = CascadeType.ALL)
    private Discipline discipline;

    private Integer equipeId;

    private String nomEquipe="non affecté";

    @JsonIgnoreProperties("entraineur")
    @OneToMany(mappedBy = "entraineur", cascade = CascadeType.ALL)
    private Set<Adherent> adherents;

    @JsonIgnoreProperties("entraineurs")
    @ManyToOne
    private Academie academie;

}