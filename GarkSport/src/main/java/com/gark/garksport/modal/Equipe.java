package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.GenreEquipe;
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
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private GenreEquipe genre;
    private String groupeAge;
    private String couleur;

    @ManyToOne
    private Discipline discipline;

    @OneToMany
    private Set<Adherent> adherents;

    @JsonIgnoreProperties("equipe")
    @ManyToOne
    private Entraineur entraineur;

    @ManyToOne
    private Academie academie;

    @JsonIgnoreProperties("equipe")
    @OneToMany(mappedBy = "equipe")
    private Set<Evenement> evenements;
}
