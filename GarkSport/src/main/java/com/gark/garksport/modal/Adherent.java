package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.StatutAdherent;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"paiement"})
@Data
@Entity
public class Adherent extends User {
    private String informationParent;

    @ManyToOne(cascade = CascadeType.ALL)
    private Discipline discipline;

    @JsonIgnoreProperties("adherents")
    @ManyToOne
    private Entraineur entraineur;

    @OneToOne
    private InformationMedicales informationMedicales;

    @OneToOne
    private InformationSportives informationSportives;

    @JsonIgnoreProperties("adherents")
    @ManyToOne
    private Academie academie;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Parent> parents;

    @JsonIgnoreProperties("adherent")
    @OneToOne(mappedBy = "adherent")
    private Paiement paiement;

    @JsonIgnoreProperties("membres")
    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "membres")
    private Set<Evenement> evenements;

    private String nomEquipe;

    @Enumerated(EnumType.STRING)
    private StatutAdherent statutAdherent=StatutAdherent.Payé;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date creationDate;
}
