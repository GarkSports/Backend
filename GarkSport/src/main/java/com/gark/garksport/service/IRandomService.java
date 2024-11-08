package com.gark.garksport.service;

import com.gark.garksport.modal.*;

import java.util.List;
import java.util.Set;

public interface IRandomService {
    public Manager addManager(Manager manager);

    public Adherent addAndAffectAdherentToAcademie(Adherent adherent, Integer academieId);

    public Set<Manager> getManagersExceptAssigned(Integer academieId);

    public Set<Manager> getManagersNotAssigned();

    public Equipe addEquipe(Equipe equipe, Integer managerId, Integer disciplineId);

    public Equipe updateEquipe(Equipe equipe, Integer equipeId);

    public Entraineur addEntraineur(Entraineur entraineur);

    public Set<Equipe> getEquipesByAcademie(Integer managerId);

    public Set<Adherent> getAdherentsByAcademie(Integer managerId, Integer equipeId);

    public Set<Entraineur> getEntraineursByAcademie(Integer managerId, Integer equipeId);

    public void deleteEquipe(Integer equipeId);

    public Equipe affectAdherentToEquipe(Integer equipeId, List<Integer> adherentIds);

    public Equipe affectEntraineurToEquipe(Integer equipeId, List<Integer> entraineurIds);

    public Academie updateAcademie(Academie academie, Integer managerId);

    public void updateAcademieBackground(Integer academieId, String background);

    public Set<Entraineur> getEntraineursByEquipe(Integer equipeId);

    public Set<Adherent> getMembersByAcademie(Integer academieId);

    public Set<Adherent> getAllAdherentsByAcademie(Integer managerId);

    public Adherent addAdherent(Adherent adherent, String CodeEquipe);

    public void removeAdherentFromEquipe(Integer adherentId, Integer equipeId);

    public void removeEntraineurFromEquipe(Integer entraineurId, Integer equipeId);
}
