package com.ucv.planillas.config;

import com.ucv.planillas.Service.GestionRRHHService;
import com.ucv.planillas.Service.IGestionRRHHService;
import com.ucv.planillas.Service.IUsuarioService;
import com.ucv.planillas.Service.UsuarioService;
import com.ucv.planillas.repository.GestionRRHHRepository;
import com.ucv.planillas.repository.IGestionRRHHRepository;
import com.ucv.planillas.repository.IUsuarioRepository;
import com.ucv.planillas.repository.UsuarioRepository;

// IMPORTAMOS LOS CONTROLADORES PARA PODER INSTANCIARLOS AQUÍ
import com.ucv.planillas.controller.EmpleadoController;
import com.ucv.planillas.controller.BonoController;
import com.ucv.planillas.controller.DepartamentoController;
import com.ucv.planillas.controller.HoraExtraController;
import com.ucv.planillas.controller.ShellController;
import com.ucv.planillas.controller.LoginController;

public class AppContext {

    private static AppContext instance;

    // Configuración de la base de datos
    private final DataBaseConfig dataBaseConfig;

    // Repository de Empleados
    private final IGestionRRHHRepository gestionRRHHRepository;

    // Service de Empleados
    private final IGestionRRHHService gestionRRHHService;

    // Repository de Usuarios
    private final IUsuarioRepository usuarioRepository;

    // Service de Usuarios
    private final IUsuarioService usuarioService;

    private AppContext() {
        // Conexión a SQL Server
        dataBaseConfig = new DataBaseConfig();

        // Empleados
        gestionRRHHRepository = new GestionRRHHRepository(dataBaseConfig);
        gestionRRHHService = new GestionRRHHService(gestionRRHHRepository);

        // Usuarios
        usuarioRepository = new UsuarioRepository(dataBaseConfig);
        usuarioService = new UsuarioService(usuarioRepository);
    }

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public Object getController(Class<?> type) {
        if (type == EmpleadoController.class) {
            return new EmpleadoController(gestionRRHHService);
        }
        if (type == BonoController.class) {
            return new BonoController(gestionRRHHService);
        }
        if (type == DepartamentoController.class) {
            return new DepartamentoController(gestionRRHHService);
        }
        if (type == HoraExtraController.class) {
            return new HoraExtraController(gestionRRHHService);
        }
        if (type == ShellController.class) {
            return new ShellController();
        }
        if (type == LoginController.class) {
            return new LoginController();
        }

        // Si JavaFX pide un controlador que se crea por defecto sin parámetros
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + type.getName(), e);
        }
    }

     //Devuelve el servicio de empleados.

    public IGestionRRHHService getGestionRRHHService() {
        return gestionRRHHService;
    }
    public IUsuarioService getUsuarioService() {
        return usuarioService;
    }

    //Libera los recursos de la aplicación.

    public void destroy() {
        if (dataBaseConfig != null) {
            dataBaseConfig.close();
        }
    }
}