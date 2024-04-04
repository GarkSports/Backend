package com.gark.garksport.modal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.gark.garksport.modal.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    ADEHERANT,
    ADMIN,
    MANAGER,
    COACH,


    PARENT,
    STAFF;

    public List<SimpleGrantedAuthority> getAuthorities(Set<Permission> permissions) {
        var authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
