// PanelVerDatosPaciente.java
package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;
import Utilidades.PanelProvider;

public class PanelVerDatosPaciente extends JPanel implements PanelProvider {
    private final FormularioVerPaciente formulario;

    /**
     * Construye el panel de visualización para el paciente con el ID proporcionado.
     * 
     * @param idPaciente identificador del paciente.
     */
    public PanelVerDatosPaciente(int idPaciente) {
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        formulario = new FormularioVerPaciente(idPaciente);
        JScrollPane scroll = new JScrollPane(formulario);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "verDatosPaciente";
    }
}