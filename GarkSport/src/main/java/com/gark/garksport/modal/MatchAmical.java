package com.gark.garksport.modal;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class MatchAmical extends Evenement{
    private String nomAdversaire;
    private String force;
    private String faiblesse;
    private String commentaire;
    private String nom;
    private String description;

}
