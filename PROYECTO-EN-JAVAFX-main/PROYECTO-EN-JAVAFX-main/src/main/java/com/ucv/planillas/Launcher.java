package com.ucv.planillas;

import com.ucv.planillas.service.Fabrica;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // la Fabrica es el contenedor de inyeccion de dependencias de toda la app
        Fabrica fabrica = new Fabrica();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/planillas/login-view.fxml"));
        loader.setControllerFactory(fabrica);

        Scene escena = new Scene(loader.load());

        stage.setTitle("Sistema de Gestion de RRHH - UCV");
        stage.setScene(escena);
        stage.setMinWidth(750);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
