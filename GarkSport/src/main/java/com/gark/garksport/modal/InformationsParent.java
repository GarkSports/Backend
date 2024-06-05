package com.gark.garksport.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    @OneToOne
    private Adherent adherent;
}
