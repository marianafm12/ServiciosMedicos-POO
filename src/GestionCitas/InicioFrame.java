package GestionCitas;

import javax.swing.*;
import java.awt.*;
import Inicio.SesionUsuario;
/*import GestionCitas.AgendaCitaFrame;
import GestionCitas.ModificarCitaFrame;*/
import Inicio.PortadaFrame;
//import Inicio.MenuPacientesFrame;

public class InicioFrame extends JFrame {

    public InicioFrame() {
        super("Gestión de Citas – Menú Principal");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        // Listener para Agendar Cita
        agendarCitaButton.addActionListener(e -> {
            int id = SesionUsuario.getPacienteActual();
            new AgendaCitaFrame(id).setVisible(true);
            dispose();
        });

        // Listener para Modificar Cita
        modificarCitaButton.addActionListener(e -> {
            new ModificarCitaFrame().setVisible(true);
            dispose();
        });

        // Panel inferior con botones Menú Principal / Regresar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton menuPrincipalButton = new JButton("Menú Principal");
        JButton regresarButton = new JButton("Regresar a Interfaz Médica");
        bottomPanel.add(menuPrincipalButton);
        bottomPanel.add(regresarButton);

        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(bottomPanel, gbc);

        // Listener para Regresar
        regresarButton.addActionListener(e -> {
            int idPaciente = SesionUsuario.getPacienteActual();
            // new MenuPacientesFrame(idPaciente).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InicioFrame::new);
    }
}