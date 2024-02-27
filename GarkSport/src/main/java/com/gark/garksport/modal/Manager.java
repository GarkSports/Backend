package com.gark.garksport.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Manager extends User {
    private String telephone2;

    @OneToOne(mappedBy = "manager")
    private Academie academie;

}
