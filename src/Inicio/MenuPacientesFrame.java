package Inicio;

import GestionCitas.NotificacionDAO; // ← Importación necesaria

import javax.swing.*;
import java.awt.*;
import java.net.URL;


public class MenuPacientesFrame extends JFrame {

    private boolean hasNewNotification = false;
    private JButton notificationButton;
    private ImageIcon iconDefault;
    private ImageIcon iconNew;

    public MenuPacientesFrame() {
        super("Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        loadIcons();
        initToolbar();
        initCenterButtons();
        setSize(600, 300);
        setLocationRelativeTo(null);

        // Verificar si el paciente tiene notificaciones reales
        int idPaciente = SesionUsuario.getPacienteActual(); // Clase utilitaria
        boolean tieneNotif = NotificacionDAO.tieneNotificacionesNoLeidas(idPaciente);
        if (tieneNotif) {
            hasNewNotification = true;
            notificationButton.setIcon(iconNew);
        }

        setVisible(true);
    }

    /** Carga los iconos de la campana desde el classpath */
    private void loadIcons() {
        URL u1 = getClass().getResource("/icons/bell.png");
        URL u2 = getClass().getResource("/icons/bell_new.png");
        if (u1 == null || u2 == null) {
            JOptionPane.showMessageDialog(
                null,
                "No se encontraron /icons/bell.png o /icons/bell_new.png en el classpath.",
                "Recursos no encontrados",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }
        Image img1 = new ImageIcon(u1).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        Image img2 = new ImageIcon(u2).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        iconDefault = new ImageIcon(img1);
        iconNew = new ImageIcon(img2);
    }

    /** Barra superior con título centrado y campana a la derecha */
    private void initToolbar() {
        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setBackground(new Color(245, 245, 245));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        GridBagConstraints gbc = new GridBagConstraints();

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Bienvenido al Sistema de Servicios Médicos UDLAP", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        toolbar.add(title, gbc);

        // Botón campana
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;

        notificationButton = new JButton(iconDefault);
        notificationButton.setToolTipText("Notificaciones");
        notificationButton.setPreferredSize(new Dimension(40, 40));
        notificationButton.setFocusPainted(false);
        notificationButton.setContentAreaFilled(false);
        notificationButton.setBorderPainted(false);
        notificationButton.addActionListener(e -> {
            hasNewNotification = false;
            notificationButton.setIcon(iconDefault);
            JOptionPane.showMessageDialog(
                this,
                "No hay nuevas notificaciones.",
                "Notificaciones",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        toolbar.add(notificationButton, gbc);
        add(toolbar, BorderLayout.NORTH);
    }

    /** Panel central con 4 botones principales */
    private void initCenterButtons() {
        JPanel center = new JPanel(new GridLayout(2, 2, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Dimension btnSize = new Dimension(250, 40);

        JButton gestionCitasButton = new JButton("Gestión de Citas");
        JButton historialMedicoButton = new JButton("Historial Médico");
        JButton justificantesMedicosButton = new JButton("Justificantes Médicos");
        JButton reportarEmergenciaButton = new JButton("Reportar Emergencia");

        for (JButton b : new JButton[] {
                gestionCitasButton, historialMedicoButton,
                justificantesMedicosButton, reportarEmergenciaButton }) {
            b.setPreferredSize(btnSize);
            center.add(b);
        }

        gestionCitasButton.addActionListener(e -> {
            new GestionCitas.InicioFrame().setVisible(true);
            dispose();
        });

        historialMedicoButton.addActionListener(e -> {
            new GestionEnfermedades.HistorialMedicoFrame().setVisible(true);
            dispose();
        });

        justificantesMedicosButton.addActionListener(e -> {
            new Justificantes.FormularioJustificanteFrame().setVisible(true);
            dispose();
        });

        reportarEmergenciaButton.addActionListener(e -> {
            new Emergencias.MenuEmergenciaFrame().setVisible(true);
            dispose();
        });

        add(center, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuPacientesFrame::new);
    }
}
