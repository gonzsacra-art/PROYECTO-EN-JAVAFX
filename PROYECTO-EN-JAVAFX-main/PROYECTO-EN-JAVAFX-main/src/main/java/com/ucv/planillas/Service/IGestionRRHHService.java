package com.ucv.planillas.Service;

import com.ucv.planillas.model.Empleado;
import com.ucv.planillas.model.Bono;
import com.ucv.planillas.model.Departamento;
import com.ucv.planillas.model.HoraExtra;
import java.util.List;

public interface IGestionRRHHService {

    // === OPERACIONES DE EMPLEADOS ===
    List<Empleado> listar();
    Empleado buscarPorId(String id);
    void crear(Empleado empleado);
    void actualizar(Empleado empleado);
    void eliminar(String id);
    void validar(Empleado empleado);

    // Métodos para cálculos de planilla y búsquedas en vistas
    Empleado buscarPorNombre(String nombre);
    double calcularDescuento(Empleado e);
    double calcularNeto(Empleado e);
    double calcularValorHora(Empleado e);

    // === OPERACIONES DE BONOS ===
    List<Bono> listarBonos();
    void registrarBono(Bono bono);

    // === OPERACIONES DE DEPARTAMENTOS ===
    List<Departamento> listarDepartamentos();
    void crearDepartamento(Departamento depto);

    // === OPERACIONES DE HORAS EXTRA ===
    List<HoraExtra> listarHorasExtra();
    void registrarHoraExtra(HoraExtra horaExtra);
}