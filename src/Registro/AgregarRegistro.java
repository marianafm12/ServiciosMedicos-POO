package Registro;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class AgregarRegistro implements ActionListener {
    private final JTextField[] campos;

    // Constructor: asigna campos de texto y agrega un listener para cargar datos al perder foco
    public AgregarRegistro(JTextField[] campos) {
        this.campos = campos;

        // Listener que se dispara cuando se pierde el foco de ciertos campos
        FocusAdapter cargaListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatosPersonales(); // Intenta cargar datos desde InformacionAlumno
            }
        };

        // Asignar listener a los campos de ID, Nombre, Apellido Paterno, Apellido Materno y Correo
        for (int i = 0; i <= 4; i++) {
            campos[i].addFocusListener(cargaListener);
        }
    }

    /**
     * Este método carga automáticamente los datos del alumno desde la tabla InformacionAlumno
     * cuando se llena el campo de ID o el conjunto de Nombre completo y Correo.
     */
    private void cargarDatosPersonales() {
        String idText = campos[0].getText().trim();
        String nom = campos[1].getText().trim();
        String apPat = campos[2].getText().trim();
        String apMat = campos[3].getText().trim();
        String correo = campos[4].getText().trim();

        String sql;
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            PreparedStatement ps;

            // Buscar por ID si es válido
            if (!idText.isEmpty() && idText.matches("\\d+")) {
                sql = "SELECT ID, Nombre, ApellidoPaterno, ApellidoMaterno, Correo "
                        + "FROM InformacionAlumno WHERE ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idText));

            // Buscar por Nombre completo y Correo si todos están llenos
            } else if (!nom.isEmpty() && !apPat.isEmpty() && !apMat.isEmpty() && !correo.isEmpty()) {
                sql = "SELECT ID, Nombre, ApellidoPaterno, ApellidoMaterno, Correo "
                        + "FROM InformacionAlumno "
                        + "WHERE Nombre = ? AND ApellidoPaterno = ? AND ApellidoMaterno = ? AND Correo = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, nom);
                ps.setString(2, apPat);
                ps.setString(3, apMat);
                ps.setString(4, correo);
            } else {
                // No hay suficientes datos para hacer la búsqueda
                return;
            }

            // Ejecutar consulta y llenar los campos si hay resultado
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

    /**
     * Este método se ejecuta al hacer clic en el botón "Agregar".
     * Valida los campos, verifica que el alumno exista y guarda el registro médico.
     */
    
     /**public boolean camposvacios(){
                // Validar que todos los campos estén llenos
        for (int i = 0; i <= 10; i++) {
            if (campos[i].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Todos los campos deben estar completos antes de guardar.",
                        "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
    }
    @Override
    public void actionPerformed(ActionEvent evento) {
        // Validar que todos los campos estén llenos
        if(camposvacios())
        return;
        }*/

    @Override
    public void actionPerformed(ActionEvent evento) {
        // Validar que todos los campos estén llenos
        for (int i = 0; i <= 10; i++) {
            if (campos[i].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Todos los campos deben estar completos antes de guardar.",
                        "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Validar que el ID sea numérico
        String idText = campos[0].getText().trim();
        if (!idText.matches("\\d+")) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero.",
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(idText);

        // Validar y convertir Edad, Altura y Peso
        int edad;
        double altura, peso;
        try {
            edad = Integer.parseInt(campos[5].getText().trim());
            altura = Double.parseDouble(campos[6].getText().trim());
            peso = Double.parseDouble(campos[7].getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Formato numérico inválido en Edad, Altura o Peso.",
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener datos médicos
        String enfPre = campos[8].getText().trim();
        String medic = campos[9].getText().trim();
        String alerg = campos[10].getText().trim();

        try (Connection conexion = BaseDeDatos.ConexionSQLite.conectar()) {
            // Verificar si el alumno existe en la tabla InformacionAlumno
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

            // Insertar datos en la tabla Registro
            String insertSQL = "INSERT INTO Registro (ID, Edad, Altura, Peso, EnfermedadesPreexistentes, Medicacion, Alergias) "
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
