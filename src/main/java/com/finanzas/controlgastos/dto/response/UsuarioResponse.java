package com.finanzas.controlgastos.dto.response;

// DTO de salida para exponer datos del usuario sin revelar la contraseña
public class UsuarioResponse {

    private Integer id;
    private String nombre;
    private String email;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
