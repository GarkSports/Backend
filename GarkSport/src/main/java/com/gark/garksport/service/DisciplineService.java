package com.gark.garksport.service;

import com.gark.garksport.modal.Discipline;
import com.gark.garksport.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DisciplineService implements IDisciplineService {
    @Autowired
    private DisciplineRepository disciplineRepository;

    @Override
    public Discipline addDiscipline(Discipline discipline) {
        try {
            discipline.setProtectedDiscipline(true);
            return disciplineRepository.save(discipline);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add Discipline", e);
        }
    }

    @Override
    public Set<Discipline> getDisciplines() {
        try {
            return new HashSet<>(disciplineRepository.findByProtectedDisciplineTrue());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Disciplines", e);
        }
    }

    @Override
    public void deleteDiscipline(Integer id) {
        try {
            disciplineRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Discipline", e);
        }
    }

    @Override
    public Discipline updateDiscipline(Integer id, Discipline updatedDiscipline) {
        if (!disciplineRepository.existsById(id)) {
            throw new RuntimeException("Discipline with id " + id + " does not exist");
        }
        try {
            updatedDiscipline.setId(id);
            return disciplineRepository.save(updatedDiscipline);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update Discipline", e);
        }
    }

    @Override
    public Discipline addDisciplineManager(Discipline discipline) {
        try {
            discipline.setProtectedDiscipline(false);
            return disciplineRepository.save(discipline);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add Discipline", e);
        }
    }

    @Override
    public Set<Discipline> getAllDisciplines() {
        try {
            return new HashSet<>(disciplineRepository.findAll());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Disciplines", e);
        }
    }
}
