package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Evenement;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class MatchAmicalRequest {
    private Evenement evenement;
    private Integer equipeId;
    private LocalTime horaire;

    public Evenement getEvenement() {
        return evenement;
    }

    public Integer getEquipeId() {
        return equipeId;
    }

    public LocalTime getHoraire() {
        return horaire;
    }
}
