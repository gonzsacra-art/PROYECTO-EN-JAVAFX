package com.ucv.planillas.controller;

import com.ucv.planillas.model.Usuario;
import com.ucv.planillas.service.AutenticacionService;
import com.ucv.planillas.service.Fabrica;
import com.ucv.planillas.service.Navegador;
import com.ucv.planillas.service.SesionService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    // INYECCION DE DEPENDENCIAS
    private final AutenticacionService authService;
    private final Fabrica fabrica;

    public LoginController(AutenticacionService authService, Fabrica fabrica) {
        this.authService = authService;
        this.fabrica = fabrica;
    }

    @FXML
    private void onIngresar() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Ingresa usuario y contraseña.");
            return;
        }

        Usuario u = authService.validarCredenciales(usuario, password);
        if (u == null) {
            lblMensaje.setText("Usuario o contraseña incorrectos.");
            return;
        }

        // guardamos quien inicio sesion (singleton)
        SesionService.getInstancia().setUsuarioActual(u);

        try {
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            Navegador.irA(stage, "/com/ucv/planillas/shell-view.fxml", "Sistema de Gestion de RRHH - UCV", fabrica);
        } catch (Exception e) {
            lblMensaje.setText("No se pudo abrir el panel.");
        }
    }
}
