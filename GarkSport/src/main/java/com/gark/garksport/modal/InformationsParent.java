package com.gark.garksport.modal;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "adherent")
@ToString(exclude = "adherent")
public class InformationsParent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomParent;
    private String prenomParent;
    private String telephoneParent;
    private String adresseParent;
    private String emailParent;
    private String nationaliteParent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adherent_id")
    private Adherent adherent;
}
