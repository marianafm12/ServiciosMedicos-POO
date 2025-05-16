package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;
import Utilidades.PanelProvider;

public class PanelEditarDatosPaciente extends JPanel implements PanelProvider {
    private final FormularioEditarPaciente formulario;
    private final PanelControlEditarPaciente panelBotones;

    /**
     * Construye el panel de edición para el paciente con ID dado.
     * 
     * @param idPaciente el identificador del paciente a editar.
     */
    public PanelEditarDatosPaciente(int idPaciente) {
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        formulario = new FormularioEditarPaciente(idPaciente);
        JScrollPane scroll = new JScrollPane(formulario);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        panelBotones = new PanelControlEditarPaciente(formulario);
        add(panelBotones, BorderLayout.SOUTH);
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
