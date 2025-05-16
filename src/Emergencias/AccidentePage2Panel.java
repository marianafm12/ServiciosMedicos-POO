package Emergencias;

import javax.swing.*;
import java.awt.*;

public class AccidentePage2Panel extends JPanel {
    private JTextArea txtDescripcion;
    private JTextField txtTestigos;

    public AccidentePage2Panel() {
        setName("PAGE2");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Descripción
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.7;
        add(new JLabel("Descripción del Accidente:"), gbc);
        gbc.gridy = 1;
        txtDescripcion = new JTextArea(6, 30);
        add(new JScrollPane(txtDescripcion), gbc);

        // Testigos
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel("Testigos (nombres):"), gbc);
        gbc.gridx = 1;
        txtTestigos = new JTextField(20);
        add(txtTestigos, gbc);
    }

    public Accidente getModel() {
        Accidente a = new Accidente();
        a.setDescripcion(txtDescripcion.getText().trim());
        a.setTestigos(txtTestigos.getText().trim());
        return a;
    }
}
