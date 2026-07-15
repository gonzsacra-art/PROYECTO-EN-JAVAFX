package com.ucv.planillas.Util;

import com.ucv.planillas.config.AppContext;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    private AppContext appContext;

    @Override
    public void start(Stage stage) throws Exception {
        // Única creación del contenedor de dependencias
        appContext = new AppContext();

        stage.setMinWidth(750);
        stage.setMinHeight(500);
        stage.setResizable(true);

        appContext.getNavegador().irA(
                stage,
                "/com/ucv/planillas/login-view.fxml",
                "Sistema de Gestión de RRHH - UCV"
        );

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (appContext != null) {
            appContext.destroy();
        }
    }
}
