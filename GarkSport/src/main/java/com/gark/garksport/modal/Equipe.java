package com.gark.garksport.modal;

import jakarta.persistence.*;
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
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL)
    private Set<Adherent> adherents;

    @ManyToOne
    private Entraineur entraineur;

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL)
    private Set<Evenement> evenements;



}
