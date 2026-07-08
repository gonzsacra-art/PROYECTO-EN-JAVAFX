package com.ucv.planillas.service;

import com.ucv.planillas.model.Empleado;

import java.util.ArrayList;

public class EmpleadoRepositorio {

    private static EmpleadoRepositorio instancia;
    private ArrayList<Empleado> empleados = new ArrayList<>();

    private EmpleadoRepositorio() {
        // datos de ejemplo para no arrancar con la app vacia
        Planilla planilla = new Planilla();
        empleados.add(new Empleado("Carlos", 3500, "AFP", planilla));
        empleados.add(new Empleado("Maria", 2800, "ONP", planilla));
        empleados.add(new Empleado("Luis", 4200, "AFP", planilla));
        empleados.add(new Empleado("Ana", 3100, "ONP", planilla));
        empleados.add(new Empleado("Roberto", 5000, "AFP", planilla));
    }

    public static EmpleadoRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new EmpleadoRepositorio();
        }
        return instancia;
    }

    public ArrayList<Empleado> getEmpleados() {
        return empleados;
    }

    public void agregar(Empleado e) {
        empleados.add(e);
    }

    public Empleado buscarPorNombre(String nombre) {
        for (Empleado e : empleados) {
            if (e.getNombre().equals(nombre)) return e;
        }
        return null;
    }
}
