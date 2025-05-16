package Inicio;

import Utilidades.*;
import BaseDeDatos.ConexionSQLite;
import Consultas.PanelConsultaNueva;
import Emergencias.PanelLlamadaEmergencia;
<<<<<<< Updated upstream
import GestionCitas.NotificacionDAO;
import GestionCitas.PanelGestionCitas;
=======
import GestionEnfermedades.PanelEditarDatosPaciente;
>>>>>>> Stashed changes
import Justificantes.PanelJustificantesProvider;
import Justificantes.PanelMenuJustificantes;
import GestionCitas.AgendaCitaFrame;
import GestionCitas.ModificarCitaFrame;
import Registro.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import java.util.List;

public class InterfazMedica extends JFrame {
    private JPanel contentPanel;
    private final boolean esMedico;
    private final int userId;
    private String nombreUsuario;
    private PanelManager panelManager;
    private JLabel notificationIcon;
    private ImageIcon iconDefault, iconNew;
    private boolean hasNewNotification = false;

    public InterfazMedica(boolean esMedico, int userId) {
        this.esMedico = esMedico;
        this.userId = userId;
        this.nombreUsuario = fetchNombreUsuario();
        loadNotificationIcons();
        initUI();
        checkNotifications();
    }

    private void loadNotificationIcons() {
        try {
            URL u1 = getClass().getResource("/icons/bell.png");
            URL u2 = getClass().getResource("/icons/bell_new.png");

            if (u1 == null || u2 == null) throw new Exception("Iconos no encontrados");

            Image img1 = new ImageIcon(u1).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            Image img2 = new ImageIcon(u2).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            iconDefault = new ImageIcon(img1);
            iconNew = new ImageIcon(img2);
        } catch (Exception e) {
            iconDefault = new ImageIcon();
            iconNew = new ImageIcon();
        }
    }

private void checkNotifications() {
    if (!esMedico) {
        hasNewNotification = NotificacionDAO.tieneNotificacionesNoLeidas(userId);
        System.out.println("¿Tiene notificaciones pendientes? " + hasNewNotification + " para userId=" + userId);
    }

    if (notificationIcon != null) {
        if (hasNewNotification) {
            System.out.println("Mostrando icono con notificaciones.");
            notificationIcon.setIcon(iconNew);
        } else {
            System.out.println("Mostrando icono sin notificaciones.");
            notificationIcon.setIcon(iconDefault);
        }
    }
}



    private void initUI() {
        setUndecorated(true);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel contenedorBarras = new JPanel();
        contenedorBarras.setLayout(new BoxLayout(contenedorBarras, BoxLayout.Y_AXIS));
        contenedorBarras.add(new BarraVentanaUDLAP(this));
        contenedorBarras.add(crearTopPanel());
        add(contenedorBarras, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColoresUDLAP.BLANCO);
        add(mainPanel, BorderLayout.CENTER);

        JPanel menuPanel = crearMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        panelManager = new PanelManager(contentPanel);
        registrarPaneles();
        panelManager.showPanel("panel0");
    }

