package com.ucv.planillas.controller;

import com.ucv.planillas.Module.Navegador;
import com.ucv.planillas.Module.SesionService;
import com.ucv.planillas.model.Rol;
import com.ucv.planillas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ShellController {

    @FXML private Label lblBienvenida;
    @FXML private Button btnDepartamentos;
    @FXML private Button btnBonos;
    @FXML private StackPane contenido;

    private final SesionService sesionService;
    private final Navegador navegador;

    public ShellController(SesionService sesionService, Navegador navegador) {
        this.sesionService = sesionService;
        this.navegador = navegador;
    }

    private boolean esAdmin() {
        Usuario usuario = sesionService.getUsuarioActual();
        return usuario != null && usuario.getRol() == Rol.ADMIN;
    }

    @FXML
    public void initialize() {
        Usuario usuario = sesionService.getUsuarioActual();
        if (usuario != null) {
            lblBienvenida.setText("Bienvenido, " + usuario.getNombreCompleto()
                    + " (" + usuario.getRol() + ")");
        } else {
            lblBienvenida.setText("Bienvenido al Sistema");
        }

        // El operador no gestiona departamentos ni bonos, solo el administrador
        // Si usuario == null los botones también se OCULTAN.
        boolean admin = esAdmin();
        btnDepartamentos.setVisible(admin);
        btnDepartamentos.setManaged(admin);
        btnBonos.setVisible(admin);
        btnBonos.setManaged(admin);

        onEmpleados();
    }

    @FXML
    private void onEmpleados() {
        cargarContenido("/com/ucv/planillas/empleado-view.fxml");
    }

    @FXML
    private void onDepartamentos() {

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
        sesionService.cerrarSesion();
        Stage stage = (Stage) lblBienvenida.getScene().getWindow();

        navegador.cambiarRaiz(
                stage,
                "/com/ucv/planillas/login-view.fxml",
                "Sistema de Gestión de RRHH - UCV"
        );
    }

    private void cargarContenido(String rutaFxml) {
        try {
            Parent vista = navegador.cargarVista(rutaFxml);
            contenido.getChildren().setAll(vista);
        } catch (Exception e) {
            System.err.println("Error al cargar el módulo: " + rutaFxml);
            e.printStackTrace();
        }
    }
}
