module com.ucv.planillas {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.ucv.planillas to javafx.fxml;
    exports com.ucv.planillas;

    opens com.ucv.planillas.controller to javafx.fxml;
    exports com.ucv.planillas.controller;

    exports com.ucv.planillas.service;
    opens com.ucv.planillas.service to javafx.fxml;

    exports com.ucv.planillas.model;
    opens com.ucv.planillas.model to javafx.fxml;
}
