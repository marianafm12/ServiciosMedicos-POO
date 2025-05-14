package GestionCitas;

import javax.swing.*;
import java.awt.*;

public class InicioFrame extends JFrame {

    public InicioFrame() {
        setTitle("Gestión de Citas-Menú Principal");
        setSize(400, 300);
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
        JLabel titleLabel = new JLabel("Bienvenido a Gestión de Citas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, gbc);

        // Botón para agendar cita
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JButton agendarCitaButton = new JButton("Agendar Cita");
        add(agendarCitaButton, gbc);

        // Botón para modificar cita
        gbc.gridy = 2;
        JButton modificarCitaButton = new JButton("Modificar Cita");
        add(modificarCitaButton, gbc);

        // Acción para abrir el formulario de agendar cita

        agendarCitaButton.addActionListener(e -> {
            new AgendaCitaFrame();
            dispose();
        });

        // Acción para abrir el formulario de modificar cita
        modificarCitaButton.addActionListener(e -> {
            new ModificarCitaFrame();
            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton menuPrincipalButton = new JButton("Menú Principal");
        JButton regresarButton = new JButton("Regresar");
        bottomPanel.add(menuPrincipalButton);
        bottomPanel.add(regresarButton);

        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(bottomPanel, gbc);

        // Evento para botón "Menú Principal"
        menuPrincipalButton.addActionListener(e -> {
            new Inicio.PortadaFrame().setVisible(true);
            dispose();
        });

        // Evento para botón "Regresar"
        regresarButton.addActionListener(e -> {
            int idPaciente = 1; // Reemplaza con el ID del paciente actual o pásalo como parámetro al crear esta clase
            new Inicio.MenuPacientesFrame(idPaciente).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new InicioFrame();
    }
}
