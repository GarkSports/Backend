package com.gark.garksport.dto.request;

import com.gark.garksport.modal.enums.Etat;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AcademieEtatRequest {
    private Etat etat;
    private long count;

    @JsonCreator
    public AcademieEtatRequest(@JsonProperty("etat") Etat etat, @JsonProperty("count") long count) {
        this.etat = etat;
        this.count = count;
    }

    // Getter and setter methods if needed
}
