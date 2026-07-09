package com.ucv.planillas.repository;

import com.ucv.planillas.model.Empleado;

import java.util.List;

public interface IGestionRRHHRepository {

    List<Empleado> findAll();

    Empleado findById(String id);

    void save(Empleado empleado);

    void update(Empleado empleado);

    void delete(String id);

}