package com.gark.garksport.modal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    READ("read"),
    UPDATE("update"),
    CREATE("create"),
    DELETE("delete"),


    ;
    @Getter
    private final String permission;
}
