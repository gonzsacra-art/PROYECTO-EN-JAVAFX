package com.ucv.planillas.service;

import com.ucv.planillas.model.Bono;

import java.util.ArrayList;

// PATRON SINGLETON - lista compartida de bonos registrados
public class BonoRepositorio {

    private static BonoRepositorio instancia;
    private ArrayList<Bono> bonos = new ArrayList<>();
    private int contador = 1;

    private BonoRepositorio() {
    }

    public static BonoRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new BonoRepositorio();
        }
        return instancia;
    }

    public ArrayList<Bono> getBonos() {
        return bonos;
    }

    public void agregar(Bono b) {
        bonos.add(b);
    }

    public String siguienteId() {
        return "B0" + (contador++);
    }
}
