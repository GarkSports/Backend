package com.gark.garksport.modal;

import jakarta.persistence.Entity;

@Entity
public class Entrainement extends Evenement{
    private String nom;
    private String description;
}
