package com.gark.garksport.service;
import com.gark.garksport.modal.*;
import com.gark.garksport.modal.enums.Etat;
import com.gark.garksport.modal.enums.EvaluationType;
import com.gark.garksport.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class AcademieService implements IAcademieService {
    @Autowired
    private AcademieRepository academieRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private AcademieHistoryRepository academieHistoryRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;

    @Override
    public Academie addAcademie(Academie academie, Integer managerId) {
        try {
            Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found"));
            academie.setManager(manager);
            Academie savedAcademie = academieRepository.save(academie);
            addTest(savedAcademie);

            return savedAcademie;

        } catch (Exception e) {
            throw new RuntimeException("Failed to add Academie", e);
        }
    }

    public void addTest(Academie academie) {
        // Create a new test
        Test test = new Test();
        test.setTestName("Default Test Name"); // Set the test name as required

        // Create and add Mentality category with KPIs
        Categorie mentalityCategorie = new Categorie("Mentalite");
        addKpiToCategorie(mentalityCategorie, "Attitude");
        addKpiToCategorie(mentalityCategorie, "Leadership");
        addKpiToCategorie(mentalityCategorie, "Intensité");
        addKpiToCategorie(mentalityCategorie, "Assiduité");
        mentalityCategorie.setTest(test);

        // Create and add Physique category with KPIs
        Categorie physiqueCategorie = new Categorie("Physique");
        addKpiToCategorie(physiqueCategorie, "Coordination");
        addKpiToCategorie(physiqueCategorie, "Vitesse");
        addKpiToCategorie(physiqueCategorie, "Endurance");
        addKpiToCategorie(physiqueCategorie, "Force");
        physiqueCategorie.setTest(test);

        // Create and add Technique category with KPIs
        Categorie techniqueCategorie = new Categorie("Technique");
        addKpiToCategorie(techniqueCategorie, "Dribble");
        addKpiToCategorie(techniqueCategorie, "Conduite");
        addKpiToCategorie(techniqueCategorie, "Passe courte");
        addKpiToCategorie(techniqueCategorie, "Passe longue");
        addKpiToCategorie(techniqueCategorie, "1er touche");
        addKpiToCategorie(techniqueCategorie, "Tir");
        addKpiToCategorie(techniqueCategorie, "Tête");
        addKpiToCategorie(techniqueCategorie, "Pied faible");
        techniqueCategorie.setTest(test);

        // Create and add Tactic category with KPIs
        Categorie tacticCategorie = new Categorie("Tactique");
        addKpiToCategorie(tacticCategorie, "Jeu Defensif");
        addKpiToCategorie(tacticCategorie, "Jeu Offensif");
        addKpiToCategorie(tacticCategorie, "Vision");
        addKpiToCategorie(tacticCategorie, "Prise de décision");
        tacticCategorie.setTest(test);

        // Add categories to the test
        test.getCategories().add(mentalityCategorie);
        test.getCategories().add(physiqueCategorie);
        test.getCategories().add(techniqueCategorie);
        test.getCategories().add(tacticCategorie);

        // Set the academie reference in the test
        test.setAcademie(academie);

        // Add the test to the academie
        if (academie.getTests() == null) {
            academie.setTests(new ArrayList<>());
        }
        academie.getTests().add(test);

        academieRepository.save(academie);
        // No need to explicitly save academie here, it will be saved in addAcademie method
    }

    private void addKpiToCategorie(Categorie categorie, String kpiType) {
        Kpi kpi = new Kpi(kpiType);
        kpi.setCategorie(categorie);
        categorie.getKpis().add(kpi);
    }


    @Override
    public Set<Academie> getAcademies() {
        try {
            return academieRepository.findByIsArchivedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Academies", e);
        }
    }

    @Override
    public Academie updateAcademie(Academie academie, Integer academieId, Integer managerId) {
        try {
            Academie academieNew = academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
            Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found"));
            academieNew.setNom(academie.getNom());
            academieNew.setType(academie.getType());
            academieNew.setFraisAdhesion(academie.getFraisAdhesion());
            academieNew.setDescription(academie.getDescription());
            academieNew.setRue(academie.getRue());
            academieNew.setVille(academie.getVille());
            academieNew.setCodePostal(academie.getCodePostal());
            academieNew.setPays(academie.getPays());
            academieNew.setLogo(academie.getLogo());
            academieNew.setManager(manager);
            return academieRepository.save(academieNew);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update Academie", e);
        }
    }

    @Override
    public Academie changeEtatAcademie(Integer academieId, Etat newEtat, String changeReason) {
        Academie academie = academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
        academie.updateEtat(newEtat, changeReason);
        return academieRepository.save(academie);
    }

    @Override
    public Set<AcademieHistory> getAcademieHistory(Integer academieId) {
        return academieHistoryRepository.findByAcademie_IdOrderByChangeDateDesc(academieId);
    }

    @Override
    public void deleteAcademie(Integer academieId) {
        try {
            Academie academie = academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
            Set<Adherent> adherents = academie.getAdherents();
            for (Adherent adherent : adherents) {
                senDeleteNotificationEmail(adherent.getEmail(), academie.getNom());
            }
            academie.setIsArchived(true);
            academieRepository.save(academie);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Academie", e);
        }
    }

    @Override
    public Academie getAcademieById(Integer managerId) {
        try {
            return academieRepository.findById(managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found")).getAcademie().getId()).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Academie by Id", e);
        }
    }

    private void senDeleteNotificationEmail(String recipientEmail, String academieNom){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("centrenationaldelinformatiquec@gmail.com");
        message.setTo(recipientEmail);
        message.setSubject("Academie Deletion Notification");
        message.setText("Dear Adherent,\n\nThe academy '" + academieNom + "' has been deleted.\n\nSincerely,\nThe Management");
        javaMailSender.send(message);
    }

    @Override
    public Manager getManagerDetails(Integer academieId) {
        try {
            Academie academie = academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
            return academie.getManager();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Manager details", e);
        }
    }

    @Override
    public Set<Academie> getArchivedAcademies() {
        try {
            return academieRepository.findByIsArchivedTrue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Academies", e);
        }
    }

    @Override
    public void deleteArchivedAcademie(Integer academieId) {
        try {
            academieRepository.deleteById(academieId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Academie", e);
        }
    }

    @Override
    public void restoreArchivedAcademie(Integer academieId) {
        try {
            Academie academie = academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
            academie.setIsArchived(false);
            academieRepository.save(academie);
        } catch (Exception e) {
            throw new RuntimeException("Failed to restore Academie", e);
        }

    }

    public boolean isManager(Integer userId) {
        String role = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"))
                .getRole()
                .toString();
        return role.equals("MANAGER");
    }

    public boolean isAdmin(Integer userId) {
        String role = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"))
                .getRole()
                .toString();
        return role.equals("ADMIN");
    }

    @Override
    public Academie getDetailAcademie(Integer academieId) {
        try {
            return academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Academie", e);
        }
    }

}
