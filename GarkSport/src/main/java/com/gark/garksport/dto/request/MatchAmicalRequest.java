package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Evenement;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class MatchAmicalRequest {
    private Evenement evenement;
    private List<EquipeHoraireDTO> equipes;

    public Evenement getEvenement() {
        return evenement;
    }

    public List<EquipeHoraireDTO> getEquipes() {
        return equipes;
    }
}
