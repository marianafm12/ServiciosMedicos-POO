package Inicio;

import javax.swing.*;
import java.awt.*;

public class MenuMedicosFrame extends JFrame {

    public MenuMedicosFrame() {
        setTitle("Menú Principal");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Dimension buttonSize = new Dimension(250, 40);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Bienvenido al Sistema de Servicios Médicos UDLAP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, gbc);

        // Botón: Consulta Nueva
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JButton consultaButton = new JButton("Consulta Nueva");
        consultaButton.setPreferredSize(buttonSize);
        add(consultaButton, gbc);

        // Botón: Gestión de Enfermedades
        gbc.gridy = 1;
        gbc.gridx = 1;
        JButton gestionEnfermedadesButton = new JButton("Gestión de Enfermedades");
        gestionEnfermedadesButton.setPreferredSize(buttonSize);
        add(gestionEnfermedadesButton, gbc);

        // Botón: Registro de nuevo paciente
        gbc.gridy = 2;
        gbc.gridx = 0;
        JButton pacienteNuevoButton = new JButton("Registro de Paciente Nuevo");
        pacienteNuevoButton.setPreferredSize(buttonSize);
        add(pacienteNuevoButton, gbc);

        // Botón: Justificante médico
        gbc.gridy = 2;
        gbc.gridx = 1;
        JButton justificantesMedicosButton = new JButton("Justificantes Médicos");
        justificantesMedicosButton.setPreferredSize(buttonSize);
        add(justificantesMedicosButton, gbc);

        // Botón: Llamada de Emergencia Nueva
        gbc.gridy = 3;
        gbc.gridx = 0;
        JButton llamadaEmergenciaNueva = new JButton("Registrar Llamada de Emergencia");
        llamadaEmergenciaNueva.setPreferredSize(buttonSize);
        add(llamadaEmergenciaNueva, gbc);

        // Botón: Formulario Reporte de accidente
        gbc.gridy = 3;
        gbc.gridx = 1;
        JButton reporteAccidente = new JButton("Llenar Reporte de accidente");
        reporteAccidente.setPreferredSize(buttonSize);
        add(reporteAccidente, gbc);

        // Evento para botón "Consulta nueva"
        consultaButton.addActionListener(e -> {
            Consultas.ConsultasFrame.launchApp();
            dispose();
        });

        // Evento para botón "Gestión de Enfermedades"
        gestionEnfermedadesButton.addActionListener(e -> {
            new GestionEnfermedades.GestionEnfermedadesFrame().setVisible(true);
            dispose();
        });

        // Evento para botón "Registro de Paciente Nuevo"
        pacienteNuevoButton.addActionListener(e -> {
            // Cambiar IDUsuario
            new Registro.AgendaDirecciones().setVisible(true);
            dispose();
        });

        // Evento para botón "Justificante Médico"
        justificantesMedicosButton.addActionListener(e -> {
            new Justificantes.SeleccionarPacienteFrame().setVisible(true);
            dispose();
        });

        // Evento para botón "Nueva Llamada de Emergencia"
        llamadaEmergenciaNueva.addActionListener(e -> {
            new Emergencias.LlamadaEmergencia().setVisible(true);
            dispose();
        });

        // Evento botón "Reporte de Accidente"
        reporteAccidente.addActionListener(e -> {
            new Emergencias.AccidenteFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new MenuMedicosFrame();
    }
}
