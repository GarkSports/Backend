package com.gark.garksport.service;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Entraineur;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.modal.Manager;

import java.util.Set;

public interface IRandomService {
    public Manager addManager(Manager manager);

    public Adherent addAndAffectAdherentToAcademie(Adherent adherent, Integer academieId);

    public Set<Manager> getManagersExceptAssigned(Integer academieId);

    public Set<Manager> getManagersNotAssigned();

    public Equipe addEquipe(Equipe equipe, Integer academieId, Integer entraineurId, Set<Integer> adherentIds, Integer disciplineId);

    public Entraineur addEntraineur(Entraineur entraineur);

    public Set<Equipe> getEquipesByAcademie(Integer academieId);

    public Set<Adherent> getAdherentsByAcademie(Integer academieId);

    public Set<Entraineur> getEntraineursByAcademie(Integer academieId);

    public void deleteEquipe(Integer equipeId);
}
