package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import BaseDeDatos.ConexionSQLite;
import Inicio.MenuPacientesFrame;
import Inicio.PortadaFrame;

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

        JPanel mainPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ID
        mainPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        mainPanel.add(idField);

        // Nombre (no editable)
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

        // Fecha de inicio
        mainPanel.add(new JLabel("Fecha de inicio de reposo:"));
        JPanel inicioFechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        diaInicioBox = new JComboBox<>(generarDias());
        mesInicioBox = new JComboBox<>(generarMeses());
        anioInicioBox = new JComboBox<>(generarAnios());
        inicioFechaPanel.add(diaInicioBox);
        inicioFechaPanel.add(mesInicioBox);
        inicioFechaPanel.add(anioInicioBox);
        mainPanel.add(inicioFechaPanel);

        // Fecha de fin
        mainPanel.add(new JLabel("Fecha de final de reposo:"));
        JPanel finFechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        diaFinBox = new JComboBox<>(generarDias());
        mesFinBox = new JComboBox<>(generarMeses());
        anioFinBox = new JComboBox<>(generarAnios());
        finFechaPanel.add(diaFinBox);
        finFechaPanel.add(mesFinBox);
        finFechaPanel.add(anioFinBox);
        mainPanel.add(finFechaPanel);

        // Diagnóstico y observaciones
        mainPanel.add(new JLabel("Diagnóstico y Observaciones:"));
        diagnosticoArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(diagnosticoArea);
        mainPanel.add(scrollPane);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        guardarButton = new JButton("Guardar Justificante");
        menuButton = new JButton("Menú Principal");
        regresarButton = new JButton("Regresar");
        buttonPanel.add(menuButton);
        buttonPanel.add(regresarButton);
        buttonPanel.add(guardarButton);

        // Eventos de botones
        guardarButton.addActionListener(e -> {
            new CorreosProfesoresFrame(1).setVisible(true);
            dispose();
        });

        menuButton.addActionListener(e -> {
            new MenuPacientesFrame().setVisible(true);
            dispose();
        });

        regresarButton.addActionListener(e -> {
            new PortadaFrame().setVisible(true);
            dispose();
        });

        // Listener para buscar nombre automáticamente
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

        // Añadir paneles
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void buscarNombrePorID(String id) {
        try (Connection conn = ConexionSQLite.conectar()) {
            String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nombreCompleto = rs.getString("Nombre") + " " +
                        rs.getString("ApellidoPaterno") + " " +
                        rs.getString("ApellidoMaterno");
                nombreField.setText(nombreCompleto);
            } else {
                nombreField.setText("No encontrado");
            }
        } catch (SQLException ex) {
            nombreField.setText("Error al conectar");
            ex.printStackTrace();
        }
    }

    private String[] generarDias() {
        String[] dias = new String[31];
        for (int i = 1; i <= 31; i++) dias[i - 1] = String.valueOf(i);
        return dias;
    }

    private String[] generarMeses() {
        return new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    }

    private String[] generarAnios() {
        String[] anios = new String[10];
        int year = 2025;
        for (int i = 0; i < 10; i++) anios[i] = String.valueOf(year + i);
        return anios;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlantillaJustificanteFrame());
    }
}
