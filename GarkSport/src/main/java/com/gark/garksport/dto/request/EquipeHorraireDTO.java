package com.gark.garksport.dto.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class EquipeHorraireDTO {
    private Integer equipeId;
    private LocalTime horraire;

    public EquipeHorraireDTO() {
    }

    public EquipeHorraireDTO(Integer equipeId, LocalTime horraire) {
        this.equipeId = equipeId;
        this.horraire = horraire;
    }
}
