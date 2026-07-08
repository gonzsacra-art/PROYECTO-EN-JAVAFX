package com.ucv.planillas.model;

import java.time.LocalDate;

// registro de horas extra trabajadas por un empleado
public class HoraExtra {

    private String id;
    private Empleado empleado;
    private LocalDate fecha;
    private int horasTrabajadas;
    private double valorHoraBase;
    private String motivo;

    // segun la ley peruana: las primeras 2 horas se pagan al 25% extra, de ahi en adelante al 35%
    private static final double TASA_PRIMERAS_2H = 1.25;
    private static final double TASA_SIGUIENTES = 1.35;

    public HoraExtra(String id, Empleado empleado, LocalDate fecha, int horasTrabajadas,
                     double valorHoraBase, String motivo) {
        this.id = id;
        this.empleado = empleado;
        this.fecha = fecha;
        this.horasTrabajadas = horasTrabajadas;
        this.valorHoraBase = valorHoraBase;
        this.motivo = motivo;
    }

    // calcula cuanto se le paga al empleado por sus horas extra
    public double calcularPago() {
        if (horasTrabajadas <= 2) {
            return horasTrabajadas * valorHoraBase * TASA_PRIMERAS_2H;
        }
        double pagoPrimeras2 = 2 * valorHoraBase * TASA_PRIMERAS_2H;
        int horasRestantes = horasTrabajadas - 2;
        double pagoRestante = horasRestantes * valorHoraBase * TASA_SIGUIENTES;
        return pagoPrimeras2 + pagoRestante;
    }

    public String getId()
    { return id; }

    public Empleado getEmpleado()
    { return empleado; }

    public LocalDate getFecha()
    { return fecha; }

    public int getHorasTrabajadas()
    { return horasTrabajadas; }

    public double getValorHoraBase()
    { return valorHoraBase; }

    public String getMotivo()
    { return motivo; }

    @Override
    public String toString() {
        return empleado.getNombre() + " - " + horasTrabajadas + "h - S/ " + String.format("%.2f", calcularPago());
    }
}
