package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.Destinataire;
import com.gark.garksport.modal.enums.EvenementType;
import com.gark.garksport.modal.enums.Repetition;
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

    //Type d'evenement
    @Enumerated(EnumType.STRING)
    private EvenementType type;
    private String nom;
    private String description;

    //Destinataires
    @Enumerated(EnumType.STRING)
    private Destinataire destinataire;

    @JsonIgnoreProperties("evenements")
    @ManyToOne
    private Academie academie;

    @JsonIgnoreProperties("evenements")
    @ManyToOne
    private Equipe equipe;
    @JsonIgnoreProperties("evenements")
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Adherent> membres;

    //Date evenement
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Temporal(TemporalType.TIME)
    private Date heur;
    private Boolean repetition;

    @Enumerated(EnumType.STRING)
    private Repetition chaque;
    private Integer numero;

    //Lieu
    private String adresse;
    private String lieu;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> invites;


}
