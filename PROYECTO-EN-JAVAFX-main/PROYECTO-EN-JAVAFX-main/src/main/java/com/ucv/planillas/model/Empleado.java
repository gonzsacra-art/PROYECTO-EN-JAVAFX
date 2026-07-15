package com.ucv.planillas.model;

import com.ucv.planillas.Module.Planilla;

public class Empleado {

    private String id;
    private String nombre;
    private String cargo;
    private double sueldo;
    private String regimen;

    // Inyección de dependencias
    private Planilla planilla;

    // Constructor vacío (muy útil para Repository, JDBC y JavaFX)
    public Empleado() {
    }

    // Constructor completo
    public Empleado(String id,
                    String nombre,
                    String cargo,
                    double sueldo,
                    String regimen,
                    Planilla planilla) {

        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.sueldo = sueldo;
        this.regimen = regimen;
        this.planilla = planilla;
    }

    // Constructor simple
    public Empleado(String nombre,
                    double sueldo,
                    String regimen,
                    Planilla planilla) {

        this("", nombre, "", sueldo, regimen, planilla);
    }

    // getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public Planilla getPlanilla() {
        return planilla;
    }

    public void setPlanilla(Planilla planilla) {
        this.planilla = planilla;
    }

    // Zona de los calculos

    public double getDescuento() {
        if (planilla == null) {
            return 0;
        }
        return planilla.calcularDescuento(this);
    }

    public double getNeto() {
        if (planilla == null) {
            return sueldo;
        }
        return planilla.calcularNeto(this);
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empleado otro = (Empleado) o;
        return id != null && id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
