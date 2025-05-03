package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;

import Inicio.MenuPacientesFrame;
import Inicio.PortadaFrame;

public class FormularioJustificanteFrame extends JFrame {
    private JTextField idField, nombreField, motivoField;
    private JComboBox<String> diaInicio, mesInicio, anioInicio;
    private JComboBox<String> diaFin, mesFin, anioFin;
    private JButton subirArchivoBtn, siguienteBtn, menuPrincipalBtn, regresarBtn;
    private File justificanteFile;

    public FormularioJustificanteFrame() {
        setTitle("Solicitud de Justificante Médico");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] dias = new String[31];
        for (int i = 0; i < 31; i++) dias[i] = String.valueOf(i + 1);

        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        String[] anios = new String[10];
        for (int i = 0; i < 10; i++) anios[i] = String.valueOf(2020 + i);

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(10);
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField(20);
        nombreField.setEditable(false);

        idField.addActionListener(e -> cargarNombreDesdeBD());

        JLabel motivoLabel = new JLabel("Motivo:");
        motivoField = new JTextField(20);

        JLabel inicioLabel = new JLabel("Inicio de Reposo:");
        diaInicio = new JComboBox<>(dias);
        mesInicio = new JComboBox<>(meses);
        anioInicio = new JComboBox<>(anios);

        JLabel finLabel = new JLabel("Fin de Reposo:");
        diaFin = new JComboBox<>(dias);
        mesFin = new JComboBox<>(meses);
        anioFin = new JComboBox<>(anios);

        subirArchivoBtn = new JButton("Subir receta (PDF)");
        siguienteBtn = new JButton("Siguiente");
        menuPrincipalBtn = new JButton("Menú Principal");
        regresarBtn = new JButton("Regresar");

        subirArchivoBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                justificanteFile = fileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(this, "Archivo seleccionado: " + justificanteFile.getName());
            }
        });

        siguienteBtn.addActionListener(e -> {
            String idTexto = idField.getText();
            String motivo = motivoField.getText();

            if (idTexto.isEmpty() || motivo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos obligatorios.");
                return;
            }

            new CorreosProfesoresFrame(1).setVisible(true); // Simulación con ID 1
            dispose();
        });

        menuPrincipalBtn.addActionListener(e -> {
            new PortadaFrame().setVisible(true);
            dispose();
        });

        regresarBtn.addActionListener(e -> {
            new MenuPacientesFrame().setVisible(true);
            dispose();
        });

        gbc.gridx = 0; gbc.gridy = 0; add(idLabel, gbc);
        gbc.gridx = 1; add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(nombreLabel, gbc);
        gbc.gridx = 1; add(nombreField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(motivoLabel, gbc);
        gbc.gridx = 1; add(motivoField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(inicioLabel, gbc);
        gbc.gridx = 1; add(diaInicio, gbc);
        gbc.gridx = 2; add(mesInicio, gbc);
        gbc.gridx = 3; add(anioInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(finLabel, gbc);
        gbc.gridx = 1; add(diaFin, gbc);
        gbc.gridx = 2; add(mesFin, gbc);
        gbc.gridx = 3; add(anioFin, gbc);

        gbc.gridx = 0; gbc.gridy = 5; add(subirArchivoBtn, gbc);
        gbc.gridx = 1; add(siguienteBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 6; add(menuPrincipalBtn, gbc);
        gbc.gridx = 1; add(regresarBtn, gbc);
    }

    private void cargarNombreDesdeBD() {
        String idTexto = idField.getText();
        if (idTexto.isEmpty()) return;

        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(idTexto));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String nombreCompleto = rs.getString("Nombre") + " " + rs.getString("ApellidoPaterno") + " " + rs.getString("ApellidoMaterno");
                nombreField.setText(nombreCompleto);
            } else {
                JOptionPane.showMessageDialog(this, "ID no encontrado en InformacionAlumno.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar en la base de datos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormularioJustificanteFrame().setVisible(true));
    }
}
