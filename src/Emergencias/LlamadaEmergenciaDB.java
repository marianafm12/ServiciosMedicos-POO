package Emergencias;

import BaseDeDatos.ConexionSQLite;
import javax.swing.*;
import java.sql.*;

public class LlamadaEmergenciaDB {

    public static boolean guardarLlamada(String idPaciente, String telefono,
            String tipoEmergencia, String gravedad,
            String descripcion, String responsable) {

        String sql = """
                    INSERT INTO LlamadasEmergencia
                    (idPaciente, telefonoContacto, tipoEmergencia, nivelGravedad, descripcion, responsable)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idPaciente);
            ps.setString(2, telefono);
            ps.setString(3, tipoEmergencia);
            ps.setString(4, gravedad);
            ps.setString(5, descripcion);
            ps.setString(6, responsable);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al guardar llamada de emergencia:\n" + e.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
