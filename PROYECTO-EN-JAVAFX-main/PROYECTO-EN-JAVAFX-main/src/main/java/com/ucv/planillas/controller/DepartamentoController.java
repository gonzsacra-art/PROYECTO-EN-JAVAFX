package com.ucv.planillas.controller;

import com.ucv.planillas.Service.IGestionRRHHService;
import com.ucv.planillas.model.Departamento;
import com.ucv.planillas.model.Empleado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DepartamentoController {

    @FXML private TextField txtIdDepto;
    @FXML private TextField txtNombreDepto;
    @FXML private TextField txtJefeDepto;
    @FXML private ListView<String> listaDepartamentos;
    @FXML private Label lblDetalleNombre;
    @FXML private Label lblDetalleJefe;
    @FXML private Label lblDetalleTotalEmp;
    @FXML private Label lblDetalleMasaSalarial;
    @FXML private ComboBox<String> cmbEmpleadosDisponibles;
    @FXML private ListView<String> listaEmpDepto;
    @FXML private Label lblMensaje;

    private final IGestionRRHHService gestionRRHHService;

    public DepartamentoController(IGestionRRHHService gestionRRHHService) {
        this.gestionRRHHService = gestionRRHHService;
    }

    @FXML
    public void initialize() {
        listaDepartamentos.getSelectionModel().selectedIndexProperty().addListener(
                (obs, oldVal, newVal) -> mostrarDetalle(newVal.intValue())
        );

        actualizarListaDepartamentos();
        actualizarComboEmpleados();
        limpiarDetalle();
    }

    @FXML
    private void onCrearDepartamento() {
        String id = txtIdDepto.getText().trim();
        String nombre = txtNombreDepto.getText().trim();
        String jefe = txtJefeDepto.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || jefe.isEmpty()) {
            lblMensaje.setText("Completa todos los campos del departamento.");
            return;
        }

        for (Departamento d : gestionRRHHService.listarDepartamentos()) {
            if (d.getId().equalsIgnoreCase(id)) {
                lblMensaje.setText("Ya existe un departamento con ese ID.");
                return;
            }
        }

        Departamento nuevo = new Departamento(id, nombre, jefe);
        gestionRRHHService.crearDepartamento(nuevo);
        actualizarListaDepartamentos();
        limpiarFormulario();
        lblMensaje.setText("Departamento '" + nombre + "' creado.");
    }

    @FXML
    private void onEliminarDepartamento() {
        int idx = listaDepartamentos.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            lblMensaje.setText("Selecciona un departamento para eliminar.");
            return;
        }

        Departamento depto = gestionRRHHService.listarDepartamentos().get(idx);
        if (depto.getTotalEmpleados() > 0) {
            lblMensaje.setText("No se puede eliminar: el departamento tiene empleados asignados.");
            return;
        }

        gestionRRHHService.listarDepartamentos().remove(idx);
        actualizarListaDepartamentos();
        limpiarDetalle();
        lblMensaje.setText("Departamento eliminado.");
    }

    @FXML
    private void onAsignarEmpleado() {
        int idxDepto = listaDepartamentos.getSelectionModel().getSelectedIndex();
        if (idxDepto < 0) {
            lblMensaje.setText("Primero selecciona un departamento.");
            return;
        }

        String nombreEmp = cmbEmpleadosDisponibles.getValue();
        if (nombreEmp == null || nombreEmp.isEmpty()) {
            lblMensaje.setText("Selecciona un empleado del combo.");
            return;
        }

        Empleado empleado = gestionRRHHService.buscarPorNombre(nombreEmp);
        if (empleado == null) return;

        Departamento depto = gestionRRHHService.listarDepartamentos().get(idxDepto);
        if (depto.getEmpleados().contains(empleado)) {
            lblMensaje.setText(nombreEmp + " ya está en este departamento.");
            return;
        }

        depto.agregarEmpleado(empleado);
        mostrarDetalle(idxDepto);
        actualizarListaDepartamentos();
        lblMensaje.setText(nombreEmp + " asignado a " + depto.getNombre() + ".");
    }

    @FXML
    private void onRemoverEmpleado() {
        int idxDepto = listaDepartamentos.getSelectionModel().getSelectedIndex();
        if (idxDepto < 0) {
            lblMensaje.setText("Selecciona un departamento primero.");
            return;
        }

        String nombreEmp = listaEmpDepto.getSelectionModel().getSelectedItem();
        if (nombreEmp == null) {
            lblMensaje.setText("Selecciona un empleado de la lista del departamento.");
            return;
        }
        String nombre = nombreEmp.split(" - ")[0];
        Empleado empleado = gestionRRHHService.buscarPorNombre(nombre);
        if (empleado == null) return;

        Departamento depto = gestionRRHHService.listarDepartamentos().get(idxDepto);
        depto.removerEmpleado(empleado);
        mostrarDetalle(idxDepto);
        actualizarListaDepartamentos();
        lblMensaje.setText(nombre + " removido de " + depto.getNombre() + ".");
    }

    private void actualizarListaDepartamentos() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Departamento d : gestionRRHHService.listarDepartamentos()) {
            items.add(String.format("[%s] %s  |  Jefe: %s  |  Emp: %d  |  Masa: S/ %.2f",
                    d.getId(), d.getNombre(), d.getJefe(), d.getTotalEmpleados(), d.getMasaSalarial()));
        }
        listaDepartamentos.setItems(items);
    }

    private void mostrarDetalle(int idx) {
        if (idx < 0 || idx >= gestionRRHHService.listarDepartamentos().size()) {
            limpiarDetalle();
            return;
        }

        Departamento d = gestionRRHHService.listarDepartamentos().get(idx);
        lblDetalleNombre.setText(d.getNombre());
        lblDetalleJefe.setText(d.getJefe());
        lblDetalleTotalEmp.setText(String.valueOf(d.getTotalEmpleados()));
        lblDetalleMasaSalarial.setText(String.format("S/ %.2f", d.getMasaSalarial()));
        ObservableList<String> empItems = FXCollections.observableArrayList();
        for (Empleado e : d.getEmpleados()) {
            empItems.add(e.getNombre() + " - S/" + e.getSueldo() + " [" + e.getRegimen() + "]");
        }
        listaEmpDepto.setItems(empItems);
    }

    private void actualizarComboEmpleados() {
        ObservableList<String> nombres = FXCollections.observableArrayList();
        for (Empleado e : gestionRRHHService.listar()) {
            nombres.add(e.getNombre());
        }
        cmbEmpleadosDisponibles.setItems(nombres);
        if (!nombres.isEmpty()) cmbEmpleadosDisponibles.setValue(nombres.get(0));
    }

    private void limpiarDetalle() {
        lblDetalleNombre.setText("-");
        lblDetalleJefe.setText("-");
        lblDetalleTotalEmp.setText("0");
        lblDetalleMasaSalarial.setText("S/ 0.00");
        listaEmpDepto.setItems(FXCollections.observableArrayList());
    }

    private void limpiarFormulario() {
        txtIdDepto.clear();
        txtNombreDepto.clear();
        txtJefeDepto.clear();
    }
}