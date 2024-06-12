package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.StatutAdherent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaiementHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @Temporal(TemporalType.DATE)
    private Date datePaiement;
    private Float montant;
    private Float reste;
    private Integer retardPaiement;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private StatutAdherent statutAdherent;

    @ManyToOne
    @JoinColumn(name = "adherent_id")
    private Adherent adherent;
}
