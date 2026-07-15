package com.ucv.planillas.Module;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;


  //esto Se encarga de cargar vistas FXML conectándolas al contenedor de dependencias.

public class Navegador {

    private final Callback<Class<?>, Object> controllerFactory;

    public Navegador(Callback<Class<?>, Object> controllerFactory) {
        this.controllerFactory = controllerFactory;
    }


    public Parent cargarVista(String rutaFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(rutaFxml));
        loader.setControllerFactory(controllerFactory);
        return loader.load();
    }
    public void irA(Stage stage, String rutaFxml, String titulo) throws IOException {
        Scene escena = new Scene(cargarVista(rutaFxml));
        stage.setScene(escena);
        stage.setTitle(titulo);
    }
    public void cambiarRaiz(Stage stage, String rutaFxml, String titulo) throws IOException {
        stage.getScene().setRoot(cargarVista(rutaFxml));
        stage.setTitle(titulo);
    }
}
