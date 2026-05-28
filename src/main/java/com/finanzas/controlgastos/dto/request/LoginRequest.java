package com.finanzas.controlgastos.dto.request;

// DTO de entrada para autenticar un usuario
public class LoginRequest {

    private String email;
    private String contrasena;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
