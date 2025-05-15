package Consultas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsultasFrame extends JFrame {
    private static int idMedico;
    private static String nombreMedico;

    public static void setMedico(int id, String nombre) {
        idMedico = id;
        nombreMedico = nombre;
    }

    public static void launchApp() {
        SwingUtilities.invokeLater(
                () -> new PatientForm(idMedico, nombreMedico));
    }
}

class PatientForm extends JFrame {
    // Identificador del médico que inició sesión
    private final int idMedico;

    // Campos de formulario
    private final JTextField IDPacienteField;
    private final JTextField NombrePacienteField;
    private final JTextField ageField;
    private final JTextField emailField;
    private final JTextField symptomsField;
    private final JTextField medsField;
    private final JTextField diagnosisField;
    private final JTextField IDExpedienteMedicoField;
    private final JTextField dateField;
    private final JTextField lastVisitField;
    private final JTextField symptomStartField;
    private final JTextArea prescriptionField;
    private final JButton saveButton;

    public PatientForm(int idMedico, String medicoNombre) {
        this.idMedico = idMedico;

        setTitle("Formulario del Paciente - Dr. " + medicoNombre);
        setSize(700, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // Título
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel headerLabel = new JLabel("Formulario del Paciente", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(headerLabel, gbc);

        gbc.gridwidth = 1;

        // ID Paciente
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("ID Paciente:"), gbc);
        gbc.gridx = 1;
        IDPacienteField = new JTextField(15);
        add(IDPacienteField, gbc);

        // Nombre Completo del Paciente
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("Nombre Completo del Paciente:"), gbc);
        gbc.gridx = 1;
        NombrePacienteField = new JTextField(15);
        add(NombrePacienteField, gbc);

        // Edad
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(15);
        add(ageField, gbc);

        // Correo Electrónico
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("Correo Electrónico:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        emailField.setEditable(false);
        add(emailField, gbc);

        // Síntomas
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("Síntomas:"), gbc);
        gbc.gridx = 1;
        symptomsField = new JTextField(15);
        add(symptomsField, gbc);

        // Medicamentos
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("Medicamento(s) administrado(s):"), gbc);
        gbc.gridx = 1;
        medsField = new JTextField(15);
        add(medsField, gbc);

