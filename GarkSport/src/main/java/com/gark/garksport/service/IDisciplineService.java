package com.gark.garksport.service;

import com.gark.garksport.modal.Discipline;

import java.util.Set;

public interface IDisciplineService {
    public Discipline addDiscipline(Discipline discipline);
    public Set<Discipline> getDisciplines();
    public Discipline updateDiscipline(Integer id, Discipline updatedDiscipline);
    public void deleteDiscipline(Integer id);
    public Discipline addDisciplineManager(Discipline discipline, Integer managerId);
    public Set<Discipline> getAllDisciplines(Integer managerId);
}
