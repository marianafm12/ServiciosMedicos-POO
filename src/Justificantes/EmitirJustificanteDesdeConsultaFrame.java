package Justificantes;

import Utilidades.ColoresUDLAP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;

import BaseDeDatos.ConexionSQLite;

public class EmitirJustificanteDesdeConsultaFrame extends JPanel {

    private final JTextField idField, nombreField, motivoField;
    private final JComboBox<String> diaInicio, mesInicio, anioInicio;
    private final JComboBox<String> diaFin, mesFin, anioFin;
    private final JTextArea diagnosticoArea;
    private final JLabel mensajeError;
    private File archivoReceta;
    private final String medicoFirmante = "Dra. Laura Gómez";

    public EmitirJustificanteDesdeConsultaFrame() {
        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Font titleFont = new Font("Arial", Font.BOLD, 18);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titulo = new JLabel("Emitir Justificante Médico", SwingConstants.CENTER);
        titulo.setFont(titleFont);
        titulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        add(titulo, gbc);

        gbc.gridwidth = 1;

        // ID
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("ID del Paciente:", labelFont), gbc);

        gbc.gridx = 1;
        idField = campoTexto(fieldFont);
        add(idField, gbc);

        // Nombre
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("Nombre del Paciente:", labelFont), gbc);

        gbc.gridx = 1;
        nombreField = campoTexto(fieldFont);
        nombreField.setEditable(false);
        add(nombreField, gbc);

        // Motivo
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("Motivo:", labelFont), gbc);

        gbc.gridx = 1;
        motivoField = campoTexto(fieldFont);
        add(motivoField, gbc);

        // Fecha Inicio
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("Inicio de Reposo:", labelFont), gbc);

        gbc.gridx = 1;
        diaInicio = new JComboBox<>(generarDias());
        mesInicio = new JComboBox<>(generarMeses());
        anioInicio = new JComboBox<>(generarAnios());
        add(panelFechas(diaInicio, mesInicio, anioInicio), gbc);

        // Fecha Fin
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("Fin de Reposo:", labelFont), gbc);

        gbc.gridx = 1;
        diaFin = new JComboBox<>(generarDias());
        mesFin = new JComboBox<>(generarMeses());
        anioFin = new JComboBox<>(generarAnios());
        add(panelFechas(diaFin, mesFin, anioFin), gbc);

        // Diagnóstico
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("Diagnóstico:", labelFont), gbc);

        gbc.gridx = 1;
        diagnosticoArea = new JTextArea(4, 20);
        diagnosticoArea.setFont(fieldFont);
        diagnosticoArea.setLineWrap(true);
        diagnosticoArea.setWrapStyleWord(true);
        diagnosticoArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(new JScrollPane(diagnosticoArea), gbc);

        // Mensaje de error
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mensajeError = new JLabel();
        mensajeError.setForeground(Color.RED);
        mensajeError.setFont(new Font("Arial", Font.BOLD, 13));
        add(mensajeError, gbc);
        gbc.gridwidth = 1;

        // Botones
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.setBackground(ColoresUDLAP.BLANCO);

        JButton subirArchivoBtn = botonEstilo("Subir Receta", ColoresUDLAP.AZUL);
        subirArchivoBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                archivoReceta = fc.getSelectedFile();
            }
        });

        JButton guardarBtn = botonEstilo("Emitir Justificante", ColoresUDLAP.VERDE_OSCURO);
        guardarBtn.addActionListener(e -> guardar());

        JButton cancelarBtn = botonEstilo("Cancelar", ColoresUDLAP.ROJO);
        cancelarBtn.addActionListener(e -> {
            Container cont = getParent();
            if (cont != null) {
                cont.removeAll();
                cont.setLayout(new BorderLayout());
                cont.add(new PanelMenuJustificantes(), BorderLayout.CENTER);
                cont.revalidate();
                cont.repaint();
            }
        });

        panelBotones.add(subirArchivoBtn);
        panelBotones.add(guardarBtn);
        panelBotones.add(cancelarBtn);

        add(panelBotones, gbc);

        // Evento de autocompletado de nombre
        idField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = idField.getText().trim();
                if (input.matches("\\d{6}")) {
                    int id = Integer.parseInt(input);
                    if (id >= 180000 && id <= 999999) {
                        try (Connection conn = ConexionSQLite.conectar();
                             PreparedStatement pst = conn.prepareStatement(
                                     "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?")) {
                            pst.setInt(1, id);
                            ResultSet rs = pst.executeQuery();
                            if (rs.next()) {
                                String nombreCompleto = rs.getString("Nombre") + " " +
                                                        rs.getString("ApellidoPaterno") + " " +
                                                        rs.getString("ApellidoMaterno");
                                nombreField.setText(nombreCompleto);
                                nombreField.setEditable(false);
                                mensajeError.setText("");
                            } else {
                                nombreField.setText("");
                                mensajeError.setText("⚠️ El paciente no se encuentra registrado.");
                            }
                        } catch (SQLException ex) {
                            mensajeError.setText("❌ Error al acceder a la base de datos.");
                            ex.printStackTrace();
                        }
                    } else {
                        nombreField.setText("");
                        mensajeError.setText("⚠️ El ID debe estar entre 180000 y 999999.");
                    }
                } else {
                    nombreField.setText("");
                    mensajeError.setText("⚠️ El ID debe tener 6 dígitos.");
                }
            }
        });
    }

    private JLabel label(String texto, Font fuente) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(fuente);
        lbl.setForeground(ColoresUDLAP.NEGRO);
        return lbl;
    }

    private JTextField campoTexto(Font fuente) {
        JTextField t = new JTextField(20);
        t.setFont(fuente);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return t;
    }

    private JPanel panelFechas(JComboBox<String> d, JComboBox<String> m, JComboBox<String> a) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(ColoresUDLAP.BLANCO);
        panel.add(d);
        panel.add(m);
        panel.add(a);
        return panel;
    }

    private JButton botonEstilo(String texto, Color colorFondo) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setBackground(colorFondo);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        return b;
    }

    private void guardar() {
        String id = idField.getText().trim();
        String nombre = nombreField.getText().trim();
        String motivo = motivoField.getText().trim();
        String diagnostico = diagnosticoArea.getText().trim();
        LocalDate inicio = construirFecha(diaInicio, mesInicio, anioInicio);
        LocalDate fin = construirFecha(diaFin, mesFin, anioFin);

        if (id.isEmpty() || motivo.isEmpty() || diagnostico.isEmpty()) {
            mensajeError.setText("⚠️ Todos los campos son obligatorios.");
            return;
        }

        if (inicio.isAfter(fin)) {
            mensajeError.setText("⚠️ Fecha de inicio posterior a la fecha de fin.");
            return;
        }

        Justificante j = new Justificante(id, nombre, motivo, inicio, fin, diagnostico, archivoReceta);
        j.setEstado("Aprobado");
        j.setResueltoPor(medicoFirmante);
        j.setFechaResolucion(LocalDate.now());

        boolean ok = JustificanteDAO.guardarJustificante(j);
        mensajeError.setText(ok ? "✅ Justificante emitido correctamente." : "❌ Error al guardar justificante.");
    }

    private LocalDate construirFecha(JComboBox<String> dia, JComboBox<String> mes, JComboBox<String> anio) {
        int d = Integer.parseInt((String) dia.getSelectedItem());
        int m = mes.getSelectedIndex() + 1;
        int y = Integer.parseInt((String) anio.getSelectedItem());
        return LocalDate.of(y, m, d);
    }

    private String[] generarDias() {
        String[] dias = new String[31];
        for (int i = 1; i <= 31; i++) dias[i - 1] = String.valueOf(i);
        return dias;
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
}
