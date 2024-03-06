package com.gark.garksport.service;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.enums.Etat;

import java.util.Set;

public interface IAcademieService {
    public Academie addAcademie(Academie academie,Set<Integer> disciplinesIds, Integer managerId);
    public Set<Academie> getAcademies();
    public Academie updateAcademie(Academie academie, Integer academieId);
    public Academie chanegeEtatAcademie(Integer academieId, Etat etat);
    public void deleteAcademie(Integer academieId);

    public Manager getManagerDetails(Integer academieId);
    public Set<String> getDisciplinesByAcademie(Integer academieId);





}
