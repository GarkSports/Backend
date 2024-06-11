package com.gark.garksport.modal;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;


@Entity
@Data
@DiscriminatorValue("D")
public class Depenses extends Benefices {

    private String Beneficiaire;



}
