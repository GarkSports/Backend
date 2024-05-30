package com.gark.garksport.service;

import com.gark.garksport.dto.request.EntrainementRequest;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.ConvocationEntrainement;
import com.gark.garksport.modal.Equipe;

import java.util.List;
import java.util.Set;

public interface IEntrainementService {
    public Equipe addEntrainement(EntrainementRequest entrainementRequest, Integer idEquipe);

    public Equipe updateConvocationEntrainement(EntrainementRequest entrainementRequest, Integer idConvocation);

    public List<Equipe> getEntrainements();

    public void deleteEntrainement(Integer idConvocation);

    public List<Adherent> getAdherentsByConvocation(Integer idConvocation);
}
