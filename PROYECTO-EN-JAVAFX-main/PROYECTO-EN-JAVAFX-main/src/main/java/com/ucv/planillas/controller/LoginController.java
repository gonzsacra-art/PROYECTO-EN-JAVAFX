package com.ucv.planillas.controller;

import com.ucv.planillas.Service.IUsuarioService;
import com.ucv.planillas.Module.Navegador;
import com.ucv.planillas.Module.SesionService;
import com.ucv.planillas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    private final IUsuarioService usuarioService;
    private final SesionService sesionService;
    private final Navegador navegador;

    public LoginController(IUsuarioService usuarioService,
                           SesionService sesionService,
                           Navegador navegador) {
        this.usuarioService = usuarioService;
        this.sesionService = sesionService;
        this.navegador = navegador;
    }

    @FXML
    private void onIngresar() {

        String username = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Ingrese usuario y contraseña");
            return;
        }

        try {
            Usuario usuario = usuarioService.login(username, password);

            if (usuario == null) {
                lblMensaje.setText("Usuario o contraseña incorrectos.");
                return;
            }


            sesionService.setUsuarioActual(usuario);

            abrirSistema();

        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error: " + e.getMessage());
        }
    }

    private void abrirSistema() throws Exception {
        Stage stage = (Stage) txtUsuario.getScene().getWindow();


        navegador.irA(
                stage,
                "/com/ucv/planillas/shell-view.fxml",
                "Sistema de Gestión de RRHH"
        );

        stage.show();
    }
}
