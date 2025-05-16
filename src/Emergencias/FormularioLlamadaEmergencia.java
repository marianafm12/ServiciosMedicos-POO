package Emergencias;

import Utilidades.*;
import BaseDeDatos.PacienteDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormularioLlamadaEmergencia extends FormularioMedicoBase {

    private final JComboBox<String> comboTipoEmergencia;
    private final JTextArea areaDescripcion;
    private final JComboBox<ResponsableItem> comboResponsable;
    private final ButtonGroup grupoGravedad;
    private final JRadioButton rbRojo, rbNaranja, rbAmarillo, rbVerde, rbAzul;
    private final PanelBotonesFormulario botones;

    private final boolean esMedico;
    private final int idUsuario;

    public FormularioLlamadaEmergencia(boolean esMedico, int idUsuario) {
        super("Formulario de Llamada de Emergencia", new String[] {
                "ID Paciente:", "Nombre del Paciente:", "Teléfono de Contacto:",
                "Tipo de Emergencia:", "Nivel de Gravedad:", "Descripción:", "Responsable:"
        });

        this.esMedico = esMedico;
        this.idUsuario = idUsuario;

        JTextField txtID = (JTextField) campos[0];
        JTextField txtNombre = (JTextField) campos[1];
        txtNombre.setEditable(false);

        if (!esMedico) {
            txtID.setText(String.valueOf(idUsuario));
            txtID.setEditable(false);
            txtNombre.setText(PacienteDB.obtenerNombrePorID(txtID.getText()));
        }

        txtID.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (esMedico) {
                    String id = txtID.getText().trim();
                    String nombre = PacienteDB.obtenerNombrePorID(id);
                    txtNombre.setText(nombre != null ? nombre : "");
                }
            }
        });

        comboTipoEmergencia = new JComboBox<>(new String[] {
                "Accidente", "Enfermedad súbita", "Crisis emocional", "Otro"
        });
        campos[3] = comboTipoEmergencia;

        // Gravedad
        rbRojo = new JRadioButton("Rojo");
        rbRojo.setForeground(ColoresUDLAP.ROJO_SOLIDO);
        rbNaranja = new JRadioButton("Naranja");
        rbNaranja.setForeground(ColoresUDLAP.NARANJA);
        rbAmarillo = new JRadioButton("Amarillo");
        rbAmarillo.setForeground(ColoresUDLAP.AMARILLO);
        rbVerde = new JRadioButton("Verde");
        rbVerde.setForeground(ColoresUDLAP.VERDE);
        rbAzul = new JRadioButton("Azul");
        rbAzul.setForeground(ColoresUDLAP.AZUL);

        grupoGravedad = new ButtonGroup();
        grupoGravedad.add(rbRojo);
        grupoGravedad.add(rbNaranja);
        grupoGravedad.add(rbAmarillo);
        grupoGravedad.add(rbVerde);
        grupoGravedad.add(rbAzul);

        JPanel gravedadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gravedadPanel.setBackground(ColoresUDLAP.BLANCO);
        gravedadPanel.add(rbRojo);
        gravedadPanel.add(rbNaranja);
        gravedadPanel.add(rbAmarillo);
        gravedadPanel.add(rbVerde);
        gravedadPanel.add(rbAzul);
        campos[4] = gravedadPanel;

        areaDescripcion = new JTextArea(3, 25);
        campos[5] = new JScrollPane(areaDescripcion);

        comboResponsable = new JComboBox<>();
        comboResponsable.addItem(new ResponsableItem(0, "Seleccione..."));

        for (ResponsableItem r : ResponsablesDB.obtenerResponsables()) {
            comboResponsable.addItem(r);
        }
        campos[6] = comboResponsable;

        if (!esMedico) {
            comboTipoEmergencia.setEnabled(false);
            for (AbstractButton b : new JRadioButton[] { rbRojo, rbNaranja, rbAmarillo, rbVerde, rbAzul }) {
                b.setEnabled(false);
            }
            areaDescripcion.setEditable(false);
            comboResponsable.setEnabled(false);
        }

        botones = new PanelBotonesFormulario("Guardar", "Buscar", "Limpiar");
        add(botones, BorderLayout.SOUTH);

        if (!esMedico) {
            botones.btnGuardar.setEnabled(false);
            botones.btnLimpiar.setEnabled(false);
        }

        botones.setListeners(
                e -> guardar(),
                e -> buscar(),
                e -> limpiarCampos());
    }

    public String getNivelGravedad() {
        if (rbRojo.isSelected())
            return "Rojo";
        if (rbNaranja.isSelected())
            return "Naranja";
        if (rbAmarillo.isSelected())
            return "Amarillo";
        if (rbVerde.isSelected())
            return "Verde";
        if (rbAzul.isSelected())
            return "Azul";
        return null;
    }

    private void guardar() {
        String id = ((JTextField) campos[0]).getText().trim();
        String telefono = ((JTextField) campos[2]).getText().trim();
        String tipo = (String) comboTipoEmergencia.getSelectedItem();
        String gravedad = getNivelGravedad();
        String descripcion = areaDescripcion.getText().trim();
        String responsable = ((ResponsableItem) comboResponsable.getSelectedItem()).toString();

        if (id.isEmpty() || telefono.isEmpty() || gravedad == null || descripcion.isEmpty()
                || responsable.equals("Seleccione...")) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean exito = LlamadaEmergenciaDB.guardarLlamada(id, telefono, tipo, gravedad, descripcion, responsable);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Llamada de emergencia registrada con éxito.");
            limpiarCampos();
        }
    }

    private void buscar() {
        JOptionPane.showMessageDialog(this, "Lógica de búsqueda aún no implementada.");
    }
}
