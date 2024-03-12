package com.gark.garksport.dto.request;

import com.gark.garksport.modal.enums.Etat;

public class EtatRequest {
    private Etat etat;
    private String changeReason;

    public Etat getEtat() {
        return etat;
    }

    public String getChangeReason() {
        return changeReason;
    }
}
