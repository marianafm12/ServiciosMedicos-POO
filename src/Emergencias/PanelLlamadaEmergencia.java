package Emergencias;

import javax.swing.*;

import Utilidades.*;

public class PanelLlamadaEmergencia implements PanelProvider {
    private final FormularioLlamadaEmergencia formulario;

    public PanelLlamadaEmergencia() {
        formulario = new FormularioLlamadaEmergencia();
    }

    @Override
    public JPanel getPanel() {
        return formulario;
    }

    @Override
    public String getPanelName() {
        return "llamadaEmergencia";
    }
}
