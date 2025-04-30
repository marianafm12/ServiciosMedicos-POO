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
        // Asignar listener a ID, Nombre, Apellido Paterno y Apellido Materno
        for (int i = 0; i <= 3; i++) {
            campos[i].addFocusListener(cargaListener);
        }
    }

    /**
     * Carga desde InformacionAlumno los datos personales.
     */
    private void cargarDatosPersonales() {
        String idText = campos[0].getText().trim();
        String nom = campos[1].getText().trim();
        String apPat = campos[2].getText().trim();
        String apMat = campos[3].getText().trim();

        String sql;
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            PreparedStatement ps;
            if (!idText.isEmpty() && idText.matches("\\d+")) {
                sql = "SELECT ID, Nombre, ApellidoPaterno, ApellidoMaterno "
                        + "FROM InformacionAlumno WHERE ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idText));

            } else if (!nom.isEmpty() && !apPat.isEmpty() && !apMat.isEmpty()) {
                sql = "SELECT ID, Nombre, ApellidoPaterno, ApellidoMaterno "
                        + "FROM InformacionAlumno "
                        + "WHERE Nombre = ? AND ApellidoPaterno = ? AND ApellidoMaterno = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, nom);
                ps.setString(2, apPat);
                ps.setString(3, apMat);

            } else {
                // No hay datos suficientes para buscar
                return;
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campos[0].setText(String.valueOf(rs.getInt("ID")));
                    campos[1].setText(rs.getString("Nombre"));
                    campos[2].setText(rs.getString("ApellidoPaterno"));
                    campos[3].setText(rs.getString("ApellidoMaterno"));
                } // Si no encuentra, no modifica los campos (podrías limpiar o avisar)
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar datos personales:\n" + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        // Validar ID
        String idText = campos[0].getText().trim();
        if (!idText.matches("\\d+")) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero.",
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(idText);

        // Validar y parsear edad, altura, peso
        int edad;
        double altura, peso;
        try {
            edad = Integer.parseInt(campos[4].getText().trim());
            altura = Double.parseDouble(campos[5].getText().trim());
            peso = Double.parseDouble(campos[6].getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Formato numérico inválido en Edad, Altura o Peso.",
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String enfPre = campos[7].getText().trim();
        String medic = campos[8].getText().trim();
        String alerg = campos[9].getText().trim();

        try (Connection conexion = BaseDeDatos.ConexionSQLite.conectar()) {
            // Comprueba que el alumno exista
            String checkSQL = "SELECT 1 FROM InformacionAlumno WHERE ID = ?";
            try (PreparedStatement psCheck = conexion.prepareStatement(checkSQL)) {
                psCheck.setInt(1, id);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "No existe un alumno con ID " + id,
                                "Error de Insertar", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // Inserta en Registro sólo los datos específicos
            String insertSQL = "INSERT INTO Registro (ID, Edad, Altura, Peso, "
                    + "EnfermedadesPreexistentes, Medicacion, Alergias) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement psIns = conexion.prepareStatement(insertSQL)) {
                psIns.setInt(1, id);
                psIns.setInt(2, edad);
                psIns.setDouble(3, altura);
                psIns.setDouble(4, peso);
                psIns.setString(5, enfPre);
                psIns.setString(6, medic);
                psIns.setString(7, alerg);

                int filas = psIns.executeUpdate();
                if (filas > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Registro guardado correctamente.",
                            "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No se pudo guardar el registro.",
                            "Error de Inserción", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error en la base de datos:\n" + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
