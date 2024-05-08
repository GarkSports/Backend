package com.gark.garksport.dto.request;

import lombok.Data;

import java.time.LocalTime;
@Data
public class EquipeHoraireDTO {
    private Integer equipeId;
    private LocalTime horaire;

    public Integer getEquipeId() {
        return equipeId;
    }
    public LocalTime getHoraire() {
        return horaire;
    }
}
