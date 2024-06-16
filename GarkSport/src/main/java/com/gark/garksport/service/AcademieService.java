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
    @Autowired
    private TestRepository testRepository;

    @Override
    public Academie addAcademie(Academie academie, Integer managerId) {
        try {
            Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found"));
            academie.setManager(manager);
            Kpi kpi = new Kpi();

            Test newTest = new Test();
            newTest.setTestName("Test par défaut");
            newTest.setAcademie(academie);

            // Create Mentality evaluation
            Categorie mentalityCategorie = new Categorie("Mentalite");
            mentalityCategorie.getKpis().add(new Kpi("Attitude"));
            mentalityCategorie.getKpis().add(new Kpi("Leadership"));
            mentalityCategorie.getKpis().add(new Kpi("Intensité"));
            mentalityCategorie.getKpis().add(new Kpi("Assiduité"));

            kpi.setCategorie(mentalityCategorie);
            mentalityCategorie.getKpis().add(kpi);


            // Create Physique evaluation
            Categorie physiqueCategorie = new Categorie("Physique");
            physiqueCategorie.getKpis().add(new Kpi("Coordination"));
            physiqueCategorie.getKpis().add(new Kpi("Vitesse"));
            physiqueCategorie.getKpis().add(new Kpi("Endurance"));
            physiqueCategorie.getKpis().add(new Kpi("Force"));

            kpi.setCategorie(physiqueCategorie);
            physiqueCategorie.getKpis().add(kpi);

            // Create Technique evaluation
            Categorie techniqueCategorie = new Categorie("Technique");
            techniqueCategorie.getKpis().add(new Kpi("Dribble"));
            techniqueCategorie.getKpis().add(new Kpi("Conduite"));
            techniqueCategorie.getKpis().add(new Kpi("Passe courte"));
            techniqueCategorie.getKpis().add(new Kpi("Passe longue"));
            techniqueCategorie.getKpis().add(new Kpi("1er touche"));
            techniqueCategorie.getKpis().add(new Kpi("Tir"));
            techniqueCategorie.getKpis().add(new Kpi("Tête"));
            techniqueCategorie.getKpis().add(new Kpi("Pied faible"));

            kpi.setCategorie(techniqueCategorie);
            techniqueCategorie.getKpis().add(kpi);

            // Create Tactic evaluation
            Categorie tacticCategorie = new Categorie("Tactique");
            tacticCategorie.getKpis().add(new Kpi("Jeu Defensif"));
            tacticCategorie.getKpis().add(new Kpi("Jeu Offensif"));
            tacticCategorie.getKpis().add(new Kpi("Vision"));
            tacticCategorie.getKpis().add(new Kpi("Prise de décision"));

            kpi.setCategorie(tacticCategorie);
            tacticCategorie.getKpis().add(kpi);

            // Add the Mentality evaluation to the academie
            if (academie.getEvaluations() == null) {
                academie.setEvaluations(new ArrayList<>());
            }
            newTest.getCategories().add(mentalityCategorie);
            newTest.getCategories().add(physiqueCategorie);
            newTest.getCategories().add(techniqueCategorie);
            newTest.getCategories().add(tacticCategorie);

            testRepository.save(newTest);

            return academieRepository.save(academie);
            // return academieRepository.save(academie);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add Academie", e);
        }
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
