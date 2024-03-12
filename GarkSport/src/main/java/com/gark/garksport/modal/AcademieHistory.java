package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.Etat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AcademieHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnoreProperties("academieHistory")
    @ManyToOne
    private Academie academie;

    @Enumerated(EnumType.STRING)
    private Etat previousEtat;

    @Enumerated(EnumType.STRING)
    private Etat newEtat;

    private String changeReason;

    private LocalDateTime changeDate;
}
