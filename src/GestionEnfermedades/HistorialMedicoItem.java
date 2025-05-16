package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;

public class HistorialMedicoItem extends JPanel {
    public HistorialMedicoItem(String fecha, String diagnostico, String observaciones) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(700, 90));

        JLabel lblFecha = new JLabel("Fecha: " + fecha);
        lblFecha.setFont(new Font("Arial", Font.BOLD, 14));
        lblFecha.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JTextArea txtDiagnostico = new JTextArea("Diagnóstico: " + diagnostico + "\nObservaciones: " + observaciones);
        txtDiagnostico.setLineWrap(true);
        txtDiagnostico.setWrapStyleWord(true);
        txtDiagnostico.setEditable(false);
        txtDiagnostico.setBackground(Color.WHITE);
        txtDiagnostico.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(lblFecha, BorderLayout.NORTH);
        add(txtDiagnostico, BorderLayout.CENTER);
    }
}
