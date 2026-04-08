package com.Jkcards.jk_user.dtos;

public class UserRequestDto {

    private String name;
    private String email;
    private String cellPhone;
    private String password;
    private String roleName;

    public UserRequestDto(){

    }

    public UserRequestDto(String name, String email, String cellPhone, String password, String roleName) {
        this.name = name;
        this.email = email;
        this.cellPhone = cellPhone;
        this.password = password;
        this.roleName = roleName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
