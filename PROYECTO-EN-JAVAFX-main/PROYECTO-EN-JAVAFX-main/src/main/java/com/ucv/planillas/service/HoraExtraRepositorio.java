package com.ucv.planillas.service;

import com.ucv.planillas.model.HoraExtra;

import java.util.ArrayList;

// PATRON SINGLETON
public class HoraExtraRepositorio {

    private static HoraExtraRepositorio instancia;
    private ArrayList<HoraExtra> registros = new ArrayList<>();
    private int contador = 1;

    private HoraExtraRepositorio() {
    }

    public static HoraExtraRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new HoraExtraRepositorio();
        }
        return instancia;
    }

    public ArrayList<HoraExtra> getRegistros() {
        return registros;
    }

    public void agregar(HoraExtra h) {
        registros.add(h);
    }

    public String siguienteId() {
        return "HE0" + (contador++);
    }
}
