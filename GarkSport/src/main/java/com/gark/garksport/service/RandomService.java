package com.gark.garksport.service;


import com.gark.garksport.modal.*;
import com.gark.garksport.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RandomService implements IRandomService {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private AcademieRepository academieRepository;
    @Autowired
    private AdherentRepository adherentRepository;
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private EntraineurRepository entraineurRepository;
    @Autowired
    private DisciplineRepository disciplineRepository;
    @Override
    public Manager addManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public Adherent addAndAffectAdherentToAcademie(Adherent adherent, Integer academieId) {
        Academie academie = academieRepository.findById(academieId).get();
        academie.getAdherents().add(adherent);
        adherent.setAcademie(academie);
        adherentRepository.save(adherent);
        academieRepository.save(academie);
        return adherent;
    }

    @Override
    public Set<Manager> getManagersExceptAssigned(Integer academieId) {
        Academie academie = academieRepository.findById(academieId).get();
        Manager academieManager =academie.getManager();
        Set<Manager> managers = managerRepository.findAll().stream().filter(manager -> manager.getAcademie() == null).collect(Collectors.toSet());
        managers.add(academieManager);
        return managers;
    }

    @Override
    public Set<Manager> getManagersNotAssigned() {
        return managerRepository.findAll()
                .stream()
                .filter(manager -> manager.getAcademie() == null)
                .collect(Collectors.toSet());
    }

    @Override
    public Equipe addEquipe(Equipe equipe, Integer academieId, Integer entraineurId, Set<Integer> adherentIds,Integer disciplineId) {
        Academie academie = academieRepository.findById(academieId).get();
        Entraineur entraineur = entraineurRepository.findById(entraineurId).get();
        Set<Adherent> adherents = adherentIds.stream().map(adherentRepository::findById).map(adherent -> adherent.orElse(null)).collect(Collectors.toSet());
        Discipline discipline = disciplineRepository.findById(disciplineId).get();
        equipe.setAcademie(academie);
        equipe.setEntraineur(entraineur);
        equipe.setAdherents(adherents);
        equipe.setDiscipline(discipline);
        return equipeRepository.save(equipe);
    }

    @Override
    public Entraineur addEntraineur(Entraineur entraineur) {
        return entraineurRepository.save(entraineur);
    }

    @Override
    public Set<Equipe> getEquipesByAcademie(Integer academieId) {
        return equipeRepository.findByAcademieId(academieId);
    }

    @Override
    public Set<Adherent> getAdherentsByAcademie(Integer academieId) {
        // Get all adherents for the academie
        Set<Adherent> allAdherents = adherentRepository.findByAcademieId(academieId);

        // Get the IDs of adherents assigned to any equipe for the academie
        Set<Integer> assignedAdherentIds = equipeRepository.findByAcademieId(academieId).stream()
                .flatMap(equipe -> equipe.getAdherents().stream().map(Adherent::getId))
                .collect(Collectors.toSet());

        // Filter out adherents who are not assigned to any equipe
        return allAdherents.stream()
                .filter(adherent -> !assignedAdherentIds.contains(adherent.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Entraineur> getEntraineursByAcademie(Integer academieId) {
        // Get all entraineurs for the academie
        Set<Entraineur> allEntraineurs = entraineurRepository.findByAcademieId(academieId);

        // Get the IDs of entraineurs assigned to any equipe for the academie
        Set<Integer> assignedEntraineurIds = equipeRepository.findByAcademieId(academieId).stream()
                .map(Equipe::getEntraineur)
                .filter(Objects::nonNull)
                .map(Entraineur::getId)
                .collect(Collectors.toSet());

        // Filter out entraineurs who are not assigned to any equipe
        return allEntraineurs.stream()
                .filter(entraineur -> !assignedEntraineurIds.contains(entraineur.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteEquipe(Integer equipeId) {
        equipeRepository.deleteById(equipeId);
    }

    @Override
    public Equipe affectAdherentToEquipe(Integer equipeId, List<Integer> adherentIds) {
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new IllegalArgumentException("Equipe not found"));

        List<Adherent> adherents = adherentRepository.findAllById(adherentIds);
        equipe.getAdherents().addAll(adherents);

        return equipeRepository.save(equipe);
    }


    @Override
    public Academie updateAcademie(Academie academie, Integer academieId) {
        Academie academieNew = academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
        if (academie.getNom() != null) {
            academieNew.setNom(academie.getNom());
        }
        if (academie.getFraisAdhesion() != null) {
            academieNew.setFraisAdhesion(academie.getFraisAdhesion());
        }
        if (academie.getAffiliation() != null) {
            academieNew.setAffiliation(academie.getAffiliation());
        }
        if (academie.getDescription() != null) {
            academieNew.setDescription(academie.getDescription());
        }
        if (academie.getRue() != null) {
            academieNew.setRue(academie.getRue());
        }
        if (academie.getVille() != null) {
            academieNew.setVille(academie.getVille());
        }
        if (academie.getCodePostal() != null) {
            academieNew.setCodePostal(academie.getCodePostal());
        }
        if (academie.getPays() != null) {
            academieNew.setPays(academie.getPays());
        }
        if (academie.getLogo() != null) {
            academieNew.setLogo(academie.getLogo());
        }
        return academieRepository.save(academieNew);
    }


}
