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
public class InformationMedicales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String blesseur;
    private String conditionMedicale;

    @ElementCollection
    @CollectionTable(name = "allergies", joinColumns = @JoinColumn(name = "information_medicales_id"))
    @Column(name = "allergy")
    private Set<String> allergies;

    @ElementCollection
    @CollectionTable(name = "medicaments_actuels", joinColumns = @JoinColumn(name = "information_medicales_id"))
    @Column(name = "medicament_actuel")
    private Set<String> medicamentActuel;

    @ElementCollection
    @CollectionTable(name = "medicaments_passes", joinColumns = @JoinColumn(name = "information_medicales_id"))
    @Column(name = "medicament_passe")
    private Set<String> medicamentPasses;

    @OneToOne(mappedBy = "informationMedicales")
    private Adherent adherent;

    @OneToMany(mappedBy = "informationMedicales", cascade = CascadeType.ALL)
    private Set<Blessure> blessures;

}
