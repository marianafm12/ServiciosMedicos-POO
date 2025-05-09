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
    private JLabel lblMotivo, lblEstado;
    private File archivoReceta;
    private int folio;
    private Justificante justificante;
    private final String medicoFirmante = "Dra. Laura Gómez";

    public RevisarSolicitudFrame(int folio) {
        this.folio = folio;

        justificante = JustificanteDAO.obtenerPorFolio(folio).orElse(null);
        if (justificante == null) {
            JOptionPane.showMessageDialog(this, "Justificante no encontrado.");
            dispose();
            return;
        }

        setTitle("Revisión de Solicitud");
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        lblEstado = new JLabel("Estado: " + justificante.getEstado());
        add(lblEstado);

        lblMotivo = new JLabel("Motivo: (" + justificante.getFechaInicio().format(formatter)
                + " - " + justificante.getFechaFin().format(formatter) + ")");
        add(lblMotivo);

        motivoField = new JTextField(justificante.getMotivo());
        motivoField.setEditable(false);
        add(motivoField);

        add(new JLabel("Inicio de Reposo:"));
        JPanel panelInicio = new JPanel();
        diaInicio = new JComboBox<>(generarDias());
        mesInicio = new JComboBox<>(generarMeses());
        anioInicio = new JComboBox<>(generarAnios());
        panelInicio.add(diaInicio); panelInicio.add(mesInicio); panelInicio.add(anioInicio);
        add(panelInicio);

        add(new JLabel("Fin de Reposo:"));
        JPanel panelFin = new JPanel();
        diaFin = new JComboBox<>(generarDias());
        mesFin = new JComboBox<>(generarMeses());
        anioFin = new JComboBox<>(generarAnios());
        panelFin.add(diaFin); panelFin.add(mesFin); panelFin.add(anioFin);
        add(panelFin);

        diagnosticoArea = new JTextArea(4, 30);
        diagnosticoArea.setText(justificante.getDiagnostico() != null ? justificante.getDiagnostico() : "");
        add(new JLabel("Diagnóstico y Observaciones:"));
        add(new JScrollPane(diagnosticoArea));

        setFechaCombo(justificante.getFechaInicio(), diaInicio, mesInicio, anioInicio);
        setFechaCombo(justificante.getFechaFin(), diaFin, mesFin, anioFin);

        archivoReceta = justificante.getArchivoReceta();

        JButton abrirArchivoBtn = new JButton("Abrir Archivo");
        abrirArchivoBtn.addActionListener(e -> {
            try {
                if (archivoReceta != null && archivoReceta.exists()) {
                    Desktop.getDesktop().open(archivoReceta);
                } else {
                    JOptionPane.showMessageDialog(this, "No hay archivo disponible.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo.");
            }
        });

        JButton btnAprobar = new JButton("✅ Aprobar");
        btnAprobar.addActionListener(e -> {
            LocalDate inicio = construirFecha(diaInicio, mesInicio, anioInicio);
            LocalDate fin = construirFecha(diaFin, mesFin, anioFin);

            if (inicio.isAfter(fin)) {
                JOptionPane.showMessageDialog(this, "Fecha de inicio posterior a la de fin.");
                return;
            }

            String diag = diagnosticoArea.getText();
            boolean ok = JustificanteDAO.aprobarJustificante(folio, diag, medicoFirmante, inicio, fin);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Justificante aprobado.");
                dispose();
                new SolicitudesJustificantesFrame().setVisible(true);
            }
        });

        JButton btnRechazar = new JButton("❌ Rechazar");
        btnRechazar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que deseas rechazar esta solicitud?",
                    "Confirmar rechazo", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = JustificanteDAO.rechazarJustificante(folio, medicoFirmante);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Justificante rechazado.");
                    dispose();
                    new SolicitudesJustificantesFrame().setVisible(true);
                }
            }
        });

        JButton limpiarBtn = new JButton("Limpiar Campos");
        limpiarBtn.addActionListener(e -> diagnosticoArea.setText(""));

        JPanel botonesPanel = new JPanel();
        botonesPanel.add(abrirArchivoBtn);
        botonesPanel.add(btnAprobar);
        botonesPanel.add(btnRechazar);
        botonesPanel.add(limpiarBtn);
        add(botonesPanel);

        JButton vistaBtn = new JButton("Vista preliminar del Justificante");
        vistaBtn.addActionListener(e -> {
            File pdf = GeneradorPDFJustificante.generar(justificante);
            if (pdf != null && pdf.exists()) {
                try {
                    Desktop.getDesktop().open(pdf);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al abrir el PDF.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo generar el PDF.");
            }
        });
        add(vistaBtn);

        JPanel navPanel = new JPanel();
        JButton menuBtn = new JButton("Menú Principal");
        menuBtn.addActionListener(e -> {
            new Inicio.MenuMedicosFrame().setVisible(true);
            dispose();
        });

        JButton regresarBtn = new JButton("Regresar");
        regresarBtn.addActionListener(e -> {
            new SolicitudesJustificantesFrame().setVisible(true);
            dispose();
        });

        navPanel.add(menuBtn);
        navPanel.add(regresarBtn);
        add(navPanel);

        // Deshabilitar si ya fue resuelto
        if (!justificante.getEstado().equalsIgnoreCase("Pendiente")) {
            diagnosticoArea.setEditable(false);
            diaInicio.setEnabled(false);
            mesInicio.setEnabled(false);
            anioInicio.setEnabled(false);
            diaFin.setEnabled(false);
            mesFin.setEnabled(false);
            anioFin.setEnabled(false);
            btnAprobar.setEnabled(false);
            btnRechazar.setEnabled(false);
            limpiarBtn.setEnabled(false);
        }
    }

    private void setFechaCombo(LocalDate fecha, JComboBox<String> dia, JComboBox<String> mes, JComboBox<String> anio) {
        dia.setSelectedItem(String.valueOf(fecha.getDayOfMonth()));
        mes.setSelectedIndex(fecha.getMonthValue() - 1);
        anio.setSelectedItem(String.valueOf(fecha.getYear()));
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
        int base = LocalDate.now().getYear();
        for (int i = 0; i < 6; i++) a[i] = String.valueOf(base + i);
        return a;
    }

    private LocalDate construirFecha(JComboBox<String> dia, JComboBox<String> mes, JComboBox<String> anio) {
        int d = Integer.parseInt((String) dia.getSelectedItem());
        int m = mes.getSelectedIndex() + 1;
        int y = Integer.parseInt((String) anio.getSelectedItem());
        return LocalDate.of(y, m, d);
    }

    public static void main(String[] args) {
        new RevisarSolicitudFrame(1).setVisible(true);
    }
}
