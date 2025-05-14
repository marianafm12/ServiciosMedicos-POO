package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditarDatosPaciente extends JFrame {
    private JTextField campoID, campoEdad, campoAltura, campoPeso, campoSintomas, campoMedicamentos, campoDiagnostico, campoFechaConsulta;
    private int idPaciente;

    public EditarDatosPaciente() {
        this(-1); // Llama al constructor con un ID inválido por defecto
    }

    public EditarDatosPaciente(int idPaciente) {
        setTitle("Editar Datos del Paciente");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(9, 2));

        // Campos para editar
        add(new JLabel("ID del Paciente:"));
        campoID = new JTextField();
        campoID.setEditable(false); // El ID no debe ser editable
        add(campoID);

        add(new JLabel("Edad:"));
        campoEdad = new JTextField();
        add(campoEdad);

        add(new JLabel("Altura:"));
        campoAltura = new JTextField();
        add(campoAltura);

        add(new JLabel("Peso:"));
        campoPeso = new JTextField();
        add(campoPeso);

        add(new JLabel("Síntomas:"));
        campoSintomas = new JTextField();
        add(campoSintomas);

        add(new JLabel("Medicamentos:"));
        campoMedicamentos = new JTextField();
        add(campoMedicamentos);

        add(new JLabel("Diagnóstico:"));
        campoDiagnostico = new JTextField();
        add(campoDiagnostico);

        add(new JLabel("Fecha de Consulta:"));
        campoFechaConsulta = new JTextField();
        add(campoFechaConsulta);

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(e -> guardarCambios());
        add(btnGuardar);

        setVisible(true);

        // Si se proporciona un ID válido, cargar los datos automáticamente
        if (idPaciente > 0) {
            this.idPaciente = idPaciente;
            cargarDatosPaciente();
        }
    }

    private void cargarDatosPaciente() {
        String queryRegistro = "SELECT Edad, Altura, Peso FROM Registro WHERE ID = ?";
        String queryConsultas = "SELECT Sintomas, Medicamentos, Diagnostico, FechaConsulta FROM Consultas WHERE IDPaciente = ? ORDER BY FechaConsulta DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Servicios medicos.db")) {
            // Cargar datos de la tabla Registro
            try (PreparedStatement stmt = conn.prepareStatement(queryRegistro)) {
                stmt.setInt(1, idPaciente);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    campoID.setText(String.valueOf(idPaciente));
                    campoEdad.setText(String.valueOf(rs.getInt("Edad")));
                    campoAltura.setText(String.valueOf(rs.getDouble("Altura")));
                    campoPeso.setText(String.valueOf(rs.getDouble("Peso")));
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontraron datos en la tabla Registro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Cargar datos de la tabla Consultas
            try (PreparedStatement stmt = conn.prepareStatement(queryConsultas)) {
                stmt.setInt(1, idPaciente);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    campoSintomas.setText(rs.getString("Sintomas"));
                    campoMedicamentos.setText(rs.getString("Medicamentos"));
                    campoDiagnostico.setText(rs.getString("Diagnostico"));
                    campoFechaConsulta.setText(rs.getString("FechaConsulta"));
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontraron datos en la tabla Consultas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarCambios() {
        String queryRegistro = "UPDATE Registro SET Edad = ?, Altura = ?, Peso = ? WHERE ID = ?";
        String queryConsultas = "UPDATE Consultas SET Sintomas = ?, Medicamentos = ?, Diagnostico = ?, FechaConsulta = ? WHERE IDPaciente = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Servicios medicos.db")) {
            // Actualizar datos en la tabla Registro
            try (PreparedStatement stmt = conn.prepareStatement(queryRegistro)) {
                stmt.setInt(1, Integer.parseInt(campoEdad.getText().trim()));
                stmt.setDouble(2, Double.parseDouble(campoAltura.getText().trim()));
                stmt.setDouble(3, Double.parseDouble(campoPeso.getText().trim()));
                stmt.setInt(4, idPaciente);

                int filasRegistro = stmt.executeUpdate();
                if (filasRegistro > 0) {
                    JOptionPane.showMessageDialog(this, "Datos de Registro actualizados correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar los datos de Registro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Actualizar datos en la tabla Consultas
            try (PreparedStatement stmt = conn.prepareStatement(queryConsultas)) {
                stmt.setString(1, campoSintomas.getText().trim());
                stmt.setString(2, campoMedicamentos.getText().trim());
                stmt.setString(3, campoDiagnostico.getText().trim());
                stmt.setString(4, campoFechaConsulta.getText().trim());
                stmt.setInt(5, idPaciente);

                int filasConsultas = stmt.executeUpdate();
                if (filasConsultas > 0) {
                    JOptionPane.showMessageDialog(this, "Datos de Consultas actualizados correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar los datos de Consultas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar los datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new EditarDatosPaciente();
    }
}