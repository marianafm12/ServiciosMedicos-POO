package Justificantes;

import Utilidades.PanelProvider;
import javax.swing.*;

public class PanelJustificantesProvider implements PanelProvider {

    public PanelJustificantesProvider() {
        // No es necesario guardar una instancia fija
    }

    @Override
    public JPanel getPanel() {
        // Se genera una nueva instancia en cada llamada
        return new PanelMenuJustificantes();
    }

    @Override
    public String getPanelName() {
        return "justificantesMedicos";
    }
}
