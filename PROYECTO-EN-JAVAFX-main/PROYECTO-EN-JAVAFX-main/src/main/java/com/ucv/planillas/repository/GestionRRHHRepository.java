package com.ucv.planillas.repository;

import com.ucv.planillas.config.DataBaseConfig;
import com.ucv.planillas.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionRRHHRepository implements IGestionRRHHRepository, AutoCloseable {

    private final DataBaseConfig dbConfig;

    public GestionRRHHRepository(DataBaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public List<Empleado> findAll() {

        String sql = "SELECT * FROM Empleados ORDER BY Nombre";

        List<Empleado> lista = new ArrayList<>();

        try (Connection cn = dbConfig.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapEmpleado(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar empleados.", e);
        }

        return lista;
    }

    @Override
    public Empleado findById(String id) {

        String sql = "SELECT * FROM Empleados WHERE Id = ?";

        try (Connection cn = dbConfig.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapEmpleado(rs);
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado.", e);
        }

        return null;
    }

    @Override
    public void save(Empleado e) {

        String sql = "INSERT INTO Empleados "
                + "(Id, Nombre, Cargo, Sueldo, Regimen) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = dbConfig.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, e.getId());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getCargo());
            ps.setDouble(4, e.getSueldo());
            ps.setString(5, e.getRegimen());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error al guardar el empleado.", ex);
        }
    }

    @Override
    public void update(Empleado e) {

        String sql = "UPDATE Empleados "
                + "SET Nombre = ?, "
                + "Cargo = ?, "
                + "Sueldo = ?, "
                + "Regimen = ? "
                + "WHERE Id = ?";

        try (Connection cn = dbConfig.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCargo());
            ps.setDouble(3, e.getSueldo());
            ps.setString(4, e.getRegimen());
            ps.setString(5, e.getId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar el empleado.", ex);
        }
    }

    @Override
    public void delete(String id) {

        String sql = "DELETE FROM Empleados WHERE Id = ?";

        try (Connection cn = dbConfig.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, id);

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar el empleado.", ex);
        }
    }

    private Empleado mapEmpleado(ResultSet rs) throws SQLException {

        return new Empleado(
                rs.getString("Id"),
                rs.getString("Nombre"),
                rs.getString("Cargo"),
                rs.getDouble("Sueldo"),
                rs.getString("Regimen"),
                null
        );

    }

    @Override
    public void close() {
        dbConfig.close();
    }
}