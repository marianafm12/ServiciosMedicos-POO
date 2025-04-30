package Justificantes;

import javax.swing.*;
import java.awt.*;

public class InicioFrame extends JFrame {
    public InicioFrame() {
        setTitle("Generación de Justificantes Médicos");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1));

        JButton internoBtn = new JButton("Justificante Médico Interno");
        JButton externoBtn = new JButton("Justificante Médico Externo");

        internoBtn.addActionListener(e -> {
            new SeleccionarPacienteFrame();
            dispose();
        });

        externoBtn.addActionListener(e -> {
            new FormularioJustificanteFrame();
            dispose();
        });

        add(internoBtn);
        add(externoBtn);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InicioFrame::new);
    }
}