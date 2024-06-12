package com.gark.garksport.service;
import com.gark.garksport.dto.request.MatchAmicalRequest;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.modal.Evenement;
import com.gark.garksport.modal.enums.StatutEvenenement;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;


public interface IEvenementService {
    public Evenement addCompetition(Evenement evenement, Integer idEquipe, List<Integer> idMembres,  Integer managerId);
    public Evenement addPersonnalisé(Evenement evenement, Integer idEquipe, List<Integer> idMembres, Integer managerId);
    public Evenement addTest(Evenement evenement, Integer idEquipe, List<Integer> idMembres,  Integer managerId);
    public Evenement addMatchAmical(MatchAmicalRequest request, Integer managerId);
    public Set<Evenement> getAllEvenements(Integer managerId);
    public void deleteEvenement(Integer id);
    public Evenement changeStatutEvenement(Integer id, StatutEvenenement statutEvenenement);
    public List<Adherent> getMembersByEquipe(Integer idEquipe);
    public List<Adherent> getMembersByEvenement(Integer idEvenement);
    public Evenement updateEvenement(Evenement evenement, List<Integer> idMembres, Integer evenementId);

    public Evenement updateEvenementMatchAmical(Evenement evenement, Integer evenementId);

    public List<Equipe> getEquipesByEvenementMatchAmical(Integer idEvenement);


}
