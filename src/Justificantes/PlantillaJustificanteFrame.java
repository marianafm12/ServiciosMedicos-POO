package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class PlantillaJustificanteFrame extends JFrame {
    private JTextField centroMedicoField, doctorField, cedulaField, pacienteField, dniField, horaField, firmaField;
    private JTextArea diagnosticoArea;
    private JComboBox<String> diaBox, mesBox, anioBox;
    private JButton guardarBtn;
    private JLabel errorLabel;

    public PlantillaJustificanteFrame() {
        setTitle("Plantilla para Justificante Médico");
        setLayout(new GridLayout(12, 2));
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        centroMedicoField = new JTextField();
        doctorField = new JTextField();
        cedulaField = new JTextField();
        pacienteField = new JTextField();
        dniField = new JTextField();
        horaField = new JTextField();
        firmaField = new JTextField();
        diagnosticoArea = new JTextArea(5, 20);

        String[] dias = new String[31];
        for (int i = 1; i <= 31; i++)
            dias[i - 1] = String.valueOf(i);

        String[] meses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        String[] anios = new String[100];
        int actualYear = java.time.Year.now().getValue();
        for (int i = 0; i < 100; i++)
            anios[i] = String.valueOf(actualYear - i);

        diaBox = new JComboBox<>(dias);
        mesBox = new JComboBox<>(meses);
        anioBox = new JComboBox<>(anios);

        guardarBtn = new JButton("Guardar Justificante");
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        add(new JLabel("Centro Médico:"));
        add(centroMedicoField);

        add(new JLabel("Doctor/a:"));
        add(doctorField);

        add(new JLabel("Cédula Profesional:"));
        add(cedulaField);

        add(new JLabel("Paciente:"));
        add(pacienteField);

        add(new JLabel("ID:"));
        add(dniField);

        add(new JLabel("Fecha de Atención:"));
        JPanel fechaPanel = new JPanel();
        fechaPanel.add(diaBox);
        fechaPanel.add(mesBox);
        fechaPanel.add(anioBox);
        add(fechaPanel);

        add(new JLabel("Hora de Atención:"));
        add(horaField);

        add(new JLabel("Diagnóstico y Observaciones:"));
        add(new JScrollPane(diagnosticoArea));

        add(new JLabel("Firma del médico:"));
        add(firmaField);

        add(errorLabel);
        add(guardarBtn);

        getRootPane().setDefaultButton(guardarBtn);

        guardarBtn.addActionListener(e -> {
            if (centroMedicoField.getText().trim().isEmpty() ||
                    doctorField.getText().trim().isEmpty() ||
                    cedulaField.getText().trim().isEmpty() ||
                    pacienteField.getText().trim().isEmpty() ||
                    dniField.getText().trim().isEmpty() ||
                    horaField.getText().trim().isEmpty() ||
                    diagnosticoArea.getText().trim().isEmpty() ||
                    firmaField.getText().trim().isEmpty()) {

                errorLabel.setText("Falta información por llenar.");
                return;
            }

            int dia = Integer.parseInt((String) diaBox.getSelectedItem());
            int mes = mesBox.getSelectedIndex() + 1;
            int anio = Integer.parseInt((String) anioBox.getSelectedItem());
            LocalDate fecha = LocalDate.of(anio, mes, dia);

            try {
                Connection conn = BaseDeDatos.ConexionSQLite.conectar();

                // Crear tabla si no existe
                String crearTablaSQL = "CREATE TABLE IF NOT EXISTS PlantillaJustificanteMedicoCompleta (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "centro_medico TEXT NOT NULL, " +
                        "doctor TEXT NOT NULL, " +
                        "cedula_profesional TEXT NOT NULL, " +
                        "paciente TEXT NOT NULL, " +
                        "fecha_atencion DATE NOT NULL, " +
                        "hora_atencion TEXT NOT NULL, " +
                        "diagnostico_observaciones TEXT, " +
                        "firma_medico TEXT)";
                Statement stmt = conn.createStatement();
                stmt.execute(crearTablaSQL);

                String sql = "INSERT INTO PlantillaJustificanteMedicoCompleta " +
                        "(centro_medico, doctor, cedula_profesional, paciente, fecha_atencion, hora_atencion, diagnostico_observaciones, firma_medico) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, centroMedicoField.getText().trim());
                pstmt.setString(2, doctorField.getText().trim());
                pstmt.setString(3, cedulaField.getText().trim());
                pstmt.setString(4, pacienteField.getText().trim());
                pstmt.setString(5, fecha.toString());
                pstmt.setString(6, horaField.getText().trim());
                pstmt.setString(7, diagnosticoArea.getText().trim());
                pstmt.setString(8, firmaField.getText().trim());

                int filas = pstmt.executeUpdate();
                if (filas > 0) {
                    JOptionPane.showMessageDialog(this, "Justificante guardado correctamente.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ No se pudo guardar el justificante.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar en base de datos:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton menuPrincipalButton = new JButton("Menú Principal");
        JButton regresarButton = new JButton("Regresar");
        bottomPanel.add(menuPrincipalButton);
        bottomPanel.add(regresarButton);

        menuPrincipalButton.addActionListener(e -> {
            new Inicio.PortadaFrame().setVisible(true);
            dispose();
        });

        regresarButton.addActionListener(e -> {
            new Inicio.MenuMedicosFrame().setVisible(true);
            dispose();
        });

        add(bottomPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PlantillaJustificanteFrame::new);
    }
}
