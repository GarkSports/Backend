package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Equipe;
import lombok.Data;

import java.util.Set;

@Data
public class EquipeRequest {
    private Equipe equipe;
    private Set<Integer>  entraineurIds;
    private Integer disciplineId;
    public Equipe getEquipe() {
        return equipe;
    }
    public Set<Integer> getEntraineurIds() {
        return entraineurIds;
    }
    public Integer getDisciplineId() {
        return disciplineId;
    }
}
