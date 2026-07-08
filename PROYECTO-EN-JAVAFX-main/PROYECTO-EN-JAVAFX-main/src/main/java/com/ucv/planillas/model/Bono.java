package com.ucv.planillas.model;

import java.time.LocalDate;

// bonos adicionales que se le pueden dar a un empleado
public class Bono {

    public enum TipoBono {
        PRODUCTIVIDAD,
        ESCOLARIDAD,
        ASIGNACION_FAMILIAR,
        OTRO
    }

    private String id;
    private Empleado empleado;
    private TipoBono tipo;
    private double monto;
    private LocalDate fecha;

    public Bono(String id, Empleado empleado, TipoBono tipo, double monto, LocalDate fecha) {
        this.id = id;
        this.empleado = empleado;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
    }

    public String getId()
    { return id; }

    public Empleado getEmpleado()
    { return empleado; }

    public TipoBono getTipo()
    { return tipo; }

    public double getMonto()
    { return monto; }
    public void setMonto(double monto)
    { this.monto = monto; }

    public LocalDate getFecha()
    { return fecha; }

    @Override
    public String toString() {
        return tipo + " - S/ " + monto + " (" + empleado.getNombre() + ")";
    }
}
