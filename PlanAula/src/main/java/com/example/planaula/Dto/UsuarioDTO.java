package com.example.planaula.Dto;

public class UsuarioDTO {

    private String username;
    private String password;
    private String repetirPassword;
    private String rol;
    private Boolean enabled;

    public UsuarioDTO() {}

    public UsuarioDTO(String username, String password, String repetirPassword, String rol, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.repetirPassword = repetirPassword;
        this.rol = rol;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepetirPassword() {
        return repetirPassword;
    }

    public void setRepetirPassword(String repetirPassword) {
        this.repetirPassword = repetirPassword;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
