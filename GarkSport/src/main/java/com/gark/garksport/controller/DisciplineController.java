package com.gark.garksport.controller;

import com.gark.garksport.modal.Discipline;
import com.gark.garksport.service.IDisciplineService;
import com.gark.garksport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/discipline")
@RequiredArgsConstructor
public class DisciplineController {
    @Autowired
    private IDisciplineService disciplineService;
    private final UserService userService;

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
    public Discipline addDisciplineManager(@RequestBody Discipline discipline, Principal connectedUser) {
        return disciplineService.addDisciplineManager(discipline,userService.getUserId(connectedUser.getName()));
    }

    @GetMapping("/getAllDisciplines")
    public Set<Discipline> getAllDisciplines(Principal connectedUser) {
        return disciplineService.getAllDisciplines(userService.getUserId(connectedUser.getName()));
    }

    @GetMapping("/getDisciplineById/{id}")
    public Discipline getDisciplineById(@PathVariable Integer id) {
        return disciplineService.getDisciplineById(id);
    }
}
