package com.finanzas.controlgastos.dto.request;

// DTO de entrada para crear o actualizar un usuario
public class UsuarioRequest {

    private String nombre;
    private String email;
    private String contrasena;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
