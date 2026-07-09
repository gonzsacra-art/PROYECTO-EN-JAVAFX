module com.ucv.planillas {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.ucv.planillas to javafx.fxml;

    opens com.ucv.planillas.controller to javafx.fxml;
    exports com.ucv.planillas.controller;

    exports com.ucv.planillas.Module;
    opens com.ucv.planillas.Module to javafx.fxml;

    exports com.ucv.planillas.model;
    opens com.ucv.planillas.model to javafx.fxml;
    exports com.ucv.planillas.Util;
    opens com.ucv.planillas.Util to javafx.fxml;
}
