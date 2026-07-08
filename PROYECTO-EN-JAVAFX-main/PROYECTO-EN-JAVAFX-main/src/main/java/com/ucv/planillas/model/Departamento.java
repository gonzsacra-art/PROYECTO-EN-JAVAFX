package com.ucv.planillas.model;

import java.util.ArrayList;
import java.util.List;

// agrupa empleados dentro de un area de la empresa
public class Departamento {

    private String id;
    private String nombre;
    private String descripcion;
    private String jefe;

    private List<Empleado> empleados;

    public Departamento(String id, String nombre, String jefe) {
        this.id = id;
        this.nombre = nombre;
        this.jefe = jefe;
        this.empleados = new ArrayList<>();
    }

    public void agregarEmpleado(Empleado e) {
        empleados.add(e);
    }

    public boolean removerEmpleado(Empleado e) {
        return empleados.remove(e);
    }

    public int getTotalEmpleados() {
        return empleados.size();
    }

    // suma todos los sueldos del departamento
    public double getMasaSalarial() {
        double total = 0;
        for (Empleado emp : empleados) {
            total += emp.getSueldo();
        }
        return total;
    }

    public String getId()
    { return id; }
    public void setId(String id)
    { this.id = id; }

    public String getNombre()
    { return nombre; }
    public void setNombre(String nombre)
    { this.nombre = nombre; }

    public String getDescripcion()
    { return descripcion; }
    public void setDescripcion(String d)
    { this.descripcion = d; }

    public String getJefe()
    { return jefe; }
    public void setJefe(String jefe)
    { this.jefe = jefe; }

    public List<Empleado> getEmpleados()
    { return empleados; }

    @Override
    public String toString() {
        return nombre + " (Jefe: " + jefe + " | " + getTotalEmpleados() + " empleados)";
    }
}
