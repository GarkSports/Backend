package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.modal.Evenement;
import lombok.Data;

@Data
public class ExtendedEventDTO {
    private Evenement event;
    private Equipe equipe;
    private Adherent adherent;

    public ExtendedEventDTO(Evenement event) {
        this.event = event;
    }

    public ExtendedEventDTO(Evenement event, Equipe equipe) {
        this.event = event;
        this.equipe = equipe;
    }

    public ExtendedEventDTO(Evenement event, Adherent adherent) {
        this.event = event;
        this.adherent = adherent;
    }

    public Evenement getEvent() {
            return event;
    }



    public Equipe getEquipe() {
            return equipe;
    }

    public Adherent getAdherent() {
            return adherent;
    }

}
