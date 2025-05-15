package Inicio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import BaseDeDatos.ConexionSQLite; // Adaptar al paquete donde tenga su método conectar()

public class InterfazLogin extends JFrame {
    private final JTextField txtID = new JTextField(15);
    private final JPasswordField txtPass = new JPasswordField(15);

    public InterfazLogin() {
        super("Servicios Médicos UDLAP - Login");
        setUndecorated(true);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Barra superior naranja ===
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(255, 102, 0));
        bar.setPreferredSize(new Dimension(0, 30));
        bar.add(createControlPanel(), BorderLayout.EAST);
        enableDrag(bar);
        add(bar, BorderLayout.NORTH);

        // === Panel central (fondo blanco) con contenido centrado ===
        JPanel main = new JPanel();
        main.setBackground(Color.WHITE);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        add(main, BorderLayout.CENTER);

        main.add(Box.createVerticalGlue());

        // Título “Servicios Médicos UDLAP”
        JLabel lblTitulo = new JLabel(
                "<html>"
                        + "<span style='font-size:24pt; font-weight:bold; color:#006400;'>Servicios Médicos</span> "
                        + "<span style='font-size:24pt; font-weight:bold; color:#FF6600;'>UDLAP</span>"
                        + "</html>",
                SwingConstants.CENTER);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(lblTitulo);
        main.add(Box.createVerticalStrut(20));

        // Subtítulo “Log In”
        JLabel lblLogin = new JLabel("Log In", SwingConstants.CENTER);
        lblLogin.setFont(lblLogin.getFont().deriveFont(Font.PLAIN, 18f));
        lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(lblLogin);
        main.add(Box.createVerticalStrut(20));

        // Formulario de credenciales
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        form.add(txtID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        form.add(txtPass, gbc);

        main.add(form);
        main.add(Box.createVerticalStrut(20));

        // Botones de acción
        JPanel botones = new JPanel();
        botones.setBackground(Color.WHITE);
        botones.setLayout(new BoxLayout(botones, BoxLayout.Y_AXIS));

        JButton btnIniciar = new JButton("Iniciar Sesión");
        btnIniciar.setFont(btnIniciar.getFont().deriveFont(Font.BOLD, 14f));
        btnIniciar.setBackground(new Color(255, 102, 0));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        botones.add(btnIniciar);
        botones.add(Box.createVerticalStrut(10));

        JButton btnRecuperar = new JButton("Recuperar contraseña");
        btnRecuperar.setFont(btnRecuperar.getFont().deriveFont(Font.BOLD, 14f));
        btnRecuperar.setBackground(new Color(0, 100, 0));
        btnRecuperar.setForeground(Color.WHITE);
        btnRecuperar.setFocusPainted(false);
        btnRecuperar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRecuperar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        botones.add(btnRecuperar);

        main.add(botones);
        main.add(Box.createVerticalGlue());

        // Panel inferior con icono SOS pequeño
        String sosPath = "C:\\Users\\cosa2\\OneDrive - Fundacion Universidad de las Americas Puebla\\"
                + "4° Semestre\\Programación Orientada a Objetos\\"
                + "ServiciosMedicos-POO\\SOS.png";
        ImageIcon sosOrig = new ImageIcon(sosPath);
        Image sosImg = sosOrig.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel lblSOS = new JLabel(new ImageIcon(sosImg));

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        footer.setBackground(Color.WHITE);
        footer.setPreferredSize(new Dimension(0, 40));
        footer.add(lblSOS);
        add(footer, BorderLayout.SOUTH);

        // ─── Lógica de login unificado ───
        btnIniciar.addActionListener(e -> {
            String idText = txtID.getText().trim();
            String password = new String(txtPass.getPassword());

            if (idText.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, ingrese sus credenciales.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID inválido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Abrimos conexión una sola vez
            try (Connection con = ConexionSQLite.conectar()) {
                // 1) Intentamos tabla de médicos
                String sqlMed = "SELECT 1 FROM InformacionMedico WHERE ID = ? AND Contraseña = ?";
                try (PreparedStatement psMed = con.prepareStatement(sqlMed)) {
                    psMed.setInt(1, id);
                    psMed.setString(2, password);
                    try (ResultSet rs = psMed.executeQuery()) {
                        if (rs.next()) {
                            new MenuMedicosFrame().setVisible(true);
                            dispose();
                            return;
                        }
                    }
                }

                // 2) Si no es médico, intentamos tabla de alumnos
                String sqlPac = "SELECT ID FROM InformacionAlumno WHERE ID = ? AND Contraseña = ?";
                try (PreparedStatement psPac = con.prepareStatement(sqlPac)) {
                    psPac.setInt(1, id);
                    psPac.setString(2, password);
                    try (ResultSet rs = psPac.executeQuery()) {
                        if (rs.next()) {
                            new MenuPacientesFrame(id).setVisible(true);
                            dispose();
                            return;
                        }
                    }
                }

                // 3) Ninguna coincidencia
                JOptionPane.showMessageDialog(this,
                        "ID o contraseña incorrectos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error en la base de datos:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRecuperar.addActionListener(e -> {
            new RecuperarContrasenaFrame().setVisible(true);
            dispose();
        });
    }

    private JPanel createControlPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        p.setOpaque(false);
        p.add(makeControlButton("_", e -> setState(Frame.ICONIFIED)));
        p.add(makeControlButton("□", e -> toggleMaximize()));
        p.add(makeControlButton("✕", e -> System.exit(0)));
        return p;
    }

    private JButton makeControlButton(String texto, ActionListener action) {
        JButton b = new JButton(texto);
        b.setPreferredSize(new Dimension(30, 30));
        b.setFont(new Font("Dialog", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(null);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(255, 102, 0));
        b.addActionListener(action);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(230, 80, 0));
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(new Color(255, 102, 0));
            }
        });
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
                comp.putClientProperty("dragOrigin", e.getPoint());
            }
        });
        comp.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point origin = (Point) comp.getClientProperty("dragOrigin");
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - origin.x,
                        loc.y + e.getY() - origin.y);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazLogin().setVisible(true));
    }
}
