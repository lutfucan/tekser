package com.tekser.web.dto;

import com.tekser.customAnnotations.PasswordMatches;
import com.tekser.customAnnotations.ValidEmail;
import com.tekser.domain.Role;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@PasswordMatches
public class UserDto {

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

    @NotBlank (message = "Şifre gerekli")
    private String password;

    @NotBlank (message = "Şifreyi tekrar girmelisiniz")
    private String matchingPassword;

    private boolean enabled;

    public UserDto(Long id, String name, String surname, String username, String email, String password, String
            matchingPassword, Set<Role> roles, boolean enabled) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.enabled = enabled;
    }

    public UserDto() {

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
