package Consultas;

import Utilidades.PanelProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.regex.Pattern;

public class PanelConsultaNueva extends JPanel implements PanelProvider {

    private final int idMedico;
    private final String nombreMedico;

    private final JTextField txtIDPaciente, txtNombre, txtEdad, txtCorreo;
    private final JTextField txtFechaConsulta, txtUltimaConsulta, txtInicioSintomas;
    private final JTextArea txtSintomas, txtMedicamentos, txtDiagnostico, txtReceta;
    private final JButton btnGuardar, btnBuscar, btnLimpiar;

    public PanelConsultaNueva(int idMedico, String nombreMedico) {
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;

        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitulo = new JLabel("Consulta Médica - Dr. " + nombreMedico);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        txtIDPaciente = new JTextField(25);
        txtNombre = new JTextField(25); txtNombre.setEditable(false);
        txtEdad = new JTextField(25);
        txtCorreo = new JTextField(25); txtCorreo.setEditable(false);
        txtSintomas = new JTextArea(2, 25);
        txtMedicamentos = new JTextArea(2, 25);
        txtDiagnostico = new JTextArea(2, 25);
        txtReceta = new JTextArea(3, 25);
        txtFechaConsulta = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()), 25);
        txtUltimaConsulta = new JTextField(25);
        txtInicioSintomas = new JTextField(25);

        agregarCampo(gbc, "ID Paciente:", txtIDPaciente, 1);
        agregarCampo(gbc, "Nombre del Paciente:", txtNombre, 2);
        agregarCampo(gbc, "Edad:", txtEdad, 3);
        agregarCampo(gbc, "Correo Electrónico:", txtCorreo, 4);
        agregarCampo(gbc, "Síntomas:", new JScrollPane(txtSintomas), 5);
        agregarCampo(gbc, "Medicamentos:", new JScrollPane(txtMedicamentos), 6);
        agregarCampo(gbc, "Diagnóstico:", new JScrollPane(txtDiagnostico), 7);
        agregarCampo(gbc, "Fecha (dd/MM/yyyy):", txtFechaConsulta, 8);
        agregarCampo(gbc, "Última Consulta (dd/MM/yyyy):", txtUltimaConsulta, 9);
        agregarCampo(gbc, "Inicio Síntomas (dd/MM/yyyy):", txtInicioSintomas, 10);
        agregarCampo(gbc, "Receta Médica:", new JScrollPane(txtReceta), 11);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("Guardar");
        btnBuscar = new JButton("Buscar");
        btnLimpiar = new JButton("Limpiar");

        btnGuardar.setBackground(new Color(0, 128, 0));
        btnGuardar.setForeground(Color.WHITE);
        btnBuscar.setBackground(new Color(255, 102, 0));
        btnBuscar.setForeground(Color.WHITE);
        btnLimpiar.setBackground(new Color(204, 0, 0));
        btnLimpiar.setForeground(Color.WHITE);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);

        btnBuscar.addActionListener(e -> buscarPaciente());
        btnGuardar.addActionListener(e -> guardarConsulta());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        txtEdad.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
    }

    private void agregarCampo(GridBagConstraints gbc, String label, Component campo, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(campo, gbc);
    }

    private void buscarPaciente() {
        String id = txtIDPaciente.getText().trim();
        if (id.isEmpty()) {
            mostrarError("Debe ingresar un ID.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Servicios medicos.db")) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT ia.Nombre || ' ' || ia.ApellidoPaterno || ' ' || ia.ApellidoMaterno AS NombreCompleto, " +
                "ia.Correo, r.Edad FROM InformacionAlumno ia " +
                "JOIN Registro r ON ia.ID = r.ID WHERE ia.ID = ?"
            );
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtNombre.setText(rs.getString("NombreCompleto"));
                txtCorreo.setText(rs.getString("Correo"));
                txtEdad.setText(rs.getString("Edad"));
            } else {
                mostrarError("El paciente no se encuentra registrado.");
                limpiarCampos();
            }
        } catch (Exception ex) {
            mostrarError("Error al buscar paciente: " + ex.getMessage());
        }
    }

    private void guardarConsulta() {
        if (!validarCampos()) return;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Servicios medicos.db")) {
            int idPaciente = Integer.parseInt(txtIDPaciente.getText().trim());
            int nuevaEdad = Integer.parseInt(txtEdad.getText().trim());

            PreparedStatement psEdad = conn.prepareStatement("UPDATE Registro SET Edad = ? WHERE ID = ?");
            psEdad.setInt(1, nuevaEdad);
            psEdad.setInt(2, idPaciente);
            psEdad.executeUpdate();

            PreparedStatement psConsulta = conn.prepareStatement(
                "INSERT INTO Consultas (IDPaciente, IDMedico, Sintomas, Medicamentos, Diagnostico, FechaConsulta, FechaUltimaConsulta, FechaInicioSintomas, Receta) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            psConsulta.setInt(1, idPaciente);
            psConsulta.setInt(2, idMedico);
            psConsulta.setString(3, txtSintomas.getText().trim());
            psConsulta.setString(4, txtMedicamentos.getText().trim());
            psConsulta.setString(5, txtDiagnostico.getText().trim());
            psConsulta.setString(6, txtFechaConsulta.getText().trim());
            psConsulta.setString(7, txtUltimaConsulta.getText().trim());
            psConsulta.setString(8, txtInicioSintomas.getText().trim());
            psConsulta.setString(9, txtReceta.getText().trim());
            psConsulta.executeUpdate();

            JOptionPane.showMessageDialog(this, "Consulta registrada y edad actualizada correctamente.");
        } catch (Exception e) {
            mostrarError("Error al guardar la información: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        Date hoy = new Date();

        try {
            Date fechaConsulta = sdf.parse(txtFechaConsulta.getText().trim());
            Date ultima = sdf.parse(txtUltimaConsulta.getText().trim());
            Date inicio = sdf.parse(txtInicioSintomas.getText().trim());

            if (!mismoDia(fechaConsulta, hoy)) {
                mostrarError("La Fecha de Consulta debe ser la actual.");
                return false;
            }
            if (ultima.after(hoy) || inicio.after(hoy)) {
                mostrarError("Las fechas de última consulta o inicio de síntomas no pueden ser futuras.");
                return false;
            }
        } catch (ParseException e) {
            mostrarError("Formato de fecha inválido. Use dd/MM/yyyy.");
            return false;
        }

        if (!validarAlfanumerico(txtSintomas.getText()) ||
            !validarAlfanumerico(txtMedicamentos.getText()) ||
            !validarAlfanumerico(txtDiagnostico.getText()) ||
            !validarAlfanumerico(txtReceta.getText())) {
            mostrarError("Los campos de texto deben contener solo letras, números y espacios.");
            return false;
        }

        return true;
    }

    private boolean mismoDia(Date d1, Date d2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(d1).equals(sdf.format(d2));
    }

    private boolean validarAlfanumerico(String texto) {
        return Pattern.matches("[a-zA-Z0-9\\s]+", texto);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtEdad.setText("");
        txtSintomas.setText("");
        txtMedicamentos.setText("");
        txtDiagnostico.setText("");
        txtReceta.setText("");
        txtFechaConsulta.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        txtUltimaConsulta.setText("");
        txtInicioSintomas.setText("");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Métodos requeridos por PanelProvider
    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "consultaNueva";
    }
}
