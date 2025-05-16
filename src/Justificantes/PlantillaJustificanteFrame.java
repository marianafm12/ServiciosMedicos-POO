/* 

package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import BaseDeDatos.ConexionSQLite;
//import Inicio.MenuPacientesFrame;
import Inicio.SesionUsuario;

public class PlantillaJustificanteFrame extends JFrame {
    private final int folio;
    private final JTextField idField, nombreField, doctorField, cedulaField;
    private final JComboBox<String> diaIni, mesIni, anioIni;
    private final JComboBox<String> diaFin, mesFin, anioFin;
    private final JTextArea diagnosticoArea;
    private final JButton guardarBtn, menuBtn, regresarBtn;

    public PlantillaJustificanteFrame(int folio) {// f
        super("Plantilla para Justificante Médico");
        this.folio = folio;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel main = new JPanel(new GridLayout(10, 2, 8, 8));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        main.add(new JLabel("ID:"));
        idField = new JTextField();
        main.add(idField);

        main.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        nombreField.setEditable(false);
        main.add(nombreField);

        main.add(new JLabel("Doctor/a:"));
        doctorField = new JTextField();
        main.add(doctorField);

        main.add(new JLabel("Cédula Profesional:"));
        cedulaField = new JTextField();
        main.add(cedulaField);

        main.add(new JLabel("Fecha de inicio de reposo:"));
        diaIni = new JComboBox<>(generarDias());
        mesIni = new JComboBox<>(generarMeses());
        anioIni = new JComboBox<>(generarAnios(2025, 2035));
        JPanel pI = new JPanel();
        pI.add(diaIni);
        pI.add(mesIni);
        pI.add(anioIni);
        main.add(pI);

        main.add(new JLabel("Fecha de final de reposo:"));
        diaFin = new JComboBox<>(generarDias());
        mesFin = new JComboBox<>(generarMeses());
        anioFin = new JComboBox<>(generarAnios(2025, 2035));
        JPanel pF = new JPanel();
        pF.add(diaFin);
        pF.add(mesFin);
        pF.add(anioFin);
        main.add(pF);

        main.add(new JLabel("Diagnóstico y Observaciones:"));
        diagnosticoArea = new JTextArea(3, 20);
        main.add(new JScrollPane(diagnosticoArea));

        guardarBtn = new JButton("Guardar Justificante");
        menuBtn = new JButton("Menú Principal");
        regresarBtn = new JButton("Regresar");
        JPanel bot = new JPanel();
        bot.add(menuBtn);
        bot.add(regresarBtn);
        bot.add(guardarBtn);

        add(main, BorderLayout.CENTER);
        add(bot, BorderLayout.SOUTH);

        // Cargar datos previos
        cargarDatos();

        idField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String id = idField.getText().trim();
                if (!id.isEmpty())
                    buscarNombre(id);
                else
                    nombreField.setText("");
            }
        });

        guardarBtn.addActionListener(e -> {
            if (actualizarJustificante()) {
                new CorreosProfesoresFrame(folio).setVisible(true);
                dispose();
            }
        });
        menuBtn.addActionListener(e -> {
            // new MenuPacientesFrame(SesionUsuario.getPacienteActual()).setVisible(true);
            dispose();
        });
        regresarBtn.addActionListener(e -> {
            new SeleccionarPacienteFrame().setVisible(true);
            dispose();
        });
    }

    private void cargarDatos() {
        String sql = "SELECT alumno_id, doctor, cedula, fecha_inicio, fecha_fin, diagnostico "
                + "FROM JustificantePaciente WHERE folio = ?";
        try (Connection c = ConexionSQLite.conectar();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, folio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int alumnoId = rs.getInt("alumno_id");
                    idField.setText(String.valueOf(alumnoId));
                    buscarNombre(String.valueOf(alumnoId));
                    doctorField.setText(rs.getString("doctor"));
                    cedulaField.setText(rs.getString("cedula"));
                    setComboDate(diaIni, mesIni, anioIni, rs.getString("fecha_inicio"));
                    setComboDate(diaFin, mesFin, anioFin, rs.getString("fecha_fin"));
                    diagnosticoArea.setText(rs.getString("diagnostico"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean actualizarJustificante() {
        String sql = "UPDATE JustificantePaciente SET doctor=?, cedula=?, "
                + "fecha_inicio=?, fecha_fin=?, diagnostico=? WHERE folio=?";
        try (Connection c = ConexionSQLite.conectar();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, doctorField.getText().trim());
            ps.setString(2, cedulaField.getText().trim());
            String inicio = diaIni.getSelectedItem() + " " + mesIni.getSelectedItem() + " " + anioIni.getSelectedItem();
            String fin = diaFin.getSelectedItem() + " " + mesFin.getSelectedItem() + " " + anioFin.getSelectedItem();
            ps.setString(3, inicio);
            ps.setString(4, fin);
            ps.setString(5, diagnosticoArea.getText().trim());
            ps.setInt(6, folio);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar justificante.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void buscarNombre(String id) {
        String sql = "SELECT nombre, apellidoPaterno, apellidoMaterno FROM InformacionAlumno WHERE id = ?";
        try (Connection c = ConexionSQLite.conectar();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nombreField.setText(
                            rs.getString("nombre") + " " +
                                    rs.getString("apellidoPaterno") + " " +
                                    rs.getString("apellidoMaterno"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Helpers de fecha…
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

    private String[] generarAnios(int desde, int hasta) {
        String[] a = new String[hasta - desde + 1];
        for (int i = 0; i < a.length; i++)
            a[i] = String.valueOf(desde + i);
        return a;
    }

    private void setComboDate(JComboBox<String> d, JComboBox<String> m, JComboBox<String> y, String txt) {
        String[] p = txt.split(" ");
        d.setSelectedItem(p[0]);
        m.setSelectedItem(p[1]);
        y.setSelectedItem(p[2]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlantillaJustificanteFrame(1).setVisible(true));
    }
}

*/