package Inicio;

import Consultas.ConsultasFrame;
import Emergencias.AccidenteFrame;
import Emergencias.LlamadaEmergencia;
import GestionEnfermedades.EditarDatosPaciente;
import Justificantes.SeleccionarPacienteFrame;
import Registro.AgregarRegistro;
import Registro.FormularioFrame;

import javax.swing.*;
import java.awt.*;
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
        setSize(900, 700); // ¡Aumenta el tamaño para mejor visualización!
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
        gbc.insets = new Insets(14, 10, 14, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        Dimension buttonSize = new Dimension(260, 48);

        // 1) Registrar paciente nuevo
        gbc.gridy = 0;
        JButton registroButton = new JButton("Registrar Paciente Nuevo");
        registroButton.setPreferredSize(buttonSize);
        center.add(registroButton, gbc);

        // 2) Consulta Nueva
        gbc.gridy++;
        JButton consultaButton = new JButton("Consulta Nueva");
        consultaButton.setPreferredSize(buttonSize);
        center.add(consultaButton, gbc);

        // 3) Editar Datos del Paciente
        gbc.gridy++;
        JButton editarDatosButton = new JButton("Editar Datos del Paciente");
        editarDatosButton.setPreferredSize(buttonSize);
        center.add(editarDatosButton, gbc);

        // 4) Justificantes Médicos
        gbc.gridy++;
        JButton justificantesButton = new JButton("Justificantes Médicos");
        justificantesButton.setPreferredSize(buttonSize);
        center.add(justificantesButton, gbc);

        // 5) Llamada de Emergencia
        gbc.gridy++;
        JButton emergenciaButton = new JButton("Registrar Llamada de Emergencia");
        emergenciaButton.setPreferredSize(buttonSize);
        center.add(emergenciaButton, gbc);

        // 6) Reporte de Accidente
        gbc.gridy++;
        JButton reporteAccidenteButton = new JButton("Llenar Reporte de Accidente");
        reporteAccidenteButton.setPreferredSize(buttonSize);
        center.add(reporteAccidenteButton, gbc);

        // --- Listeners de cada botón ---
        /*
         * registroButton.addActionListener(e -> {
         * // Abre el formulario de registro de paciente nuevo
         * new Registro.AgendaDirecciones().setVisible(true);
         * dispose();
         * });
         */

        consultaButton.addActionListener(e -> {
            new Consultas.ConsultasFrame().setVisible(true);
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
                    // dispose();
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
            // dispose();
        });

        emergenciaButton.addActionListener(e -> {
            new LlamadaEmergencia().setVisible(true);
            // dispose();
        });

        reporteAccidenteButton.addActionListener(e -> {
            new AccidenteFrame().setVisible(true);
            // dispose();
        });

        add(center, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuMedicosFrame::new);
    }
}