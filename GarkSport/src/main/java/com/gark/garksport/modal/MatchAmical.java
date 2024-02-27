package com.gark.garksport.modal;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MatchAmical extends Evenement{
    private String nomAdversaire;
    private String force;
    private String faiblesse;
    private String commentaire;
    private String nom;
    private String description;

}
