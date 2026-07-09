package com.ucv.planillas.Util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/ucv/planillas/login-view.fxml")
        );

        Scene escena = new Scene(loader.load());

        stage.setTitle("Sistema de Gestión de RRHH - UCV");
        stage.setScene(escena);
        stage.setMinWidth(750);
        stage.setMinHeight(500);
        stage.setResizable(true);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Cierra correctamente la conexión con la base de datos
        com.ucv.planillas.config.AppContext.getInstance().destroy();
    }

    public static void main(String[] args) {
        launch(args);
    }
}