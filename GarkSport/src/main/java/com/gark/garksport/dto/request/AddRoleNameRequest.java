package com.gark.garksport.dto.request;

import java.util.List;

public class AddRoleNameRequest {
    private String roleName;
    private List<String> permissions;

    public String getRoleName() {
        return roleName;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}