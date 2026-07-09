module com.ucv.planillas {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Paquete principal
    opens com.ucv.planillas to javafx.fxml;

    // Controladores
    exports com.ucv.planillas.controller;
    opens com.ucv.planillas.controller to javafx.fxml;

    // Modelos
    exports com.ucv.planillas.model;
    opens com.ucv.planillas.model to javafx.fxml;

    // Configuración
    exports com.ucv.planillas.config;

    // Repositorios
    exports com.ucv.planillas.repository;

    // Servicios
    exports com.ucv.planillas.Service;

    // Módulos
    exports com.ucv.planillas.Module;
    opens com.ucv.planillas.Module to javafx.fxml;

    // Utilidades
    exports com.ucv.planillas.Util;
    opens com.ucv.planillas.Util to javafx.fxml;
}