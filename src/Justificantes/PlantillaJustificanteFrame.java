package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import BaseDeDatos.ConexionSQLite;
import Inicio.MenuPacientesFrame;
import Justificantes.SeleccionarPacienteFrame;

public class PlantillaJustificanteFrame extends JFrame {

    private JTextField idField, nombreField, doctorField, cedulaField;
    private JComboBox<String> diaInicioBox, mesInicioBox, anioInicioBox;
    private JComboBox<String> diaFinBox, mesFinBox, anioFinBox;
    private JTextArea diagnosticoArea;
    private JButton guardarButton, menuButton, regresarButton;

    public PlantillaJustificanteFrame() {
        setTitle("Plantilla para Justificante Médico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel principal con GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ID
        mainPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        mainPanel.add(idField);

        // Nombre
        mainPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        nombreField.setEditable(false);
        mainPanel.add(nombreField);

        // Doctor/a
        mainPanel.add(new JLabel("Doctor/a:"));
        doctorField = new JTextField();
        mainPanel.add(doctorField);

        // Cédula Profesional
        mainPanel.add(new JLabel("Cédula Profesional:"));
        cedulaField = new JTextField();
        mainPanel.add(cedulaField);

        // Fecha de inicio de reposo
        mainPanel.add(new JLabel("Fecha de inicio de reposo:"));
        diaInicioBox = new JComboBox<>(generarDias());
        mesInicioBox = new JComboBox<>(generarMeses());
        anioInicioBox = new JComboBox<>(generarAnios());
        JPanel panelInicio = new JPanel();
        panelInicio.add(diaInicioBox);
        panelInicio.add(mesInicioBox);
        panelInicio.add(anioInicioBox);
        mainPanel.add(panelInicio);

        // Fecha de fin de reposo
        mainPanel.add(new JLabel("Fecha de final de reposo:"));
        diaFinBox = new JComboBox<>(generarDias());
        mesFinBox = new JComboBox<>(generarMeses());
        anioFinBox = new JComboBox<>(generarAnios());
        JPanel panelFin = new JPanel();
        panelFin.add(diaFinBox);
        panelFin.add(mesFinBox);
        panelFin.add(anioFinBox);
        mainPanel.add(panelFin);

        // Diagnóstico y observaciones
        mainPanel.add(new JLabel("Diagnóstico y Observaciones:"));
        diagnosticoArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(diagnosticoArea);
        mainPanel.add(scrollPane);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        guardarButton = new JButton("Guardar Justificante");
        menuButton    = new JButton("Menú Principal");
        regresarButton= new JButton("Regresar");
        buttonPanel.add(menuButton);
        buttonPanel.add(regresarButton);
        buttonPanel.add(guardarButton);

        // Eventos de botones
        guardarButton.addActionListener(e -> {
            guardarJustificanteEnBD();
            new CorreosProfesoresFrame(1).setVisible(true);
            dispose();
        });

        menuButton.addActionListener(e -> {
            new MenuPacientesFrame().setVisible(true);
            dispose();
        });

        regresarButton.addActionListener(e -> {
            new SeleccionarPacienteFrame().setVisible(true);
            dispose();
        });

        // Listener para buscar nombre automáticamente al escribir el ID
        idField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String id = idField.getText().trim();
                if (!id.isEmpty()) {
                    buscarNombrePorID(id);
                } else {
                    nombreField.setText("");
                }
            }
        });

        // Añadir paneles al frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private String[] generarDias() {
        String[] dias = new String[31];
        for (int i = 1; i <= 31; i++) dias[i - 1] = String.valueOf(i);
        return dias;
    }

    private String[] generarMeses() {
        return new String[] {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
    }

    private String[] generarAnios() {
        String[] anios = new String[10];
        int year = 2025;
        for (int i = 0; i < 10; i++) anios[i] = String.valueOf(year + i);
        return anios;
    }

    private void buscarNombrePorID(String id) {
        try (Connection conn = ConexionSQLite.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT nombre FROM InformacionAlumno WHERE id = ?"
            );
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nombreField.setText(rs.getString("nombre"));
            } else {
                nombreField.setText("");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void guardarJustificanteEnBD() {
        String id         = idField.getText().trim();
        String doctor     = doctorField.getText().trim();
        String cedula     = cedulaField.getText().trim();
        String diag       = diagnosticoArea.getText().trim();

        String fechaInicio = diaInicioBox.getSelectedItem() + " "
            + mesInicioBox.getSelectedItem() + " "
            + anioInicioBox.getSelectedItem();
        String fechaFin    = diaFinBox.getSelectedItem() + " "
            + mesFinBox.getSelectedItem() + " "
            + anioFinBox.getSelectedItem();

        try (Connection conn = ConexionSQLite.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Justificantes " +
                "(alumno_id, doctor, cedula, fecha_inicio, fecha_fin, diagnostico) " +
                "VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, id);
            ps.setString(2, doctor);
            ps.setString(3, cedula);
            ps.setString(4, fechaInicio);
            ps.setString(5, fechaFin);
            ps.setString(6, diag);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Justificante guardado correctamente.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar justificante.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlantillaJustificanteFrame());
    }
}
