package com.ucv.planillas.service;

import com.ucv.planillas.model.Rol;
import com.ucv.planillas.model.Usuario;

import java.util.ArrayList;

// se encarga de validar las credenciales del login
public class AutenticacionService {
    //cuenta del sistema
    private static ArrayList<Usuario> usuarios = new ArrayList<>();

    static {
        usuarios.add(new Usuario("admin", "admin123", "Administrador General", Rol.ADMIN));
        usuarios.add(new Usuario("operador", "oper123", "Juan Perez", Rol.OPERADOR));
    }

    public Usuario validarCredenciales(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
