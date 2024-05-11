package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Evenement;
import lombok.Data;

import java.util.List;

@Data
public class TestRequest {
    private Evenement evenement;
    private Integer idEquipe;
    private List<Integer> idMembres;

    public Evenement getEvenement() {
        return evenement;
    }

    public Integer getIdEquipe() {
        return idEquipe;
    }

    public List<Integer> getIdMembres() {
        return idMembres;
    }
}
