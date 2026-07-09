package com.ucv.planillas.controller;

import com.ucv.planillas.config.AppContext;
import com.ucv.planillas.model.Rol;
import com.ucv.planillas.model.Usuario;
import com.ucv.planillas.Module.SesionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Controlador "cascarón": la barra superior/lateral siempre está visible,
 * solo cambia el contenido dinámico del panel central (StackPane).
 */
public class ShellController {

    @FXML private Label lblBienvenida;
    @FXML private Button btnDepartamentos;
    @FXML private Button btnBonos;
    @FXML private StackPane contenido;

    // CONSTRUCTOR VACÍO
    // Obligatorio porque en AppContext haces: "return new ShellController();"
    public ShellController() {
    }

    @FXML
    public void initialize() {
        Usuario usuario = SesionService.getInstancia().getUsuarioActual();
        if (usuario != null) {
            lblBienvenida.setText("Bienvenido, " + usuario.getNombreCompleto());

            // El operador no gestiona departamentos ni bonos, solo el administrador
            boolean esAdmin = usuario.getRol() == Rol.ADMIN;
            btnDepartamentos.setVisible(esAdmin);
            btnDepartamentos.setManaged(esAdmin);
            btnBonos.setVisible(esAdmin);
            btnBonos.setManaged(esAdmin);
        } else {
            lblBienvenida.setText("Bienvenido al Sistema");
        }

        onEmpleados(); // Módulo por defecto al ingresar
    }

    @FXML
    private void onEmpleados() {
        cargarContenido("/com/ucv/planillas/empleado-view.fxml");
    }

    @FXML
    private void onDepartamentos() {
        cargarContenido("/com/ucv/planillas/departamento-view.fxml");
    }

    @FXML
    private void onBonos() {
        cargarContenido("/com/ucv/planillas/bono-view.fxml");
    }

    @FXML
    private void onHorasExtra() {
        cargarContenido("/com/ucv/planillas/horaextra-view.fxml");
    }

    @FXML
    private void onCerrarSesion() throws Exception {
        SesionService.getInstancia().cerrarSesion();
        Stage stage = (Stage) lblBienvenida.getScene().getWindow();

        // Redirigir a la vista de Login limpia sin depender de la clase Navegador antigua
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/planillas/login-view.fxml"));
        Parent root = loader.load();

        stage.getScene().setRoot(root);
        stage.setTitle("Sistema de Gestión de RRHH - UCV");
    }

    /**
     * Carga de forma dinámica un módulo FXML dentro del panel central 'contenido'
     * inyectando las dependencias necesarias a través de AppContext.
     */
    private void cargarContenido(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));

            // CONECTAMOS LAS SUB-VISTAS CON NUESTRO CONTENEDOR CENTRAL DE DEPENDENCIAS
            loader.setControllerFactory(type -> AppContext.getInstance().getController(type));

            Parent vista = loader.load();
            contenido.getChildren().setAll(vista);
        } catch (Exception e) {
            System.err.println("Error al cargar el módulo: " + rutaFxml);
            e.printStackTrace();
        }
    }
}