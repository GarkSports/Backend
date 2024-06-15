package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.StatutAdherent;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.*;

@EqualsAndHashCode(callSuper = true, exclude = {"paiement", "informationsParent"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(callSuper = true, exclude = "informationsParent")
public class Adherent extends User {

    private String niveauScolaire;


//    @JsonIgnore
//    @ManyToMany(mappedBy = "adherents", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "equipe_adherent",
//            joinColumns = @JoinColumn(name = "adherent_id"),
//            inverseJoinColumns = @JoinColumn(name = "equipe_id")
//    )
//    private Set<Equipe> equipes = new HashSet<>();


    @JsonIgnoreProperties("adherent")
    @OneToOne(mappedBy = "adherent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private InformationsParent informationsParent;

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

    @JsonIgnoreProperties("adherent")
    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Parent> parents;


    @JsonIgnoreProperties("adherent")
    @OneToOne(mappedBy = "adherent")
    private Paiement paiement;

//    @ManyToMany
//    @JoinTable(
//            name = "adherent_equipe",
//            joinColumns = @JoinColumn(name = "adherent_id"),
//            inverseJoinColumns = @JoinColumn(name = "equipe_id")
//    )
//    private Set<Equipe> equipes = new HashSet<>();
//
//    private String nomEquipe="non affecté";

    private Integer equipeId;

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    private String nomEquipe="non affecté";

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private StatutAdherent statutAdherent;

    @Temporal(TemporalType.DATE)
    private Date paiementDate = new Date();

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @JsonIgnoreProperties("adherent") // Exclude adherent from serialization to prevent infinite recursion
    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaiementHistory> paiementHistory = new ArrayList<>();

    public void addEvaluation(Evaluation evaluation) {
        evaluations.add(evaluation);
        evaluation.setAdherent(this);
    }

    public void removeEvaluation(Evaluation evaluation) {
        evaluations.remove(evaluation);
        evaluation.setAdherent(null);
    }
}