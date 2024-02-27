package com.gark.garksport.modal;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Entrainement extends Evenement{
    private String nom;
    private String description;
}
