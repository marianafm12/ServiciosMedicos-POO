package Justificantes;

import javax.swing.*;
import java.awt.*;

public class JustificanteFinalFrame extends JFrame {
    public JustificanteFinalFrame(JustificanteMedico justificante) {
        setTitle("Justificante Médico");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea justificanteArea = new JTextArea(justificante.toString() + "\nCódigo QR: [Generado]");
        justificanteArea.setEditable(false);
        add(justificanteArea, BorderLayout.CENTER);
        setVisible(true);
    }
}
