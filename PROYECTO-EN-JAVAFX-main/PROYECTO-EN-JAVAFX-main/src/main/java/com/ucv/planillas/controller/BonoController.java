package com.ucv.planillas.controller;

import com.ucv.planillas.model.Bono;
import com.ucv.planillas.model.Empleado;
import com.ucv.planillas.service.BonoRepositorio;
import com.ucv.planillas.service.EmpleadoRepositorio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class BonoController {

    @FXML private ComboBox<String> cmbEmpleado;
    @FXML private ComboBox<Bono.TipoBono> cmbTipo;
    @FXML private TextField txtMonto;
    @FXML private ListView<String> listaBonos;
    @FXML private Label lblMensaje;
    @FXML private Label lblTotal;

    // INYECCION DE DEPENDENCIAS
    private final BonoRepositorio bonoRepo;
    private final EmpleadoRepositorio empleadoRepo;

    public BonoController(BonoRepositorio bonoRepo, EmpleadoRepositorio empleadoRepo) {
        this.bonoRepo = bonoRepo;
        this.empleadoRepo = empleadoRepo;
    }

    @FXML
    public void initialize() {
        ObservableList<String> nombres = FXCollections.observableArrayList();
        for (Empleado e : empleadoRepo.getEmpleados()) nombres.add(e.getNombre());
        cmbEmpleado.setItems(nombres);
        if (!nombres.isEmpty()) cmbEmpleado.setValue(nombres.get(0));

        cmbTipo.setItems(FXCollections.observableArrayList(Bono.TipoBono.values()));
        cmbTipo.setValue(Bono.TipoBono.PRODUCTIVIDAD);

        actualizarLista();
        actualizarTotal();
    }

    @FXML
    private void onRegistrarBono() {
        String nombreEmp = cmbEmpleado.getValue();
        if (nombreEmp == null) {
            lblMensaje.setText("Selecciona un empleado.");
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText());
        } catch (NumberFormatException e) {
            lblMensaje.setText("El monto debe ser un numero.");
            return;
        }

        Empleado empleado = empleadoRepo.buscarPorNombre(nombreEmp);
        Bono nuevo = new Bono(bonoRepo.siguienteId(), empleado, cmbTipo.getValue(), monto, LocalDate.now());
        bonoRepo.agregar(nuevo);
        actualizarLista();
        actualizarTotal();
        txtMonto.clear();
        lblMensaje.setText("Bono registrado correctamente.");
    }

    private void actualizarLista() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Bono b : bonoRepo.getBonos()) items.add(b.toString());
        listaBonos.setItems(items);
    }

    private void actualizarTotal() {
        double total = 0;
        for (Bono b : bonoRepo.getBonos()) total += b.getMonto();
        lblTotal.setText(String.format("S/ %.2f", total));
    }
}