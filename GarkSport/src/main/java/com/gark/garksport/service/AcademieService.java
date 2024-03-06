package com.gark.garksport.service;
import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.Discipline;
import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.enums.Etat;
import com.gark.garksport.repository.AcademieRepository;
import com.gark.garksport.repository.DisciplineRepository;
import com.gark.garksport.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AcademieService implements IAcademieService {
    @Autowired
    private AcademieRepository academieRepository;
    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public Academie addAcademie(Academie academie, Set<Integer> disciplinesIds, Integer managerId) {
        try {
            Set<Discipline> disciplines = new HashSet<>(disciplineRepository.findAllById(disciplinesIds));
            Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found"));
            academie.setDisciplines(disciplines);
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
    public Academie updateAcademie(Academie academie, Integer academieId) {
        try {
            Academie academieNew = academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
            academieNew.setNom(academie.getNom());
            academieNew.setType(academie.getType());
            academieNew.setFraisAdhesion(academie.getFraisAdhesion());
            academieNew.setLogo(academie.getLogo());
            academieNew.setAffiliation(academie.getAffiliation());
            academieNew.setDescription(academie.getDescription());
            academieNew.setAdresse(academie.getAdresse());
            return academieRepository.save(academieNew);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update Academie", e);
        }
    }

    @Override
    public Academie chanegeEtatAcademie(Integer academieId, Etat etat) {
        try {
            Academie academie = academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
            academie.setEtat(etat);
            return academieRepository.save(academie);
        } catch (Exception e) {
            throw new RuntimeException("Failed to change Academie state", e);
        }
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
    public Set<String> getDisciplinesByAcademie(Integer academieId) {
        try {
            Academie academie = academieRepository.findById(academieId).orElseThrow(() -> new IllegalArgumentException("Academie not found"));
            Set<String> disciplines = new HashSet<>();
            for (Discipline discipline : academie.getDisciplines()) {
                disciplines.add(discipline.getNom());
            }
            return disciplines;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Disciplines for Academie", e);
        }
    }
}
