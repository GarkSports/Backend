package com.gark.garksport.dto.authentication;

import com.gark.garksport.modal.enums.Role;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;

    @Column(unique=true)
    private String email;
    private String password;
    private Role role;
}
