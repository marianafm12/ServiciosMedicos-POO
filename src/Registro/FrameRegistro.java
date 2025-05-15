package Registro;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;

public class FrameRegistro extends JPanel {
    private final JTextField[] campos;
    private final String[] etiquetas = {
            "ID:", "Nombre:", "Apellido Paterno:", "Apellido Materno:", "Correo:",
            "Edad:", "Altura (cm):", "Peso (kg):",
            "Enfermedades Preexistentes:", "Medicación:", "Alergias:"
    };

    public FrameRegistro() {
        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);
        campos = new JTextField[etiquetas.length];

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuración de fuente
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;

            JLabel label = new JLabel(etiquetas[i]);
            label.setFont(labelFont);
            label.setForeground(ColoresUDLAP.NEGRO);
            add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;

            campos[i] = new JTextField(25);
            campos[i].setFont(fieldFont);
            campos[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            add(campos[i], gbc);
        }

        // Espacio adicional al final
        gbc.gridy = etiquetas.length;
        gbc.weighty = 1.0;
        add(Box.createGlue(), gbc);
    }

    public JTextField[] obtenerCampos() {
        return campos;
    }
}