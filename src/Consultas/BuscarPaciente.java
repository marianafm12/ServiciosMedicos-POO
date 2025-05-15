package Consultas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import BaseDeDatos.ConexionSQLite;
import java.sql.*;

public class BuscarPaciente implements ActionListener {
    private final JTextField[] campos;

    public BuscarPaciente(JTextField[] campos) {
        this.campos = campos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String idText = campos[0].getText().trim();
        String nombreComp = campos[1].getText().trim();

        try (Connection conn = ConexionSQLite.conectar()) {
            if (idText.matches("\\d+")) {
                buscarPorID(conn, Integer.parseInt(idText));
            } else if (!nombreComp.isBlank()) {
                buscarPorNombre(conn, nombreComp);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al buscar paciente:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorID(Connection conn, int id) throws SQLException {
        String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno, Correo, Edad FROM InformacionAlumno WHERE ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campos[1].setText(rs.getString("Nombre") + " " +
                            rs.getString("ApellidoPaterno") + " " +
                            rs.getString("ApellidoMaterno"));
                    campos[2].setText(rs.getString("Edad"));
                    campos[3].setText(rs.getString("Correo"));
                } else {
                    JOptionPane.showMessageDialog(null, "Paciente no encontrado", "Búsqueda",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void buscarPorNombre(Connection conn, String nombreComp) throws SQLException {
        String[] parts = nombreComp.split("\\s+", 3);
        if (parts.length < 3)
            return;

        String sql = "SELECT ID, Correo, Edad FROM InformacionAlumno WHERE Nombre = ? AND ApellidoPaterno = ? AND ApellidoMaterno = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, parts[0]);
            ps.setString(2, parts[1]);
            ps.setString(3, parts[2]);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campos[0].setText(String.valueOf(rs.getInt("ID")));
                    campos[2].setText(rs.getString("Edad"));
                    campos[3].setText(rs.getString("Correo"));
                } else {
                    JOptionPane.showMessageDialog(null, "Paciente no encontrado", "Búsqueda",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}