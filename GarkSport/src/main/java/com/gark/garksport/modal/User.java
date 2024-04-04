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

        @ElementCollection(targetClass = Permission.class)
        @Enumerated(EnumType.STRING)
        @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
        @Column(name = "permission")
        private Set<Permission> permissions = new HashSet<>();

    //    @ElementCollection(targetClass = Permission.class)
    //    @Enumerated(EnumType.STRING)
    //    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    //    @Column(name = "permission")
    @ElementCollection
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Set<Permission> authorities = new HashSet<>();


        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities.stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                    .collect(Collectors.toSet());
        }
//        @Override
//        public Collection<? extends GrantedAuthority> getAuthorities() {
//            return authorities.stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toSet());
//        }


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
