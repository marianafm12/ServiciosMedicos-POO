package Emergencias;

import javax.swing.*;
import java.awt.*;

public class MenuEmergenciaFrame extends JFrame {

    public MenuEmergenciaFrame() {
        setTitle("Sistema de Emergencias");
        setSize(600, 500);
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
        JLabel titleLabel = new JLabel("Menú de Emergencias", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, gbc);

        // Tamaño de los botones
        Dimension buttonSize = new Dimension(400, 100);

        // Botón: Llamar Seguridad
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JButton llamarSeguridadButton = new JButton("Llamar Seguridad");
        llamarSeguridadButton.setPreferredSize(buttonSize);
        add(llamarSeguridadButton, gbc);

        // Botón: Llamar Paramédicos
        gbc.gridy = 1;
        gbc.gridx = 1;
        JButton llamarParamedicosButton = new JButton("Llamar Paramédicos");
        llamarParamedicosButton.setPreferredSize(buttonSize);
        add(llamarParamedicosButton, gbc);

        // Evento para botón "Llamar Seguridad"
        llamarSeguridadButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Llamando a Seguridad...");
        });

        // Evento para botón "Llamar Paramédicos"
        llamarParamedicosButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Llamando a Paramédicos...");
        });

        // Botón: Menú Principal
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        JButton menuPrincipalButton = new JButton("Menú Principal");
        add(menuPrincipalButton, gbc);

        // Evento para botón "Menú Principal"
        menuPrincipalButton.addActionListener(e -> {
            new Inicio.PortadaFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new MenuEmergenciaFrame();
    }
}
