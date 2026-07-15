package com.ucv.planillas.Module;

import com.ucv.planillas.model.Usuario;


 //Guarda quién inició sesión.

 // AHORA: es una clase normal. AppContext crea UNA sola instancia y la
 // INYECTA POR CONSTRUCTOR a los controladores que la necesitan
 //(LoginController y ShellController). Sigue existiendo una única sesión
 //activa, pero eso lo garantiza el contenedor de dependencias, no la clase.

public class SesionService {

    private Usuario usuarioActual;

    public SesionService() {
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }
}
