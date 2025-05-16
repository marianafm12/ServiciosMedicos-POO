package GestionCitas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelGestionCitas extends JPanel {

    private final int userId;

    public PanelGestionCitas(int userId) {
        this.userId = userId;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        JLabel titulo = new JLabel("Gestión de Citas - Seleccione una opción:");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón para agendar cita
        JButton btnAgendarCita = botonTransparente(
                "Agendar Cita",
                new Color(0, 102, 0, 100),  // Verde transparente
                new Color(0, 102, 0, 170)   // Verde más opaco al hover
        );
        btnAgendarCita.setMaximumSize(new Dimension(350, 60));
        btnAgendarCita.addActionListener(e -> {
            new AgendaCitaFrame(userId).setVisible(true);
            ((Window) SwingUtilities.getRoot(this)).dispose();
        });

        // Botón para modificar cita
        JButton btnModificarCita = botonTransparente(
                "Modificar Cita",
                new Color(255, 102, 0, 100),  // Naranja transparente
                new Color(255, 102, 0, 170)   // Naranja más opaco al hover
        );
        btnModificarCita.setMaximumSize(new Dimension(350, 60));
        btnModificarCita.addActionListener(e -> {
            new ModificarCitaFrame().setVisible(true);
            ((Window) SwingUtilities.getRoot(this)).dispose();
        });

        // Agregado al panel
        add(titulo);
        add(Box.createVerticalStrut(40));
        add(btnAgendarCita);
        add(Box.createVerticalStrut(30));
        add(btnModificarCita);
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