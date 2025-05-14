package Inicio;

import GestionCitas.NotificacionDAO;
import GestionCitas.NotificacionCitasFrame;
import Justificantes.MisJustificantesPacienteFrame;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class MenuPacientesFrame extends JFrame {
    private int idPaciente; // Variable para almacenar el ID del paciente

    public MenuPacientesFrame(int idPaciente) {
        this.idPaciente = idPaciente; // Guardar el ID del paciente
    }

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
        setSize(650, 400);
        setLocationRelativeTo(null);

        int idPaciente = SesionUsuario.getPacienteActual();
        boolean tieneNotif = NotificacionDAO.tieneNotificacionesNoLeidas(idPaciente);
        if (tieneNotif) {
            hasNewNotification = true;
            notificationButton.setIcon(iconNew);
        }

        setVisible(true);
    }

    private void loadIcons() {
        URL u1 = getClass().getResource("/icons/bell.png");
        URL u2 = getClass().getResource("/icons/bell_new.png");
        if (u1 == null || u2 == null) {
            JOptionPane.showMessageDialog(
                    null,
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

    private void initToolbar() {
        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setBackground(new Color(245, 245, 245));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Bienvenido al Sistema de Servicios Médicos UDLAP", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        toolbar.add(title, gbc);

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
        notificationButton.addActionListener(e -> mostrarNotificaciones());

        toolbar.add(notificationButton, gbc);
        add(toolbar, BorderLayout.NORTH);
    }

    private void mostrarNotificaciones() {
        int idPaciente = SesionUsuario.getPacienteActual();
        List<NotificacionDAO.Notificacion> notificaciones = NotificacionDAO.obtenerNotificaciones(idPaciente);

        if (notificaciones.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay nuevas notificaciones.",
                    "Notificaciones",
                    JOptionPane.INFORMATION_MESSAGE);
            notificationButton.setIcon(iconDefault);
            return;
        }

        for (NotificacionDAO.Notificacion notif : notificaciones) {
            new NotificacionCitasFrame(notif.fecha, notif.hora, notif.servicio, String.valueOf(notif.idPaciente));
        }

        hasNewNotification = false;
        notificationButton.setIcon(iconDefault);
    }

    private void initCenterButtons() {
        JPanel center = new JPanel(new GridLayout(3, 2, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Dimension btnSize = new Dimension(250, 40);

        JButton gestionCitasButton = new JButton("Gestión de Citas");
        JButton historialMedicoButton = new JButton("Historial Médico");
        JButton justificarNuevoButton = new JButton("Solicitar Justificante");
        JButton verJustificantesButton = new JButton("Mis Justificantes Emitidos"); // Nuevo
        JButton reportarEmergenciaButton = new JButton("Reportar Emergencia");

        for (JButton b : new JButton[] {
                gestionCitasButton, historialMedicoButton,
                justificarNuevoButton, verJustificantesButton,
                reportarEmergenciaButton }) {
            b.setPreferredSize(btnSize);
            center.add(b);
        }

        gestionCitasButton.addActionListener(e -> {
            new GestionCitas.InicioFrame().setVisible(true);
            dispose();
        });

        historialMedicoButton.addActionListener(e -> {
            new GestionEnfermedades.VerDatosPaciente(idPaciente).setVisible(true); // Usar el ID del paciente
            dispose();
        });

        justificarNuevoButton.addActionListener(e -> {
            new Justificantes.FormularioJustificanteFrame().setVisible(true);
            dispose();
        });

        verJustificantesButton.addActionListener(e -> {
            int idPaciente = SesionUsuario.getPacienteActual();
            new MisJustificantesPacienteFrame(String.valueOf(idPaciente)).setVisible(true);
            dispose();
        });

        reportarEmergenciaButton.addActionListener(e -> {
            new Emergencias.MenuEmergenciaFrame().setVisible(true);
            dispose();
        });

        add(center, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new MenuPacientesFrame(0); // Llamar al constructor con un ID de paciente
    }
}
