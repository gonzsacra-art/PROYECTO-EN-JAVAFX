package com.ucv.planillas.repository;

import com.ucv.planillas.config.DataBaseConfig;
import com.ucv.planillas.model.Rol;
import com.ucv.planillas.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepository implements IUsuarioRepository {

    private final DataBaseConfig dbConfig;

    public UsuarioRepository(DataBaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public Usuario login(String username, String password) {

        String sql = "SELECT Usuario, Contrasena, Rol FROM Usuarios "
                + "WHERE Usuario = ? AND Contrasena = ?";

        try (Connection cn = dbConfig.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Rol rol = Rol.valueOf(rs.getString("Rol").toUpperCase());

                    return new Usuario(
                            rs.getString("Usuario"),
                            rs.getString("Contrasena"),
                            rs.getString("Usuario"),
                            rol
                    );
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al iniciar sesión.", e);
        }

        return null;
    }
}