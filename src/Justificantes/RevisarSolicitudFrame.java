package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RevisarSolicitudFrame extends JFrame {
    private JTextField motivoField;
    private JComboBox<String> diaInicio, mesInicio, anioInicio;
    private JComboBox<String> diaFin, mesFin, anioFin;
    private JTextArea diagnosticoArea;
    private JButton abrirArchivoBtn, actualizarBtn, limpiarBtn, regresarBtn, menuBtn;
    private SolicitudJustificante solicitud;
    private JFrame parent;
    private JLabel lblMotivo;

    public RevisarSolicitudFrame(SolicitudJustificante solicitud, JFrame parent) {
        this.solicitud = solicitud;
        this.parent = parent;

        setTitle("Revisión de Solicitud");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));

        // Formato de fechas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaInicioStr = solicitud.getFechaInicio().format(formatter);
        String fechaFinStr = solicitud.getFechaFin().format(formatter);

        // Motivo con fechas
        lblMotivo = new JLabel("Motivo: (" + fechaInicioStr + " - " + fechaFinStr + ")");
        add(lblMotivo);

        motivoField = new JTextField(solicitud.getMotivo());
        motivoField.setEditable(false);
        add(motivoField);

        diaInicio = new JComboBox<>(generarDias());
        mesInicio = new JComboBox<>(generarMeses());
        anioInicio = new JComboBox<>(generarAnios());
        add(new JLabel("Inicio de Reposo:"));
        JPanel inicioPanel = new JPanel();
        inicioPanel.add(diaInicio); inicioPanel.add(mesInicio); inicioPanel.add(anioInicio);
        add(inicioPanel);

        diaFin = new JComboBox<>(generarDias());
        mesFin = new JComboBox<>(generarMeses());
        anioFin = new JComboBox<>(generarAnios());
        add(new JLabel("Fin de Reposo:"));
        JPanel finPanel = new JPanel();
        finPanel.add(diaFin); finPanel.add(mesFin); finPanel.add(anioFin);
        add(finPanel);

        diagnosticoArea = new JTextArea(4, 30);
        diagnosticoArea.setText(solicitud.getDiagnosticoObservaciones() != null ? solicitud.getDiagnosticoObservaciones() : "");
        add(new JLabel("Diagnóstico y Observaciones:"));
        add(new JScrollPane(diagnosticoArea));

        abrirArchivoBtn = new JButton("Abrir Archivo");
        abrirArchivoBtn.addActionListener(e -> {
            try {
                File receta = solicitud.getArchivoReceta();
                if (receta != null && receta.exists()) {
                    Desktop.getDesktop().open(receta);
                } else {
                    JOptionPane.showMessageDialog(this, "No hay archivo cargado.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo.");
            }
        });

        actualizarBtn = new JButton("Actualizar campos");
        actualizarBtn.addActionListener(e -> {
            solicitud.setDiagnosticoObservaciones(diagnosticoArea.getText());
            solicitud.setFechaInicio(construirFecha(diaInicio, mesInicio, anioInicio));
            solicitud.setFechaFin(construirFecha(diaFin, mesFin, anioFin));

            // Actualizar también el label con las nuevas fechas
            String nuevaInicio = solicitud.getFechaInicio().format(formatter);
            String nuevaFin = solicitud.getFechaFin().format(formatter);
            lblMotivo.setText("Motivo: (" + nuevaInicio + " - " + nuevaFin + ")");

            JOptionPane.showMessageDialog(this, "Campos actualizados.");
        });

        limpiarBtn = new JButton("Limpiar Campos");
        limpiarBtn.addActionListener(e -> diagnosticoArea.setText(""));

        JPanel botonesPanel = new JPanel();
        botonesPanel.add(abrirArchivoBtn);
        botonesPanel.add(actualizarBtn);
        botonesPanel.add(limpiarBtn);
        add(botonesPanel);

        JButton vistaBtn = new JButton("Vista preliminar del Justificante");
        add(vistaBtn); // Sin acción aún

        JPanel navPanel = new JPanel();
        menuBtn = new JButton("Menú Principal");
        menuBtn.addActionListener(e -> {
            new Inicio.MenuMedicosFrame().setVisible(true);
            dispose();
        });

        regresarBtn = new JButton("Regresar");
        regresarBtn.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });

        navPanel.add(menuBtn);
        navPanel.add(regresarBtn);
        add(navPanel);
    }

    private String[] generarDias() {
        String[] d = new String[31];
        for (int i = 1; i <= 31; i++) d[i - 1] = String.valueOf(i);
        return d;
    }

    private String[] generarMeses() {
        return new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    }

    private String[] generarAnios() {
        String[] a = new String[6];
        int base = 2023;
        for (int i = 0; i < 6; i++) a[i] = String.valueOf(base + i);
        return a;
    }

    private LocalDate construirFecha(JComboBox<String> dia, JComboBox<String> mes, JComboBox<String> anio) {
        int d = Integer.parseInt((String) dia.getSelectedItem());
        int m = mes.getSelectedIndex() + 1;
        int y = Integer.parseInt((String) anio.getSelectedItem());
        return LocalDate.of(y, m, d);
    }
}
