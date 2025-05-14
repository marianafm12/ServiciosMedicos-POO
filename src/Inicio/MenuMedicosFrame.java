package Inicio;

import GestionEnfermedades.EditarDatosPaciente;
import GestionEnfermedades.VerDatosPaciente;

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

        // Botón: Editar Datos del Paciente
        gbc.gridy = 1;
        gbc.gridx = 1;
        JButton editarDatosButton = new JButton("Editar Datos del Paciente");
        editarDatosButton.setPreferredSize(buttonSize);
        add(editarDatosButton, gbc);

        // Botón: Justificante Médico
        gbc.gridy = 2;
        gbc.gridx = 0;
        JButton justificantesMedicosButton = new JButton("Justificantes Médicos");
        justificantesMedicosButton.setPreferredSize(buttonSize);
        add(justificantesMedicosButton, gbc);

        // Botón: Llamada de Emergencia Nueva
        gbc.gridy = 2;
        gbc.gridx = 1;
        JButton llamadaEmergenciaNueva = new JButton("Registrar Llamada de Emergencia");
        llamadaEmergenciaNueva.setPreferredSize(buttonSize);
        add(llamadaEmergenciaNueva, gbc);

        // Botón: Formulario Reporte de Accidente
        gbc.gridy = 3;
        gbc.gridx = 0;
        JButton reporteAccidente = new JButton("Llenar Reporte de accidente");
        reporteAccidente.setPreferredSize(buttonSize);
        add(reporteAccidente, gbc);

        // Evento para botón "Consulta Nueva"
        consultaButton.addActionListener(e -> {
            Consultas.ConsultasFrame.launchApp();
            dispose();
        });

        // Evento para botón "Editar Datos del Paciente"
        editarDatosButton.addActionListener(e -> {
            String idPacienteStr = JOptionPane.showInputDialog(this, "Ingrese el ID del Paciente:");
            if (idPacienteStr != null && !idPacienteStr.isEmpty()) {
                try {
                    int idPaciente = Integer.parseInt(idPacienteStr);
                    new EditarDatosPaciente(idPaciente).setVisible(true); // Usa el nuevo constructor
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID inválido. Debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
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

        // Evento para botón "Reporte de Accidente"
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
