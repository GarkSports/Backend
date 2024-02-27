package com.gark.garksport.modal;

import jakarta.persistence.Entity;

@Entity
public class MatchAmical extends Evenement{
    private String nomAdversaire;
    private String force;
    private String faiblesse;
    private String commentaire;
    private String nom;
    private String description;

}
