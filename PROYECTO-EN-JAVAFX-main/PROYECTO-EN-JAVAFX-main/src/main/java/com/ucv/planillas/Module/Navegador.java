package com.ucv.planillas.Module;

import com.ucv.planillas.config.AppContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Navegador {

    public static void irA(Stage stage, String rutaFxml, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(rutaFxml));

        // CONECTAMOS CON APPCONTEXT PARA LA INYECCIÓN AUTOMÁTICA DE DEPENDENCIAS
        loader.setControllerFactory(type -> AppContext.getInstance().getController(type));

        Scene escena = new Scene(loader.load());
        stage.setScene(escena);
        stage.setTitle(titulo);
    }
}