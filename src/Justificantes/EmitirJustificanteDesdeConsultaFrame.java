package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;

public class EmitirJustificanteDesdeConsultaFrame extends JFrame {
    private JTextField idField, nombreField, motivoField;
    private JComboBox<String> diaInicio, mesInicio, anioInicio;
    private JComboBox<String> diaFin, mesFin, anioFin;
    private JTextArea diagnosticoArea;
    private File archivoReceta;
    private final String medicoFirmante = "Dra. Laura Gómez";

    public EmitirJustificanteDesdeConsultaFrame() {
        setTitle("Emitir Justificante Médico");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));

        idField = new JTextField();
        nombreField = new JTextField();
        motivoField = new JTextField();

        add(new JLabel("ID del Paciente:"));
        add(idField);
        add(new JLabel("Nombre del Paciente:"));
        add(nombreField);
        add(new JLabel("Motivo:"));
        add(motivoField);

        diaInicio = new JComboBox<>(generarDias());
        mesInicio = new JComboBox<>(generarMeses());
        anioInicio = new JComboBox<>(generarAnios());
        add(new JLabel("Inicio de Reposo:"));
        JPanel panelInicio = new JPanel();
        panelInicio.add(diaInicio);
        panelInicio.add(mesInicio);
        panelInicio.add(anioInicio);
        add(panelInicio);

        diaFin = new JComboBox<>(generarDias());
        mesFin = new JComboBox<>(generarMeses());
        anioFin = new JComboBox<>(generarAnios());
        add(new JLabel("Fin de Reposo:"));
        JPanel panelFin = new JPanel();
        panelFin.add(diaFin);
        panelFin.add(mesFin);
        panelFin.add(anioFin);
        add(panelFin);

        diagnosticoArea = new JTextArea(4, 30);
        add(new JLabel("Diagnóstico:"));
        add(new JScrollPane(diagnosticoArea));

        JButton subirArchivoBtn = new JButton("Subir Receta");
        subirArchivoBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int op = fc.showOpenDialog(this);
            if (op == JFileChooser.APPROVE_OPTION) {
                archivoReceta = fc.getSelectedFile();
            }
        });

        JButton guardarBtn = new JButton("Emitir Justificante");
        guardarBtn.addActionListener(e -> guardar());

        JButton cancelarBtn = new JButton("Cancelar");
        cancelarBtn.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(subirArchivoBtn);
        panelBotones.add(guardarBtn);
        panelBotones.add(cancelarBtn);
        add(panelBotones);
    }

    private void guardar() {
        String id = idField.getText().trim();
        String nombre = nombreField.getText().trim();
        String motivo = motivoField.getText().trim();
        String diagnostico = diagnosticoArea.getText().trim();
        LocalDate inicio = construirFecha(diaInicio, mesInicio, anioInicio);
        LocalDate fin = construirFecha(diaFin, mesFin, anioFin);

        if (id.isEmpty() || nombre.isEmpty() || motivo.isEmpty() || diagnostico.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            return;
        }

        if (inicio.isAfter(fin)) {
            JOptionPane.showMessageDialog(this, "Fecha inicio > fin.");
            return;
        }

        Justificante j = new Justificante(id, nombre, motivo, inicio, fin, diagnostico, archivoReceta);
        j.setEstado("Aprobado");
        j.setResueltoPor(medicoFirmante);
        j.setFechaResolucion(LocalDate.now());

        boolean ok = JustificanteDAO.guardarJustificante(j);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Justificante emitido y guardado.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar justificante.");
        }
    }

    private String[] generarDias() {
        String[] d = new String[31];
        for (int i = 1; i <= 31; i++)
            d[i - 1] = String.valueOf(i);
        return d;
    }

    private String[] generarMeses() {
        return new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };
    }

    private String[] generarAnios() {
        String[] a = new String[6];
        int base = LocalDate.now().getYear();
        for (int i = 0; i < 6; i++)
            a[i] = String.valueOf(base + i);
        return a;
    }

    private LocalDate construirFecha(JComboBox<String> dia, JComboBox<String> mes, JComboBox<String> anio) {
        int d = Integer.parseInt((String) dia.getSelectedItem());
        int m = mes.getSelectedIndex() + 1;
        int y = Integer.parseInt((String) anio.getSelectedItem());
        return LocalDate.of(y, m, d);
    }

    public static void main(String[] args) {
        new EmitirJustificanteDesdeConsultaFrame().setVisible(true);
    }
}
