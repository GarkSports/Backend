    package com.gark.garksport.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gark.garksport.modal.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
    import com.gark.garksport.modal.enums.Permission;
    import com.gark.garksport.modal.enums.Role;
    import jakarta.persistence.*;
    import lombok.*;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.security.Permissions;
    import java.time.Duration;
    import java.time.Instant;
    import java.util.*;
    import java.util.stream.Collectors;

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
        @Temporal(TemporalType.DATE)
        private Date dateNaissance;
        private String adresse;
        private String telephone;
        private String nationalite;
        private String photo;
        private boolean blocked;
        private Instant blockedTimestamp;
        private Duration blockedDuration;

        @Override
        public int hashCode() {
            return Objects.hash(id);  // Use a unique field to calculate hashCode
        }


        @ManyToMany(cascade = CascadeType.ALL,mappedBy = "invites")
        private Set<Evenement> evenements;

        @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
        @Enumerated(EnumType.STRING)
        @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
        @Column(name = "permission")
        private Set<Permission> permissions;


        @Override
        @JsonIgnore
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<SimpleGrantedAuthority> auths = new ArrayList<>();
            auths.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
            for (Permission permission : permissions) {
                auths.add(new SimpleGrantedAuthority(permission.name()));
            }
            return auths;
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
