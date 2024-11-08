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
    private Integer userId;
    private String token;
    private Integer EquipeId;
    private Integer academieId;

    // Getters and setters
}
