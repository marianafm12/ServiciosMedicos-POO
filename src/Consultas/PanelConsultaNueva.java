package Consultas;

import javax.swing.*;
import Utilidades.*;
import java.awt.*;

public class PanelConsultaNueva extends JPanel implements PanelProvider {
    private FrameConsulta panelDatos;
    private PanelControlConsultas panelBotones;

    public PanelConsultaNueva(int idMedico, String nombreMedico) {
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        panelDatos = new FrameConsulta(idMedico, nombreMedico);
        JScrollPane scrollPane = new JScrollPane(panelDatos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        panelBotones = new PanelControlConsultas(panelDatos.obtenerCampos(), panelDatos.obtenerAreaTexto());
        add(panelBotones, BorderLayout.SOUTH);
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "consultaNueva";
    }
}