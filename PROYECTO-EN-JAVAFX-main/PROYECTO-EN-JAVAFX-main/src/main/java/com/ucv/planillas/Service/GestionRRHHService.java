package com.ucv.planillas.Service;

import com.ucv.planillas.model.Empleado;
import com.ucv.planillas.model.Bono;
import com.ucv.planillas.model.Departamento;
import com.ucv.planillas.model.HoraExtra;
import com.ucv.planillas.Module.Planilla;
import com.ucv.planillas.repository.IGestionRRHHRepository;

import java.util.ArrayList;
import java.util.List;

public class GestionRRHHService implements IGestionRRHHService {

    private final IGestionRRHHRepository repository;
    private final Planilla planillaEngine; // Motor de cálculos integrado

    // Listas en memoria temporales para módulos secundarios
    private final List<Bono> listaBonos = new ArrayList<>();
    private final List<Departamento> listaDepartamentos = new ArrayList<>();
    private final List<HoraExtra> listaHorasExtra = new ArrayList<>();

    public GestionRRHHService(IGestionRRHHRepository repository) {
        this.repository = repository;
        this.planillaEngine = new Planilla();
    }

    // === OPERACIONES DE EMPLEADOS ===
    @Override
    public List<Empleado> listar() {
        List<Empleado> emps = repository.findAll();
        // Le inyectamos el motor de planilla a cada empleado que viene de la BD
        for (Empleado e : emps) {
            e.setPlanilla(planillaEngine);
        }
        return emps;
    }

    @Override
    public Empleado buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del empleado es obligatorio.");
        }
        Empleado e = repository.findById(id);
        if (e != null) e.setPlanilla(planillaEngine);
        return e;
    }

    @Override
    public Empleado buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return null;
        for (Empleado e : listar()) {
            if (e.getNombre().equalsIgnoreCase(nombre)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void crear(Empleado empleado) {
        validar(empleado);
        repository.save(empleado);
    }

    @Override
    public void actualizar(Empleado empleado) {
        validar(empleado);
        repository.update(empleado);
    }

    @Override
    public void eliminar(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el ID del empleado.");
        }
        repository.delete(id);
    }

    // === MÉTODOS DE CÁLCULO (DELEGADOS A PLANILLA) ===
    @Override
    public double calcularDescuento(Empleado e) {
        return planillaEngine.calcularDescuento(e);
    }

    @Override
    public double calcularNeto(Empleado e) {
        return planillaEngine.calcularNeto(e);
    }

    @Override
    public double calcularValorHora(Empleado e) {
        return planillaEngine.calcularValorHora(e);
    }

    // === OPERACIONES DE BONOS ===
    @Override
    public List<Bono> listarBonos() {
        return listaBonos;
    }

    @Override
    public void registrarBono(Bono bono) {
        listaBonos.add(bono);
    }

    // === OPERACIONES DE DEPARTAMENTOS ===
    @Override
    public List<Departamento> listarDepartamentos() {
        return listaDepartamentos;
    }

    @Override
    public void crearDepartamento(Departamento depto) {
        listaDepartamentos.add(depto);
    }

    // === OPERACIONES DE HORAS EXTRA ===
    @Override
    public List<HoraExtra> listarHorasExtra() {
        return listaHorasExtra;
    }

    @Override
    public void registrarHoraExtra(HoraExtra horaExtra) {
        listaHorasExtra.add(horaExtra);
    }

    @Override
    public void validar(Empleado e) {
        if (e.getId() == null || e.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del empleado es obligatorio.");
        }
        if (e.getNombre() == null || e.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del empleado es obligatorio.");
        }
        if (e.getSueldo() <= 0) {
            throw new IllegalArgumentException("El sueldo debe ser mayor que cero.");
        }
        if (e.getRegimen() == null || e.getRegimen().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar el régimen (AFP u ONP).");
        }
        if (!e.getRegimen().equalsIgnoreCase("AFP") && !e.getRegimen().equalsIgnoreCase("ONP")) {
            throw new IllegalArgumentException("El régimen debe ser AFP u ONP.");
        }
    }
}