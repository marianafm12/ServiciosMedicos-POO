package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import Utilidades.ColoresUDLAP;
import Justificantes.JustificanteDAO;


public class RevisarSolicitudFrame extends JPanel {
    private JTextField motivoField;
    private JComboBox<String> diaInicio, mesInicio, anioInicio;
    private JComboBox<String> diaFin, mesFin, anioFin;
    private JTextArea diagnosticoArea;
    private JLabel lblEstado;
    private File archivoReceta;
    private int folio;
    private Justificante justificante;
    private final String medicoFirmante = "Dra. Laura Gómez";

    public RevisarSolicitudFrame(int folio, ActionListener volverAction) {
        this.folio = folio;
        justificante = JustificanteDAO.obtenerPorFolio(folio).orElse(null);

        if (justificante == null) {
            JOptionPane.showMessageDialog(this, "Justificante no encontrado.");
            return;
        }

        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Font titleFont = new Font("Arial", Font.BOLD, 16);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Revisión de Solicitud de Justificante", SwingConstants.CENTER);
        lblTitulo.setFont(titleFont);
        lblTitulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        gbc.gridx = 0;
        lblEstado = new JLabel("Estado:");
        lblEstado.setFont(labelFont);
        lblEstado.setForeground(ColoresUDLAP.NEGRO);
        add(lblEstado, gbc);

        gbc.gridx = 1;
        JLabel lblEstadoValor = new JLabel(justificante.getEstado());
        lblEstadoValor.setFont(fieldFont);
        add(lblEstadoValor, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblMotivo = new JLabel("Motivo:");
        lblMotivo.setFont(labelFont);
        lblMotivo.setForeground(ColoresUDLAP.NEGRO);
        add(lblMotivo, gbc);

        gbc.gridx = 1;
        motivoField = new JTextField(justificante.getMotivo());
        motivoField.setFont(fieldFont);
        motivoField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        add(motivoField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblInicioReposo = new JLabel("Inicio de Reposo:");
        lblInicioReposo.setFont(labelFont);
        lblInicioReposo.setForeground(ColoresUDLAP.NEGRO);
        add(lblInicioReposo, gbc);

        gbc.gridx = 1;
        JPanel panelInicio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelInicio.setBackground(ColoresUDLAP.BLANCO);
        diaInicio = new JComboBox<>(generarDias());
        mesInicio = new JComboBox<>(generarMeses());
        anioInicio = new JComboBox<>(generarAnios());
        panelInicio.add(diaInicio);
        panelInicio.add(mesInicio);
        panelInicio.add(anioInicio);
        add(panelInicio, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblFinReposo = new JLabel("Fin de Reposo:");
        lblFinReposo.setFont(labelFont);
        lblFinReposo.setForeground(ColoresUDLAP.NEGRO);
        add(lblFinReposo, gbc);

        gbc.gridx = 1;
        JPanel panelFin = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelFin.setBackground(ColoresUDLAP.BLANCO);
        diaFin = new JComboBox<>(generarDias());
        mesFin = new JComboBox<>(generarMeses());
        anioFin = new JComboBox<>(generarAnios());
        panelFin.add(diaFin);
        panelFin.add(mesFin);
        panelFin.add(anioFin);
        add(panelFin, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblDiagnostico = new JLabel("Diagnóstico:");
        lblDiagnostico.setFont(labelFont);
        lblDiagnostico.setForeground(ColoresUDLAP.NEGRO);
        add(lblDiagnostico, gbc);

        gbc.gridx = 1;
        diagnosticoArea = new JTextArea(4, 30);
        diagnosticoArea.setText(justificante.getDiagnostico() != null ? justificante.getDiagnostico() : "");
        diagnosticoArea.setFont(fieldFont);
        diagnosticoArea.setLineWrap(true);
        diagnosticoArea.setWrapStyleWord(true);
        diagnosticoArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        add(new JScrollPane(diagnosticoArea), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JPanel filaBotones1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        filaBotones1.setBackground(ColoresUDLAP.BLANCO);

        JButton btnAprobar = crearBotonSolido("Aprobar", ColoresUDLAP.VERDE);
        JButton btnRechazar = crearBotonSolido("Rechazar", ColoresUDLAP.ROJO);
        JButton limpiarBtn = crearBotonSolido("Limpiar", ColoresUDLAP.NARANJA);

        filaBotones1.add(btnAprobar);
        filaBotones1.add(btnRechazar);
        filaBotones1.add(limpiarBtn);
        add(filaBotones1, gbc);

        gbc.gridy++;
        JPanel filaBotones2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        filaBotones2.setBackground(ColoresUDLAP.BLANCO);

        JButton abrirArchivoBtn = crearBotonSolido("Abrir Archivo", ColoresUDLAP.AZUL);
        JButton vistaBtn = crearBotonSolido("Vista preliminar del Justificante", ColoresUDLAP.VERDE_OSCURO);
        JButton volverBtn = crearBotonSolido("Volver", ColoresUDLAP.GRIS_OSCURO);

        filaBotones2.add(abrirArchivoBtn);
        filaBotones2.add(vistaBtn);
        filaBotones2.add(volverBtn);
        add(filaBotones2, gbc);

btnAprobar.addActionListener(e -> {
    LocalDate inicio = construirFecha(diaInicio, mesInicio, anioInicio);
    LocalDate fin = construirFecha(diaFin, mesFin, anioFin);

    if (inicio.isAfter(fin)) {
        JOptionPane.showMessageDialog(this, "Fecha de inicio posterior a la de fin.");
        return;
    }

    String motivo = motivoField.getText();
    String diag = diagnosticoArea.getText();
    boolean ok = JustificanteDAO.aprobarJustificante(folio, motivo, diag, medicoFirmante, inicio, fin);
    if (ok) {
        JOptionPane.showMessageDialog(this, "Justificante aprobado.");
        volverAction.actionPerformed(null);
    }
});

        btnRechazar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que deseas rechazar esta solicitud?",
                    "Confirmar rechazo", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = JustificanteDAO.rechazarJustificante(folio, medicoFirmante);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Justificante rechazado.");
                    volverAction.actionPerformed(null);
                }
            }
        });

        limpiarBtn.addActionListener(e -> {
            diagnosticoArea.setText("");
            motivoField.setText(justificante.getMotivo());
        });

        abrirArchivoBtn.addActionListener(e -> {
            try {
                if (archivoReceta != null && archivoReceta.exists()) {
                    Desktop.getDesktop().open(archivoReceta);
                } else {
                    JOptionPane.showMessageDialog(this, "No hay archivo disponible.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo.");
            }
        });

        vistaBtn.addActionListener(e -> {
            File pdf = GeneradorPDFJustificante.generar(justificante);
            if (pdf != null && pdf.exists()) {
                try {
                    Desktop.getDesktop().open(pdf);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al abrir el PDF.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo generar el PDF.");
            }
        });

        volverBtn.addActionListener(volverAction);

        setFechaCombo(justificante.getFechaInicio(), diaInicio, mesInicio, anioInicio);
        setFechaCombo(justificante.getFechaFin(), diaFin, mesFin, anioFin);
        archivoReceta = justificante.getArchivoReceta();

        if (!justificante.getEstado().equalsIgnoreCase("Pendiente")) {
            motivoField.setEditable(false);
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

    private JButton crearBotonSolido(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
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
}
