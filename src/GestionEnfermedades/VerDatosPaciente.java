package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VerDatosPaciente extends JFrame {
    private JTextField campoID, campoEdad, campoAltura, campoPeso, campoSintomas, campoMedicamentos, campoDiagnostico, campoFechaConsulta;
    private int idPaciente;

    public VerDatosPaciente(int idPaciente) {
        this.idPaciente = idPaciente; // Guardar el ID del paciente

        setTitle("Datos del Paciente");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(9, 2));

        // Campos para mostrar datos
        add(new JLabel("ID del Paciente:"));
        campoID = new JTextField(String.valueOf(idPaciente));
        campoID.setEditable(false); // El ID no debe ser editable
        add(campoID);

        add(new JLabel("Edad:"));
        campoEdad = new JTextField();
        campoEdad.setEditable(false); // Solo lectura
        add(campoEdad);

        add(new JLabel("Altura:"));
        campoAltura = new JTextField();
        campoAltura.setEditable(false); // Solo lectura
        add(campoAltura);

        add(new JLabel("Peso:"));
        campoPeso = new JTextField();
        campoPeso.setEditable(false); // Solo lectura
        add(campoPeso);

        add(new JLabel("Síntomas:"));
        campoSintomas = new JTextField();
        campoSintomas.setEditable(false); // Solo lectura
        add(campoSintomas);

        add(new JLabel("Medicamentos:"));
        campoMedicamentos = new JTextField();
        campoMedicamentos.setEditable(false); // Solo lectura
        add(campoMedicamentos);

        add(new JLabel("Diagnóstico:"));
        campoDiagnostico = new JTextField();
        campoDiagnostico.setEditable(false); // Solo lectura
        add(campoDiagnostico);

        add(new JLabel("Fecha de Consulta:"));
        campoFechaConsulta = new JTextField();
        campoFechaConsulta.setEditable(false); // Solo lectura
        add(campoFechaConsulta);

        cargarDatosPaciente();

        setVisible(true);
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
}