    private JPanel crearMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(ColoresUDLAP.BLANCO);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 8, 8, 8));

        String[] items = esMedico
                ? new String[]{
                    "Registrar Paciente\nNuevo", "Consulta Nueva", "Editar Datos del\nPaciente",
                    "Justificantes Médicos", "Registrar Llamada\nde Emergencia", "Llenar Reporte de\nAccidente"
                }
                : new String[]{
                    "Gestión de Citas", "Historial Médico", "Solicitar Justificante",
                    "Mis Justificantes", "Reportar Emergencia"
                };

        Font btnFont = new Font("Arial", Font.BOLD, 21);

        for (int i = 0; i < items.length; i++) {
            String texto = "<html><div style='text-align:center;'>" + items[i].replace("\n", "<br>") + "</div></html>";
            Color base = (i % 2 == 0) ? ColoresUDLAP.VERDE : ColoresUDLAP.NARANJA;
            Color hover = (i % 2 == 0) ? ColoresUDLAP.VERDE_HOVER : ColoresUDLAP.NARANJA_HOVER;
            JButton boton = botonTransparente(texto, base, hover, btnFont);
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.setMaximumSize(new Dimension(260, 80));
            final int idx = i;
            boton.addActionListener(e -> manejarClickBoton(idx));
            menuPanel.add(boton);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 13)));
        }

        JButton btnEmergencia = botonTransparente("<html><div style='text-align:center;'>Emergencias</div></html>",
                ColoresUDLAP.ROJO, ColoresUDLAP.ROJO_HOVER, btnFont);
        btnEmergencia.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEmergencia.setPreferredSize(new Dimension(260, 56));
        btnEmergencia.addActionListener(e -> panelManager.showPanel("emergencia"));
        menuPanel.add(btnEmergencia);

        menuPanel.add(Box.createVerticalGlue());
        return menuPanel;
    }

    private void manejarClickBoton(int idx) {
        if (esMedico) {
            switch (idx) {
                case 0 -> panelManager.showPanel("formularioRegistro");
                case 1 -> panelManager.showPanel("consultaNueva");
                case 2 -> panelManager.showPanel("editarDatosPaciente");
                case 3 -> {
                    JPanel nuevoPanel = new PanelMenuJustificantes();
                    contentPanel.add(nuevoPanel, "justificantesMedicos");
                    ((CardLayout) contentPanel.getLayout()).show(contentPanel, "justificantesMedicos");
                }
                case 4 -> panelManager.showPanel("llamadaEmergencia");
                case 5 -> panelManager.showPanel("reporteAccidente");
            }
        } else {
            switch (idx) {
                case 0 -> panelManager.showPanel("panelGestionCitas");
                case 1 -> panelManager.showPanel("historialMedico");
                case 2 -> panelManager.showPanel("solicitarJustificante");
                case 3 -> panelManager.showPanel("misJustificantes");
                case 4 -> panelManager.showPanel("reportarEmergencia");
            }
        }
    }

<<<<<<< Updated upstream
private void registrarPaneles() {
    for (int i = 0; i < (esMedico ? 6 : 5); i++) {
        final int idx = i;
=======
    private void registrarPaneles() {
        for (int i = 0; i < (esMedico ? 6 : 5); i++) {
            final int idx = i;
            panelManager.registerPanel(new PanelProvider() {
                @Override
                public JPanel getPanel() {
                    JPanel panel = new JPanel();
                    panel.setBackground(Color.WHITE);
                    JLabel lbl = new JLabel("Panel " + (idx + 1), SwingConstants.CENTER);
                    lbl.setFont(new Font("Arial", Font.BOLD, 26));
                    panel.add(lbl);
                    return panel;
                }

                @Override
                public String getPanelName() {
                    return "panel" + idx;
                }
            });

            // Registrar panel de formulario de registro
            panelManager.registerPanel(new PanelRegistroPaciente());
            // Registrar panel de consulta nueva
            panelManager.registerPanel(new PanelConsultaNueva(userId, nombreUsuario));
            // Registrar panel de edición de datos del paciente
            panelManager.registerPanel(new PanelEditarDatosPaciente(esMedico, userId));

            // Registrar panel de llamada de emergencia
            panelManager.registerPanel(new PanelLlamadaEmergencia(esMedico, userId));

        }

        // Panel de emergencias
>>>>>>> Stashed changes
        panelManager.registerPanel(new PanelProvider() {
            @Override
            public JPanel getPanel() {
                JPanel panel = new JPanel();
                panel.setBackground(Color.WHITE);
                panel.add(new JLabel("Panel " + (idx + 1)));
                return panel;
            }

            @Override
            public String getPanelName() {
<<<<<<< Updated upstream
                return "panel" + idx;
=======
                return "emergencia";
            }
        });

        // Paneles funcionales ya existentes
        panelManager.registerPanel(new PanelRegistroPaciente());
        panelManager.registerPanel(new PanelConsultaNueva(userId, nombreUsuario));
        panelManager.registerPanel(new PanelLlamadaEmergencia(esMedico, userId));

        // ➕ Panel funcional de Justificantes
        panelManager.registerPanel(new PanelJustificantesProvider() {
            @Override
            public String getPanelName() {
                return "justificantesMedicos";
>>>>>>> Stashed changes
            }
        });
    }

    panelManager.registerPanel(new PanelProvider() {
        @Override
        public JPanel getPanel() {
            return new JPanel(new BorderLayout()) {{
                add(new JLabel("Panel de Emergencias", SwingConstants.CENTER));
            }};
        }

        @Override
        public String getPanelName() {
            return "emergencia";
        }
    });

    panelManager.registerPanel(new PanelRegistroPaciente());
    panelManager.registerPanel(new PanelConsultaNueva(userId, nombreUsuario));
    panelManager.registerPanel(new PanelLlamadaEmergencia());

    panelManager.registerPanel(new PanelJustificantesProvider() {
        @Override
        public String getPanelName() {
            return "justificantesMedicos";
        }
    });

    if (!esMedico) {
    // Gestión de citas general
    panelManager.registerPanel(new PanelProvider() {
        @Override
        public JPanel getPanel() {
            return new PanelGestionCitas(userId, panelManager);
        }

        @Override
        public String getPanelName() {
            return "panelGestionCitas";  // Cambiado de "gestionCitas" a "panelGestionCitas" para consistencia
        }
    });


    // Panel de Agendar Cita
    panelManager.registerPanel(new PanelProvider() {
        @Override
        public JPanel getPanel() {
            return new AgendaCitaFrame(userId, panelManager);
        }

        @Override
        public String getPanelName() {
            return "agendarCita";
        }
    });



    
    // Panel de Modificar Cita
    panelManager.registerPanel(new PanelProvider() {
        @Override
        public JPanel getPanel() {
            return new ModificarCitaFrame(userId, panelManager);
        }

        @Override
        public String getPanelName() {
            return "modificarCita";
        }
    });

    }
}


    private JPanel crearTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ColoresUDLAP.BLANCO);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        topPanel.setPreferredSize(new Dimension(0, 56));

        JLabel saludo = new JLabel("Hola, " + nombreUsuario);
        saludo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        saludo.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 12));
        left.setOpaque(false);
        left.add(saludo);

        JLabel titulo = new JLabel(
                "<html><span style='font-size:25pt;font-weight:bold;color:#006400;'>Servicios Médicos</span> " +
                        "<span style='font-size:25pt;font-weight:bold;color:#FF6600;'>UDLAP</span></html>",
                SwingConstants.CENTER);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        center.setOpaque(false);
        center.add(titulo);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        right.setOpaque(false);

        notificationIcon = new JLabel(iconDefault);
        notificationIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        notificationIcon.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
                System.out.println("Campana clickeada por: " + userId); // ← DEBUG
                mostrarNotificaciones();
            }
        });

        right.add(notificationIcon);

        JButton cerrarSesion = new JButton("Cerrar sesión");
        cerrarSesion.setFont(new Font("Arial", Font.BOLD, 15));
        cerrarSesion.setBackground(ColoresUDLAP.NARANJA_BARRA);
        cerrarSesion.setForeground(Color.WHITE);
        cerrarSesion.setFocusPainted(false);
        cerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cerrarSesion.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        cerrarSesion.addActionListener(e -> {
            new InterfazLogin().setVisible(true);
            dispose();
        });
        right.add(cerrarSesion);

        topPanel.add(left, BorderLayout.WEST);
        topPanel.add(center, BorderLayout.CENTER);
        topPanel.add(right, BorderLayout.EAST);
        return topPanel;
    }

