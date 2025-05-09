package Inicio;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MenuMedicosFrame extends JFrame {

    private boolean hasNewNotification = false;
    private JButton notificationButton;
    private ImageIcon iconDefault;
    private ImageIcon iconNew;

    public MenuMedicosFrame() {
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
        iconNew     = new ImageIcon(img2);
    }

    /** Crea la barra superior con título centrado y campana a la derecha */
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

        new Timer(5000, e -> {
            hasNewNotification = true;
            notificationButton.setIcon(iconNew);
        }).start();
    }

    /** Construye el panel central con los siete botones */
    private void initCenterButtons() {
        JPanel center = new JPanel(new GridLayout(4, 2, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Dimension btnSize = new Dimension(250, 40);
        JButton consulta      = new JButton("Consulta Nueva");
        JButton gestion       = new JButton("Gestión de Enfermedades");
        JButton registro      = new JButton("Registro de Paciente Nuevo");
        JButton justificantes = new JButton("Justificantes Médicos");
        JButton emitir        = new JButton("Emitir Justificante Médico"); // NUEVO
        JButton emergencia    = new JButton("Registrar Llamada de Emergencia");
        JButton accidente     = new JButton("Llenar Reporte de Accidente");

        for (JButton b : new JButton[]{consulta, gestion, registro, justificantes,
                                       emitir, emergencia, accidente}) {
            b.setPreferredSize(btnSize);
            center.add(b);
        }

        // Asignar acciones
        consulta.addActionListener(e -> {
            Consultas.ConsultasFrame.launchApp();
            dispose();
        });
        gestion.addActionListener(e -> {
            new GestionEnfermedades.GestionEnfermedadesFrame().setVisible(true);
            dispose();
        });
        registro.addActionListener(e -> {
            new Registro.AgendaDirecciones().setVisible(true);
            dispose();
        });
        justificantes.addActionListener(e -> {
            new Justificantes.SeleccionarPacienteFrame().setVisible(true);
            dispose();
        });
        emitir.addActionListener(e -> {
            new Justificantes.EmitirJustificanteDesdeConsultaFrame().setVisible(true);
            dispose();
        });
        emergencia.addActionListener(e -> {
            new Emergencias.LlamadaEmergencia().setVisible(true);
            dispose();
        });
        accidente.addActionListener(e -> {
            new Emergencias.AccidenteFrame().setVisible(true);
            dispose();
        });

        add(center, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuMedicosFrame::new);
    }
}
