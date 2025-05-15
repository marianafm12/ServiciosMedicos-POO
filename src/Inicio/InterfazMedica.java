package Inicio;

import Utilidades.*;
import BaseDeDatos.ConexionSQLite;
import Consultas.PanelConsultaNueva;
import Emergencias.PanelLlamadaEmergencia;
import Registro.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class InterfazMedica extends JFrame {
    private JPanel contentPanel;
    private final boolean esMedico;
    private final int userId;
    private String nombreUsuario;
    private PanelManager panelManager;

    public InterfazMedica(boolean esMedico, int userId) {
        this.esMedico = esMedico;
        this.userId = userId;
        this.nombreUsuario = fetchNombreUsuario();
        initUI();
    }

    private void initUI() {
        setUndecorated(true);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        /* ========== 1.1 Barras apiladas en un contenedor ========== */
        JPanel contenedorBarras = new JPanel();
        contenedorBarras.setLayout(new BoxLayout(contenedorBarras, BoxLayout.Y_AXIS));

        // Barra superior con botones (minimizar, maximizar, cerrar)
        BarraVentanaUDLAP barraVentana = new BarraVentanaUDLAP(this);
        contenedorBarras.add(barraVentana);

        // Panel superior blanco con saludo, título, campana y cerrar sesión
        JPanel topPanel = crearTopPanel();
        contenedorBarras.add(topPanel);

        // Añadimos el contenedor completo a la parte NORTH del marco
        add(contenedorBarras, BorderLayout.NORTH);

        /*
         * ========== 1.2 Panel principal: menú lateral izquierdo + panel central
         * ==========
         */
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColoresUDLAP.BLANCO);
        add(mainPanel, BorderLayout.CENTER);

        // Menú lateral izquierdo
        JPanel menuPanel = crearMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.WEST);

        // Panel central de contenido (CardLayout)
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Inicializar PanelManager y registrar paneles
        panelManager = new PanelManager(contentPanel);
        registrarPaneles();

        // Mostrar panel inicial
        panelManager.showPanel("panel0");
    }

    private JPanel crearMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(ColoresUDLAP.BLANCO);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 8, 8, 8));

        // Definición de botones del menú
        String[] items = esMedico
                ? new String[] {
                        "Registrar Paciente\nNuevo",
                        "Consulta Nueva",
                        "Editar Datos del\nPaciente",
                        "Justificantes Médicos",
                        "Registrar Llamada\nde Emergencia",
                        "Llenar Reporte de\nAccidente"
                }
                : new String[] {
                        "Gestión de Citas",
                        "Historial Médico",
                        "Solicitar Justificante",
                        "Mis Justificantes",
                        "Reportar Emergencia"
                };

        Font btnFont = new Font("Arial", Font.BOLD, 21);

        for (int i = 0; i < items.length; i++) {
            String texto = "<html><div style='text-align:center;'>" + items[i].replace("\n", "<br>") + "</div></html>";
            Color base = (i % 2 == 0) ? ColoresUDLAP.VERDE : ColoresUDLAP.NARANJA;
            Color hover = (i % 2 == 0) ? ColoresUDLAP.VERDE_HOVER : ColoresUDLAP.NARANJA_HOVER;
            JButton boton = botonTransparente(texto, base, hover, btnFont);
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.setMaximumSize(new Dimension(260, 80));
            boton.setPreferredSize(new Dimension(260, 80));
            boton.setMinimumSize(new Dimension(260, 80));
            final int idx = i;
            boton.addActionListener(e -> manejarClickBoton(idx));
            menuPanel.add(boton);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 13)));
        }

        // Botón emergencias, centrado abajo
        JButton btnEmergencia = botonTransparente(
                "<html><div style='text-align:center;'>Emergencias</div></html>",
                ColoresUDLAP.ROJO, ColoresUDLAP.ROJO_HOVER, btnFont);
        btnEmergencia.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEmergencia.setMaximumSize(new Dimension(260, 56));
        btnEmergencia.setPreferredSize(new Dimension(260, 56));
        btnEmergencia.setMinimumSize(new Dimension(260, 56));
        btnEmergencia.addActionListener(e -> panelManager.showPanel("emergencia"));
        menuPanel.add(btnEmergencia);

        menuPanel.add(Box.createVerticalGlue());

        return menuPanel;
    }

    private void manejarClickBoton(int indiceBoton) {
        if (indiceBoton == 0 && esMedico) {
            panelManager.showPanel("formularioRegistro");
        } else if (indiceBoton == 1 && esMedico) {
            panelManager.showPanel("consultaNueva");
        } else if (indiceBoton == 2 && esMedico) {
            panelManager.showPanel("editarDatosPaciente");
        } else if (indiceBoton == 3 && esMedico) {
            panelManager.showPanel("justificantesMedicos");
        } else if (indiceBoton == 4 && esMedico) {
            panelManager.showPanel("llamadaEmergencia");
        } else if (indiceBoton == 5 && esMedico) {
            panelManager.showPanel("reporteAccidente");
        } else if (indiceBoton == 0 && !esMedico) {
            panelManager.showPanel("gestionCitas");
        } else if (indiceBoton == 1 && !esMedico) {
            panelManager.showPanel("historialMedico");
        } else if (indiceBoton == 2 && !esMedico) {
            panelManager.showPanel("solicitarJustificante");
        } else if (indiceBoton == 3 && !esMedico) {
            panelManager.showPanel("misJustificantes");
        } else if (indiceBoton == 4 && !esMedico) {
            panelManager.showPanel("reportarEmergencia");
        } else {
            panelManager.showPanel("panel" + indiceBoton);
        }
    }

    private void registrarPaneles() {
        // Registrar paneles básicos
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
        }

        // Registrar panel de emergencia
        panelManager.registerPanel(new PanelProvider() {

            @Override
            public JPanel getPanel() {
                JPanel panel = new JPanel();
                panel.setBackground(Color.WHITE);
                JLabel lbl = new JLabel("Panel de Emergencias", SwingConstants.CENTER);
                lbl.setFont(new Font("Arial", Font.BOLD, 26));
                panel.add(lbl);
                return panel;
            }

            @Override
            public String getPanelName() {
                return "emergencia";
            }
        });

        // Registrar panel de formulario de registro
        panelManager.registerPanel(new PanelRegistroPaciente());
        // Registrar panel de consulta nueva
        panelManager.registerPanel(new PanelConsultaNueva(userId, nombreUsuario));
        // Registrar panel de llamada de emergencia
        panelManager.registerPanel(new PanelLlamadaEmergencia());


    }

    // Nuevo método auxiliar para crear el panel superior
    private JPanel crearTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ColoresUDLAP.BLANCO);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        topPanel.setPreferredSize(new Dimension(0, 56));

        JLabel lblSaludo = new JLabel("Hola, " + nombreUsuario);
        lblSaludo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        lblSaludo.setFont(new Font("Arial", Font.PLAIN, 18));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 12));
        left.setOpaque(false);
        left.add(lblSaludo);

        JLabel lblTitulo = new JLabel(
                "<html><span style='font-size:25pt;font-weight:bold;color:#006400;'>Servicios Médicos</span> " +
                        "<span style='font-size:25pt;font-weight:bold;color:#FF6600;'>UDLAP</span></html>",
                SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        center.setOpaque(false);
        center.add(lblTitulo);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        right.setOpaque(false);

        JLabel bell = new JLabel("\uD83D\uDD14");
        bell.setFont(new Font("Dialog", Font.PLAIN, 24));
        right.add(bell);

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setFont(new Font("Arial", Font.BOLD, 15));
        btnCerrarSesion.setBackground(ColoresUDLAP.NARANJA_BARRA);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnCerrarSesion.addActionListener(e -> {
            new InterfazLogin().setVisible(true);
            dispose();
        });
        right.add(btnCerrarSesion);

        topPanel.add(left, BorderLayout.WEST);
        topPanel.add(center, BorderLayout.CENTER);
        topPanel.add(right, BorderLayout.EAST);

        return topPanel;
    }

    // Botón traslúcido personalizado
    private JButton botonTransparente(String texto, Color base, Color hover, Font font) {
        JButton button = new JButton(texto) {
            @Override
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
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }

            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });
        return button;
    }

    // Mostrar un panel central según su CardLayout
    private void mostrarPanel(String panelName) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, panelName);
    }

    private void mostrarPanelEmergencia() {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, "emergencia");
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
        return "Usuario";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazMedica(true, 1).setVisible(true));
    }
}
