package com.gark.garksport.modal;

import com.gark.garksport.modal.enums.Permission;
import com.gark.garksport.modal.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String roleName; //added y manager
//    @Enumerated(EnumType.STRING)
//    private Permission permissions;
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    private String adresse;
    private String telephone;
    private String nationalite;
    private String photo;
    private boolean blocked;
    private Instant blockedTimestamp;
    private Duration blockedDuration;

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "invites")
    private Set<Evenement> evenements;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private Set<Permission> permissions = new HashSet<>();
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
//    private Set<Permission> permissions = new HashSet<>();

    @ElementCollection(targetClass = Permission.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private Set<Permission> permissions = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role != null) {
            return role.getAuthorities();
        } else {
            return Collections.emptyList(); // or return null, depending on your use case
        }
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
