package com.gark.garksport.service;
import com.gark.garksport.dto.request.EquipeHoraireDTO;
import com.gark.garksport.modal.Evenement;
import com.gark.garksport.modal.enums.StatutEvenenement;

import java.util.List;


public interface IEvenementService {
    public Integer getEquipesMatchAmical(Integer managerId);
    public Integer getEquipesTest(Integer managerId);
    public Integer getMembersTest(Integer managerId);
    public Integer getMembersPersonnalise(Integer managerId);
    public Integer getEquipesPersonnalise(Integer managerId);
    public Evenement addCompetition(Evenement evenement, Integer managerId);
    public Evenement addPersonnalisé(Evenement evenement, Integer idEquipe, List<Integer> idMembres, Integer managerId);
    public Evenement addTest(Evenement evenement, List<Integer> idEquipe, List<Integer> idMembres,  Integer managerId);
    public Evenement addMatchAmical(Evenement evenement, List<EquipeHoraireDTO> equipesHoraire, Integer managerId);
    public List<Evenement> getAllEvenements();
    public void deleteEvenement(Integer id);
    public Evenement changeStatutEvenement(Integer id, StatutEvenenement statutEvenenement);

}
