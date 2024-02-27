package com.gark.garksport.modal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MatchEntreNous extends Evenement {
    private String nom;
    private String description;
    private String colorEquipe1;
    private String colorEquipe2;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Adherent> equipe1;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Adherent> equipe2;



}
