package com.gark.garksport.modal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.gark.garksport.modal.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    ADHERENT,

    ADMIN,

    ENTRAINEUR,
    MANAGER,
    PARENT,
    STAFF;




}