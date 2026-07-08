package com.ucv.planillas.controller;

import com.ucv.planillas.model.Empleado;
import com.ucv.planillas.model.HoraExtra;
import com.ucv.planillas.service.EmpleadoRepositorio;
import com.ucv.planillas.service.HoraExtraRepositorio;
import com.ucv.planillas.service.Planilla;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

// aqui el bono por horas extra es AUTOMATICO: el usuario solo dice cuantas horas
// trabajo, y el sistema calcula el valor de la hora (segun el sueldo) y el pago
// (segun la ley peruana: 25% extra en las primeras 2 horas, 35% en las siguientes)
public class HoraExtraController {

    @FXML private ComboBox<String> cmbEmpleado;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHoras;
    @FXML private TextField txtMotivo;
    @FXML private Label lblValorHora;
    @FXML private Label lblPagoEstimado;
    @FXML private ListView<String> listaHoras;
    @FXML private Label lblMensaje;

    // INYECCION DE DEPENDENCIAS
    private final HoraExtraRepositorio horaExtraRepo;
    private final EmpleadoRepositorio empleadoRepo;
    private final Planilla planilla;

    public HoraExtraController(HoraExtraRepositorio horaExtraRepo, EmpleadoRepositorio empleadoRepo, Planilla planilla) {
        this.horaExtraRepo = horaExtraRepo;
        this.empleadoRepo = empleadoRepo;
        this.planilla = planilla;
    }

    @FXML
    public void initialize() {
        ObservableList<String> nombres = FXCollections.observableArrayList();
        for (Empleado e : empleadoRepo.getEmpleados()) nombres.add(e.getNombre());
        cmbEmpleado.setItems(nombres);
        if (!nombres.isEmpty()) cmbEmpleado.setValue(nombres.get(0));

        dpFecha.setValue(LocalDate.now());

        cmbEmpleado.valueProperty().addListener((obs, viejo, nuevo) -> actualizarEstimado());
        txtHoras.textProperty().addListener((obs, viejo, nuevo) -> actualizarEstimado());

        actualizarLista();
        actualizarEstimado();
    }

    private void actualizarEstimado() {
        Empleado empleado = empleadoRepo.buscarPorNombre(cmbEmpleado.getValue());
        if (empleado == null) {
            lblValorHora.setText("S/ 0.00");
            lblPagoEstimado.setText("S/ 0.00");
            return;
        }

        double valorHora = planilla.calcularValorHora(empleado);
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
            lblMensaje.setText("Las horas deben ser un numero.");
            return;
        }

        if (dpFecha.getValue() == null) {
            lblMensaje.setText("Selecciona una fecha.");
            return;
        }

        Empleado empleado = empleadoRepo.buscarPorNombre(nombreEmp);
        double valorHora = planilla.calcularValorHora(empleado); // calculado, no lo escribe el usuario

        HoraExtra nueva = new HoraExtra(horaExtraRepo.siguienteId(), empleado, dpFecha.getValue(),
                horas, valorHora, txtMotivo.getText());
        horaExtraRepo.agregar(nueva);
        actualizarLista();
        limpiarCampos();
        lblMensaje.setText("Registrado. El bono se calculo automaticamente segun ley.");
    }

    private void actualizarLista() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (HoraExtra h : horaExtraRepo.getRegistros()) items.add(h.toString());
        listaHoras.setItems(items);
    }

    private void limpiarCampos() {
        txtHoras.clear();
        txtMotivo.clear();
        actualizarEstimado();
    }
}
