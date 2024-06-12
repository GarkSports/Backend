package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.EvenementType;
import com.gark.garksport.modal.enums.StatutEvenenement;
import com.gark.garksport.modal.enums.TypeRepetition;
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

    private Boolean repetition;
    private TypeRepetition typeRepetition=TypeRepetition.SEMAINE;
    private Integer nbRepetition;


    @JsonIgnoreProperties("evenements")
    @ManyToOne
    private Equipe convocationEquipe;
    @ManyToMany
    private Set<Adherent> convocationMembres;

    @JsonIgnoreProperties("evenements")
    @ManyToMany
    private Set<Equipe> convocationEquipesMatchAmical;


    @ManyToOne
    private Academie academie;
}