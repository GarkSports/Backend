package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Equipe;
import lombok.Data;

import java.util.Set;

@Data
public class EquipeRequest {
    private Equipe equipe;
    private Integer entraineurId;
    private Set<Integer> adherentIds;

    private Integer disciplineId;

    public Equipe getEquipe() {
        return equipe;
    }

    public Integer getEntraineurId() {
        return entraineurId;
    }

    public Set<Integer> getAdherentIds() {
        return adherentIds;
    }

    public Integer getDisciplineId() {
        return disciplineId;
    }
}
