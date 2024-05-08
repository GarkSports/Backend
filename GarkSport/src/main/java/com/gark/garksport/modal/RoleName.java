package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gark.garksport.modal.enums.Permission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String roleName;

    @ElementCollection(targetClass = Permission.class)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions;

    @ManyToOne
    @JsonIgnoreProperties("roleNames")
    private Academie academie;

}