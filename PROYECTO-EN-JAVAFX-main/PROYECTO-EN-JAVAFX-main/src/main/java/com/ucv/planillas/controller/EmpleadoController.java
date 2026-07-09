package com.ucv.planillas.controller;

import com.ucv.planillas.Service.IGestionRRHHService;
import com.ucv.planillas.model.Empleado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EmpleadoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtSueldo;
    @FXML private ComboBox<String> cmbRegimen;

    @FXML private Label lblDescuento;
    @FXML private Label lblNeto;
    @FXML private Label lblMensaje;

    @FXML private ListView<String> listaEmpleados;

    // INYECCIÓN DEL SERVICIO CENTRALIZADO
    private final IGestionRRHHService gestionRRHHService;

    public EmpleadoController(IGestionRRHHService gestionRRHHService) {
        this.gestionRRHHService = gestionRRHHService;
    }

    @FXML
    public void initialize() {
        cmbRegimen.setItems(FXCollections.observableArrayList("AFP", "ONP"));
        cmbRegimen.setValue("AFP");
        actualizarLista();
    }

    @FXML
    private void onAgregar() {
        if (txtNombre.getText().trim().isEmpty()) {
            lblMensaje.setText("Falta el nombre!");
            return;
        }

        double sueldo;
        try {
            sueldo = Double.parseDouble(txtSueldo.getText());
        } catch (NumberFormatException e) {
            lblMensaje.setText("El sueldo debe ser un número!");
            return;
        }

        try {
            // Creamos el empleado sin pasar el objeto Planilla manual,
            // ya que el Service se encarga de inyectarlo/gestionarlo al mapear.
            Empleado nuevo = new Empleado();
            nuevo.setId(java.util.UUID.randomUUID().toString().substring(0, 8)); // Genera un ID corto único temporal
            nuevo.setNombre(txtNombre.getText().trim());
            nuevo.setCargo("Operario"); // Cargo por defecto
            nuevo.setSueldo(sueldo);
            nuevo.setRegimen(cmbRegimen.getValue());

            gestionRRHHService.crear(nuevo);
            actualizarLista();
            limpiarCampos();
            lblMensaje.setText("Empleado agregado!");
        } catch (Exception e) {
            lblMensaje.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void onSeleccionar() {
        int idx = listaEmpleados.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;

        try {
            Empleado e = gestionRRHHService.listar().get(idx);
            lblDescuento.setText(String.format("S/ %.2f", e.getDescuento()));
            lblNeto.setText(String.format("S/ %.2f", e.getNeto()));
        } catch (Exception e) {
            lblMensaje.setText("Error al calcular: " + e.getMessage());
        }
    }

    private void actualizarLista() {
        try {
            ObservableList<String> items = FXCollections.observableArrayList();
            for (Empleado e : gestionRRHHService.listar()) {
                items.add(e.getNombre() + " - S/" + e.getSueldo() + " [" + e.getRegimen() + "]");
            }
            listaEmpleados.setItems(items);
        } catch (Exception e) {
            lblMensaje.setText("Error al cargar lista: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtSueldo.clear();
        cmbRegimen.setValue("AFP");
    }
}