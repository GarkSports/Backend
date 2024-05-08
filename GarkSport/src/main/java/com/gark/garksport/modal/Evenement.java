package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.EvenementType;
import com.gark.garksport.modal.enums.StatutEvenenement;
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
    private LocalTime heure;
    private String description;
    private StatutEvenenement statut = StatutEvenenement.Activé;

    //Match Amical
    @JsonIgnoreProperties({"academie", "adherents"})
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Equipe> convocationEquipesMatchAmical;

    //Evenement personnalisé
    @OneToOne
    private Equipe convocationEquipePersonnalise;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Adherent> convocationMembresPersonnalise;



    //Test evaulation
    @JsonIgnoreProperties({"academie", "adherents"})
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Equipe> convocationEquipesTest;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Adherent> convocationMembresTest;


    @OneToOne
    private Academie academie;



}