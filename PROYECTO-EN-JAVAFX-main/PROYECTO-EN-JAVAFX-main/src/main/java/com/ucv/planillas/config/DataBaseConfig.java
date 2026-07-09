package com.ucv.planillas.config;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DataBaseConfig implements AutoCloseable {

    private static final String URL =
            "jdbc:sqlserver://localhost:1433;"
                    + "instanceName=SQLEXPRESS;"
                    + "databaseName=BD_GestionPlanillas;"
                    + "user=admin;"
                    + "password=admin1234;"
                    + "trustServerCertificate=true;"
                    + "encrypt=false;";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    @Override
    public void close() {
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ignored) {
                }
            }
        } catch (Exception ignored) {
        }
    }
}