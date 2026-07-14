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
 *
 * ACCESOS POR ROL:
 *  - ADMIN:    Empleados, Departamentos, Bonos, Horas Extra (todo)
 *  - OPERADOR: Empleados y Horas Extra (solo 2 accesos)
 */
public class ShellController {

    @FXML private Label lblBienvenida;
    @FXML private Button btnDepartamentos;
    @FXML private Button btnBonos;
    @FXML private StackPane contenido;

    // Obligatorio porque en AppContext haces: "return new ShellController();"
    public ShellController() {
    }

    /**
     * Devuelve true solo si hay una sesión activa y el usuario es ADMIN.
     * Si no hay sesión (null), se asume el rol MÁS restrictivo por seguridad.
     */
    private boolean esAdmin() {
        Usuario usuario = SesionService.getInstancia().getUsuarioActual();
        return usuario != null && usuario.getRol() == Rol.ADMIN;
    }

    @FXML
    public void initialize() {
        Usuario usuario = SesionService.getInstancia().getUsuarioActual();
        if (usuario != null) {
            lblBienvenida.setText("Bienvenido, " + usuario.getNombreCompleto()
                    + " (" + usuario.getRol() + ")");
        } else {
            lblBienvenida.setText("Bienvenido al Sistema");
        }

        // El operador no gestiona departamentos ni bonos, solo el administrador.
        // OJO: ahora si usuario == null los botones también se OCULTAN
        // (antes el 'else' los dejaba visibles para todos).
        boolean admin = esAdmin();
        btnDepartamentos.setVisible(admin);
        btnDepartamentos.setManaged(admin);
        btnBonos.setVisible(admin);
        btnBonos.setManaged(admin);

        onEmpleados(); // Módulo por defecto al ingresar
    }

    @FXML
    private void onEmpleados() {
        cargarContenido("/com/ucv/planillas/empleado-view.fxml");
    }

    @FXML
    private void onDepartamentos() {
        // DEFENSA EN PROFUNDIDAD: aunque el botón esté oculto,
        // verificamos el rol antes de cargar el módulo.
        if (!esAdmin()) return;
        cargarContenido("/com/ucv/planillas/departamento-view.fxml");
    }

    @FXML
    private void onBonos() {
        if (!esAdmin()) return;
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/planillas/login-view.fxml"));

        // También aquí usamos la fábrica para mantener la DI consistente
        loader.setControllerFactory(type -> AppContext.getInstance().getController(type));

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
