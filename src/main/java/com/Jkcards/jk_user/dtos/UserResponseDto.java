package com.Jkcards.jk_user.dtos;

import com.Jkcards.jk_user.entities.User;

public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private String cellPhone;
    private Boolean active;
    private String roleName;

    public UserResponseDto(){

    }

    public UserResponseDto(Long id, String name, String email, String cellPhone, Boolean active, String roleName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cellPhone = cellPhone;
        this.active = active;
        this.roleName = roleName;
    }

    public UserResponseDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.cellPhone = user.getCellPhone();
        this.active = user.getActive();
        this.roleName = user.getRole().getRoleName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
