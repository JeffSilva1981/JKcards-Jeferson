package com.Jkcards.jk_user.dtos;

import com.Jkcards.jk_user.entities.Role;

public class RoleDto {

    private String roleName;

    public RoleDto(){

    }

    public RoleDto(String roleName) {
        this.roleName = roleName;
    }

    public RoleDto(Role role){
        this.roleName = role.getRoleName();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
