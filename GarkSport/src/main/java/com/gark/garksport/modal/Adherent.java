package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.StatutAdherent;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"paiement"})
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonIgnoreProperties("adherent")
    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Parent> parents;


    @JsonIgnoreProperties("adherent")
    @OneToOne(mappedBy = "adherent")
    private Paiement paiement;

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

    public void addEvaluation(Evaluation evaluation) {
        evaluations.add(evaluation);
        evaluation.setAdherent(this);
    }

    public void removeEvaluation(Evaluation evaluation) {
        evaluations.remove(evaluation);
        evaluation.setAdherent(null);
    }
}