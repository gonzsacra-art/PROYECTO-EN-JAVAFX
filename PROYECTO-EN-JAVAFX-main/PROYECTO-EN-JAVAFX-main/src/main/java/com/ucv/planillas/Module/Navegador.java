package com.ucv.planillas.Module;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Navegador {

    public static void irA(Stage stage, String rutaFxml, String titulo, Fabrica fabrica) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(rutaFxml));
        loader.setControllerFactory(fabrica);
        Scene escena = new Scene(loader.load());
        stage.setScene(escena);
        stage.setTitle(titulo);
    }
}
