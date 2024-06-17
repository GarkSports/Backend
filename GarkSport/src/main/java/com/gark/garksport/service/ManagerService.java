package com.gark.garksport.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.gark.garksport.dto.request.AddRoleNameRequest;
import com.gark.garksport.dto.request.ResetPasswordRequest;
import com.gark.garksport.modal.*;
import com.gark.garksport.modal.enums.Permission;
import com.gark.garksport.modal.enums.Role;
import com.gark.garksport.modal.enums.StatutAdherent;
import com.gark.garksport.modal.enums.TypeAbonnement;
import com.gark.garksport.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.webjars.NotFoundException;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ManagerRepository managerRepository;
    private final AcademieRepository academieRepository;
    private final RoleNameRepository roleNameRepository;
    private final StaffRepository staffRepository;
    private final EntraineurRepository entraineurRepository;
    private final AdherentRepository adherentRepository;
    private final ParentRepository parentRepository;
    private final InformationsParentRepository informationsParentRepository;
    private final PaiementRepository paiementRepository;
    private final PaiementHistoryRepository paiementHistoryRepository;
    private final EquipeRepository equipeRepository;


    @Autowired
    private JavaMailSender mailSender;
    public String generateRandomPassword(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        int length = 20;

        StringBuilder randomPassword = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());

            randomPassword.append(characters.charAt(randomIndex));
        }

        return randomPassword.toString();
    }
    public User getProfil(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<User> userOptional = repository.findById(user.getId());

        return userOptional.orElse(null);
    }

    public Manager getManagerProfil(Principal connectedUser) {
        var manager = (Manager) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<Manager> managerOptional = managerRepository.findById(manager.getId());

        return managerOptional.orElse(null);
    }

    public String getRoleNames(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Academie academie = null;
        if (user instanceof Manager) {
            Manager manager = (Manager) user;
            academie = manager.getAcademie();
        }


        return academie.getRoleNames().toString();

    }

    public String blockUser(Integer id) {
        var userOptional = repository.findById(id);
        System.out.println("id is : "+id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Set the blocked status and other relevant information
            user.setBlocked(true);
            user.setBlockedTimestamp(Instant.now());
            //user.setBlockedDuration(Duration.ofDays(7)); // Example: Block for 7 days

            repository.save(user);

            return "User blocked successfully";
        } else {
            return "User not found";
        }

    }

    public String unblockUser(Integer id){
        var userOptional = repository.findById(id);
        System.out.println("id is : "+id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Set the blocked status and other relevant information
            user.setBlocked(false);

            repository.save(user);

            return "User unblocked successfully";
        } else {
            return "User not found";
        }

    }

    public String archiveUser(Integer id) {
        var user = repository.findById(id);
        System.out.println("id is : "+id);
        if (user.isPresent()) {

            repository.delete(user.get()); //turn it to archive we don't want to delete any data !!!!
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }

    public Optional<User> getManagerById(Integer id) {
        return repository.findById(id);
    }
        public ResponseEntity<RoleName> addRoleName(RoleName request, Principal connectedUser) {
        User user = getProfil(connectedUser);
        if (user instanceof Manager) {
            Manager manager = (Manager) user;
            Academie academie = academieRepository.findByManagerId(manager.getId());
            if (academie != null) {
                RoleName roleName = new RoleName();
                roleName.setName(request.getName());
                roleName.setPermissions(request.getPermissions());
                roleName.setAcademie(academie);
                academie.getRoleNames().add(roleName);
                academieRepository.save(academie);
                return ResponseEntity.ok(roleName);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    // Method to remove a role name from the academy
    public void removeRoleName(String roleName, Principal connectedUser) {
        User user = getProfil(connectedUser);
        if (user instanceof Manager) {
            Manager manager = (Manager) user;
            Academie academie = manager.getAcademie();
            if (academie != null) {
                academie.getRoleNames().remove(roleName);
                academieRepository.save(academie);
            }
        }
    }

    // Method to assign role names to a new user added to the academy
    public void assignRoleNamesToUser(User user) {
        // Implement your logic to assign role names to the user
        // For example:
        // user.setRoleNames(this.roleNames);
    }
    String generatedPWD = generateRandomPassword();


    public Set<Adherent> getAdherentsByAcademie(Integer managerId) {

        // Get all adherents for the academie
        Set<Adherent> allAdherents = adherentRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(()
                -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        return allAdherents;
    }

    public Set<Staff> getStaffByAcademie(Integer managerId) {

        // Get all adherents for the academie
        Set<Staff> allStaff = staffRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(()
                -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        return allStaff;
    }

    public Set<Entraineur> getEntraineurByAcademie(Integer managerId) {

        // Get all adherents for the academie
        Set<Entraineur> allEntraineurs = entraineurRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(()
                -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        return allEntraineurs;
    }

    public Set<Parent> getParentByAcademie(Integer managerId) {

        // Get all adherents for the academie
        Set<Parent> allParent = parentRepository.findByAcademieId(managerRepository.findById(managerId).orElseThrow(()
                -> new IllegalArgumentException("Manager not found")).getAcademie().getId());

        return allParent;
    }

    public List<User> getUsersByAcademie(Integer managerId) {
        Academie academie = managerRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"))
                .getAcademie();

        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(staffRepository.findByAcademieId(academie.getId()));
        allUsers.addAll(adherentRepository.findByAcademieId(academie.getId()));
        allUsers.addAll(entraineurRepository.findByAcademieId(academie.getId()));
       // allUsers.addAll(parentRepository.findByAcademieId(academie.getId()));

        return allUsers;
    }


    public Staff addStaff(Staff staff, Principal connectedUser) throws MessagingException {

        staff.setFirstname(staff.getFirstname());
        staff.setLastname(staff.getLastname());
        staff.setEmail(staff.getEmail());
        staff.setRole(Role.STAFF);
        staff.setPassword(passwordEncoder.encode(generatedPWD));
        staff.setRoleName(staff.getRoleName());
        staff.setTelephone(staff.getTelephone());
        staff.setPhoto(staff.getPhoto());
        staff.setAdresse(staff.getAdresse());
        staff.setNationalite(staff.getNationalite());
        staff.setDateNaissance(staff.getDateNaissance());

        User user = getProfil(connectedUser);

        Manager manager = (Manager) user;
        Academie academie = academieRepository.findByManagerId(manager.getId());
        //Long academieId = Long.valueOf(academie.getId());

        staff.setAcademie(academie);
       // staff.setAcademieId(academieId);

        if (academie == null) {
            throw new RuntimeException("Academie not found for the current manager.");
        }

        RoleName roleNameToAdd = roleNameRepository.findByNameAndAcademie(staff.getRoleName(), academie);
        if (roleNameToAdd == null) {
            throw new RuntimeException("RoleName not found for roleName: " + staff.getRoleName());
        }

        Set<Permission> permissions = roleNameToAdd.getPermissions().stream()
                .map(permissionName -> Permission.valueOf(String.valueOf(permissionName)))
                .collect(Collectors.toSet());

        staff.setPermissions(permissions);


        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("${spring.mail.username}"));
        message.setRecipients(MimeMessage.RecipientType.TO, staff.getEmail());
        message.setSubject(staff.getRoleName() + " Login");
        message.setText("<div> Login using your email and this password: " + staff.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

        mailSender.send(message);

        return repository.save(staff);
    }
    public Entraineur addCoach(Entraineur entraineur,List<String> equipeNames, Principal connectedUser) throws MessagingException {

        entraineur.setFirstname(entraineur.getFirstname());
        entraineur.setLastname(entraineur.getLastname());
        entraineur.setEmail(entraineur.getEmail());
        entraineur.setRole(Role.ENTRAINEUR);
        entraineur.setPassword(passwordEncoder.encode(generatedPWD));
        entraineur.setRoleName(entraineur.getRoleName());
        entraineur.setTelephone(entraineur.getTelephone());
        entraineur.setPhoto(entraineur.getPhoto());
        entraineur.setAdresse(entraineur.getAdresse());
        entraineur.setNationalite(entraineur.getNationalite());
        entraineur.setDateNaissance(entraineur.getDateNaissance());

//        Set<Permission> permissions = entraineur.getPermissions();
//        entraineur.setPermissions(permissions);

        User user = getProfil(connectedUser);

        Manager manager = (Manager) user;
        Academie academie = manager.getAcademie();
        entraineur.setAcademie(academie);

        Entraineur savedEntraineur = repository.save(entraineur);

        for (String equipeName : equipeNames) {
            Equipe equipe = equipeRepository.findByNom(equipeName);
            equipe.getEntraineurs().add(savedEntraineur);
            equipeRepository.save(equipe);
        }

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("${spring.mail.username}"));
        message.setRecipients(MimeMessage.RecipientType.TO, entraineur.getEmail());
        message.setSubject(entraineur.getRoleName() + " Login");
        message.setText("<div> Login using your email and this password: " + entraineur.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

        mailSender.send(message);

        return savedEntraineur;
    }

    public Parent addParent(Parent parent, Principal connectedUser) throws MessagingException {

        parent.setEmail(parent.getEmail());
        parent.setRole(Role.PARENT);
        parent.setPassword(passwordEncoder.encode(generatedPWD));
        parent.setTelephone(parent.getTelephone());
        parent.setPhoto(parent.getPhoto());

        User user = getProfil(connectedUser);

        Manager manager = (Manager) user;
        Academie academie = manager.getAcademie();
        parent.setAcademie(academie);


        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("${spring.mail.username}"));
        message.setRecipients(MimeMessage.RecipientType.TO, parent.getEmail());
        message.setSubject(parent.getRoleName() + " Login");
        message.setText("<div> Login using your email and this password: " + parent.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

        mailSender.send(message);

        return repository.save(parent);
    }

    public Adherent addAdherent(Adherent adherent, List<String> equipeNames, Principal connectedUser) throws MessagingException {

        adherent.setFirstname(adherent.getFirstname());
        adherent.setLastname(adherent.getLastname());
        adherent.setEmail(adherent.getEmail());
        adherent.setRole(Role.ADHERENT);
        adherent.setPassword(passwordEncoder.encode(generatedPWD));
        adherent.setAdresse(adherent.getAdresse());
        adherent.setTelephone(adherent.getTelephone());
        adherent.setPhoto(adherent.getPhoto());
        adherent.setStatutAdherent(StatutAdherent.Non_Payé);
        adherent.setNiveauScolaire(adherent.getNiveauScolaire());
        adherent.setDateNaissance(adherent.getDateNaissance());
        adherent.setNationalite(adherent.getNationalite());
        if (adherent.getInformationsParent() != null) {
            InformationsParent informationsParent = adherent.getInformationsParent();
            informationsParent.setAdherent(adherent); // Set the adherent for the informationsParent

            // Set the fields of informationsParent
            informationsParent.setNomParent(adherent.getInformationsParent().getNomParent());
            informationsParent.setPrenomParent(adherent.getInformationsParent().getPrenomParent());
            informationsParent.setTelephoneParent(adherent.getInformationsParent().getTelephoneParent());
            informationsParent.setAdresseParent(adherent.getInformationsParent().getAdresseParent());
            informationsParent.setEmailParent(adherent.getInformationsParent().getEmailParent());
            informationsParent.setNationaliteParent(adherent.getInformationsParent().getNationaliteParent());

            adherent.setInformationsParent(informationsParent);
        }

        User user = getProfil(connectedUser);

        Manager manager = (Manager) user;
        Academie academie = manager.getAcademie();
        adherent.setAcademie(academie);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date dateFin = cal.getTime();
        adherent.setPaiementDate(dateFin);

        // Save the adherent first
        Adherent savedAdherent = repository.save(adherent);
        for (String equipeName : equipeNames) {
            Equipe equipe = equipeRepository.findByNom(equipeName);
            equipe.getAdherents().add(savedAdherent);
            equipeRepository.save(equipe);
        }

        Paiement paiement = new Paiement();
        paiement.setAdherent(savedAdherent);
        paiement.setTypeAbonnement(TypeAbonnement.Mensuel);
        paiement.setDateDebut(new Date());

        // Set dateFin to 30 days from today;

        paiement.setDateFin(dateFin);
        paiement.setDatePaiement(null);
        paiement.setMontant(0f);
        paiement.setReste(savedAdherent.getAcademie().getFraisAdhesion());

        // Save the paiement
        paiementRepository.save(paiement);

        PaiementHistory paiementHistory = PaiementHistory.builder()
                .dateDebut(paiement.getDateDebut())
                .dateFin(paiement.getDateFin())
                .datePaiement(null)
                .montant(paiement.getMontant())
                .reste(paiement.getReste())
                .retardPaiement(0)
                .statutAdherent(StatutAdherent.Non_Payé)
                .adherent(adherent)
                .build();

        // Save the paiement history
        paiementHistoryRepository.save(paiementHistory);

        // Send the email
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("${spring.mail.username}"));
        message.setRecipients(MimeMessage.RecipientType.TO, adherent.getEmail());
        message.setSubject(adherent.getRoleName() + " Login");
        message.setText("<div> Login using your email and this password: " + adherent.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

        mailSender.send(message);

        return savedAdherent;
    }
    public List<Equipe> findEquipesByAdherentId(Integer id) {
        return equipeRepository.findEquipesByAdherentId(id);
    }


    public List<Equipe> findEquipesByEntraineurId(Integer id) {
        return equipeRepository.findEquipesByEntraineurId(id);
    }

    public RoleName updateRoleName(Integer id, RoleName request, Manager manager) {
        Academie academie = academieRepository.findByManagerId(manager.getId());
        if (academie != null) {

            Optional<RoleName> existingRoleNameOptional = roleNameRepository.findById(id);
            if (existingRoleNameOptional.isPresent()) {
                RoleName existingRoleName = existingRoleNameOptional.get();
                String oldRoleName = existingRoleName.getName();
                existingRoleName.setName(request.getName());
                existingRoleName.setPermissions(request.getPermissions());
                existingRoleName.setAcademie(academie);
                System.out.println("this is rolename: " + existingRoleName);
                System.out.println("this is rolename: " + existingRoleName.getAcademie().getRoleNames());
                System.out.println("this is rolename: " + existingRoleNameOptional.get());

                List<User> usersToUpdate = repository.findByRoleName(oldRoleName);
                for (User user : usersToUpdate) {
                    user.setRoleName(request.getName());
                    user.setPermissions(request.getPermissions());
                }
                repository.saveAll(usersToUpdate);

// Print out the updated users
                for (User updatedUser : usersToUpdate) {
                    System.out.println("This is the new user with permissions: " + updatedUser.getPermissions());
                }

                return roleNameRepository.save(existingRoleName);
            } else {
                throw new RuntimeException("RoleName not found with ID: " + id);
            }
        } else {
            throw new RuntimeException("Academie not found for manager with ID: " + manager.getId());
        }
    }

    public Staff updateStaff(Integer id, Staff request) throws MessagingException {
        Optional<Staff> existingStaff = staffRepository.findById(id);

        if (existingStaff.isPresent()) {
            Staff staffToUpdate = existingStaff.get();

            staffToUpdate.setEmail(request.getEmail());
            staffToUpdate.setRoleName(request.getRoleName());
            staffToUpdate.setFirstname(request.getFirstname());
            staffToUpdate.setLastname(request.getLastname());
            staffToUpdate.setAdresse(request.getAdresse());
            staffToUpdate.setTelephone(request.getTelephone());
            staffToUpdate.setPhoto(request.getPhoto());
            staffToUpdate.setNationalite(request.getNationalite());
            staffToUpdate.setDateNaissance(request.getDateNaissance());

//            Set<Permission> permissions = request.getPermissions();
//            staffToUpdate.setPermissions(permissions);



            return staffRepository.save(staffToUpdate);
        }
        else {
            // Manager not found, handle the case accordingly
            throw new RuntimeException("Manager not found with ID: " + id);
        }
    }

    public Entraineur updateCoach(Integer id, Entraineur request, List<String> newEquipeNames) throws MessagingException {
        Optional<Entraineur> existingEntraineur = entraineurRepository.findById(id);

        if (existingEntraineur.isPresent()) {
            Entraineur entraineurToUpdate = existingEntraineur.get();

            entraineurToUpdate.setEmail(request.getEmail());
            entraineurToUpdate.setRoleName(request.getRoleName());
            entraineurToUpdate.setFirstname(request.getFirstname());
            entraineurToUpdate.setLastname(request.getLastname());
            entraineurToUpdate.setAdresse(request.getAdresse());
            entraineurToUpdate.setTelephone(request.getTelephone());
            entraineurToUpdate.setPhoto(request.getPhoto());
            entraineurToUpdate.setNationalite(request.getNationalite());

            Entraineur updatedEntraineur = entraineurRepository.save(entraineurToUpdate);

            // Step 1: Retrieve the existing equipes associated with the entraineur
            List<Equipe> existingEquipes = equipeRepository.findEquipesByEntraineurId(id);

            // Step 2: Remove the entraineur from the existing equipes
            for (Equipe equipe : existingEquipes) {
                equipe.getEntraineurs().remove(updatedEntraineur);
                equipeRepository.save(equipe);
            }

// Step 3: Add the entraineur to the new equipes
            List<Equipe> newEquipes = new ArrayList<>();
            for (String newEquipeName : newEquipeNames) {
                Equipe newEquipe = equipeRepository.findByNom(newEquipeName);
                if (newEquipe != null) {
                    newEquipe.getEntraineurs().add(updatedEntraineur);
                    newEquipes.add(newEquipe);
                }
            }

            for (Equipe equipe : newEquipes) {
                equipeRepository.save(equipe);
            }



            return updatedEntraineur;
        }
        else {
            // Manager not found, handle the case accordingly
            throw new RuntimeException("Manager not found with ID: " + id);
        }
    }
    public int calculateAge(Date birthDate) {
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthLocalDate, LocalDate.now()).getYears();
    }
    public Adherent updateAdherent(Integer id, Adherent request, List<String> newEquipeNames) throws MessagingException {
        Optional<Adherent> existingAdherent = adherentRepository.findById(id);

        if (existingAdherent.isPresent()) {
            Adherent adherentToUpdate = existingAdherent.get();

            adherentToUpdate.setEmail(request.getEmail());
            adherentToUpdate.setFirstname(request.getFirstname());
            adherentToUpdate.setLastname(request.getLastname());
            adherentToUpdate.setAdresse(request.getAdresse());
            adherentToUpdate.setTelephone(request.getTelephone());
            adherentToUpdate.setPhoto(request.getPhoto());
            adherentToUpdate.setDateNaissance(request.getDateNaissance());
            adherentToUpdate.setNationalite(request.getNationalite());
            adherentToUpdate.setNiveauScolaire(request.getNiveauScolaire());

            Adherent updatedAdherent = adherentRepository.save(adherentToUpdate);

            // Step 1: Retrieve the existing equipes associated with the adherent
            List<Equipe> existingEquipes = equipeRepository.findEquipesByAdherentId(id);

            // Step 2: Remove the adherent from the existing equipes
            for (Equipe equipe : existingEquipes) {
                equipe.getAdherents().remove(updatedAdherent);
                equipeRepository.save(equipe);
            }

            // Step 3: Add the adherent to the new equipes
            List<Equipe> newEquipes = new ArrayList<>();
            for (String newEquipeName : newEquipeNames) {
                Equipe newEquipe = equipeRepository.findByNom(newEquipeName);
                if (newEquipe != null) {
                    newEquipe.getAdherents().add(updatedAdherent);
                    newEquipes.add(newEquipe);
                }
            }

            if (request.getInformationsParent() != null) {
                InformationsParent parentInfoToUpdate = adherentToUpdate.getInformationsParent();
                if (parentInfoToUpdate == null) {
                    parentInfoToUpdate = new InformationsParent();
                    parentInfoToUpdate.setAdherent(adherentToUpdate);
                    adherentToUpdate.setInformationsParent(parentInfoToUpdate);
                }
                parentInfoToUpdate.setNomParent(request.getInformationsParent().getNomParent());
                parentInfoToUpdate.setPrenomParent(request.getInformationsParent().getPrenomParent());
                parentInfoToUpdate.setTelephoneParent(request.getInformationsParent().getTelephoneParent());
                parentInfoToUpdate.setAdresseParent(request.getInformationsParent().getAdresseParent());
                parentInfoToUpdate.setEmailParent(request.getInformationsParent().getEmailParent());
                parentInfoToUpdate.setNationaliteParent(request.getInformationsParent().getNationaliteParent());

                // Save the InformationsParent entity before saving the Adherent entity
                informationsParentRepository.save(parentInfoToUpdate);
            } else {
                throw new RuntimeException("Parent information is missing for the adherent.");
            }

            return updatedAdherent;
        } else {
            throw new RuntimeException("Adherent not found with ID: " + id);
        }
    }


    public Manager updateManager(Principal connectedUser, Manager request) throws MessagingException {
        User user = getProfil(connectedUser);

        // Cast the user to Manager
        if (user instanceof Manager) {
            Manager manager = (Manager) user;

            // Update the manager's details
            manager.setEmail(request.getEmail());
            manager.setFirstname(request.getFirstname());
            manager.setLastname(request.getLastname());
            manager.setAdresse(request.getAdresse());
            manager.setTelephone(request.getTelephone());
            manager.setTelephone2(request.getTelephone2());
            // manager.setPhoto(request.getPhoto());
            manager.setNationalite(request.getNationalite());
            manager.setDateNaissance(request.getDateNaissance());

            // Save the updated manager
            return managerRepository.save(manager);
        } else {
            // Manager not found, handle the case accordingly
            throw new RuntimeException("Manager not found for the connected user");
        }
    }


    public String resetPassword(Principal connectedUser, ResetPasswordRequest request) throws MessagingException {

            User user = getProfil(connectedUser);

            // Check if the current password matches
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new IllegalStateException("Current password is incorrect");
            }

            // Check if the new password matches the confirmation password
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new IllegalStateException("New password does not match the confirmation password");
            }

            // Update password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            repository.save(user);
            return "Password updated successfully";

    }

//    public Parent updateParent(Integer id, Adherent request) throws MessagingException {
//        Optional<Parent> existingParent = parentRepository.findById(id);
//
//        if (existingParent.isPresent()) {
//            Parent parentToUpdate = existingParent.get();
//
//            parentToUpdate.setEmail(request.getEmail());
//            //staffToUpdate.setRoleName(request.getRoleName());
//            parentToUpdate.setFirstname(request.getFirstname());
//            parentToUpdate.setLastname(request.getLastname());
//            parentToUpdate.setAdresse(request.getAdresse());
//
////            Set<Permission> permissions = request.getPermissions();
////            staffToUpdate.setPermissions(permissions);
//
//
//
//            return parentRepository.save(parentToUpdate);
//        }
//        else {
//            // Manager not found, handle the case accordingly
//            throw new RuntimeException("Manager not found with ID: " + id);
//        }
//    }
public void deleteRoleName(Integer id, Manager manager) {
    Academie academie = academieRepository.findByManagerId(manager.getId());

    if (academie == null) {
        throw new RuntimeException("Academie not found for the current manager.");
    }

    Optional<RoleName> existingRoleNameOptional = roleNameRepository.findById(id);
    if (existingRoleNameOptional.isPresent()) {
        RoleName existingRoleName = existingRoleNameOptional.get();
        if (existingRoleName.getAcademie().equals(academie)) {
            academie.getRoleNames().remove(existingRoleName);
            roleNameRepository.delete(existingRoleName);
            academieRepository.save(academie);
        } else {
            throw new RuntimeException("Role name does not belong to the current manager's academie");
        }
    } else {
        throw new NotFoundException("Role name not found");
    }
}

    public String deleteUser(Integer id) {
        var user = repository.findById(id);
        System.out.println("id is : "+id);
        if (user.isPresent()) {

            repository.delete(user.get()); //turn it to archive we don't want to delete any data !!!!
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }


//    public Staff addStaff(Staff staff, Set<Permission> inputPermissions) throws MessagingException {
//
//        String generatedPWD = generateRandomPassword();
//
//        staff.setEmail(staff.getEmail());
//        staff.setRole(Role.STAFF);
//        //staff.setPassword(passwordEncoder.encode(generatedPWD));
//        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
//        staff.setRoleName(staff.getRoleName());
//        staff.setPermissions(inputPermissions);
//
//        Staff newStaff = new Staff();
//// Set other properties...
//        Set<Permission> inputPermissions = new HashSet<>(Arrays.asList(Permission.STAFF_READ, Permission.STAFF_UPDATE));
//        newStaff.setPermissions(inputPermissions);
//        List<SimpleGrantedAuthority> authorities = Role.STAFF.getAuthorities(inputPermissions);
//// Assign the authorities to the staff instance
//
//        //Set<Permission> newPermissions = staff.getPermissions();
//
//
//
//        //staff.setPermissions(staff.getPermissions());
//
//        MimeMessage message = mailSender.createMimeMessage();
//            message.setFrom(new InternetAddress("${spring.mail.username}"));
//            message.setRecipients(MimeMessage.RecipientType.TO, staff.getEmail());
//            message.setSubject(staff.getRoleName() + " Login");
//            message.setText("<div> Login using your email and this password: " + staff.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");
//
//            mailSender.send(message);
//
//        return repository.save(staff);
//    }

}