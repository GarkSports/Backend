package com.gark.garksport.controller;

import com.gark.garksport.modal.Discipline;
import com.gark.garksport.service.IDisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/discipline")
public class DisciplineController {
    @Autowired
    private IDisciplineService disciplineService;

    @PostMapping("/addDiscipline")
    public Discipline addDiscipline(@RequestBody Discipline discipline) {
        return disciplineService.addDiscipline(discipline);
    }

    @GetMapping("/getDisciplines")
    public Set<Discipline> getDisciplines() {
        return disciplineService.getDisciplines();
    }

    @DeleteMapping("/deleteDiscipline/{id}")
    public void deleteDiscipline(@PathVariable Integer id) {
        disciplineService.deleteDiscipline(id);
    }

    @PutMapping("/updateDiscipline/{id}")
    public Discipline updateDiscipline(@PathVariable Integer id, @RequestBody Discipline updatedDiscipline) {
        return disciplineService.updateDiscipline(id, updatedDiscipline);
    }

    @PostMapping("/addDisciplineManager")
    public Discipline addDisciplineManager(@RequestBody Discipline discipline) {
        return disciplineService.addDisciplineManager(discipline);
    }

    @GetMapping("/getAllDisciplines")
    public Set<Discipline> getAllDisciplines() {
        return disciplineService.getAllDisciplines();
    }
}
