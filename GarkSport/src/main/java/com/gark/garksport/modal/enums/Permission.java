package com.gark.garksport.modal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    Ajouter_utilisateurs("addUsers"),
    Voir_utilisateurs("getUsers"),
    Modifier_utilisateurs("updateUsers"),
    Bloquer_utilisateur("blocUsers"),
    Supprimer_utilisateurs("deleteUsers"),
    Ajouter_tests("addTests"),
    Modifier_tests("updateTests"),
    Supprimer_tests("deleteTests"),
    Ajouter_blog("addBlog"),
    Modifier_blog("updateBlog"),
    Supprimer_blog("deleteBlog");

//    READ("read"),
//    UPDATE("update"),
//    UPDATE_ADHERENT("update_adherent"),
//    CREATE("create"),
//    DELETE("delete"),
//
//    READ_USERS("read_users"),
//    UPDATE_USERS("update_users"),
//    CREATE_USERS("create_users"),
//    DELETE_USERS("delete_users");

    @Getter
    private final String permission;
}
