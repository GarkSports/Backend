package com.gark.garksport.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.gark.garksport.dto.request.AddRoleNameRequest;
import com.gark.garksport.dto.request.ResetPasswordRequest;
import com.gark.garksport.modal.*;
import com.gark.garksport.modal.enums.Permission;
import com.gark.garksport.modal.enums.Role;
import com.gark.garksport.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
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

import java.security.Principal;
import java.security.SecureRandom;
import java.time.Instant;
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

        staff.setEmail(staff.getEmail());
        staff.setRole(Role.STAFF);
        staff.setPassword(passwordEncoder.encode(generatedPWD));
        staff.setRoleName(staff.getRoleName());
        staff.setTelephone(staff.getTelephone());
        staff.setPhoto(staff.getPhoto());

        User user = getProfil(connectedUser);

        Manager manager = (Manager) user;
        Academie academie = academieRepository.findByManagerId(manager.getId());
        //Long academieId = Long.valueOf(academie.getId());

        staff.setAcademie(academie);
       // staff.setAcademieId(academieId);


        if (academie == null) {
            throw new RuntimeException("Academie not found for the current manager.");
        }



//        RoleName roleNameToAdd = roleNameRepository.findByNameAndAcademie(staff.getRoleName(), academie);
//        if (roleNameToAdd == null) {
//            throw new RuntimeException("RoleName not found for roleName: " + staff.getRoleName());
//        }
//
//        Set<Permission> permissions = roleNameToAdd.getPermissions().stream()
//                .map(permissionName -> Permission.valueOf(String.valueOf(permissionName)))
//                .collect(Collectors.toSet());
//
//        staff.setPermissions(permissions);


        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("${spring.mail.username}"));
        message.setRecipients(MimeMessage.RecipientType.TO, staff.getEmail());
        message.setSubject(staff.getRoleName() + " Login");
        message.setText("<div> Login using your email and this password: " + staff.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

        mailSender.send(message);

        return repository.save(staff);
    }
    public Entraineur addCoach(Entraineur entraineur, Principal connectedUser) throws MessagingException {

        entraineur.setEmail(entraineur.getEmail());
        entraineur.setRole(Role.ENTRAINEUR);
        entraineur.setPassword(passwordEncoder.encode(generatedPWD));
        entraineur.setRoleName(entraineur.getRoleName());// Set the permissions for the staff
        entraineur.setTelephone(entraineur.getTelephone());
        entraineur.setPhoto(entraineur.getPhoto());

//        Set<Permission> permissions = entraineur.getPermissions();
//        entraineur.setPermissions(permissions);

        User user = getProfil(connectedUser);

        Manager manager = (Manager) user;
        Academie academie = manager.getAcademie();
        entraineur.setAcademie(academie);

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("${spring.mail.username}"));
        message.setRecipients(MimeMessage.RecipientType.TO, entraineur.getEmail());
        message.setSubject(entraineur.getRoleName() + " Login");
        message.setText("<div> Login using your email and this password: " + entraineur.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

        mailSender.send(message);

        return repository.save(entraineur);
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

    public Adherent addAdherent(Adherent adherent, Principal connectedUser) throws MessagingException {

        adherent.setEmail(adherent.getEmail());
        adherent.setRole(Role.ADHERENT);
        adherent.setPassword(passwordEncoder.encode(generatedPWD));
        adherent.setTelephone(adherent.getTelephone());
        adherent.setPhoto(adherent.getPhoto());

        User user = getProfil(connectedUser);

            Manager manager = (Manager) user;
            Academie academie = manager.getAcademie();
            adherent.setAcademie(academie);





        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("${spring.mail.username}"));
        message.setRecipients(MimeMessage.RecipientType.TO, adherent.getEmail());
        message.setSubject(adherent.getRoleName() + " Login");
        message.setText("<div> Login using your email and this password: " + adherent.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

        mailSender.send(message);

        return repository.save(adherent);
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
                    repository.saveAll(usersToUpdate);
                }
                System.out.println("this is rolename: " + usersToUpdate);


                //updateUserRoleNames(oldRoleName, request.getRoleName());

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
            //staffToUpdate.setRoleName(request.getRoleName());
            staffToUpdate.setFirstname(request.getFirstname());
            staffToUpdate.setLastname(request.getLastname());
            staffToUpdate.setAdresse(request.getAdresse());
            staffToUpdate.setTelephone(request.getTelephone());
            staffToUpdate.setPhoto(request.getPhoto());

//            Set<Permission> permissions = request.getPermissions();
//            staffToUpdate.setPermissions(permissions);



            return staffRepository.save(staffToUpdate);
        }
        else {
            // Manager not found, handle the case accordingly
            throw new RuntimeException("Manager not found with ID: " + id);
        }
    }

    public Entraineur updateCoach(Integer id, Entraineur request) throws MessagingException {
        Optional<Entraineur> existingEntraineur = entraineurRepository.findById(id);

        if (existingEntraineur.isPresent()) {
            Entraineur entraineurToUpdate = existingEntraineur.get();

            entraineurToUpdate.setEmail(request.getEmail());
            //staffToUpdate.setRoleName(request.getRoleName());
            entraineurToUpdate.setFirstname(request.getFirstname());
            entraineurToUpdate.setLastname(request.getLastname());
            entraineurToUpdate.setAdresse(request.getAdresse());
            entraineurToUpdate.setTelephone(request.getTelephone());
            entraineurToUpdate.setPhoto(request.getPhoto());

//            Set<Permission> permissions = request.getPermissions();
//            staffToUpdate.setPermissions(permissions);



            return entraineurRepository.save(entraineurToUpdate);
        }
        else {
            // Manager not found, handle the case accordingly
            throw new RuntimeException("Manager not found with ID: " + id);
        }
    }

    public Adherent updateAdherent(Integer id, Adherent request) throws MessagingException {
        Optional<Adherent> existingAdherent = adherentRepository.findById(id);

        if (existingAdherent.isPresent()) {
            Adherent adherentToUpdate = existingAdherent.get();

            adherentToUpdate.setEmail(request.getEmail());
            //staffToUpdate.setRoleName(request.getRoleName());
            adherentToUpdate.setFirstname(request.getFirstname());
            adherentToUpdate.setLastname(request.getLastname());
            adherentToUpdate.setAdresse(request.getAdresse());
            adherentToUpdate.setTelephone(request.getTelephone());
            adherentToUpdate.setPhoto(request.getPhoto());

//            Set<Permission> permissions = request.getPermissions();
//            staffToUpdate.setPermissions(permissions);



            return adherentRepository.save(adherentToUpdate);
        }
        else {
            // Manager not found, handle the case accordingly
            throw new RuntimeException("Manager not found with ID: " + id);
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
        if (academie != null) {
            Optional<RoleName> existingRoleNameOptional = roleNameRepository.findById(id);
            if (existingRoleNameOptional.isPresent()) {
                RoleName existingRoleName = existingRoleNameOptional.get();
                String oldRoleName = existingRoleName.getName();
                // Remove the RoleName from the Academie
                academie.getRoleNames().remove(existingRoleName);

            }
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