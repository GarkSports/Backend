package com.gark.garksport.dto.request;

import java.util.Date;

public class EquipeHoraireDTO {
    private Integer equipeId;
    private String horaire; // Correct type: String

    public EquipeHoraireDTO(Integer equipeId, String horaire) {
        this.equipeId = equipeId;
        this.horaire = horaire;
    }

    // Getters and setters
    public Integer getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(Integer equipeId) {
        this.equipeId = equipeId;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }
}
