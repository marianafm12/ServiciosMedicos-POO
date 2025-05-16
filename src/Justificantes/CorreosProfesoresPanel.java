package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import BaseDeDatos.ConexionSQLite;
import Utilidades.ColoresUDLAP;
import Utilidades.PanelProvider;

public class CorreosProfesoresPanel extends JPanel implements PanelProvider {
    private final JComboBox<Integer> countBox = new JComboBox<>();
    private final JPanel correosPanel = new JPanel();
    private final JButton enviarBtn = new JButton("Enviar Justificante");
    private final int folio;
    private final List<JTextField> camposCorreos = new ArrayList<>();

    public CorreosProfesoresPanel(int folio) {
        this.folio = folio;
        setLayout(new BorderLayout(10, 10));
        setBackground(ColoresUDLAP.BLANCO);

        // ─── Top Panel ─────────────────────────────────────────────
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        top.add(new JLabel("Cantidad de profesores:"));
        for (int i = 1; i <= 12; i++)
            countBox.addItem(i);
        top.add(countBox);
        countBox.setSelectedItem(3);
        countBox.addActionListener(e -> actualizarCampos((int) countBox.getSelectedItem()));
        add(top, BorderLayout.NORTH);

        // ─── Center Panel ──────────────────────────────────────────
        correosPanel.setLayout(new BoxLayout(correosPanel, BoxLayout.Y_AXIS));
        correosPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(correosPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
        actualizarCampos(3);

        // ─── Bottom Panel ─────────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        enviarBtn.setBackground(ColoresUDLAP.VERDE_SOLIDO);
        enviarBtn.setForeground(Color.WHITE);
        enviarBtn.setFocusPainted(false);
        enviarBtn.addActionListener(e -> {
            if (guardarCorreos()) {
                JOptionPane.showMessageDialog(this, "Correos registrados para folio " + folio);
            }
        });
        bottom.add(enviarBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private void actualizarCampos(int n) {
        correosPanel.removeAll();
        camposCorreos.clear();
        for (int i = 1; i <= n; i++) {
            JLabel label = new JLabel("Correo profesor " + i + ":");
            JTextField field = new JTextField(30);
            correosPanel.add(label);
            correosPanel.add(field);
            correosPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            camposCorreos.add(field);
        }
        correosPanel.revalidate();
        correosPanel.repaint();
    }

    private boolean guardarCorreos() {
        String sql = "INSERT INTO JustificanteProfesores(folio, correo) VALUES (?, ?)";
        try (Connection c = ConexionSQLite.conectar();
                PreparedStatement ps = c.prepareStatement(sql)) {

            for (JTextField campo : camposCorreos) {
                String correo = campo.getText().trim();
                if (!correo.isEmpty()) {
                    ps.setInt(1, folio);
                    ps.setString(2, correo);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al guardar correos.\n" + ex.getMessage(),
                    "Error de BD",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "correosProfesores";
    }
}
