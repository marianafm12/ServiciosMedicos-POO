package Consultas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import BaseDeDatos.ConexionSQLite;

public class GuardarConsulta implements ActionListener {
    private final JTextField[] campos;
    private final JTextArea areaTexto;

    public GuardarConsulta(JTextField[] campos, JTextArea areaTexto) {
        this.campos = campos;
        this.areaTexto = areaTexto;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String idPaciente = campos[0].getText().trim();
        String nombre = campos[1].getText().trim();
        String edad = campos[2].getText().trim();
        String correo = campos[3].getText().trim();
        String diagnostico = campos[4].getText().trim();
        String observaciones = areaTexto.getText().trim();

        if (idPaciente.isEmpty() || nombre.isEmpty() || edad.isEmpty() || correo.isEmpty()
                || diagnostico.isEmpty() || observaciones.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Todos los campos deben estar completos.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!idPaciente.matches("\\d+")) {
            JOptionPane.showMessageDialog(null,
                    "El ID del paciente debe ser numérico.",
                    "ID inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = ConexionSQLite.conectar()) {
            // Verificar si existe el paciente
            String checkSql = "SELECT COUNT(*) FROM InformacionAlumno WHERE ID = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, Integer.parseInt(idPaciente));
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(null,
                            "No existe un paciente con ese ID.",
                            "Paciente no encontrado", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String insertSql = "INSERT INTO Consultas (IDPaciente, FechaConsulta, Diagnostico, Observaciones) VALUES (?, CURRENT_DATE, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, Integer.parseInt(idPaciente));
                ps.setString(2, diagnostico);
                ps.setString(3, observaciones);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(null,
                    "Consulta guardada exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiarCampos();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al guardar la consulta:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        for (JTextField campo : campos) {
            campo.setText("");
        }
        areaTexto.setText("");
    }
}
