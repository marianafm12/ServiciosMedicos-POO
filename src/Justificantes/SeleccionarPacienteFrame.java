/* 

package Justificantes;

import javax.swing.*;
import java.awt.*;

public class SeleccionarPacienteFrame extends JFrame {

    public SeleccionarPacienteFrame() {
        setTitle("Seleccionar Paciente");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con margen
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnSolicitud = new JButton("Generar desde Solicitud");
        JButton btnConsulta  = new JButton("Generar a partir de una consulta interna");

        btnSolicitud.setFont(new Font("Arial", Font.BOLD, 14));
        btnConsulta.setFont(new Font("Arial", Font.BOLD, 14));

        btnSolicitud.addActionListener(e -> {
            new SolicitudesJustificantesFrame().setVisible(true);
            dispose();
        });

        btnConsulta.addActionListener(e -> {
            int folioDePrueba = 1;
            new PlantillaJustificanteFrame(folioDePrueba).setVisible(true);
            dispose();
        });

        panel.add(btnSolicitud);
        panel.add(btnConsulta);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SeleccionarPacienteFrame().setVisible(true));
    }
}
//f

*/