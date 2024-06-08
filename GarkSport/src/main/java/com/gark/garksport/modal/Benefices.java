package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gark.garksport.modal.enums.Comptabiliteetat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Benefices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;

    private Comptabiliteetat etat;

    private double quantite;

    @Column(name = "prix_unite")
    private BigDecimal prixunite;

    private BigDecimal total;

    private LocalDate date;

    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne
    private Academie academie;


    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        this.total = BigDecimal.valueOf(this.quantite).multiply(this.prixunite);
    }

    // Autres méthodes si nécessaire
}

