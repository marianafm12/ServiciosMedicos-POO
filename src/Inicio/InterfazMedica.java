package Inicio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InterfazMedica extends JFrame {
    private Point mouseDownCompCoords = null;
    private final JLabel contentLabel = new JLabel("Selecciona una opción del menú", SwingConstants.CENTER);

    public InterfazMedica() {
        // 1) Ventana sin decoración nativa
        setUndecorated(true);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 2) Barra de controles (naranja)
        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controlBar.setPreferredSize(new Dimension(0, 30));
        controlBar.setBackground(new Color(255, 102, 0));
        controlBar.add(createControlButton("_"));
        controlBar.add(createControlButton("□"));
        controlBar.add(createControlButton("✕"));
        enableDrag(controlBar);
        add(controlBar, BorderLayout.NORTH);

        // 3) Panel menú (izquierda), ocupa toda la altura bajo la barra
        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setPreferredSize(new Dimension(120, 0));
        panelMenu.setBackground(Color.WHITE);

        // 3.1) Encabezado del menú: “Hola, usuario” arriba y “Menú”
        JPanel menuHeader = new JPanel(new GridLayout(2, 1));
        menuHeader.setBackground(panelMenu.getBackground());
        JLabel lblUsuario = new JLabel("Hola, usuario", SwingConstants.CENTER);
        lblUsuario.setFont(lblUsuario.getFont().deriveFont(Font.PLAIN, 12f));
        JLabel lblMenu = new JLabel("Menú", SwingConstants.CENTER);
        lblMenu.setFont(lblMenu.getFont().deriveFont(Font.BOLD, 14f));
        menuHeader.add(lblUsuario);
        menuHeader.add(lblMenu);
        panelMenu.add(menuHeader, BorderLayout.NORTH);

        // 3.2) Botones del menú incluyendo SOS al final
        JPanel panelButtons = new JPanel(new GridLayout(5, 1));
        panelButtons.setBackground(panelMenu.getBackground());
        String[] textos = { "Btn 1", "Btn 2", "Btn 3", "Btn 4" };
        // Verde UDLAP según tu encabezado HTML
        Color verdeUDLAP = Color.decode("#006400");

        for (int i = 0; i < textos.length; i++) {
            JButton b = new JButton(textos[i]);
            b.setFocusPainted(false);
            b.setOpaque(true);
            b.setContentAreaFilled(true);
            b.setBackground(verdeUDLAP); // Aquí aplicamos el verde UDLAP
            b.setForeground(Color.WHITE); // Texto en blanco
            b.setBorder(BorderFactory.createMatteBorder(
                    0, 0, 1, 0, new Color(200, 200, 200)));
            final int idx = i + 1;
            b.addActionListener(e -> contentLabel.setText("Contenido de Btn " + idx));
            panelButtons.add(b);
        }

        // Escalar la imagen del botón SOS
        ImageIcon sosIcon = new ImageIcon(
                "C:\\Users\\cosa2\\OneDrive - Fundacion Universidad de las Americas Puebla\\4° Semestre\\Programación Orientada a Objetos\\ServiciosMedicos-POO\\SOS.png");
        Image img = sosIcon.getImage();

        // Definir el tamaño del botón
        int buttonWidth = 120; // Establecer el tamaño deseado del botón
        int buttonHeight = 120;

        // Escalar la imagen proporcionalmente
        Image scaledImg = img.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);

        // Crear el botón con la imagen ajustada
        sosIcon = new ImageIcon(scaledImg);
        JButton btnSOS = new JButton(sosIcon);
        btnSOS.setFocusPainted(false);
        btnSOS.setBackground(panelMenu.getBackground());
        btnSOS.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Ajuste del borde
        btnSOS.addActionListener(e -> JOptionPane.showMessageDialog(this, "¡SOS activado!"));
        panelButtons.add(btnSOS);

        panelMenu.add(panelButtons, BorderLayout.CENTER);
        add(panelMenu, BorderLayout.WEST);

        // 4) Contenedor central a la derecha del menú
        JPanel centerContainer = new JPanel(new BorderLayout());
        add(centerContainer, BorderLayout.CENTER);

        // 4.1) Encabezado “Servicios Médicos UDLAP”
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        String texto = "<html>"
                + "<span style='font-size:18pt; font-weight:bold; color:#006400;'>"
                + "Servicios Médicos"
                + "</span> "
                + "<span style='font-size:18pt; font-weight:bold; color:#FF6600;'>"
                + "UDLAP"
                + "</span>"
                + "</html>";
        JLabel lblHeader = new JLabel(texto, SwingConstants.CENTER);

        // Botón “Cerrar sesión”
        JButton btnLogout = new JButton("Cerrar sesión");
        btnLogout.setFont(new Font("Dialog", Font.PLAIN, 12));
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> System.exit(0));

        headerPanel.add(lblHeader, BorderLayout.CENTER);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        centerContainer.add(headerPanel, BorderLayout.NORTH);

        // 4.2) Área de contenido
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.add(contentLabel, BorderLayout.NORTH);
        centerContainer.add(panelContenido, BorderLayout.CENTER);
    }

    // Crea los botones de control de la ventana
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
        if ((getExtendedState() & Frame.MAXIMIZED_BOTH) != Frame.MAXIMIZED_BOTH) {
            setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            setExtendedState(Frame.NORMAL);
        }
    }

    // Permite mover la ventana arrastrando el componente
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazMedica().setVisible(true));
    }
}