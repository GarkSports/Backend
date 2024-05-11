package com.gark.garksport.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationToken {
    @Id
    private String userId;
    private String token;
    private String codeEquipe;
    private Integer academieId;

    // Getters and setters
}
