package com.ucv.planillas.service;

import com.ucv.planillas.model.Usuario;

// PATRON SINGLETON
// guarda quien inicio sesion, solo puede existir UNA sesion activa a la vez
public class SesionService {

    private static SesionService instancia;
    private Usuario usuarioActual;

    // constructor privado, nadie de afuera puede hacer "new SesionService()"
    private SesionService() {
    }

    public static SesionService getInstancia() {
        if (instancia == null) {
            instancia = new SesionService();
        }
        return instancia;
    }

    public Usuario getUsuarioActual()
    { return usuarioActual; }

    public void setUsuarioActual(Usuario usuario)
    { this.usuarioActual = usuario; }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }
}
