package Inicio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import BaseDeDatos.ConexionSQLite;

public class InterfazMedica extends JFrame {
    private Point mouseDownCompCoords;
    private final boolean esMedico;
    private final int userId;
    private JPanel contentPanel; // Panel dinámico central

    public InterfazMedica(boolean esMedico, int userId) {
        this.esMedico = esMedico;
        this.userId = userId;
        initUI();
    }

    private void initUI() {
        setUndecorated(true);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Barra naranja superior
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bar.setPreferredSize(new Dimension(0, 30));
        bar.setBackground(new Color(255, 102, 0));
        bar.add(createControlButton("_"));
        bar.add(createControlButton("□"));
        bar.add(createControlButton("✕"));
        enableDrag(bar);
        add(bar, BorderLayout.NORTH);

        // Menú lateral izquierdo
        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setPreferredSize(new Dimension(120, 0));
        panelMenu.setBackground(Color.WHITE);

        // Header del menú
        JPanel menuHeader = new JPanel(new GridLayout(2, 1));
        menuHeader.setBackground(Color.WHITE);
        String nombre = fetchNombreUsuario();
        JLabel lblUsuario = new JLabel(
                nombre != null ? "Hola, " + nombre : "Hola, usuario",
                SwingConstants.CENTER);
        lblUsuario.setFont(lblUsuario.getFont().deriveFont(Font.PLAIN, 12f));
        JLabel lblMenu = new JLabel("Menú", SwingConstants.CENTER);
        lblMenu.setFont(lblMenu.getFont().deriveFont(Font.BOLD, 14f));
        menuHeader.add(lblUsuario);
        menuHeader.add(lblMenu);
        panelMenu.add(menuHeader, BorderLayout.NORTH);

        // Opciones dinámicas según tipo de usuario
        String[] items = esMedico
                ? new String[] {
                        "Registrar Paciente Nuevo",
                        "Consulta Nueva",
                        "Editar Datos del Paciente",
                        "Justificantes Médicos",
                        "Registrar Llamada de Emergencia",
                        "Llenar Reporte de Accidente"
                }
                : new String[] {
                        "Gestión de Citas",
                        "Historial Médico",
                        "Solicitar Justificante",
                        "Mis Justificantes",
                        "Reportar Emergencia"
                };

        JPanel panelButtons = new JPanel(new GridLayout(items.length + 1, 1));
        panelButtons.setBackground(Color.WHITE);
        Color verde = Color.decode("#006400");

        // Panel central dinámico
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Color.WHITE);

        // Paneles de contenido (uno por opción)
        contentPanel.add(PanelesContenidoFactory.crearPanelInicio(), "inicio");
        if (esMedico) {
            contentPanel.add(PanelesContenidoFactory.crearPanelRegistrarPacienteNuevo(), "panel0"); // Registrar
                                                                                                    // Paciente Nuevo
            contentPanel.add(PanelesContenidoFactory.crearPanelConsultaNueva(), "panel1"); // Consulta Nueva
            contentPanel.add(PanelesContenidoFactory.crearPanelEditarDatos(), "panel2");
            contentPanel.add(PanelesContenidoFactory.crearPanelJustificantesMedicos(), "panel3");
            contentPanel.add(PanelesContenidoFactory.crearPanelLlamadaEmergencia(), "panel4");
            contentPanel.add(PanelesContenidoFactory.crearPanelReporteAccidente(), "panel5");
        } else {
            contentPanel.add(PanelesContenidoFactory.crearPanelGestionCitas(), "panel0");
            contentPanel.add(PanelesContenidoFactory.crearPanelHistorialMedico(), "panel1");
            contentPanel.add(PanelesContenidoFactory.crearPanelSolicitarJustificante(), "panel2");
            contentPanel.add(PanelesContenidoFactory.crearPanelMisJustificantes(), "panel3");
            contentPanel.add(PanelesContenidoFactory.crearPanelReportarEmergencia(), "panel4");
        }

        // Botones de menú que cambian el contenido central
        for (int i = 0; i < items.length; i++) {
            JButton b = new JButton(items[i]);
            b.setFocusPainted(false);
            b.setOpaque(true);
            b.setBackground(verde);
            b.setForeground(Color.WHITE);
            b.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
            final int idx = i;
            b.addActionListener(e -> mostrarPanel("panel" + idx));
            panelButtons.add(b);
        }

        // Botón SOS al final
        ImageIcon sosOrig = new ImageIcon(
                "C:\\Users\\cosa2\\OneDrive - Fundacion Universidad de las Americas Puebla\\" +
                        "4° Semestre\\Programación Orientada a Objetos\\ServiciosMedicos-POO\\SOS.png");
        Image img = sosOrig.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JButton btnSOS = new JButton(new ImageIcon(img));
        btnSOS.setFocusPainted(false);
        btnSOS.setBackground(Color.WHITE);
        btnSOS.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnSOS.addActionListener(e -> JOptionPane.showMessageDialog(this, "¡SOS activado!"));
        panelButtons.add(btnSOS);

        panelMenu.add(panelButtons, BorderLayout.CENTER);
        add(panelMenu, BorderLayout.WEST);

        // Panel central, incluye header superior y panel dinámico
        JPanel center = new JPanel(new BorderLayout());
        add(center, BorderLayout.CENTER);

        // Header superior (Servicios Médicos UDLAP)
        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 60));
        String html = "<html>"
                + "<span style='font-size:18pt; color:#006400;'>Servicios Médicos</span> "
                + "<span style='font-size:18pt; color:#FF6600;'>UDLAP</span>"
                + "</html>";
        JLabel lblHeader = new JLabel(html, SwingConstants.CENTER);
        JButton btnLogout = new JButton("Cerrar sesión");
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            new InterfazLogin().setVisible(true);
            dispose();
        });
        header.add(lblHeader, BorderLayout.CENTER);
        header.add(btnLogout, BorderLayout.EAST);
        center.add(header, BorderLayout.NORTH);

        // Panel de contenido dinámico (CardLayout)
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.add(contentPanel, BorderLayout.CENTER);
        center.add(content, BorderLayout.CENTER);

        // Muestra el panel de inicio por defecto
        mostrarPanel("inicio");
    }

    // Método para mostrar un panel específico
    private void mostrarPanel(String panelName) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, panelName);
    }

    // Consulta nombre de usuario (médico o paciente)
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
        return null;
    }

    // Botones de control de ventana (minimizar, maximizar, cerrar)
    private JButton createControlButton(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Dialog", Font.BOLD, 12));
        b.setPreferredSize(new Dimension(45, 30));
        b.setFocusPainted(false);
        b.setBorder(null);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(255, 102, 0));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(230, 80, 0));
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(new Color(255, 102, 0));
            }
        });
        switch (texto) {
            case "_":
                b.addActionListener(e -> setState(Frame.ICONIFIED));
                break;
            case "□":
                b.addActionListener(e -> toggleMaximize());
                break;
            case "✕":
                b.addActionListener(e -> System.exit(0));
                break;
        }
        return b;
    }

    private void toggleMaximize() {
        if ((getExtendedState() & Frame.MAXIMIZED_BOTH) != Frame.MAXIMIZED_BOTH)
            setExtendedState(Frame.MAXIMIZED_BOTH);
        else
            setExtendedState(Frame.NORMAL);
    }

    // Permite arrastrar la ventana sin bordes
    private void enableDrag(JComponent comp) {
        comp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }
        });
        comp.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point curr = e.getLocationOnScreen();
                setLocation(curr.x - mouseDownCompCoords.x, curr.y - mouseDownCompCoords.y);
            }
        });
    }
}
