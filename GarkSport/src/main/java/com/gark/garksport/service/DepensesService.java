package com.gark.garksport.service;

import com.gark.garksport.modal.Depenses;
import com.gark.garksport.modal.User;
import com.gark.garksport.modal.enums.Role;
import com.gark.garksport.repository.DepensesRepository;
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

public class DepensesService {

    @Autowired
    private DepensesRepository depensesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private StaffRepository staffRepository;



    private final UserService userService;

    public Depenses saveDepenses(Principal connectedUser,Depenses depenses) {
        Integer userId = userService.getUserId(connectedUser.getName());
        User user = userRepository.findById(userId).orElse(null);
        System.out.println("this is depenses from body"+depenses);
        assert user != null;
        if(user.getRole() == Role.MANAGER){
            depenses.setAcademie(managerRepository.findById(userId).orElse(null).getAcademie());
        } else if (user.getRole() == Role.STAFF) {
            depenses.setAcademie(staffRepository.findById(userId).orElse(null).getAcademie());
        }
        depenses.setUser(user);

        return depensesRepository.save(depenses);
    }

    public List<Depenses> getAllDepenses(Principal connectedUser) {
        Integer userId = userService.getUserId(connectedUser.getName());
        User user = userRepository.findById(userId).orElse(null);
        Integer IdAcademie = null;
        assert user != null;
        if(user.getRole() == Role.MANAGER){
            IdAcademie = managerRepository.findById(userId).orElse(null).getAcademie().getId();
        } else if (user.getRole() == Role.STAFF) {
            IdAcademie = staffRepository.findById(userId).orElse(null).getAcademie().getId();
        }

        return depensesRepository.findByAcademieIdOrderByDateDesc(IdAcademie);
    }

    public Optional<Depenses> getDepensesById(Principal connectedUser,Integer id) {
        return depensesRepository.findById(id);
    }

    public Depenses updateDepenses(Principal connectedUser,Integer id, Depenses newDepenses) {
        Integer userId = userService.getUserId(connectedUser.getName());
        User user = userRepository.findById(userId).orElse(null);
        Optional<Depenses> existingDepensesOptional = depensesRepository.findById(id);
        if (existingDepensesOptional.isPresent() && existingDepensesOptional.get().getUser() == user) {
            Depenses existingDepenses = existingDepensesOptional.get();
            // Adjust properties here according to Depenses class
            existingDepenses.setBeneficiaire(newDepenses.getBeneficiaire());
            existingDepenses.setEtat(newDepenses.getEtat());
            existingDepenses.setType(newDepenses.getType());
            existingDepenses.setQuantite(newDepenses.getQuantite());
            existingDepenses.setPrixunite(newDepenses.getPrixunite());

            // Adjust other properties as needed
            return depensesRepository.save(existingDepenses);
        }
        return null;
    }

    public void deleteDepenses(Principal connectedUser,Integer id) {
        Integer userId = userService.getUserId(connectedUser.getName());
        User user = userRepository.findById(userId).orElse(null);
        Optional<Depenses> existingDepensesOptional = depensesRepository.findById(id);
        if (existingDepensesOptional.isPresent() && existingDepensesOptional.get().getUser() == user) {
            depensesRepository.deleteById(id);
        }


    }

}
