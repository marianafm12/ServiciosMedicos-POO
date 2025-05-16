package Inicio;

import Utilidades.*;
import BaseDeDatos.ConexionSQLite;
import Consultas.PanelConsultaNueva;
import Emergencias.PanelLlamadaEmergencia;
import Emergencias.PanelReportarEmergencia;
import GestionCitas.NotificacionDAO;
import GestionCitas.PanelGestionCitas;
import GestionEnfermedades.PanelHistorialMedico;
import Justificantes.PanelJustificantesProvider;
import Justificantes.PanelMenuJustificantes;
import Registro.PanelRegistroPaciente;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.*;

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
            iconDefault = new ImageIcon(new ImageIcon(u1).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
            iconNew = new ImageIcon(new ImageIcon(u2).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            iconDefault = new ImageIcon();
            iconNew = new ImageIcon();
        }
    }

    private void checkNotifications() {
        if (!esMedico) {
            hasNewNotification = NotificacionDAO.tieneNotificacionesNoLeidas(userId);
        }
        if (notificationIcon != null) {
            notificationIcon.setIcon(hasNewNotification ? iconNew : iconDefault);
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
        panelManager.showPanel("inicio");
    }

    private JPanel crearMenuPanel() {
        JPanel menu = new JPanel();
        menu.setBackground(ColoresUDLAP.BLANCO);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(15, 8, 8, 8));

        String[] items = esMedico
                ? new String[] { "Registrar Paciente", "Consulta Nueva", "Historial Médico", "Justificantes",
                        "Emergencias", "Accidente" }
                : new String[] { "Mis Citas", "Historial Médico", "Justificantes", "Reportar Emergencia" };

        Font btnFont = new Font("Arial", Font.BOLD, 20);

        for (int i = 0; i < items.length; i++) {
            String texto = "<html><div style='text-align:center;'>" + items[i].replace("\n", "<br>") + "</div></html>";
            Color base = (i % 2 == 0) ? ColoresUDLAP.VERDE : ColoresUDLAP.NARANJA;
            Color hover = (i % 2 == 0) ? ColoresUDLAP.VERDE_HOVER : ColoresUDLAP.NARANJA_HOVER;
            JButton boton = botonTransparente(texto, base, hover, btnFont);
            int idx = i;
            boton.addActionListener(e -> manejarClick(idx));
            boton.setMaximumSize(new Dimension(260, 80));
            menu.add(boton);
            menu.add(Box.createVerticalStrut(10));
        }

        return menu;
    }

    private void registrarPaneles() {
        if (esMedico) {
            // Panel: Registro de paciente
            panelManager.registerPanel(new PanelRegistroPaciente());

            // Panel: Consulta nueva
            panelManager.registerPanel(new PanelConsultaNueva(userId, nombreUsuario));

            // Panel: Historial médico (paciente) accesado por el médico
            panelManager.registerPanel(new PanelHistorialMedico(userId));

            // Panel: Justificantes médicos
            panelManager.registerPanel(new PanelProvider() {
                public JPanel getPanel() {
                    return new PanelMenuJustificantes();
                }

                public String getPanelName() {
                    return "justificantes";
                }
            });

            // Panel: Llamada de emergencia
            panelManager.registerPanel(new PanelLlamadaEmergencia(esMedico, userId));

            // Panel: Reporte de accidente (placeholder por ahora)
            panelManager.registerPanel(new PanelProvider() {
                public JPanel getPanel() {
                    JPanel panel = new JPanel();
                    panel.setBackground(Color.WHITE);
                    panel.add(new JLabel("Formulario de Accidente (pendiente)"));
                    return panel;
                }

                public String getPanelName() {
                    return "reporteAccidente";
                }
            });

        } else {
            panelManager.registerPanel(new PanelProvider() {
                @Override
                public JPanel getPanel() {
                    return new PanelHistorialMedico(userId);
                }

                @Override
                public String getPanelName() {
                    return "historialMedico";
                }
            });

            // Panel: Gestión de citas
            panelManager.registerPanel(new PanelProvider() {
                public JPanel getPanel() {
                    return new PanelGestionCitas(userId, panelManager);
                }

                public String getPanelName() {
                    return "panelGestionCitas";
                }
            });

            // Panel: Historial médico del paciente
            panelManager.registerPanel(new PanelHistorialMedico(userId));

            // Panel: Justificantes (paciente)
            panelManager.registerPanel(new PanelJustificantesProvider() {
                public String getPanelName() {
                    return "justificantesPaciente";
                }
            });

            // Panel: Reportar emergencia
            panelManager.registerPanel(new PanelReportarEmergencia());
        }
    }

    private void manejarClick(int idx) {
        String[] medicoKeys = { "formularioRegistro", "consultaNueva", "editarDatosPaciente", "justificantes",
                "llamadaEmergencia", "reporteAccidente" };
        String[] pacienteKeys = { "panelGestionCitas", "historialMedico", "justificantesPaciente",
                "reportarEmergencia" };
        String key = esMedico ? medicoKeys[idx] : pacienteKeys[idx];
        panelManager.showPanel(key);
    }

    private JPanel crearTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColoresUDLAP.BLANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        panel.setPreferredSize(new Dimension(0, 56));

        JLabel saludo = new JLabel("Hola, " + nombreUsuario);
        saludo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        saludo.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 12));
        left.setOpaque(false);
        left.add(saludo);

        JLabel titulo = new JLabel(
                "<html><span style='font-size:25pt;font-weight:bold;color:#006400;'>Servicios Médicos</span> <span style='font-size:25pt;font-weight:bold;color:#FF6600;'>UDLAP</span></html>",
                SwingConstants.CENTER);
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        center.setOpaque(false);
        center.add(titulo);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        right.setOpaque(false);

        notificationIcon = new JLabel(iconDefault);
        notificationIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

        panel.add(left, BorderLayout.WEST);
        panel.add(center, BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);

        return panel;
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
                if (rs.next())
                    return rs.getString(1);
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
