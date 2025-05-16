package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;

public class PanelControlEditarPaciente extends JPanel {
    public PanelControlEditarPaciente(FormularioEditarPaciente formulario) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setBackground(ColoresUDLAP.BLANCO);
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnGuardar = crearBoton("Guardar Cambios", ColoresUDLAP.VERDE_SOLIDO);
        btnGuardar.addActionListener(e -> formulario.guardarDatos());
        add(btnGuardar);
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(160, 35));
        return btn;
    }
}
