package com.ucv.planillas.controller;

import com.ucv.planillas.Service.IUsuarioService;
import com.ucv.planillas.config.AppContext;
import com.ucv.planillas.model.Usuario;
import com.ucv.planillas.Module.SesionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

    public LoginController() {
        this.usuarioService = AppContext
                .getInstance()
                .getUsuarioService();
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

            // CORRECCIÓN DEL BUG DE ROLES:
            // Antes nunca se guardaba el usuario en SesionService, por eso
            // en ShellController.initialize() getUsuarioActual() devolvía
            // null y los botones de ADMIN quedaban visibles para TODOS
            // (el operador veía Departamentos y Bonos).
            SesionService.getInstancia().setUsuarioActual(usuario);

            abrirSistema();

        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error: " + e.getMessage());
        }
    }

    private void abrirSistema() throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/ucv/planillas/shell-view.fxml")
        );

        // Usamos la misma fábrica de AppContext para mantener la
        // inyección de dependencias consistente en toda la app.
        loader.setControllerFactory(type -> AppContext.getInstance().getController(type));

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) txtUsuario.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Sistema de Gestión de RRHH");
        stage.show();
    }
}
