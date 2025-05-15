package Inicio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import BaseDeDatos.ConexionSQLite; // Ajusta si cambias de paquete

public class InterfazMedica extends JFrame {
    private Point mouseDownCompCoords;
    private final JLabel contentLabel = new JLabel("Selecciona una opción del menú", SwingConstants.CENTER);
    private final boolean esMedico;
    private final int userId;

    public InterfazMedica(boolean esMedico, int userId) {
        this.esMedico = esMedico;
        this.userId = userId;
        initUI();
    }

    private void initUI() {
        // Ventana
        setUndecorated(true);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Barra naranja
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bar.setPreferredSize(new Dimension(0, 30));
        bar.setBackground(new Color(255, 102, 0));
        bar.add(createControlButton("_"));
        bar.add(createControlButton("□"));
        bar.add(createControlButton("✕"));
        enableDrag(bar);
        add(bar, BorderLayout.NORTH);

        // Menú lateral
        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setPreferredSize(new Dimension(120, 0));
        panelMenu.setBackground(Color.WHITE);

        // Header menú
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

        // Botones dinámicos
        String[] items = esMedico
                ? new String[] {
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

        JPanel panelButtons = new JPanel(
                new GridLayout(items.length + 1, 1));
        panelButtons.setBackground(Color.WHITE);
        Color verde = Color.decode("#006400");
        for (int i = 0; i < items.length; i++) {
            JButton b = new JButton(items[i]);
            b.setFocusPainted(false);
            b.setOpaque(true);
            b.setBackground(verde);
            b.setForeground(Color.WHITE);
            b.setBorder(BorderFactory.createMatteBorder(
                    0, 0, 1, 0, new Color(200, 200, 200)));
            final int idx = i;
            b.addActionListener(e -> contentLabel.setText("Has seleccionado: " + items[idx]));
            panelButtons.add(b);
        }
        // SOS
        ImageIcon sosOrig = new ImageIcon(
                "C:\\Users\\cosa2\\OneDrive - Fundacion Universidad de las Americas Puebla\\"
                        + "4° Semestre\\Programación Orientada a Objetos\\ServiciosMedicos-POO\\SOS.png");
        Image img = sosOrig.getImage()
                .getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JButton btnSOS = new JButton(new ImageIcon(img));
        btnSOS.setFocusPainted(false);
        btnSOS.setBackground(Color.WHITE);
        btnSOS.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnSOS.addActionListener(e -> JOptionPane.showMessageDialog(this, "¡SOS activado!"));
        panelButtons.add(btnSOS);

        panelMenu.add(panelButtons, BorderLayout.CENTER);
        add(panelMenu, BorderLayout.WEST);

        // Panel central
        JPanel center = new JPanel(new BorderLayout());
        add(center, BorderLayout.CENTER);

        // Header
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

        // Contenido
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.add(contentLabel, BorderLayout.NORTH);
        center.add(content, BorderLayout.CENTER);
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
        return null;
    }

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
                setLocation(curr.x - mouseDownCompCoords.x,
                        curr.y - mouseDownCompCoords.y);
            }
        });
    }
}
