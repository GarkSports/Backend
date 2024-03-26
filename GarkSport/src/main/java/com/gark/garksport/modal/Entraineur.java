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

    @JsonIgnoreProperties("entraineur")
    @OneToMany(mappedBy = "entraineur", cascade = CascadeType.ALL)
    private Set<Adherent> adherents;

    @JsonIgnoreProperties("entraineur")
    @OneToMany(mappedBy = "entraineur", cascade = CascadeType.ALL)
    private Set<Equipe> equipes;

    @ManyToOne(cascade = CascadeType.ALL)
    private Academie academie;
}
