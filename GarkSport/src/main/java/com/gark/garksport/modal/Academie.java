package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.AcademieType;
import com.gark.garksport.modal.enums.Etat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Academie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    @Enumerated(EnumType.STRING)
    private AcademieType type;
    private Float fraisAdhesion;
    private String logo;
    private String backgroundImage;
    private String affiliation;
    @Enumerated(EnumType.STRING)
    private Etat etat;
    @JsonIgnoreProperties("academie")
    @OneToMany(mappedBy = "academie", cascade = CascadeType.ALL)
    private Set<AcademieHistory> academieHistory;

    private String description;
    private Boolean isArchived=false;
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;

    @JsonIgnoreProperties("academie")
    @OneToMany(mappedBy = "academie", cascade = CascadeType.ALL)
    private Set<Adherent> adherents;

    @JsonIgnoreProperties("academie")
    @OneToOne(cascade = CascadeType.ALL)
    private Manager manager;

    @OneToMany(mappedBy = "academie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleName> roleNames = new HashSet<>();


    @OneToMany(mappedBy = "academie", cascade = CascadeType.ALL)
    private Set<Posts> PostsList = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id);  // Use a unique field to calculate hashCode
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Academie academie = (Academie) obj;
        return Objects.equals(id, academie.id);
    }

    public void updateEtat (Etat newEtat, String changeReason) {
        AcademieHistory academieHistory = new AcademieHistory();
        academieHistory.setAcademie(this);
        academieHistory.setPreviousEtat(this.etat);
        academieHistory.setNewEtat(newEtat);
        academieHistory.setChangeReason(changeReason);
        academieHistory.setChangeDate(LocalDateTime.now());
        this.etat = newEtat;
        if(this.academieHistory == null){
            this.academieHistory = new HashSet<>();
    }
        this.academieHistory.add(academieHistory);
    }
}
