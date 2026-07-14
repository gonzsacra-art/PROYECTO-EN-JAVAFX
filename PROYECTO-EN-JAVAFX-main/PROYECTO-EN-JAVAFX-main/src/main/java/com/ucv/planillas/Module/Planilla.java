package com.ucv.planillas.Module;

import com.ucv.planillas.model.Empleado;

// motor de calculos de la planilla
// se inyecta en el Empleado desde afuera
public class Planilla {

    private double tasaAFP = 0.12;  // 12%
    private double tasaONP = 0.13;  // 13%

    public double calcularDescuento(Empleado e) {
        if (e.getRegimen().equals("AFP")) {
            return e.getSueldo() * tasaAFP;
        } else {
            return e.getSueldo() * tasaONP;
        }
    }

    public double calcularNeto(Empleado e) {
        return e.getSueldo() - calcularDescuento(e);
    }

    // valor de la hora de trabajo, para calcular el bono de horas extra automaticamente
    // 30 dias al mes, 8 horas por dia
    public double calcularValorHora(Empleado e) {
        return e.getSueldo() / 30.0 / 8.0;
    }
}
