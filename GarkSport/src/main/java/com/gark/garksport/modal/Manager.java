package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Manager extends User {
    private String telephone2;

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), telephone2);
    }


    @JsonIgnoreProperties({"academie"})
    @OneToOne(mappedBy = "manager")
    private Academie academie;

    @Override
    public String toString() {
        return "Manager{telephone2='" + telephone2 + "', " + super.toString() + "}";
    }
}