private void mostrarNotificaciones() {
    if (esMedico) {
        JOptionPane.showMessageDialog(this,
                "Funcionalidad de notificaciones para médicos en desarrollo.",
                "Notificaciones Médicas",
                JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    List<NotificacionDAO.Notificacion> lista = NotificacionDAO.obtenerNotificaciones(userId);
    if (lista.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "No hay nuevas notificaciones.",
                "Notificaciones",
                JOptionPane.INFORMATION_MESSAGE);
        notificationIcon.setIcon(iconDefault);
    } else {
for (NotificacionDAO.Notificacion n : lista) {
    System.out.println("[NOTIFICACIÓN PENDIENTE] Para ID: " + userId);
    System.out.println(" → Fecha: " + n.fecha);
    System.out.println(" → Hora: " + n.hora);
    System.out.println(" → Servicio: " + n.servicio);
    System.out.println(" → Estado: pendiente");
    
    // Crea el panel emergente para aceptar o rechazar la cita
    new GestionCitas.NotificacionCitasFrame(
        n.fecha,
        n.hora,
        n.servicio,
        String.valueOf(userId)
    );
}

        hasNewNotification = false;
        notificationIcon.setIcon(iconDefault);
    }
}



    private JButton botonTransparente(String texto, Color base, Color hover, Font font) {
        JButton button = new JButton(texto) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private String fetchNombreUsuario() {
        String sql = esMedico
                ? "SELECT Nombre||' '||ApellidoPaterno FROM InformacionMedico WHERE ID=?"
                : "SELECT Nombre||' '||ApellidoPaterno FROM InformacionAlumno WHERE ID=?";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Usuario";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazMedica(true, 1).setVisible(true));
    }
}
