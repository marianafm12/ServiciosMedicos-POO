package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DiagnosticoMedicoFrame extends JFrame {
    private JTextArea diagnosticoArea, observacionesArea;
    private JButton guardarBtn;

    public DiagnosticoMedicoFrame(int idJustificante) {
        setTitle("Generar Diagnóstico Médico");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        JPanel panelDiagnostico = new JPanel(new BorderLayout());
        JLabel diagnosticoLabel = new JLabel("Diagnóstico:");
        diagnosticoArea = new JTextArea(5, 20);
        panelDiagnostico.add(diagnosticoLabel, BorderLayout.NORTH);
        panelDiagnostico.add(new JScrollPane(diagnosticoArea), BorderLayout.CENTER);

        JPanel panelObservaciones = new JPanel(new BorderLayout());
        JLabel observacionesLabel = new JLabel("Observaciones:");
        observacionesArea = new JTextArea(5, 20);
        panelObservaciones.add(observacionesLabel, BorderLayout.NORTH);
        panelObservaciones.add(new JScrollPane(observacionesArea), BorderLayout.CENTER);

        guardarBtn = new JButton("Guardar Diagnóstico");
        guardarBtn.addActionListener(e -> {
            String diagnostico = diagnosticoArea.getText();
            String observaciones = observacionesArea.getText();

            if (diagnostico.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo de diagnóstico no puede estar vacío.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                String sql = "INSERT INTO diagnosticos (id_justificante, diagnostico, observaciones) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, idJustificante);
                pstmt.setString(2, diagnostico);
                pstmt.setString(3, observaciones);

                int filas = pstmt.executeUpdate();
                if (filas > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Diagnóstico guardado exitosamente:\n" + diagnostico + "\nObservaciones:\n" + observaciones,
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo guardar el diagnóstico.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error de base de datos:\n" + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        add(panelDiagnostico);
        add(panelObservaciones);
        add(guardarBtn);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DiagnosticoMedicoFrame(1));
    }
}
