package com.gark.garksport.modal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    READ("read"),
    UPDATE("update"),
    CREATE("create"),
    DELETE("delete"),

    Voir_utilisateurs("read_users"),

    MiseAJour_utilisateurs("update_users"),

    Creer_utilisateurs("creer_users"),

    Supprimer_utilisateurs("supprimer_users")


    ;
    @Getter
    private final String permission;
}
