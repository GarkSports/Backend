package com.gark.garksport.modal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Entraineur extends User {
    @ManyToOne(cascade = CascadeType.ALL)
    private Discipline discipline;

    @OneToMany(mappedBy = "entraineur", cascade = CascadeType.ALL)
    private Set<Adherent> adherents;

    @OneToMany(mappedBy = "entraineur", cascade = CascadeType.ALL)
    private Set<Equipe> equipes;
}
