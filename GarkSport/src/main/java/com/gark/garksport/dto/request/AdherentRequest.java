package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Adherent;

import java.util.List;

public class AdherentRequest {
    private Adherent adherent;
    private List<String> equipeNames;

    // Getters and setters
    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    public List<String> getEquipeNames() {
        return equipeNames;
    }

    public void setEquipeNames(List<String> equipeNames) {
        this.equipeNames = equipeNames;
    }
}
