package com.ucv.planillas.Module;

import com.ucv.planillas.model.Departamento;
import com.ucv.planillas.model.Empleado;

import java.util.ArrayList;

// PATRON SINGLETON - lista compartida de departamentos
public class DepartamentoRepositorio {

    private static DepartamentoRepositorio instancia;
    private ArrayList<Departamento> departamentos = new ArrayList<>();

    private DepartamentoRepositorio() {
        // usamos los mismos empleados de ejemplo del repositorio de empleados
        ArrayList<Empleado> empleados = EmpleadoRepositorio.getInstancia().getEmpleados();

        Departamento sistemas = new Departamento("D01", "Sistemas", "Luis");
        Departamento contab   = new Departamento("D02", "Contabilidad", "Ana");
        if (empleados.size() >= 3) {
            sistemas.agregarEmpleado(empleados.get(0));
            sistemas.agregarEmpleado(empleados.get(2));
            contab.agregarEmpleado(empleados.get(1));
        }
        departamentos.add(sistemas);
        departamentos.add(contab);
    }

    public static DepartamentoRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new DepartamentoRepositorio();
        }
        return instancia;
    }

    public ArrayList<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void agregar(Departamento d) {
        departamentos.add(d);
    }
}
