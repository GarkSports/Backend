package com.gark.garksport.modal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    READ("read"),
    UPDATE("update"),
    CREATE("create"),
    DELETE("delete"),

    READ_USERS("read_users"),
    UPDATE_USERS("update_users"),
    CREATE_USERS("create_users"),
    DELETE_USERS("delete_users");


    @Getter
    private final String permission;
}
