package Registro;

import javax.swing.*;

public class AgendaDirecciones extends JFrame {
    public AgendaDirecciones() {
        JFrame ventana = new JFrame("📘 Agenda de Direcciones (SQLite)");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(600, 500);

        FrameRegistro panelDatos = new FrameRegistro();
        JScrollPane scrollPane = new JScrollPane(panelDatos);
        ventana.add(scrollPane, "Center");

        PanelControl panelBotones = new PanelControl(panelDatos.obtenerCampos());
        ventana.add(panelBotones, "South");

        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        new AgendaDirecciones();
    }
}
