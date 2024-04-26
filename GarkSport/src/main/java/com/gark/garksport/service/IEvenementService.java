package com.gark.garksport.service;

import com.gark.garksport.dto.request.EquipeHoraireDTO;
import com.gark.garksport.modal.Evenement;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IEvenementService {
    public Evenement addMatchAmical(Evenement evenement, List<EquipeHoraireDTO> equipeHoraires);

}
