package com.tekser.web.dto;

import com.tekser.customAnnotations.ValidEmail;
import com.tekser.domain.Role;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class UserUpdateDto {

    private Long id;

    @NotBlank (message = "İsim gerekli")
    private String name;

    @NotBlank (message = "Soyadı gerekli")
    private String surname;

    @NotBlank (message = "Kullanıcı adı gerekli")
    private String username;

    @ValidEmail
    @NotBlank (message = "Email gerekli")
    private String email;

    private List<Role> roles = new ArrayList<>();

    private boolean enabled;

    public UserUpdateDto(Long id, String name, String surname, String username, String email, boolean enabled) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
    }

    public UserUpdateDto(Long id, String name, String surname, String username, String email, boolean enabled,
                         List<Role> roles) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles;
    }

    public UserUpdateDto() {

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}