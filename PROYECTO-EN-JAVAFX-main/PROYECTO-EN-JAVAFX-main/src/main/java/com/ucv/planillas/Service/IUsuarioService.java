package com.ucv.planillas.Service;

import com.ucv.planillas.model.Usuario;

public interface IUsuarioService {

    Usuario login(String usuario, String password);

}