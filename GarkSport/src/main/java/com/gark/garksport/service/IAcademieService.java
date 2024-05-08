package com.gark.garksport.service;

import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.AcademieHistory;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.enums.Etat;

import java.util.Set;

public interface IAcademieService {
    public Academie addAcademie(Academie academie, Integer managerId);
    public Set<Academie> getAcademies();
    public Academie updateAcademie(Academie academie, Integer academieId, Integer managerId);
    public Academie changeEtatAcademie(Integer academieId, Etat newEtat, String changeReason);
    public Set<AcademieHistory> getAcademieHistory(Integer academieId);
    public void deleteAcademie(Integer academieId);
    public Academie getAcademieById(Integer managerId);
    public Manager getManagerDetails(Integer academieId);
    public Set<Academie> getArchivedAcademies();
    public void deleteArchivedAcademie(Integer academieId);
    public void restoreArchivedAcademie(Integer academieId);
    public boolean isManager(Integer userId);
    public boolean isAdmin(Integer userId);
}
