package com.gark.garksport.service;

import com.gark.garksport.modal.Benefices;
import com.gark.garksport.modal.User;
import com.gark.garksport.modal.enums.Role;
import com.gark.garksport.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BeneficesService {

    @Autowired
    private BeneficesRepository beneficesRepository;

    @Autowired
    private DepensesRepository depensesRepository;

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
        return beneficesRepository.findByAcademieIdOrderByDateDesc(IdAcademie);
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
            existingBenefices.setPrixunite(newBenefices.getPrixunite());
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



    public Map<String, BigDecimal> getMonthlySumsForAcademie(Principal connectedUser) {
        Integer userId = userService.getUserId(connectedUser.getName());
        User user = userRepository.findById(userId).orElse(null);
        Integer academieId = null;
        assert user != null;
        if (user.getRole() == Role.MANAGER) {
            academieId = managerRepository.findById(userId).orElse(null).getAcademie().getId();
        } else if (user.getRole() == Role.STAFF) {
            academieId = staffRepository.findById(userId).orElse(null).getAcademie().getId();
        }

        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(now);
        YearMonth lastMonth = currentMonth.minusMonths(1);

        BigDecimal currentMonthBenefices = beneficesRepository.sumByMonthAndAcademie(currentMonth.atDay(1), currentMonth.atEndOfMonth(), academieId);
        BigDecimal currentMonthDepenses = depensesRepository.sumByMonthAndAcademie(currentMonth.atDay(1), currentMonth.atEndOfMonth(), academieId);
        BigDecimal lastMonthBenefices = beneficesRepository.sumByMonthAndAcademie(lastMonth.atDay(1), lastMonth.atEndOfMonth(), academieId);
        BigDecimal lastMonthDepenses = depensesRepository.sumByMonthAndAcademie(lastMonth.atDay(1), lastMonth.atEndOfMonth(), academieId);

        BigDecimal currentMonthNet = currentMonthBenefices.subtract(currentMonthDepenses);
        BigDecimal lastMonthNet = lastMonthBenefices.subtract(lastMonthDepenses);

        Map<String, BigDecimal> sums = new HashMap<>();
        sums.put("currentMonthBenefices", currentMonthBenefices);
        sums.put("currentMonthDepenses", currentMonthDepenses);
        sums.put("lastMonthBenefices", lastMonthBenefices);
        sums.put("lastMonthDepenses", lastMonthDepenses);
        sums.put("currentMonthNet", currentMonthNet);
        sums.put("lastMonthNet", lastMonthNet);

        return sums;
    }

    public Map<String, BigDecimal> getMonthlyComparisonsForAcademie(Principal connectedUser) {
        Map<String, BigDecimal> sums = getMonthlySumsForAcademie(connectedUser);

        BigDecimal currentMonthBenefices = sums.get("currentMonthBenefices");
        BigDecimal currentMonthDepenses = sums.get("currentMonthDepenses");
        BigDecimal lastMonthBenefices = sums.get("lastMonthBenefices");
        BigDecimal lastMonthDepenses = sums.get("lastMonthDepenses");
        BigDecimal currentMonthNet = sums.get("currentMonthNet");
        BigDecimal lastMonthNet = sums.get("lastMonthNet");

        BigDecimal beneficesComparison = lastMonthBenefices.compareTo(BigDecimal.ZERO) == 0 ?
                BigDecimal.ZERO : currentMonthBenefices.subtract(lastMonthBenefices).divide(lastMonthBenefices,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal depensesComparison = lastMonthDepenses.compareTo(BigDecimal.ZERO) == 0 ?
                BigDecimal.ZERO : currentMonthDepenses.subtract(lastMonthDepenses).divide(lastMonthDepenses,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal netComparison = lastMonthNet.compareTo(BigDecimal.ZERO) == 0 ?
                BigDecimal.ZERO : currentMonthNet.subtract(lastMonthNet).divide(lastMonthNet,BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100));

        Map<String, BigDecimal> comparisons = new HashMap<>();
        comparisons.put("beneficesComparison", beneficesComparison);
        comparisons.put("depensesComparison", depensesComparison);
        comparisons.put("netComparison", netComparison);

        return comparisons;
    }
}