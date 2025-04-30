package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;

public class FormularioJustificanteFrame extends JFrame {
    private JTextField motivoField, diasField;
    private JButton subirArchivoBtn, siguienteBtn;
    private File justificanteFile;

    public FormularioJustificanteFrame() {
        setTitle("Solicitud de Justificante Médico");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel motivoLabel = new JLabel("Motivo:");
        JLabel diasLabel = new JLabel("Días de Ausencia:");
        motivoField = new JTextField(20);
        diasField = new JTextField(20);
        subirArchivoBtn = new JButton("Subir Justificante (PDF)");
        siguienteBtn = new JButton("Siguiente");

        subirArchivoBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                justificanteFile = fileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(this, "Archivo seleccionado: " + justificanteFile.getName());
            }
        });

        siguienteBtn.addActionListener(e -> {
            String motivo = motivoField.getText();
            String diasStr = diasField.getText();

            if (motivo.isEmpty() || diasStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.");
                return;
            }

            try {
                int diasAusencia = Integer.parseInt(diasStr);

                Connection conn = BaseDeDatos.ConexionSQLite.conectar();

                // Crear tabla si no existe
                String crearTabla = "CREATE TABLE IF NOT EXISTS SolicitudJustificantes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "motivo TEXT NOT NULL, " +
                        "dias_ausencia INTEGER NOT NULL)";
                Statement stmt = conn.createStatement();
                stmt.execute(crearTabla);

                // Insertar datos
                String sql = "INSERT INTO SolicitudJustificantes (motivo, dias_ausencia) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, motivo);
                pstmt.setInt(2, diasAusencia);

                int filas = pstmt.executeUpdate();
                if (filas > 0) {
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        JOptionPane.showMessageDialog(this,
                                "Justificante registrado exitosamente.\nID generado: " + id,
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo guardar el justificante.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El campo de días debe ser un número entero.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos:\n" + ex.getMessage());
                ex.printStackTrace();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton menuPrincipalButton = new JButton("Menú Principal");
        JButton regresarButton = new JButton("Regresar");
        bottomPanel.add(menuPrincipalButton);
        bottomPanel.add(regresarButton);

        menuPrincipalButton.addActionListener(e -> {
            new Inicio.PortadaFrame().setVisible(true);
            dispose();
        });

        regresarButton.addActionListener(e -> {
            new Inicio.MenuPacientesFrame().setVisible(true);
            dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        JLabel titleLabel = new JLabel("Solicitud Justificantes Médicos UDLAP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(motivoLabel, gbc);

        gbc.gridx = 1;
        add(motivoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(diasLabel, gbc);

        gbc.gridx = 1;
        add(diasField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(subirArchivoBtn, gbc);

        gbc.gridx = 1;
        add(siguienteBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(bottomPanel, gbc);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FormularioJustificanteFrame::new);
    }
}
