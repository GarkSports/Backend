package com.gark.garksport.service;

import com.gark.garksport.modal.Discipline;
import com.gark.garksport.repository.DisciplineRepository;
import com.gark.garksport.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DisciplineService implements IDisciplineService {
    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private ManagerRepository managerRepository;

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
    public Discipline addDisciplineManager(Discipline discipline, Integer managerId) {
        try {
            discipline.setAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());
            discipline.setProtectedDiscipline(false);
            return disciplineRepository.save(discipline);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add Discipline", e);
        }
    }

    @Override
    public Set<Discipline> getAllDisciplines(Integer managerId) {
        try {
            List<Discipline> protectedDisciplines = disciplineRepository.findAll().stream()
                    .filter(Discipline::getProtectedDiscipline)
                    .collect(Collectors.toList());

            List<Discipline> academieDisciplines = new ArrayList<>();
            if (managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId() != null) {
                academieDisciplines = disciplineRepository.findAll().stream()
                        .filter(discipline -> managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId().equals(discipline.getAcademieId()))
                        .collect(Collectors.toList());
            }

            protectedDisciplines.addAll(academieDisciplines);
            return new HashSet<>(protectedDisciplines);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Disciplines", e);
        }
    }

    @Override
    public Discipline getDisciplineById(Integer id) {
        return disciplineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Discipline not found"));
    }
}
