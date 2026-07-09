package com.ucv.planillas.controller;

import com.ucv.planillas.Service.IGestionRRHHService;
import com.ucv.planillas.model.Bono;
import com.ucv.planillas.model.Empleado;
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

    // INYECCIÓN DEL SERVICIO CENTRALIZADO
    private final IGestionRRHHService gestionRRHHService;

    // El constructor ahora recibe el servicio único desde AppContext
    public BonoController(IGestionRRHHService gestionRRHHService) {
        this.gestionRRHHService = gestionRRHHService;
    }

    @FXML
    public void initialize() {
        try {
            ObservableList<String> nombres = FXCollections.observableArrayList();
            // Listamos los empleados desde la base de datos a través del servicio
            for (Empleado e : gestionRRHHService.listar()) {
                nombres.add(e.getNombre());
            }
            cmbEmpleado.setItems(nombres);
            if (!nombres.isEmpty()) cmbEmpleado.setValue(nombres.get(0));

            cmbTipo.setItems(FXCollections.observableArrayList(Bono.TipoBono.values()));
            cmbTipo.setValue(Bono.TipoBono.PRODUCTIVIDAD);

            actualizarLista();
            actualizarTotal();
        } catch (Exception e) {
            lblMensaje.setText("Error al inicializar bonos: " + e.getMessage());
        }
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
            lblMensaje.setText("El monto debe ser un número.");
            return;
        }

        try {
            // Buscamos al empleado activo usando el servicio
            Empleado empleado = gestionRRHHService.buscarPorNombre(nombreEmp);
            if (empleado == null) {
                lblMensaje.setText("No se encontró al empleado seleccionado.");
                return;
            }

            // Creamos el nuevo bono asignándole un ID único temporal en texto
            Bono nuevo = new Bono(
                    java.util.UUID.randomUUID().toString().substring(0, 8),
                    empleado,
                    cmbTipo.getValue(),
                    monto,
                    LocalDate.now()
            );

            // Registramos el bono en el servicio
            gestionRRHHService.registrarBono(nuevo);

            actualizarLista();
            actualizarTotal();
            txtMonto.clear();
            lblMensaje.setText("Bono registrado correctamente.");
        } catch (Exception e) {
            lblMensaje.setText("Error al registrar bono: " + e.getMessage());
        }
    }

    private void actualizarLista() {
        try {
            ObservableList<String> items = FXCollections.observableArrayList();
            for (Bono b : gestionRRHHService.listarBonos()) {
                items.add(b.toString());
            }
            listaBonos.setItems(items);
        } catch (Exception e) {
            lblMensaje.setText("Error al cargar lista: " + e.getMessage());
        }
    }

    private void actualizarTotal() {
        try {
            double total = 0;
            for (Bono b : gestionRRHHService.listarBonos()) {
                total += b.getMonto();
            }
            lblTotal.setText(String.format("S/ %.2f", total));
        } catch (Exception e) {
            lblTotal.setText("S/ 0.00");
        }
    }
}