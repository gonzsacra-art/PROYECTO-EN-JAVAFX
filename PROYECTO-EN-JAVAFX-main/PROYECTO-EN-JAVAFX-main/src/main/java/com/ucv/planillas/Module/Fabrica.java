package com.ucv.planillas.Module;

import com.ucv.planillas.controller.*;
import javafx.util.Callback;

// PATRON de Inyeccion de Dependencias (version simple, hecha a mano)
// aqui es el UNICO lugar donde se arman los controladores con sus dependencias ya "inyectadas".
// FXMLLoader llama a call(...) cada vez que necesita crear un controlador nuevo.
public class Fabrica implements Callback<Class<?>, Object> {

    // servicios y repositorios una sola instancia para toda la app
    private final Planilla planilla = new Planilla();
    private final AutenticacionService authService = new AutenticacionService();
    private final EmpleadoRepositorio empleadoRepo = EmpleadoRepositorio.getInstancia();
    private final DepartamentoRepositorio departamentoRepo = DepartamentoRepositorio.getInstancia();
    private final BonoRepositorio bonoRepo = BonoRepositorio.getInstancia();
    private final HoraExtraRepositorio horaExtraRepo = HoraExtraRepositorio.getInstancia();

    @Override
    public Object call(Class<?> tipoControlador) {
        try {
            if (tipoControlador == LoginController.class) {
                return new LoginController(authService, this);
            }
            if (tipoControlador == ShellController.class) {
                return new ShellController(this);
            }
            if (tipoControlador == EmpleadoController.class) {
                return new EmpleadoController(empleadoRepo, planilla);
            }
            if (tipoControlador == DepartamentoController.class) {
                return new DepartamentoController(departamentoRepo, empleadoRepo);
            }
            if (tipoControlador == BonoController.class) {
                return new BonoController(bonoRepo, empleadoRepo);
            }
            if (tipoControlador == HoraExtraController.class) {
                return new HoraExtraController(horaExtraRepo, empleadoRepo, planilla);
            }
            // por si acaso aparece un controlador que no necesita dependencias
            return tipoControlador.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + tipoControlador, e);
        }
    }
}
