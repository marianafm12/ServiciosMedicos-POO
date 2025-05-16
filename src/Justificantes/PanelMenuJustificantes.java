package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelMenuJustificantes extends JPanel {

    public PanelMenuJustificantes() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        JLabel titulo = new JLabel("Seleccione una opción para emitir un justificante:");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón para solicitudes desde BD
        JButton btnDesdeSolicitud = botonTransparente(
                "Solicitudes",
                new Color(0, 102, 0, 100),
                new Color(0, 102, 0, 170)
        );
        btnDesdeSolicitud.setMaximumSize(new Dimension(350, 60));
        btnDesdeSolicitud.addActionListener(e -> mostrarPanel(new SolicitudesJustificantesFrame()));

        // Botón para emitir directamente
        JButton btnDesdeConsulta = botonTransparente(
                "Consulta Interna",
                new Color(255, 102, 0, 100),
                new Color(255, 102, 0, 170)
        );
        btnDesdeConsulta.setMaximumSize(new Dimension(350, 60));
        btnDesdeConsulta.addActionListener(e -> {
            removeAll();
            setLayout(new BorderLayout());
            add(new EmitirJustificanteDesdeConsultaFrame(), BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        // Agregado al panel
        add(titulo);
        add(Box.createVerticalStrut(40));
        add(btnDesdeSolicitud);
        add(Box.createVerticalStrut(30));
        add(btnDesdeConsulta);
    }

    private void mostrarPanel(JFrame frame) {
        removeAll();
        setLayout(new BorderLayout());
        add(frame.getContentPane(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JButton botonTransparente(String texto, Color base, Color hover) {
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
        button.setFont(new Font("Arial", Font.BOLD, 19));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
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
}
