package Justificantes;

import Utilidades.ColoresUDLAP;
import Utilidades.PanelManager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import BaseDeDatos.ConexionSQLite;

public class CorreosProfesoresPanel extends JPanel {

    private final JComboBox<Integer> countBox = new JComboBox<>();
    private final JPanel correosPanel = new JPanel();
    private final JButton enviarBtn = new JButton("Enviar Justificante");
    private final JButton menuBtn = new JButton("Menú Principal");
    private final JButton backBtn = new JButton("Regresar");

    private final int folio;
    private final PanelManager panelManager;

    public CorreosProfesoresPanel(int folio, PanelManager panelManager) {
        this.folio = folio;
        this.panelManager = panelManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(ColoresUDLAP.BLANCO);

        // Top
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(ColoresUDLAP.BLANCO);
        top.add(new JLabel("Cantidad de profesores:"));

        for (int i = 1; i <= 12; i++)
            countBox.addItem(i);
        countBox.setSelectedItem(3);
        countBox.setBackground(Color.WHITE);
        top.add(countBox);

        countBox.addActionListener(e -> actualizarCampos((int) countBox.getSelectedItem()));

        // Center
        correosPanel.setLayout(new BoxLayout(correosPanel, BoxLayout.Y_AXIS));
        correosPanel.setBackground(ColoresUDLAP.BLANCO);
        actualizarCampos(3);
        JScrollPane scroll = new JScrollPane(correosPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Bottom
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bot.setBackground(ColoresUDLAP.BLANCO);

        enviarBtn.setBackground(ColoresUDLAP.VERDE_OSCURO);
        enviarBtn.setForeground(Color.WHITE);
        enviarBtn.setFocusPainted(false);

        menuBtn.setBackground(ColoresUDLAP.NARANJA);
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setFocusPainted(false);

        backBtn.setBackground(new Color(120, 120, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);

        bot.add(menuBtn);
        bot.add(backBtn);
        bot.add(enviarBtn);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bot, BorderLayout.SOUTH);

        // Acciones
        enviarBtn.addActionListener(e -> {
            if (guardarCorreos()) {
                JOptionPane.showMessageDialog(this, "Correos registrados correctamente.");
                panelManager.showPanel("justificantesPaciente");
            }
        });

        menuBtn.addActionListener(e -> panelManager.showPanel("justificantesPaciente"));

        backBtn.addActionListener(e -> {
            panelManager.showPanel("justificantesPaciente"); // o regresar a un formulario si tienes uno específico
        });
    }

    private void actualizarCampos(int n) {
        correosPanel.removeAll();
        for (int i = 1; i <= n; i++) {
            correosPanel.add(new JLabel("Correo profesor " + i + ":"));
            JTextField campo = new JTextField(30);
            campo.setMaximumSize(new Dimension(400, 30));
            correosPanel.add(campo);
            correosPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        correosPanel.revalidate();
        correosPanel.repaint();
    }

    private boolean guardarCorreos() {
        String sql = "INSERT INTO JustificanteProfesores(folio, correo) VALUES (?, ?)";
        try (Connection c = ConexionSQLite.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (Component comp : correosPanel.getComponents()) {
                if (comp instanceof JTextField) {
                    String mail = ((JTextField) comp).getText().trim();
                    if (!mail.isEmpty()) {
                        ps.setInt(1, folio);
                        ps.setString(2, mail);
                        ps.addBatch();
                    }
                }
            }
            ps.executeBatch();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar correos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
