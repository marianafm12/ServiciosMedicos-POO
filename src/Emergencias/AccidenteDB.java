package Emergencias;

import BaseDeDatos.ConexionSQLite;
import javax.swing.*;
import java.sql.*;

public class AccidenteDB {

    public static boolean guardarAccidente(String id, String fecha, String lugar, String descripcion, String testigos) {
        String sql = "INSERT INTO Accidentes (idEmergencia, fecha, lugar, descripcion, testigos) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, fecha);
            ps.setString(3, lugar);
            ps.setString(4, descripcion);
            ps.setString(5, testigos);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar accidente:\n" + e.getMessage());
            return false;
        }
    }

    public static Accidente buscarAccidente(String id) {
        String sql = "SELECT * FROM Accidentes WHERE idEmergencia = ?";
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Accidente(
                        rs.getString("idEmergencia"),
                        rs.getString("fecha"),
                        rs.getString("lugar"),
                        rs.getString("descripcion"),
                        rs.getString("testigos"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar accidente:\n" + e.getMessage());
        }
        return null;
    }
}
