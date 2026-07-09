package com.ucv.planillas.model;

import com.ucv.planillas.Module.Planilla;

// clase Empleado - modelo principal de la planilla
public class Empleado {

    private String id;
    private String nombre;
    private String cargo;
    private double sueldo;
    private String regimen; // AFP u ONP

    // PATRON INYECCION DE DEPENDENCIAS
    // el empleado no calcula solo, necesita que le pasen la planilla
    private Planilla planilla;

    // constructor simple (se usa en el modulo de departamentos)
    public Empleado(String nombre, double sueldo, String regimen, Planilla planilla) {
        this.nombre = nombre;
        this.sueldo = sueldo;
        this.regimen = regimen;
        this.planilla = planilla; // aqui es la inyeccion
        this.cargo = "";
        this.id = "";
    }

    public Empleado(String id, String nombre, String cargo, double sueldo, String regimen, Planilla planilla) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.sueldo = sueldo;
        this.regimen = regimen;
        this.planilla = planilla;
    }

    // usa la planilla inyectada para obtener resultados
    public double getDescuento() {
        return planilla.calcularDescuento(this);
    }

    public double getNeto() {
        return planilla.calcularNeto(this);
    }

    public String getId()
    { return id; }
    public void setId(String id)
    { this.id = id; }

    public String getCargo()
    { return cargo; }
    public void setCargo(String cargo)
    { this.cargo = cargo; }

    public String getNombre()
    { return nombre; }
    public void setNombre(String n)
    { this.nombre = n; }

    public double getSueldo()
    { return sueldo; }
    public void setSueldo(double s)
    { this.sueldo = s; }

    public String getRegimen()
    { return regimen; }
    public void setRegimen(String r)
    { this.regimen = r; }

    @Override
    public String toString() {
        return nombre;
    }
}
