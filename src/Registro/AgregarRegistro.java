package Registro;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class AgregarRegistro implements ActionListener {
    private final JTextField[] campos;

    public AgregarRegistro(JTextField[] campos) {
        this.campos = campos;

        FocusAdapter cargaListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatosPersonales();
            }
        };

        for (int i = 0; i <= 4; i++) {
            campos[i].addFocusListener(cargaListener);
        }
    }

    private void cargarDatosPersonales() {
        String idText = campos[0].getText().trim();
        String nom = campos[1].getText().trim();
        String apPat = campos[2].getText().trim();
        String apMat = campos[3].getText().trim();
        String correo = campos[4].getText().trim();

        String sql;
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            PreparedStatement ps;

            if (!idText.isEmpty() && idText.matches("\\d+")) {
                sql = "SELECT ID, Nombre, ApellidoPaterno, ApellidoMaterno, Correo FROM InformacionAlumno WHERE ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idText));
            } else if (!nom.isEmpty() && !apPat.isEmpty() && !apMat.isEmpty() && !correo.isEmpty()) {
                sql = "SELECT ID, Nombre, ApellidoPaterno, ApellidoMaterno, Correo FROM InformacionAlumno "
                    + "WHERE Nombre = ? AND ApellidoPaterno = ? AND ApellidoMaterno = ? AND Correo = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, nom);
                ps.setString(2, apPat);
                ps.setString(3, apMat);
                ps.setString(4, correo);
            } else {
                return;
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campos[0].setText(String.valueOf(rs.getInt("ID")));
                    campos[1].setText(rs.getString("Nombre"));
                    campos[2].setText(rs.getString("ApellidoPaterno"));
                    campos[3].setText(rs.getString("ApellidoMaterno"));
                    campos[4].setText(rs.getString("Correo"));
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Error al cargar datos personales:\n" + ex.getMessage(),
                "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = campos[0].getText().trim();
        String nombre = campos[1].getText().trim();
        String apellidoP = campos[2].getText().trim();
        String apellidoM = campos[3].getText().trim();
        String correo = campos[4].getText().trim();
        String edad = campos[5].getText().trim();
        String altura = campos[6].getText().trim();
        String peso = campos[7].getText().trim();
        String enfermedades = campos[8].getText().trim();
        String medicacion = campos[9].getText().trim();
        String alergias = campos[10].getText().trim();

        if (!ValidadorPaciente.esIDValido(id)) {
            JOptionPane.showMessageDialog(null, "El ID debe estar entre 180000 y 999999.");
            return;
        }

        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT ID FROM InformacionAlumno WHERE ID = ?");
            checkStmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "El ID ingresado ya está registrado.");
                return;
            }

            if (!ValidadorPaciente.esTextoAlfabetico(nombre) || 
                !ValidadorPaciente.esTextoAlfabetico(apellidoP) || 
                !ValidadorPaciente.esTextoAlfabetico(apellidoM)) {
                JOptionPane.showMessageDialog(null, "Nombre y apellidos deben contener solo letras.");
                return;
            }

            if (!ValidadorPaciente.esCorreoValido(correo)) {
                JOptionPane.showMessageDialog(null, "Correo no válido. Use el formato usuario@dominio.com");
                return;
            }

            if (!ValidadorPaciente.esNumerico(edad) || 
                !ValidadorPaciente.esNumerico(altura) || 
                !ValidadorPaciente.esNumerico(peso)) {
                JOptionPane.showMessageDialog(null, "Edad, altura y peso deben ser valores numéricos.");
                return;
            }

            if (!ValidadorPaciente.esAlfanumerico(enfermedades) ||
                !ValidadorPaciente.esAlfanumerico(medicacion) ||
                !ValidadorPaciente.esAlfanumerico(alergias)) {
                JOptionPane.showMessageDialog(null, "Los campos médicos solo deben contener letras, números y signos básicos.");
                return;
            }

            // Inserción en InformacionAlumno
            PreparedStatement insertAlumno = conn.prepareStatement(
                "INSERT INTO InformacionAlumno (ID, Nombre, ApellidoPaterno, ApellidoMaterno, Correo, Contraseña) " +
                "VALUES (?, ?, ?, ?, ?, ?)");
            insertAlumno.setInt(1, Integer.parseInt(id));
            insertAlumno.setString(2, nombre);
            insertAlumno.setString(3, apellidoP);
            insertAlumno.setString(4, apellidoM);
            insertAlumno.setString(5, correo);
            insertAlumno.setString(6, "pass" + id);
            insertAlumno.executeUpdate();

            // Inserción en Registro
            PreparedStatement insertRegistro = conn.prepareStatement(
                "INSERT INTO Registro (ID, Edad, Altura, Peso, EnfermedadesPreexistentes, Medicacion, Alergias) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)");
            insertRegistro.setInt(1, Integer.parseInt(id));
            insertRegistro.setInt(2, Integer.parseInt(edad));
            insertRegistro.setDouble(3, Double.parseDouble(altura));
            insertRegistro.setDouble(4, Double.parseDouble(peso));
            insertRegistro.setString(5, enfermedades);
            insertRegistro.setString(6, medicacion);
            insertRegistro.setString(7, alergias);
            insertRegistro.executeUpdate();

            JOptionPane.showMessageDialog(null, "Paciente registrado exitosamente.");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar al paciente:\n" + ex.getMessage());
        }
    }
}
