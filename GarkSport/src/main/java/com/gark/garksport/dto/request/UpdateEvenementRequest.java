package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Evenement;
import lombok.Data;

import java.util.List;
@Data
public class UpdateEvenementRequest {
    private Evenement evenement;
    private List<Integer> idMembres;

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public List<Integer> getIdMembres() {
        return idMembres;
    }

    public void setIdMembres(List<Integer> idMembres) {
        this.idMembres = idMembres;
    }

}
