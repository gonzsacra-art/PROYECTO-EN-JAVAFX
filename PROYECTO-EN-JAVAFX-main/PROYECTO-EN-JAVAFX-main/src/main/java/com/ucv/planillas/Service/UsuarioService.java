package com.ucv.planillas.Service;

import com.ucv.planillas.model.Usuario;
import com.ucv.planillas.repository.IUsuarioRepository;

public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository repository;

    public UsuarioService(IUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario login(String usuario, String password) {

        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese el usuario.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese la contraseña.");
        }

        return repository.login(usuario, password);
    }
}