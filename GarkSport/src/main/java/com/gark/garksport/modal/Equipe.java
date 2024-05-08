package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.GenreEquipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
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
    private String codeEquipe;

    @ManyToOne
    private Discipline discipline;

    @OneToMany
    private Set<Adherent> adherents;

    @OneToMany
    private Set<Entraineur> entraineurs;

    @ManyToOne
    private Academie academie;

    private LocalTime dateMatchAmical;



}
