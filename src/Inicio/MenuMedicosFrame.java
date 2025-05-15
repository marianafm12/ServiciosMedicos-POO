package Inicio;

import Consultas.ConsultasFrame;
import Emergencias.AccidenteFrame;
import Emergencias.LlamadaEmergencia;
import GestionEnfermedades.EditarDatosPaciente;
import Justificantes.EmitirJustificanteDesdeConsultaFrame;
import Justificantes.SeleccionarPacienteFrame;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.net.URL;

/**
 * Ventana principal para el médico, con barra superior y botones de acción en
 * el centro.
 */
public class MenuMedicosFrame extends JFrame {

    private boolean hasNewNotification = false;
    private JButton notificationButton;
    private ImageIcon iconDefault;
    private ImageIcon iconNew;
    private JPanel center; // panel central

    public MenuMedicosFrame() {
        super("Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        loadIcons();
        initToolbar();
        initCenterButtons();
        setSize(600, 350);
        super("Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        loadIcons();
        initToolbar();
        initCenterButtons();
        setSize(600, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Carga los iconos de la campana desde el classpath */
    private void loadIcons() {
        URL u1 = getClass().getResource("/icons/bell.png");
        URL u2 = getClass().getResource("/icons/bell_new.png");
        if (u1 == null || u2 == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron /icons/bell.png o /icons/bell_new.png en el classpath.",
                    "Recursos no encontrados",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        Image img1 = new ImageIcon(u1).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        Image img2 = new ImageIcon(u2).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        iconDefault = new ImageIcon(img1);
        iconNew = new ImageIcon(img2);
    }

    /**
     * Crea la barra superior con título centrado y campana de notificaciones a la
     * derecha
     */
    private void initToolbar() {
        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setBackground(new Color(245, 245, 245));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        // Título (usa todo el espacio disponible)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel title = new JLabel(
                "Bienvenido al Sistema de Servicios Médicos UDLAP",
                SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        toolbar.add(title, gbc);

        // Botón de notificaciones
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        notificationButton = new JButton(iconDefault);
        notificationButton.setBorderPainted(false);
        notificationButton.setContentAreaFilled(false);
        notificationButton.addActionListener(e -> toggleNotification());
        toolbar.add(notificationButton, gbc);

        add(toolbar, BorderLayout.NORTH);
    }

    /** Alterna el icono de la campana según haya notificaciones nuevas */
    private void toggleNotification() {
        hasNewNotification = !hasNewNotification;
        notificationButton.setIcon(hasNewNotification ? iconNew : iconDefault);
        // Aquí podrías, por ejemplo, abrir una ventana de notificaciones...
    }

    /** Crea el panel central con todos los botones y sus listeners */
    private void initCenterButtons() {
        center = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Dimension buttonSize = new Dimension(200, 40);

        // 1) Consulta Nueva
        JButton consultaButton = new JButton("Consulta Nueva");
        consultaButton.setPreferredSize(buttonSize);
        center.add(consultaButton, gbc);

        // 2) Editar Datos del Paciente
        gbc.gridy++;
        JButton editarDatosButton = new JButton("Editar Datos del Paciente");
        editarDatosButton.setPreferredSize(buttonSize);
        center.add(editarDatosButton, gbc);

        // 3) Justificantes Médicos
        gbc.gridy++;
        JButton justificantesButton = new JButton("Justificantes Médicos");
        justificantesButton.setPreferredSize(buttonSize);
        center.add(justificantesButton, gbc);

        // 4) Llamada de Emergencia
        gbc.gridy++;
        JButton emergenciaButton = new JButton("Registrar Llamada de Emergencia");
        emergenciaButton.setPreferredSize(buttonSize);
        center.add(emergenciaButton, gbc);

        // 5) Reporte de Accidente
        gbc.gridy++;
        JButton reporteAccidenteButton = new JButton("Llenar Reporte de Accidente");
        reporteAccidenteButton.setPreferredSize(buttonSize);
        center.add(reporteAccidenteButton, gbc);

        // --- Listeners de cada botón ---
        consultaButton.addActionListener(e -> {
            ConsultasFrame.launchApp();
            dispose();
        });

        editarDatosButton.addActionListener(e -> {
            String idPacienteStr = JOptionPane.showInputDialog(
                    this,
                    "Ingrese el ID del Paciente:");
            if (idPacienteStr != null && !idPacienteStr.isEmpty()) {
                try {
                    int idPaciente = Integer.parseInt(idPacienteStr);
                    new EditarDatosPaciente(idPaciente).setVisible(true);
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "ID inválido. Debe ser un número.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        justificantesButton.addActionListener(e -> {
            new SeleccionarPacienteFrame().setVisible(true);
            dispose();
        });

        emergenciaButton.addActionListener(e -> {
            new LlamadaEmergencia().setVisible(true);
            dispose();
        });

        reporteAccidenteButton.addActionListener(e -> {
            new AccidenteFrame().setVisible(true);
            dispose();
        });

        add(center, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuMedicosFrame::new);
        SwingUtilities.invokeLater(MenuMedicosFrame::new);
    }
}
