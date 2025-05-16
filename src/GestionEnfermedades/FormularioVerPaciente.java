package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import Utilidades.ColoresUDLAP;

/**
 * Formulario con campos de solo lectura para mostrar los datos del paciente.
 */
public class FormularioVerPaciente extends JPanel {
    private final JTextField[] campos;
    private final int idPaciente;
    private static final String DB_URL = "jdbc:sqlite:Servicios medicos.db";

    private static final String[] ETIQUETAS = {
            "ID del Paciente:", "Edad:", "Altura:", "Peso:",
            "Síntomas:", "Medicamentos:", "Diagnóstico:", "Fecha de Consulta:"
    };

    public FormularioVerPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campos = new JTextField[ETIQUETAS.length];
        for (int i = 0; i < ETIQUETAS.length; i++) {
            gbc.gridy = i;
            gbc.gridx = 0;
            JLabel lbl = new JLabel(ETIQUETAS[i]);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            lbl.setForeground(ColoresUDLAP.NEGRO);
            add(lbl, gbc);

            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            campos[i].setEditable(false);
            campos[i].setBorder(BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO));
            add(campos[i], gbc);
        }

        cargarDatos();
    }

    /**
     * Carga los datos de las tablas Registro y Consultas para el paciente.
     * Muestra mensajes de error si no encuentra registros.
     */
    private void cargarDatos() {
        String sqlReg = "SELECT Edad, Altura, Peso FROM Registro WHERE ID = ?";
        String sqlCon = "SELECT Sintomas, Medicamentos, Diagnostico, FechaConsulta " +
                "FROM Consultas WHERE IDPaciente = ? ORDER BY FechaConsulta DESC LIMIT 1";
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            // Datos de Registro
            try (PreparedStatement ps = c.prepareStatement(sqlReg)) {
                ps.setInt(1, idPaciente);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    campos[0].setText(String.valueOf(idPaciente));
                    campos[1].setText(String.valueOf(rs.getInt("Edad")));
                    campos[2].setText(String.valueOf(rs.getDouble("Altura")));
                    campos[3].setText(String.valueOf(rs.getDouble("Peso")));
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se encontraron datos en la tabla Registro.",
                            "Datos no encontrados", JOptionPane.WARNING_MESSAGE);
                }
            }
            // Datos de Consultas
            try (PreparedStatement ps = c.prepareStatement(sqlCon)) {
                ps.setInt(1, idPaciente);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    campos[4].setText(rs.getString("Sintomas"));
                    campos[5].setText(rs.getString("Medicamentos"));
                    campos[6].setText(rs.getString("Diagnostico"));
                    campos[7].setText(rs.getString("FechaConsulta"));
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se encontraron datos en la tabla Consultas.",
                            "Datos no encontrados", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }
}
