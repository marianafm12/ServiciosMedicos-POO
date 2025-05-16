package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;
import Utilidades.PanelProvider;

public class PanelGestionEnfermedades extends JPanel implements PanelProvider {
    private final CardLayout cards = new CardLayout();
    private final JPanel container = new JPanel(cards);
    private final boolean isDoctor;

    public PanelGestionEnfermedades(boolean isDoctor) {
        this.isDoctor = isDoctor;
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        container.add(crearPanelInput(), "INPUT");
        add(container, BorderLayout.CENTER);
        cards.show(container, "INPUT");
    }

    private JPanel crearPanelInput() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ColoresUDLAP.BLANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lbl = new JLabel("Ingrese ID de paciente:");
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        JTextField txtId = new JTextField(10);
        panel.add(txtId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JButton btn = new JButton("Cargar historial");
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(ColoresUDLAP.VERDE_SOLIDO);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> {
            String input = txtId.getText().trim();
            if (!input.matches("\\d+")) {
                JOptionPane.showMessageDialog(this,
                        "ID inválido. Debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = Integer.parseInt(input);
            JPanel next = isDoctor
                    ? new PanelEditarDatosPaciente(id)
                    : new PanelVerDatosPaciente(id);
            container.add(next, "NEXT");
            cards.show(container, "NEXT");
        });
        panel.add(btn, gbc);

        return panel;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "gestionEnfermedades";
    }
}
