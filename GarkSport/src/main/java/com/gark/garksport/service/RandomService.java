package com.gark.garksport.service;


import com.gark.garksport.modal.*;
import com.gark.garksport.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
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
    public Equipe addEquipe(Equipe equipe, Integer managerId, Integer disciplineId) {
        Academie academie = academieRepository.findById(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId()).get();
        Discipline discipline = disciplineRepository.findById(disciplineId).get();
        equipe.setAcademie(academie);
        equipe.setDiscipline(discipline);
        // Generate random code for the equipe
        String randomCode = generateRandomCode();
        equipe.setCodeEquipe(randomCode);
        return equipeRepository.save(equipe);
    }

    private String generateRandomCode() {
        // Generate a random code
        SecureRandom random = new SecureRandom();
        byte[] codeBytes = new byte[6]; // Adjust the length of the code as needed
        random.nextBytes(codeBytes);
        return Base64.getUrlEncoder().encodeToString(codeBytes).substring(0, 6).toUpperCase(); // Adjust the substring range and case as needed
    }

    @Override
    public Entraineur addEntraineur(Entraineur entraineur) {
        return entraineurRepository.save(entraineur);
    }

    @Override
    public Set<Equipe> getEquipesByAcademie(Integer managerId) {
        return equipeRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());
    }

    @Override
    public Set<Adherent> getAdherentsByAcademie(Integer managerId) {
        // Get all adherents for the academie
        Set<Adherent> allAdherents = adherentRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        // Get the IDs of adherents assigned to any equipe for the academie
        Set<Integer> assignedAdherentIds = equipeRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId()).stream()
                .flatMap(equipe -> equipe.getAdherents().stream().map(Adherent::getId))
                .collect(Collectors.toSet());

        // Filter out adherents who are not assigned to any equipe
        return allAdherents.stream()
                .filter(adherent -> !assignedAdherentIds.contains(adherent.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Entraineur> getEntraineursByAcademie(Integer managerId) {
        // Get all entraineurs for the academie
        Set<Entraineur> allEntraineurs = entraineurRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        // Get the IDs of entraineurs assigned to any equipe for the academie
        Set<Integer> assignedEntraineurIds = equipeRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId()).stream()
                .flatMap(equipe -> equipe.getEntraineurs().stream()) // FlatMap to handle multiple entraineurs per equipe
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

        // Update the nomEquipe property of each adherent
        for (Adherent adherent : adherents) {
            adherent.setNomEquipe(equipe.getNom());
        }
        return equipeRepository.save(equipe);
    }


    @Override
    public Equipe affectEntraineurToEquipe(Integer equipeId, List<Integer> entraineurIds) {
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new IllegalArgumentException("Equipe not found"));

        List<Entraineur> entraineurs = entraineurRepository.findAllById(entraineurIds);
        equipe.getEntraineurs().addAll(entraineurs);

        return equipeRepository.save(equipe);
    }

    @Override
    public Academie updateAcademie(Academie academie, Integer managerId) {
        Academie academieNew = academieRepository.findById(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId()).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
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

    @Override
    public void updateAcademieBackground(Integer academieId, String background) {
        try {
            Academie academie = academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
            academie.setBackgroundImage(background);
            academieRepository.save(academie);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update Academie background", e);
        }
    }

    @Override
    public Set<Entraineur> getEntraineursByEquipe(Integer equipeId) {
        return equipeRepository.findById(equipeId)
                .map(Equipe::getEntraineurs)
                .orElseThrow(() -> new IllegalArgumentException("Equipe not found"));
    }

    @Override
    public Set<Adherent> getMembersByAcademie(Integer academieId) {
        return adherentRepository.findByAcademieId(academieId);
    }

    @Override
    public Set<Adherent> getAllAdherentsByAcademie(Integer managerId) {
        return adherentRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId());
    }
}
