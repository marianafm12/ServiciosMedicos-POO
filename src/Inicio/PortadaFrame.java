package Inicio;

import javax.swing.*;
import java.awt.*;

public class PortadaFrame extends JFrame {

    public PortadaFrame() {
        setTitle("UDLAP Servicios Médicos");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Título del sistema
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        JLabel titleLabel = new JLabel("Servicios Médicos UDLAP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, gbc);

        // Botón para Médicos
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JButton btnMedicos = new JButton("Personal Médico");
        add(btnMedicos, gbc);

        // Botón para Estudiantes
        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton btnEstudiantes = new JButton("Paciente");
        add(btnEstudiantes, gbc);

        // Botón para Emergencias
        gbc.gridx = 2;
        gbc.gridy = 1;
        JButton btnEmergencias = new JButton("Emergencias");
        add(btnEmergencias, gbc);

        // Eventos de los botones
        btnMedicos.addActionListener(e -> {
            // Llama al login para médicos
            new LoginMedicosFrame().setVisible(true);
            dispose();
        });

        btnEstudiantes.addActionListener(e -> {
            // Llama al login para pacientes
            new LoginPacientesFrame().setVisible(true);
            dispose();
        });

        btnEmergencias.addActionListener(e -> {
            new Emergencias.MenuEmergenciaFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PortadaFrame::new);
    }
}
