package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

import Inicio.MenuMedicosFrame;
import Utilidades.ColoresUDLAP;

public class SolicitudJustificante extends JFrame {

    private JTextField motivoField;
    private JComboBox<String> diaInicio, mesInicio, anioInicio;
    private JComboBox<String> diaFin, mesFin, anioFin;
    private JTextArea diagnosticoArea;
    private File archivoReceta;
    private int folio;

    public SolicitudJustificante(int folio) {
        this.folio = folio;
        setTitle("Revisión de Solicitud");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setBackground(ColoresUDLAP.BLANCO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));

        motivoField = new JTextField();
        motivoField.setEditable(false);
        add(new JLabel("Motivo:"));
        add(motivoField);

        diaInicio = new JComboBox<>(generarDias());
        mesInicio = new JComboBox<>(generarMeses());
        anioInicio = new JComboBox<>(generarAnios());
        add(new JLabel("Inicio de Reposo:"));
        JPanel inicioPanel = new JPanel();
        inicioPanel.add(diaInicio);
        inicioPanel.add(mesInicio);
        inicioPanel.add(anioInicio);
        add(inicioPanel);

        diaFin = new JComboBox<>(generarDias());
        mesFin = new JComboBox<>(generarMeses());
        anioFin = new JComboBox<>(generarAnios());
        add(new JLabel("Fin de Reposo:"));
        JPanel finPanel = new JPanel();
        finPanel.add(diaFin);
        finPanel.add(mesFin);
        finPanel.add(anioFin);
        add(finPanel);

        diagnosticoArea = new JTextArea(4, 30);
        add(new JLabel("Diagnóstico y Observaciones:"));
        add(new JScrollPane(diagnosticoArea));

        JButton abrirArchivoBtn = new JButton("Abrir Archivo");
        abrirArchivoBtn.addActionListener(e -> {
            try {
                if (archivoReceta != null && archivoReceta.exists()) {
                    Desktop.getDesktop().open(archivoReceta);
                } else {
                    JOptionPane.showMessageDialog(this, "No hay archivo de receta disponible.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo.");
            }
        });

        JButton actualizarBtn = new JButton("Actualizar campos");
        actualizarBtn.addActionListener(e -> {
            LocalDate nuevaInicio = construirFecha(diaInicio, mesInicio, anioInicio);
            LocalDate nuevaFin = construirFecha(diaFin, mesFin, anioFin);
            String diagnostico = diagnosticoArea.getText();

            try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
                String sql = "UPDATE JustificantePaciente SET fechaInicio = ?, fechaFin = ?, diagnostico = ? WHERE folio = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, nuevaInicio.toString());
                pst.setString(2, nuevaFin.toString());
                pst.setString(3, diagnostico);
                pst.setInt(4, folio);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar los datos.");
                ex.printStackTrace();
            }
        });

        JButton limpiarBtn = new JButton("Limpiar Campos");
        limpiarBtn.addActionListener(e -> diagnosticoArea.setText(""));

        JPanel botonesPanel = new JPanel();
        botonesPanel.add(abrirArchivoBtn);
        botonesPanel.add(actualizarBtn);
        botonesPanel.add(limpiarBtn);
        add(botonesPanel);

        JButton vistaBtn = new JButton("Vista preliminar del Justificante");
        add(vistaBtn); // sin acción aún

        JPanel navPanel = new JPanel();
        JButton menuBtn = new JButton("Menú Principal");
        JButton regresarBtn = new JButton("Regresar");

        menuBtn.addActionListener(e -> {
            new MenuMedicosFrame().setVisible(true);
            dispose();
        });

        regresarBtn.addActionListener(e -> {
            new SolicitudesJustificantesFrame().setVisible(true);
            dispose();
        });

        navPanel.add(menuBtn);
        navPanel.add(regresarBtn);
        add(navPanel);

        cargarDatosDesdeBD(folio);
    }

    private void cargarDatosDesdeBD(int folio) {
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String sql = "SELECT * FROM JustificantePaciente WHERE folio = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, folio);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                motivoField.setText(rs.getString("motivo"));
                diagnosticoArea.setText(rs.getString("diagnostico"));

                LocalDate fechaInicio = LocalDate.parse(rs.getString("fechaInicio"));
                LocalDate fechaFin = LocalDate.parse(rs.getString("fechaFin"));

                diaInicio.setSelectedItem(String.valueOf(fechaInicio.getDayOfMonth()));
                mesInicio.setSelectedIndex(fechaInicio.getMonthValue() - 1);
                anioInicio.setSelectedItem(String.valueOf(fechaInicio.getYear()));

                diaFin.setSelectedItem(String.valueOf(fechaFin.getDayOfMonth()));
                mesFin.setSelectedIndex(fechaFin.getMonthValue() - 1);
                anioFin.setSelectedItem(String.valueOf(fechaFin.getYear()));

                // Ruta del archivo PDF (esto depende de cómo se guardó en tu sistema)
                String rutaArchivo = rs.getString("rutaArchivo");
                if (rutaArchivo != null) {
                    archivoReceta = new File(rutaArchivo);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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
    }// f

    public static void main(String[] args) {
        new SolicitudJustificante(1);
    }
}
