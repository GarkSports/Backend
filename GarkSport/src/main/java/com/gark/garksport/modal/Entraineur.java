package com.gark.garksport.modal;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Entraineur extends User {
    @ManyToOne(cascade = CascadeType.ALL)
    private Discipline discipline;

    @OneToMany(mappedBy = "entraineur", cascade = CascadeType.ALL)
    private Set<Adherent> adherents;

    @OneToMany(mappedBy = "entraineur", cascade = CascadeType.ALL)
    private Set<Equipe> equipes;
}
