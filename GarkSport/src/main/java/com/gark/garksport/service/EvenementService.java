package com.gark.garksport.service;

import com.gark.garksport.dto.request.EquipeHoraireDTO;
import com.gark.garksport.modal.Academie;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.modal.Evenement;
import com.gark.garksport.modal.enums.EvenementType;
import com.gark.garksport.repository.AcademieRepository;
import com.gark.garksport.repository.EquipeRepository;
import com.gark.garksport.repository.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EvenementService implements IEvenementService {
    @Autowired
    private EvenementRepository evenementRepository;
    @Autowired
    private EquipeRepository equipeRepository;


    @Override
    public Evenement addMatchAmical(Evenement evenement, List<EquipeHoraireDTO> equipeHoraires) {
        evenement.setType(EvenementType.MATCH_AMICAL);

        Set<Equipe> equipes = new HashSet<>();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");

        for (EquipeHoraireDTO equipeHoraire : equipeHoraires) {
            Optional<Equipe> equipeOptional = equipeRepository.findById(equipeHoraire.getEquipeId());
            equipeOptional.ifPresent(equipe -> {
                // Parse the time string and set it to a Date object
                try {
                    Date time = timeFormat.parse(equipeHoraire.getHoraire());
                    equipe.setDateMatchAmical(time);
                    equipeRepository.save(equipe); // Save the equipe after setting dateMatchAmical
                    equipes.add(equipe);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Handle parsing error
                }
            });
        }
        evenement.setConvocationEquipes(equipes);
        // Save the evenement and associated equipes
        evenement = evenementRepository.save(evenement);
        return evenement;
    }
}
