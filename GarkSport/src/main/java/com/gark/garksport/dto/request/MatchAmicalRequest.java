package com.gark.garksport.dto.request;

import com.gark.garksport.modal.Evenement;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class MatchAmicalRequest {
    private Evenement evenement;
    List<EquipeHorraireDTO> equipesHorraires;

    public Evenement getEvenement() {
        return evenement;
    }
}