        // Diagnóstico
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("Diagnóstico:"), gbc);
        gbc.gridx = 1;
        diagnosisField = new JTextField(15);
        add(diagnosisField, gbc);

        // ID Expediente (no editable, se autoincrementa en BD)
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("ID Expediente Médico:"), gbc);
        gbc.gridx = 1;
        IDExpedienteMedicoField = new JTextField(15);
        IDExpedienteMedicoField.setEditable(false);
        add(IDExpedienteMedicoField, gbc);

        // Fecha de la consulta
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("Fecha (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        dateField = new JTextField(15);
        add(dateField, gbc);

        // Última Consulta
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("Última Consulta (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        lastVisitField = new JTextField(15);
        add(lastVisitField, gbc);

        // Inicio de síntomas
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel("Fecha de inicio de síntomas (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        symptomStartField = new JTextField(15);
        add(symptomStartField, gbc);

        // Receta Médica
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(new JLabel("Receta Médica:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        prescriptionField = new JTextArea(5, 15);
        JScrollPane scrollPane = new JScrollPane(
                prescriptionField,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, gbc);

        // Botón Guardar
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        saveButton = new JButton("Guardar Datos");
        add(saveButton, gbc);

        // Tecla Enter dispara el guardado
        getRootPane().setDefaultButton(saveButton);

        // Listener para búsqueda de paciente
        FocusAdapter pacienteListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatosPaciente();
            }
        };
        IDPacienteField.addFocusListener(pacienteListener);
        NombrePacienteField.addFocusListener(pacienteListener);

        // Acción de guardar: primero inserta en BD, luego muestra confirmación
        saveButton.addActionListener(e -> {
            if (validateFields()) {
                insertarConsulta();
                showConfirmationScreen();
            }
        });

        setVisible(true);

        // Al arrancar, crear la tabla Consultas si no existe
        crearTablaConsultas();
    }

    /** Crea la tabla Consultas en SQLite si no existe */
    private void crearTablaConsultas() {
        String ddl = "CREATE TABLE IF NOT EXISTS Consultas (IDExpedienteMedico INTEGER PRIMARY KEY AUTOINCREMENT, IDPaciente INTEGER NOT NULL, IDMedico INTEGER NOT NULL, Sintomas TEXT NOT NULL, Medicamentos TEXT NOT NULL, Diagnostico TEXT NOT NULL, FechaConsulta DATETIME NOT NULL, FechaUltimaConsulta DATETIME NOT NULL, FechaInicioSintomas DATETIME NOT NULL, Receta TEXT NOT NULL, FOREIGN KEY(IDPaciente) REFERENCES InformacionAlumno(ID), FOREIGN KEY(IDMedico) REFERENCES InformacionMedico(ID) ); ";
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                Statement st = conn.createStatement()) {
            st.execute(ddl);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear tabla Consultas:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Intenta cargar el paciente por ID o por nombre completo */
    private void cargarDatosPaciente() {
        String idText = IDPacienteField.getText().trim();
        String nombreComp = NombrePacienteField.getText().trim();

        String sql;
        PreparedStatement ps = null;
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            if (idText.matches("\\d+")) {
                sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno, Correo FROM InformacionAlumno WHERE ID = ? ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idText));

            } else if (!nombreComp.isBlank()) {
                // Dividir nombre completo en 3 tokens
                String[] parts = nombreComp.split("\\s+", 3);
                if (parts.length < 3)
                    return;
                sql = " SELECT ID, Correo FROM InformacionAlumno WHERE Nombre = ? AND ApellidoPaterno = ? AND ApellidoMaterno = ? ";
                ps = conn.prepareStatement(sql);
                ps.setString(1, parts[0]);
                ps.setString(2, parts[1]);
                ps.setString(3, parts[2]);
            } else {
                return;
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si vino por nombre, rellenar ID
                    if (!idText.matches("\\d+")) {
                        IDPacienteField.setText(String.valueOf(rs.getInt("ID")));
                    }
                    // Nombre completo + correo
                    NombrePacienteField.setText(nombreComp);
                    emailField.setText(rs.getString("Correo"));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar paciente:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Inserta la nueva consulta en la tabla Consultas */
    private void insertarConsulta() {
        String sql = "INSERT INTO Consultas (IDPaciente, IDMedico, Sintomas, Medicamentos, Diagnostico, FechaConsulta, FechaUltimaConsulta, FechaInicioSintomas, Receta) VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(IDPacienteField.getText().trim()));
            ps.setInt(2, idMedico);
            ps.setString(3, symptomsField.getText().trim());
            ps.setString(4, medsField.getText().trim());
            ps.setString(5, diagnosisField.getText().trim());

            // Parsear fechas dd/MM/yyyy → yyyy-MM-dd HH:mm:ss
            SimpleDateFormat srcFmt = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat dstFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fechaC = srcFmt.parse(dateField.getText().trim());
            Date lastV = srcFmt.parse(lastVisitField.getText().trim());
            Date sympStart = srcFmt.parse(symptomStartField.getText().trim());
            ps.setString(6, dstFmt.format(fechaC));
            ps.setString(7, dstFmt.format(lastV));
            ps.setString(8, dstFmt.format(sympStart));

            ps.setString(9, prescriptionField.getText().trim());

            ps.executeUpdate();
        } catch (SQLException | ParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar consulta:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    // VALIDACIONES
    public boolean validateFields() {
        StringBuilder errores = new StringBuilder();

        if (IDPacienteField.getText().isEmpty() || NombrePacienteField.getText().isEmpty()
                || ageField.getText().isEmpty() ||
                symptomsField.getText().isEmpty() || medsField.getText().isEmpty() ||
                diagnosisField.getText().isEmpty() ||
                dateField.getText().isEmpty() ||
                lastVisitField.getText().isEmpty() || symptomStartField.getText().isEmpty()
                || prescriptionField.getText().isEmpty()) {
            errores.append("- No puedes dejar campos vacíos.\n");
        }

        if (!isInteger(IDPacienteField.getText()))
            errores.append("- El ID del paciente debe ser un número entero.\n");
        // if (!isInteger(IDExpedienteMedicoField.getText()))
        // errores.append("- El ID del expediente médico debe ser un número entero.\n");
        if (!isInteger(ageField.getText()))
            errores.append("- La edad debe ser un número entero.\n");

        if (!isText(NombrePacienteField.getText()))
            errores.append("- El nombre del paciente solo debe contener letras.\n");
        if (!isText(diagnosisField.getText()))
            errores.append("- El diagnóstico solo debe contener letras.\n");

        // if (!isValidEmail(emailField.getText()))
        // errores.append("- Ingresa un correo válido (ejemplo@dominio.com).\n");

        if (!isValidDate(dateField.getText()))
            errores.append("- La fecha debe estar en formato dd/MM/yyyy.\n");
        if (!isValidDate(lastVisitField.getText()))
            errores.append("- La fecha de última consulta debe estar en formato dd/MM/yyyy.\n");
        if (!isValidDate(symptomStartField.getText()))
            errores.append("- La fecha de inicio de síntomas debe estar en formato dd/MM/yyyy.\n");

        if (errores.length() > 0) {
            showError("Errores detectados:\n" + errores.toString());
            return false;
        }

        return true;
    }

    public boolean isInteger(String str) {
        return str.matches("\\d+");
    }

    public boolean isText(String str) {
        return str.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }

    public boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$");
    }

    public boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showConfirmationScreen() {
        JFrame confirmationFrame = new JFrame("Confirmación de Datos");
        confirmationFrame.setSize(400, 500);
        confirmationFrame.setLayout(new BorderLayout());

        String email = emailField.getText();

        JTextArea confirmationText = new JTextArea();
        confirmationText.setEditable(false);
        confirmationText.setText("CONFIRMACIÓN DE DATOS:\n\n"
                + "ID Paciente: " + IDPacienteField.getText() + "\n"
                + "Nombre del Paciente: " + NombrePacienteField.getText() + "\n"
                + "Edad: " + ageField.getText() + "\n"
                + "Correo Electrónico: " + email + "\n"
                + "Síntomas: " + symptomsField.getText() + "\n"
                + "Medicamentos: " + medsField.getText() + "\n"
                + "Diagnóstico: " + diagnosisField.getText() + "\n"
                + "ID Expediente Médico: " + IDExpedienteMedicoField.getText() + "\n"
                + "Fecha: " + dateField.getText() + "\n"
                + "Última Consulta: " + lastVisitField.getText() + "\n"
                + "Fecha de inicio de síntomas: " + symptomStartField.getText() + "\n"
                + "Receta Médica: " + prescriptionField.getText());

        confirmationFrame.add(new JScrollPane(confirmationText), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Confirmar");
        JButton editButton = new JButton("Regresar y Editar");

        buttonPanel.add(confirmButton);
        buttonPanel.add(editButton);
        confirmationFrame.add(buttonPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(confirmationFrame, "📩 La receta médica ha sido enviada al correo: " + email,
                    "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            confirmationFrame.dispose();
        });

        editButton.addActionListener(e -> confirmationFrame.dispose());

        confirmationFrame.setLocationRelativeTo(null);
        confirmationFrame.setVisible(true);
    }
}
