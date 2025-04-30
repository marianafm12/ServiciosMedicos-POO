package Justificantes;

import javax.swing.*;
import java.awt.*;

public class SeleccionarPacienteFrame extends JFrame {
    private JTextField pacienteField;
    private JButton validarBtn;

    public SeleccionarPacienteFrame() {
        setTitle("Seleccionar Paciente");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2));

        JLabel pacienteLabel = new JLabel("ID o Nombre del Paciente:");
        pacienteField = new JTextField();
        validarBtn = new JButton("Validar");

        // Valida con enter
        getRootPane().setDefaultButton(validarBtn);

        validarBtn.addActionListener(e -> {
            String paciente = pacienteField.getText();
            if (!paciente.isEmpty()) {
                new PlantillaJustificanteFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Ingrese un ID o nombre válido", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        add(pacienteLabel);
        add(pacienteField);
        add(new JLabel());
        add(validarBtn);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SeleccionarPacienteFrame::new);
    }
}
