package com.ucv.planillas.config;

import com.ucv.planillas.Service.GestionRRHHService;
import com.ucv.planillas.Service.IGestionRRHHService;
import com.ucv.planillas.Service.IUsuarioService;
import com.ucv.planillas.Service.UsuarioService;
import com.ucv.planillas.repository.GestionRRHHRepository;
import com.ucv.planillas.repository.IGestionRRHHRepository;
import com.ucv.planillas.repository.IUsuarioRepository;
import com.ucv.planillas.repository.UsuarioRepository;
import com.ucv.planillas.Module.Navegador;
import com.ucv.planillas.Module.SesionService;

import com.ucv.planillas.controller.EmpleadoController;
import com.ucv.planillas.controller.BonoController;
import com.ucv.planillas.controller.DepartamentoController;
import com.ucv.planillas.controller.HoraExtraController;
import com.ucv.planillas.controller.ShellController;
import com.ucv.planillas.controller.LoginController;


  //Contenedor de INYECCIÓN DE DEPENDENCIAS
// cosas a saber aqui: es una clase normal con un constructor publico
//MainApp crea una sola insancia una vez que se arraca la app y tofdo lo demas recibe sus dependencias
//otra cosa mas. por constructor. nadie en el proyecto accede a estado global estatico.

public class AppContext {

    // Configuración de la base de datos
    private final DataBaseConfig dataBaseConfig;

    // Repositories
    private final IGestionRRHHRepository gestionRRHHRepository;
    private final IUsuarioRepository usuarioRepository;

    // Services
    private final IGestionRRHHService gestionRRHHService;
    private final IUsuarioService usuarioService;

    private final SesionService sesionService;

    private final Navegador navegador;

    public AppContext() {
        // Conexión a SQL Server
        this.dataBaseConfig = new DataBaseConfig();

        // Empleados
        this.gestionRRHHRepository = new GestionRRHHRepository(dataBaseConfig);
        this.gestionRRHHService = new GestionRRHHService(gestionRRHHRepository);

        // Usuarios
        this.usuarioRepository = new UsuarioRepository(dataBaseConfig);
        this.usuarioService = new UsuarioService(usuarioRepository);

        // Sesión única de la aplicación
        this.sesionService = new SesionService();

        // El navegador recibe la fábrica de controladores por constructor.
        this.navegador = new Navegador(this::getController);
    }
//fab de controladores
    // se hace la injecion por constructor de todos los controladores de javafx

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
            return new ShellController(sesionService, navegador);
        }
        if (type == LoginController.class) {
            return new LoginController(usuarioService, sesionService, navegador);
        }

        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + type.getName(), e);
        }
    }

    public IGestionRRHHService getGestionRRHHService() {
        return gestionRRHHService;
    }

    public IUsuarioService getUsuarioService() {
        return usuarioService;
    }

    public SesionService getSesionService() {
        return sesionService;
    }

    public Navegador getNavegador() {
        return navegador;
    }

    public void destroy() {
        if (dataBaseConfig != null) {
            dataBaseConfig.close();
        }
    }
}
