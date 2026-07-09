package com.ucv.planillas.controller;

import com.ucv.planillas.Service.IUsuarioService;
import com.ucv.planillas.config.AppContext;
import com.ucv.planillas.model.Usuario;
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


    private IUsuarioService usuarioService;


    public LoginController() {
        this.usuarioService = AppContext
                .getInstance()
                .getUsuarioService();
    }


    @FXML
    private void onIngresar() {

        String username = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();


        if(username.isEmpty() || password.isEmpty()){

            lblMensaje.setText(
                    "Ingrese usuario y contraseña"
            );

            return;
        }


        try {

            Usuario usuario = usuarioService.login(
                    username,
                    password
            );


            if(usuario == null){

                lblMensaje.setText(
                        "Usuario o contraseña incorrectos."
                );

                return;
            }


            abrirSistema();


        } catch(Exception e) {
            // CAMBIA ESTO TEMPORALMENTE:
            e.printStackTrace(); // Esto imprimirá el error real en la consola de tu IDE (abajo)
            lblMensaje.setText("Detalle del Error: " + e.getMessage());
        }

    }



    private void abrirSistema() throws Exception {


        FXMLLoader loader =
                new FXMLLoader(
                        getClass()
                                .getResource(
                                        "/com/ucv/planillas/shell-view.fxml"
                                )
                );


        Scene scene =
                new Scene(loader.load());


        Stage stage =
                (Stage) txtUsuario
                        .getScene()
                        .getWindow();


        stage.setScene(scene);

        stage.setTitle(
                "Sistema de Gestión de RRHH"
        );

        stage.show();

    }

}