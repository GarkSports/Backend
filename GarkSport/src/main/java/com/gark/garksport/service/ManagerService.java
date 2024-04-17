package com.gark.garksport.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.gark.garksport.dto.request.AddRoleNameRequest;
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
    private final StaffRepository staffRepository;
    private final RoleNameRepository roleNameRepository;
    private final UserService userService;

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


    public void addRoleName(AddRoleNameRequest request, Principal connectedUser) {
        User user = getProfil(connectedUser);
        if (user instanceof Manager) {
            Manager manager = (Manager) user;
            Academie academie = manager.getAcademie(); // Assuming a relationship between Manager and Academie

            if (academie != null) {
                RoleName roleName = new RoleName();
                roleName.setRoleName(request.getRoleName());
                roleName.setPermissions(request.getPermissions().stream()
                        .map(Permission::valueOf)
                        .collect(Collectors.toSet()));
                roleName.setAcademie(academie);
                academie.getRoleNames().add(roleName);
                academieRepository.save(academie);
            }
        }
    }

    public RoleName updateRoleName(Integer id, RoleName request, Manager manager) {
        Academie academie = academieRepository.findByManagerId(manager.getId());
        if (academie != null) {
            Optional<RoleName> existingRoleName = roleNameRepository.findById(id);
            if (existingRoleName.isPresent()) {
                RoleName roleNameToUpdate = existingRoleName.get();
                roleNameToUpdate.setRoleName(request.getRoleName());
                roleNameToUpdate.setPermissions(request.getPermissions());
                roleNameToUpdate.setAcademie(academie);

                //List<User> usersToUpdate = repository.findByRoleName(existingRoleName);
//                for (User user : usersToUpdate) {
//                    user.setPermissions(request.getPermissions());
//                    repository.save(user);
//                }
               // System.out.println("this is rolename: " + usersToUpdate);


                return roleNameRepository.save(roleNameToUpdate);
            } else {
                throw new RuntimeException("RoleName not found with ID: " + id);
            }
        } else {
            throw new RuntimeException("Academie not found for manager with ID: " + manager.getId());
        }
    }


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


    public Staff addStaff(Staff request, Principal connectedUser) throws MessagingException {
        String generatedPWD = generateRandomPassword();

        Staff staff = new Staff();
        staff.setEmail(request.getEmail());
        staff.setRole(Role.STAFF);
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setRoleName(request.getRoleName());
        var rolename = request.getRoleName();
        User user = getProfil(connectedUser);
        if (!(user instanceof Manager)) {
            throw new RuntimeException("Only managers can add staff.");
        }

        Academie academie = academieRepository.findByManagerId(user.getId());
        if (academie == null) {
            throw new RuntimeException("Academie not found for the current manager.");
        }

        RoleName roleNameEntity = academie.getRoleNames().stream()
                .filter(roleName -> roleName.getRoleName().equals(request.getRoleName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("RoleName not found for roleName: " + request.getRoleName()));

        Set<Permission> permissions = roleNameEntity.getPermissions().stream()
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

    public Staff updateStaff(Integer id, Staff request) throws MessagingException {
        Optional<Staff> existingStaff = staffRepository.findById(id);

        if (existingStaff.isPresent()) {
            Staff staffToUpdate = existingStaff.get();

            staffToUpdate.setEmail(request.getEmail());
            staffToUpdate.setRoleName(request.getRoleName());

            Set<Permission> permissions = request.getPermissions();
            staffToUpdate.setPermissions(permissions);



            return staffRepository.save(staffToUpdate);
        }
     else {
        // Manager not found, handle the case accordingly
        throw new RuntimeException("Manager not found with ID: " + id);
    }
    }

    public Entraineur addCoach(Entraineur entraineur) throws MessagingException {
        String generatedPWD = generateRandomPassword();

        entraineur.setEmail(entraineur.getEmail());
        entraineur.setRole(Role.ENTRAINEUR);
        entraineur.setPassword(passwordEncoder.encode(entraineur.getPassword()));
        entraineur.setRoleName(entraineur.getRoleName());// Set the permissions for the staff


        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("${spring.mail.username}"));
        message.setRecipients(MimeMessage.RecipientType.TO, entraineur.getEmail());
        message.setSubject(entraineur.getRoleName() + " Login");
        message.setText("<div> Login using your email and this password: " + entraineur.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

        mailSender.send(message);

        return repository.save(entraineur);
    }

    public Parent addParent(Parent parent) throws MessagingException {
        String generatedPWD = generateRandomPassword();

        parent.setEmail(parent.getEmail());
        parent.setRole(Role.PARENT);
        parent.setPassword(passwordEncoder.encode(parent.getPassword()));


        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("${spring.mail.username}"));
        message.setRecipients(MimeMessage.RecipientType.TO, parent.getEmail());
        message.setSubject(parent.getRoleName() + " Login");
        message.setText("<div> Login using your email and this password: " + parent.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

        mailSender.send(message);

        return repository.save(parent);
    }

    public Adherent addAdherent(Adherent adherent) throws MessagingException {
        String generatedPWD = generateRandomPassword();

        adherent.setEmail(adherent.getEmail());
        adherent.setRole(Role.ADEHERANT);
        adherent.setPassword(passwordEncoder.encode(adherent.getPassword()));


        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("${spring.mail.username}"));
        message.setRecipients(MimeMessage.RecipientType.TO, adherent.getEmail());
        message.setSubject(adherent.getRoleName() + " Login");
        message.setText("<div> Login using your email and this password: " + adherent.getEmail() + generatedPWD + "<a href=\"http://localhost:8080/login" + "\">Login</a></div>");

        mailSender.send(message);

        return repository.save(adherent);
    }

    public ResponseEntity<Academie> getAcademie(Integer id) {
        Academie academie = academieRepository.findByManagerId(id);
        if (academie != null) {
            return ResponseEntity.ok(academie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public User getProfilById(Integer id) {
        Optional<User> userOptional = repository.findById(id);

        return userOptional.orElse(null);
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
