package com.ucv.planillas.controller;

import com.ucv.planillas.model.Empleado;
import com.ucv.planillas.Module.EmpleadoRepositorio;
import com.ucv.planillas.Module.Planilla;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

// controlador de la pantalla de empleados
public class EmpleadoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtSueldo;
    @FXML private ComboBox<String> cmbRegimen;

    @FXML private Label lblDescuento;
    @FXML private Label lblNeto;
    @FXML private Label lblMensaje;

    @FXML private ListView<String> listaEmpleados;

    // INYECCION DE DEPENDENCIAS: el repositorio (datos compartidos) y la planilla (calculos)
    // llegan armados desde la Fabrica, este controlador no los crea por su cuenta
    private final EmpleadoRepositorio repositorio;
    private final Planilla planilla;

    public EmpleadoController(EmpleadoRepositorio repositorio, Planilla planilla) {
        this.repositorio = repositorio;
        this.planilla = planilla;
    }

    @FXML
    public void initialize() {
        cmbRegimen.setItems(FXCollections.observableArrayList("AFP", "ONP"));
        cmbRegimen.setValue("AFP");
        actualizarLista();
    }

    @FXML
    private void onAgregar() {
        if (txtNombre.getText().isEmpty()) {
            lblMensaje.setText("Falta el nombre!");
            return;
        }

        double sueldo;
        try {
            sueldo = Double.parseDouble(txtSueldo.getText());
        } catch (NumberFormatException e) {
            lblMensaje.setText("El sueldo debe ser un numero!");
            return;
        }

        Empleado nuevo = new Empleado(txtNombre.getText(), sueldo, cmbRegimen.getValue(), planilla);
        repositorio.agregar(nuevo);
        actualizarLista();
        limpiarCampos();
        lblMensaje.setText("Empleado agregado!");
    }

    @FXML
    private void onSeleccionar() {
        int idx = listaEmpleados.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;

        Empleado e = repositorio.getEmpleados().get(idx);
        lblDescuento.setText(String.format("S/ %.2f", e.getDescuento()));
        lblNeto.setText(String.format("S/ %.2f", e.getNeto()));
    }

    private void actualizarLista() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Empleado e : repositorio.getEmpleados()) {
            items.add(e.getNombre() + " - S/" + e.getSueldo() + " [" + e.getRegimen() + "]");
        }
        listaEmpleados.setItems(items);
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtSueldo.clear();
        cmbRegimen.setValue("AFP");
    }
}
