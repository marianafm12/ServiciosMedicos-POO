package GestionEnfermedades;

import BaseDeDatos.ConexionSQLite;
import javax.swing.*;
import java.sql.*;

public class EnfermedadesDB {

    public static boolean guardarEnfermedades(String id, String enf, String alg, String med) {
        String sql = "REPLACE INTO Enfermedades (idPaciente, enfermedades, alergias, medicacion) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, enf);
            ps.setString(3, alg);
            ps.setString(4, med);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar:\n" + e.getMessage());
            return false;
        }
    }

    public static EnfermedadPaciente buscarPorID(String id) {
        String sql = "SELECT * FROM Enfermedades WHERE idPaciente = ?";
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new EnfermedadPaciente(
                        rs.getString("enfermedades"),
                        rs.getString("alergias"),
                        rs.getString("medicacion"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar:\n" + e.getMessage());
        }
        return null;
    }
}
