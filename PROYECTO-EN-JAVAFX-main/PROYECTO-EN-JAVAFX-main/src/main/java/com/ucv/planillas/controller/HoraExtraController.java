package com.ucv.planillas.controller;

import com.ucv.planillas.Service.IGestionRRHHService;
import com.ucv.planillas.model.Empleado;
import com.ucv.planillas.model.HoraExtra;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class HoraExtraController {

    @FXML private ComboBox<String> cmbEmpleado;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHoras;
    @FXML private TextField txtMotivo;
    @FXML private Label lblValorHora;
    @FXML private Label lblPagoEstimado;
    @FXML private ListView<String> listaHoras;
    @FXML private Label lblMensaje;

    private final IGestionRRHHService gestionRRHHService;

    public HoraExtraController(IGestionRRHHService gestionRRHHService) {
        this.gestionRRHHService = gestionRRHHService;
    }

    @FXML
    public void initialize() {
        ObservableList<String> nombres = FXCollections.observableArrayList();
        for (Empleado e : gestionRRHHService.listar()) nombres.add(e.getNombre());
        cmbEmpleado.setItems(nombres);
        if (!nombres.isEmpty()) cmbEmpleado.setValue(nombres.get(0));

        dpFecha.setValue(LocalDate.now());

        cmbEmpleado.valueProperty().addListener((obs, viejo, nuevo) -> actualizarEstimado());
        txtHoras.textProperty().addListener((obs, viejo, nuevo) -> actualizarEstimado());

        actualizarLista();
        actualizarEstimado();
    }

    private void actualizarEstimado() {
        Empleado empleado = gestionRRHHService.buscarPorNombre(cmbEmpleado.getValue());
        if (empleado == null) {
            lblValorHora.setText("S/ 0.00");
            lblPagoEstimado.setText("S/ 0.00");
            return;
        }

        double valorHora = gestionRRHHService.calcularValorHora(empleado);
        lblValorHora.setText(String.format("S/ %.2f", valorHora));

        try {
            int horas = Integer.parseInt(txtHoras.getText());
            HoraExtra simulada = new HoraExtra("", empleado, LocalDate.now(), horas, valorHora, "");
            lblPagoEstimado.setText(String.format("S/ %.2f", simulada.calcularPago()));
        } catch (NumberFormatException e) {
            lblPagoEstimado.setText("S/ 0.00");
        }
    }

    @FXML
    private void onRegistrar() {
        String nombreEmp = cmbEmpleado.getValue();
        if (nombreEmp == null) {
            lblMensaje.setText("Selecciona un empleado.");
            return;
        }

        int horas;
        try {
            horas = Integer.parseInt(txtHoras.getText());
        } catch (NumberFormatException e) {
            lblMensaje.setText("Las horas deben ser un número.");
            return;
        }

        if (dpFecha.getValue() == null) {
            lblMensaje.setText("Selecciona una fecha.");
            return;
        }

        Empleado empleado = gestionRRHHService.buscarPorNombre(nombreEmp);
        if (empleado == null) return;

        double valorHora = gestionRRHHService.calcularValorHora(empleado);

        HoraExtra nueva = new HoraExtra(
                java.util.UUID.randomUUID().toString().substring(0, 8),
                empleado,
                dpFecha.getValue(),
                horas,
                valorHora,
                txtMotivo.getText()
        );

        gestionRRHHService.registrarHoraExtra(nueva);
        actualizarLista();
        limpiarCampos();
        lblMensaje.setText("Registrado. El bono se calculó automáticamente según ley.");
    }

    private void actualizarLista() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (HoraExtra h : gestionRRHHService.listarHorasExtra()) items.add(h.toString());
        listaHoras.setItems(items);
    }

    private void limpiarCampos() {
        txtHoras.clear();
        txtMotivo.clear();
        actualizarEstimado();
    }
}