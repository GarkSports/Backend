package com.gark.garksport.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Manager extends User {
    private String telephone2;

    @OneToOne(mappedBy = "manager")
    private Academie academie;

}
