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
import java.util.*;

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
    private String logo="https://firebasestorage.googleapis.com/v0/b/angularimage-43fec.appspot.com/o/academie%2Fteam_img_default-removebg-preview.png?alt=media&token=c51799d5-c417-4e85-abcd-40847dcf0616";

    @ManyToOne
    private Discipline discipline;

    @OneToMany
    private Set<Adherent> adherents;

//    @ManyToMany(mappedBy = "equipes")
//    private Set<Adherent> adherents = new HashSet<>();

    @OneToMany
    private Set<Entraineur> entraineurs;

    @ManyToOne
    private Academie academie;

    private LocalTime dateMatchAmical;

    @JsonIgnoreProperties("convocationEquipe")
    @OneToMany(mappedBy = "convocationEquipe")
    private List<Evenement> evenements;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ConvocationEntrainement> convocations;

}