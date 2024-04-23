package com.gark.garksport.modal;

import com.gark.garksport.modal.enums.EvenementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private EvenementType type;
    private String nomEvent;
    private String lieu;
    @Temporal(TemporalType.DATE)
    private Date date;
    @Temporal(TemporalType.TIME)
    private Date heure;
    private String description;

    //Evenement personnalisé+Test evaulation
    @OneToOne
    private Equipe convocationEquipe;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Adherent> convocationMembres;

    //Test evaulation
    private String test;

    //Match Amical
    private String nomAdversaire;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Equipe> convocationEquipes;

    @OneToOne
    private Academie academie;
}