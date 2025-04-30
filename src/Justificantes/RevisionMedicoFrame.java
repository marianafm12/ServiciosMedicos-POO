package Justificantes;

import javax.swing.*;
import java.awt.*;

public class RevisionMedicoFrame extends JFrame {
    public RevisionMedicoFrame(JustificanteMedico justificante) {
        setTitle("Revisión del Médico");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea infoArea = new JTextArea(justificante.toString());
        infoArea.setEditable(false);
        JButton aprobarBtn = new JButton("Aprobar");

        aprobarBtn.addActionListener(e -> {
            new JustificanteFinalFrame(justificante);
            dispose();
        });

        add(infoArea, BorderLayout.CENTER);
        add(aprobarBtn, BorderLayout.SOUTH);
        setVisible(true);
    }
}