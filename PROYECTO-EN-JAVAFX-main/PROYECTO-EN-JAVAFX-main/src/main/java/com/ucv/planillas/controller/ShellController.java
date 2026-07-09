package com.ucv.planillas.controller;

import com.ucv.planillas.model.Rol;
import com.ucv.planillas.model.Usuario;
import com.ucv.planillas.Module.Fabrica;
import com.ucv.planillas.Module.Navegador;
import com.ucv.planillas.Module.SesionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// controlador "cascaron": la barra de arriba siempre esta visible,
// solo cambia lo de adentro (el centro). Asi el usuario elige un modulo
// y el resto de opciones sigue ahi arriba, sin necesidad de darle "Volver".
public class ShellController {

    @FXML private Label lblBienvenida;
    @FXML private Button btnDepartamentos;
    @FXML private Button btnBonos;
    @FXML private StackPane contenido;

    // INYECCION DE DEPENDENCIAS: la fabrica llega desde afuera y se reutiliza
    // para cargar cada modulo con sus propias dependencias ya resueltas.
    private final Fabrica fabrica;

    public ShellController(Fabrica fabrica) {
        this.fabrica = fabrica;
    }

    @FXML
    public void initialize() {
        Usuario usuario = SesionService.getInstancia().getUsuarioActual();
        lblBienvenida.setText("Bienvenido, " + usuario.getNombreCompleto());

        // el operador no gestiona departamentos ni bonos, solo empleados y horas extra
        boolean esAdmin = usuario.getRol() == Rol.ADMIN;
        btnDepartamentos.setVisible(esAdmin);
        btnDepartamentos.setManaged(esAdmin);
        btnBonos.setVisible(esAdmin);
        btnBonos.setManaged(esAdmin);

        onEmpleados(); // modulo que se muestra apenas se entra
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
        Navegador.irA(stage, "/com/ucv/planillas/login-view.fxml", "Sistema de Gestion de RRHH - UCV", fabrica);
    }

    // carga un modulo dentro del mismo panel, sin abrir una ventana nueva
    private void cargarContenido(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            loader.setControllerFactory(fabrica);
            Parent vista = loader.load();
            contenido.getChildren().setAll(vista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
