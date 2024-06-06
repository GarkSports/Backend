package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gark.garksport.modal.enums.Comptabiliteetat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@Entity
public class Benefices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;

    private Comptabiliteetat etat;

    private int quantite;

    @Column(name = "prix_unite")
    private BigDecimal prixUnite;

    private BigDecimal total;

    private LocalDate date;

    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne
    private Academie academie;


    public Benefices() {
        // Initialisez la date avec la date actuelle par défaut
        this.date = LocalDate.now();
    }


    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        this.total = BigDecimal.valueOf(this.quantite).multiply(this.prixUnite);
    }

    // Autres méthodes si nécessaire
}

