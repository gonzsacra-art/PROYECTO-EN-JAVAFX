package com.ucv.planillas.model;

// esto es la cuenta para iniciar sesion (diferente al Empleado de la planilla)
public class Usuario {

    private String username;
    private String password; // nota: en un proyecto real esto NO se guarda asi, se usaria un hash
    private String nombreCompleto;
    private Rol rol;

    public Usuario(String username, String password, String nombreCompleto, Rol rol) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public String getUsername()
    { return username; }
    public void setUsername(String username)
    { this.username = username; }

    public String getPassword()
    { return password; }
    public void setPassword(String password)
    { this.password = password; }

    public String getNombreCompleto()
    { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto)
    { this.nombreCompleto = nombreCompleto; }

    public Rol getRol()
    { return rol; }
    public void setRol(Rol rol)
    { this.rol = rol; }

    @Override
    public String toString() {
        return username + " (" + rol + ")";
    }
}
