package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import Utilidades.ColoresUDLAP;

public class FormularioEditarPaciente extends JPanel {
    private final JTextField[] campos;
    private final int idPaciente;
    private static final String DB_URL = "jdbc:sqlite:Servicios medicos.db";

    private static final String[] ETIQUETAS = {
            "ID del Paciente:", "Edad:", "Altura:", "Peso:",
            "Síntomas:", "Medicamentos:", "Diagnóstico:", "Fecha de Consulta:"
    };

    public FormularioEditarPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titulo = new JLabel("Editar Datos del Paciente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(ColoresUDLAP.VERDE_SOLIDO);
        add(titulo, gbc);
        gbc.gridwidth = 1;

        // Campos de formulario
        campos = new JTextField[ETIQUETAS.length];
        for (int i = 0; i < ETIQUETAS.length; i++) {
            gbc.gridy = i + 1;
            gbc.gridx = 0;
            JLabel lbl = new JLabel(ETIQUETAS[i]);
            lbl.setFont(labelFont);
            lbl.setForeground(ColoresUDLAP.NEGRO);
            add(lbl, gbc);

            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            campos[i].setFont(fieldFont);
            campos[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            if (i == 0) {
                campos[i].setEditable(false);
            }
            add(campos[i], gbc);
        }

        // Relleno al final
        gbc.gridy = ETIQUETAS.length + 1;
        gbc.weighty = 0.2;
        add(Box.createGlue(), gbc);

        // Carga inicial de datos si es ID válido
        if (idPaciente > 0) {
            cargarDatos();
        }
    }

    private void cargarDatos() {
        String sqlReg = "SELECT Edad, Altura, Peso FROM Registro WHERE ID = ?";
        String sqlCon = "SELECT Sintomas, Medicamentos, Diagnostico, FechaConsulta "
                + "FROM Consultas WHERE IDPaciente = ? "
                + "ORDER BY FechaConsulta DESC LIMIT 1";
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            // Registro
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
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Consultas
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
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void guardarDatos() {
        String sqlReg = "UPDATE Registro SET Edad = ?, Altura = ?, Peso = ? WHERE ID = ?";
        String sqlCon = "UPDATE Consultas SET Sintomas = ?, Medicamentos = ?, Diagnostico = ?, FechaConsulta = ? "
                + "WHERE IDPaciente = ?";
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            // Actualizar Registro
            try (PreparedStatement ps = c.prepareStatement(sqlReg)) {
                ps.setInt(1, Integer.parseInt(campos[1].getText().trim()));
                ps.setDouble(2, Double.parseDouble(campos[2].getText().trim()));
                ps.setDouble(3, Double.parseDouble(campos[3].getText().trim()));
                ps.setInt(4, idPaciente);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Registro actualizado.");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo actualizar Registro.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Actualizar Consultas
            try (PreparedStatement ps = c.prepareStatement(sqlCon)) {
                ps.setString(1, campos[4].getText().trim());
                ps.setString(2, campos[5].getText().trim());
                ps.setString(3, campos[6].getText().trim());
                ps.setString(4, campos[7].getText().trim());
                ps.setInt(5, idPaciente);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Consultas actualizadas.");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo actualizar Consultas.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
