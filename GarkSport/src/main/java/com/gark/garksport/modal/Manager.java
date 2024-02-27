package com.gark.garksport.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Manager extends User {
    private String telephone2;

    @OneToOne(mappedBy = "manager")
    private Academie academie;

}
