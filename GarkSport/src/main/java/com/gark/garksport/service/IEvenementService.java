package com.gark.garksport.service;
import com.gark.garksport.dto.request.EquipeHoraireDTO;
import com.gark.garksport.dto.request.ExtendedEventDTO;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Evenement;
import com.gark.garksport.modal.enums.StatutEvenenement;

import java.lang.reflect.Member;
import java.time.LocalTime;
import java.util.List;


public interface IEvenementService {
    public Evenement addCompetition(Evenement evenement, Integer managerId);
    public Evenement addPersonnalisé(Evenement evenement, Integer idEquipe, List<Integer> idMembres, Integer managerId);
    public Evenement addTest(Evenement evenement, Integer idEquipe, List<Integer> idMembres,  Integer managerId);
    public Evenement addMatchAmical(Evenement evenement, Integer equipeId, LocalTime horraire, Integer managerId);
    public List<Evenement> getAllEvenements();
    public void deleteEvenement(Integer id);
    public Evenement changeStatutEvenement(Integer id, StatutEvenenement statutEvenenement);
    public List<Adherent> getMembersByEquipe(Integer idEquipe);

}
