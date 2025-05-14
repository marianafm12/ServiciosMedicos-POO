package Inicio;

import javax.swing.*;
import java.awt.*;

public class MenuPacientesFrame extends JFrame {
    private int idPaciente; // Variable para almacenar el ID del paciente

    public MenuPacientesFrame(int idPaciente) {
        this.idPaciente = idPaciente; // Guardar el ID del paciente

        setTitle("Menú Principal");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Bienvenido al Sistema de Servicios Médicos UDLAP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, gbc);
        Dimension buttonSize = new Dimension(250, 40);

        // Botón: Gestión de Citas
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JButton gestionCitasButton = new JButton("Gestión de Citas");
        gestionCitasButton.setPreferredSize(buttonSize);
        add(gestionCitasButton, gbc);

        // Botón: Historial Médico
        gbc.gridy = 1;
        gbc.gridx = 1;
        JButton historialMedicoButton = new JButton("Historial Médico");
        historialMedicoButton.setPreferredSize(buttonSize);
        add(historialMedicoButton, gbc);

        // Botón: Justificante Médico
        gbc.gridy = 2;
        gbc.gridx = 0;
        JButton justificantesMedicosButton = new JButton("Justificantes Médicos");
        justificantesMedicosButton.setPreferredSize(buttonSize);
        add(justificantesMedicosButton, gbc);

        // Botón: Personal Médico
        gbc.gridy = 2;
        gbc.gridx = 1;
        JButton reportarEmergenciaButton = new JButton("Reportar Emergencia");
        reportarEmergenciaButton.setPreferredSize(buttonSize);
        add(reportarEmergenciaButton, gbc);

        // Evento para botón "Gestión de Citas"
        gestionCitasButton.addActionListener(e -> {
            new GestionCitas.InicioFrame().setVisible(true);
            dispose();
        });

        // Evento para botón "Historial Médico"
        historialMedicoButton.addActionListener(e -> {
            new GestionEnfermedades.VerDatosPaciente(idPaciente).setVisible(true); // Usar el ID del paciente
            dispose();
        });

        // Evento para botón "Justificante Médico"
        justificantesMedicosButton.addActionListener(e -> {
            new Justificantes.FormularioJustificanteFrame().setVisible(true);
            dispose();
        });

        // Evento para botón "Reportar Emergencia"
        reportarEmergenciaButton.addActionListener(e -> {
            new Emergencias.MenuEmergenciaFrame().setVisible(true);
            dispose();
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        new MenuPacientesFrame(0); // Llamar al constructor con un ID de paciente
    }
}
