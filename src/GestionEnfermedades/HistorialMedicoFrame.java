package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;

public class HistorialMedicoFrame extends JFrame {
    public HistorialMedicoFrame() {
        setTitle("Historial Médico");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea historialArea = new JTextArea("Enfermedades registradas:\n- Gripe\n- Alergia al polen");
        historialArea.setEditable(false);
        add(new JScrollPane(historialArea));

        // Agregar los botones "Menú principal" y "Regresar" al final
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton menuPrincipalButton = new JButton("Menú Principal");
        JButton regresarButton = new JButton("Regresar");
        bottomPanel.add(menuPrincipalButton);
        bottomPanel.add(regresarButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Evento para el botón "Menú Principal"
        menuPrincipalButton.addActionListener(e -> {
            new Inicio.PortadaFrame().setVisible(true);
            dispose();
        });

        // Evento para el botón "Regresar"
        regresarButton.addActionListener(e -> {
            new Inicio.MenuPacientesFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }
}
