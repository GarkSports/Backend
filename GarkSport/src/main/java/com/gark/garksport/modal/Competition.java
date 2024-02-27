package com.gark.garksport.modal;

import jakarta.persistence.Entity;

@Entity
public class Competition extends Evenement {
    private String nom;
    private String description;
}
