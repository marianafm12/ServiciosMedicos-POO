package Emergencias;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AccidentePage1Panel extends JPanel {
    private JTextField txtIdEmergencia;
    private JComboBox<Integer> cbDia, cbMes, cbAnio;
    private JTextField txtLugar;

    public AccidentePage1Panel() {
        setName("PAGE1");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID Emergencia
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("ID Emergencia:"), gbc);
        gbc.gridx = 1;
        txtIdEmergencia = new JTextField(10);
        add(txtIdEmergencia, gbc);

        // Fecha
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Fecha (D/M/A):"), gbc);
        gbc.gridx = 1;
        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        cbDia = new JComboBox<>(range(1, 31));
        cbMes = new JComboBox<>(range(1, 12));
        cbAnio = new JComboBox<>(range(LocalDate.now().getYear() - 1, LocalDate.now().getYear() + 1));
        fechaPanel.add(cbDia);
        fechaPanel.add(cbMes);
        fechaPanel.add(cbAnio);
        add(fechaPanel, gbc);

        // Lugar
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Lugar del Accidente:"), gbc);
        gbc.gridx = 1;
        txtLugar = new JTextField(20);
        add(txtLugar, gbc);
    }

    public Accidente getModel() {
        Accidente a = new Accidente();
        a.setIdEmergencia(txtIdEmergencia.getText().trim());
        a.setFecha(cbDia.getItemAt(cbDia.getSelectedIndex()) + "/" +
                cbMes.getItemAt(cbMes.getSelectedIndex()) + "/" +
                cbAnio.getItemAt(cbAnio.getSelectedIndex()));
        a.setLugar(txtLugar.getText().trim());
        return a;
    }

    // Helper para generar Integer[]
    private Integer[] range(int start, int end) {
        Integer[] arr = new Integer[end - start + 1];
        for (int i = 0; i < arr.length; i++)
            arr[i] = start + i;
        return arr;
    }
}
