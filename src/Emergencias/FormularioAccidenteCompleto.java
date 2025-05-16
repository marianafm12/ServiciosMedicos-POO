package Emergencias;

import Utilidades.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.stream.IntStream;

public class FormularioAccidenteCompleto extends FormularioMedicoBase {

    private final JComboBox<Integer> cbDia, cbMes, cbAnio;
    private final JTextArea txtDescripcion;
    private final PanelBotonesFormulario botones;

    public FormularioAccidenteCompleto() {
        super("Registro de Accidente", new String[] {
                "ID Emergencia:", "Fecha del Accidente:", "Lugar del Accidente:",
                "Descripción del Accidente:", "Testigos:"
        });

        cbDia = new JComboBox<>(toIntegerArray(1, 31));
        cbMes = new JComboBox<>(toIntegerArray(1, 12));
        cbAnio = new JComboBox<>(toIntegerArray(LocalDate.now().getYear() - 1, LocalDate.now().getYear() + 1));

        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fechaPanel.setBackground(ColoresUDLAP.BLANCO);
        fechaPanel.add(cbDia);
        fechaPanel.add(cbMes);
        fechaPanel.add(cbAnio);
        campos[1] = fechaPanel;

        txtDescripcion = new JTextArea(4, 25);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDescripcion.setBorder(BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO));
        campos[3] = new JScrollPane(txtDescripcion);

        botones = new PanelBotonesFormulario("Guardar", "Buscar", "Limpiar");
        add(botones, BorderLayout.SOUTH);

        botones.setListeners(
                e -> guardarDatos(),
                e -> buscarDatos(),
                e -> limpiarCampos());
    }

    private Integer[] toIntegerArray(int start, int end) {
        return IntStream.rangeClosed(start, end).boxed().toArray(Integer[]::new);
    }

    public String getFechaAccidente() {
        return cbDia.getSelectedItem() + "/" + cbMes.getSelectedItem() + "/" + cbAnio.getSelectedItem();
    }

    public String getDescripcion() {
        return txtDescripcion.getText().trim();
    }

    public boolean validarFormulario() {
        return ValidadorAccidente.validar(
                (JTextField) campos[0], (JTextField) campos[2], txtDescripcion, (JTextField) campos[4]);
    }

    private void guardarDatos() {
        if (validarFormulario()) {
            boolean exito = AccidenteDB.guardarAccidente(
                    ((JTextField) campos[0]).getText().trim(),
                    getFechaAccidente(),
                    ((JTextField) campos[2]).getText().trim(),
                    getDescripcion(),
                    ((JTextField) campos[4]).getText().trim());
            if (exito) {
                JOptionPane.showMessageDialog(this, "Accidente registrado con éxito.");
                limpiarCampos();
            }
        }
    }

    private void buscarDatos() {
        String id = ((JTextField) campos[0]).getText().trim();
        Accidente accidente = AccidenteDB.buscarAccidente(id);
        if (accidente != null) {
            cbDia.setSelectedItem(accidente.dia());
            cbMes.setSelectedItem(accidente.mes());
            cbAnio.setSelectedItem(accidente.anio());
            ((JTextField) campos[2]).setText(accidente.lugar());
            txtDescripcion.setText(accidente.descripcion());
            ((JTextField) campos[4]).setText(accidente.testigos());
        }
    }
}
