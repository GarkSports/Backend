package com.gark.garksport.service;

import com.gark.garksport.modal.Benefices;
import com.gark.garksport.modal.User;
import com.gark.garksport.modal.enums.Role;
import com.gark.garksport.repository.BeneficesRepository;
import com.gark.garksport.repository.ManagerRepository;
import com.gark.garksport.repository.StaffRepository;
import com.gark.garksport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BeneficesService {

    @Autowired
    private BeneficesRepository beneficesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private StaffRepository staffRepository;



    private final UserService userService;

    // Méthode pour enregistrer un bénéfice
    public Benefices saveBenefices(Principal connectedUser,Benefices benefices) {
        Integer userId = userService.getUserId(connectedUser.getName());
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        if(user.getRole() == Role.MANAGER){
            benefices.setAcademie(managerRepository.findById(userId).orElse(null).getAcademie());
        } else if (user.getRole() == Role.STAFF) {
            benefices.setAcademie(staffRepository.findById(userId).orElse(null).getAcademie());
        }
        benefices.setUser(user);

        return beneficesRepository.save(benefices);
    }

    // Méthode pour récupérer tous les bénéfices
    public List<Benefices> getAllBenefices(Principal connectedUser) {
        Integer userId = userService.getUserId(connectedUser.getName());
        User user = userRepository.findById(userId).orElse(null);
        Integer IdAcademie = null;
        assert user != null;
        if(user.getRole() == Role.MANAGER){
            IdAcademie = managerRepository.findById(userId).orElse(null).getAcademie().getId();
        } else if (user.getRole() == Role.STAFF) {
            IdAcademie = staffRepository.findById(userId).orElse(null).getAcademie().getId();
        }
        return beneficesRepository.findByAcademieId(IdAcademie);
    }

    // Méthode pour récupérer un bénéfice par son ID
    public Optional<Benefices> getBeneficesById(Principal connectedUser,Integer id) {
        return beneficesRepository.findById(id);
    }

    // Méthode pour mettre à jour un bénéfice
    public Benefices updateBenefices(Principal connectedUser,Integer id, Benefices newBenefices) {
        Integer userId = userService.getUserId(connectedUser.getName());
        User user = userRepository.findById(userId).orElse(null);

        Optional<Benefices> existingBeneficesOptional = beneficesRepository.findById(id);
        if (existingBeneficesOptional.isPresent() && existingBeneficesOptional.get().getUser() == user) {
            Benefices existingBenefices = existingBeneficesOptional.get();
            existingBenefices.setType(newBenefices.getType());
            existingBenefices.setEtat(newBenefices.getEtat());
            existingBenefices.setQuantite(newBenefices.getQuantite());
            existingBenefices.setPrixUnite(newBenefices.getPrixUnite());
            existingBenefices.setTotal(newBenefices.getTotal());
            existingBenefices.setDate(newBenefices.getDate());
            return beneficesRepository.save(existingBenefices);
        }
        return null;
    }

    // Méthode pour supprimer un bénéfice par son ID
    public void deleteBenefices(Principal connectedUser,Integer id) {
        Integer userId = userService.getUserId(connectedUser.getName());
        User user = userRepository.findById(userId).orElse(null);
        Optional<Benefices> existingBeneficesOptional = beneficesRepository.findById(id);
        if (existingBeneficesOptional.isPresent() && existingBeneficesOptional.get().getUser() == user) {
            beneficesRepository.deleteById(id);
        }


    }
}
