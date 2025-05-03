package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SeleccionarPacienteFrame extends JFrame {

    public SeleccionarPacienteFrame() {
        setTitle("Seleccionar Paciente");
        setSize(500, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de botones con GridLayout para tamaño uniforme
        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnSolicitudPaciente = new JButton("Generar a partir de solicitud de un paciente");
        JButton btnConsulta = new JButton("Generar a partir de una consulta interna");

        // Acción para solicitud de paciente
        btnSolicitudPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SolicitudJustificante().setVisible(true);
                dispose();
            }
        });

        // Acción para consulta interna
        btnConsulta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PlantillaJustificanteFrame().setVisible(true);
                dispose();
            }
        });

        panelBotones.add(btnSolicitudPaciente);
        panelBotones.add(btnConsulta);

        add(panelBotones, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SeleccionarPacienteFrame();
    }
}
