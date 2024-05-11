package com.gark.garksport.service;
import com.gark.garksport.modal.*;
import com.gark.garksport.modal.enums.Etat;
import com.gark.garksport.repository.AcademieHistoryRepository;
import com.gark.garksport.repository.AcademieRepository;
import com.gark.garksport.repository.ManagerRepository;
import com.gark.garksport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

    @Override
    public Academie addAcademie(Academie academie, Integer managerId) {
        try {
            Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found"));
            academie.setManager(manager);
            return academieRepository.save(academie);
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
