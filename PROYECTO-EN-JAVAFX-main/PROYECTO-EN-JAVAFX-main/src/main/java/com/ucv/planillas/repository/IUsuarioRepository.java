package com.ucv.planillas.repository;

import com.ucv.planillas.model.Usuario;

public interface IUsuarioRepository {

    Usuario login(String username, String password);

}