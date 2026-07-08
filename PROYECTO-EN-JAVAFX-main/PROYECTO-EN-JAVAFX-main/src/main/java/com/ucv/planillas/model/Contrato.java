package com.ucv.planillas.model;

import java.time.LocalDate;

// contrato laboral de un empleado
public class Contrato {

    public enum TipoContrato {
        INDEFINIDO,
        PLAZO_FIJO,
        PRACTICAS
    }

    private String id;
    private Empleado empleado;
    private TipoContrato tipo;
    private double sueldoPactado;
    private LocalDate fechaInicio;
    private boolean activo;

    public Contrato(String id, Empleado empleado, TipoContrato tipo, double sueldoPactado, LocalDate fechaInicio) {
        this.id = id;
        this.empleado = empleado;
        this.tipo = tipo;
        this.sueldoPactado = sueldoPactado;
        this.fechaInicio = fechaInicio;
        this.activo = true;
    }

    public String getId()
    { return id; }

    public Empleado getEmpleado()
    { return empleado; }

    public TipoContrato getTipo()
    { return tipo; }

    public double getSueldoPactado()
    { return sueldoPactado; }

    public LocalDate getFechaInicio()
    { return fechaInicio; }

    public boolean isActivo()
    { return activo; }
    public void setActivo(boolean activo)
    { this.activo = activo; }

    @Override
    public String toString() {
        return id + " - " + tipo + " - " + empleado.getNombre() + (activo ? " (activo)" : " (cerrado)");
    }
}
