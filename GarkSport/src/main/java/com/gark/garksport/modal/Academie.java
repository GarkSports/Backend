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
    private String logo="https://firebasestorage.googleapis.com/v0/b/angularimage-43fec.appspot.com/o/academie%2Ffile.png?alt=media&token=80bf1bec-48e9-4980-ae53-5a532aff0fc3";
    private String backgroundImage="https://firebasestorage.googleapis.com/v0/b/angularimage-43fec.appspot.com/o/academie%2F2eeb6d_5d7cdbcf50e24419ab6df9e1540c7eb.jpg?alt=media&token=475ea6a6-a246-472a-a487-65f6a7f495b8";
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
    @OneToOne
    private Manager manager;

    @JsonIgnoreProperties("academie")
    @OneToMany(mappedBy = "academie", cascade = CascadeType.ALL)
    private Set<Adherent> adherents;

    @JsonIgnoreProperties("academie")
    @OneToMany(mappedBy = "academie", cascade = CascadeType.ALL)
    private Set<Entraineur> entraineurs;

    @JsonIgnoreProperties("academie")
    @OneToMany(mappedBy = "academie", cascade = CascadeType.ALL)
    private Set<Staff> staffs;

    @JsonIgnoreProperties("academie")
    @OneToMany(mappedBy = "academie", cascade = CascadeType.ALL)
    private Set<Parent> parents;

    @JsonIgnoreProperties("academie")
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