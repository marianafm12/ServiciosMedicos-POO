package GestionCitas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;

public class ModificarCitaFrame extends JFrame {
    private static final String NO_EDITABLE = "No editable";

    private JTextField campoNombre, campoApellidos, campoID;
    private JComboBox<String> comboCitas, comboServicio;
    private JComboBox<String> comboDia, comboMes, comboAño, comboHora, comboMinuto;
    private JLabel errorLabel;

    public ModificarCitaFrame() {
        setTitle("Modificar Cita");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoNombre = new JTextField(NO_EDITABLE, 12);
        campoNombre.setEditable(false);
        campoNombre.setForeground(Color.GRAY);

        campoApellidos = new JTextField(NO_EDITABLE, 12);
        campoApellidos.setEditable(false);
        campoApellidos.setForeground(Color.GRAY);

        campoID = new JTextField(12);
        campoID.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatosPersonales();
            }
        });

        comboCitas = new JComboBox<>();
        comboServicio = new JComboBox<>(new String[]{"Consulta", "Enfermería", "Examen Médico"});

        comboDia = new JComboBox<>(crearRango(1, 31));
        comboMes = new JComboBox<>(new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"});
        comboAño = new JComboBox<>(crearRango(2025, 2030));
        comboHora = new JComboBox<>(new String[]{"08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"});
        comboMinuto = new JComboBox<>(new String[]{"00", "30"});

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nombre:"), gbc); gbc.gridx = 1;
        add(campoNombre, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Apellidos:"), gbc); gbc.gridx = 1;
        add(campoApellidos, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("ID:"), gbc); gbc.gridx = 1;
        add(campoID, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JButton btnBuscar = new JButton("Buscar Cita");
        btnBuscar.addActionListener(e -> cargarCitas());
        add(btnBuscar, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Selecciona tu cita:"), gbc); gbc.gridx = 1;
        add(comboCitas, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Servicio:"), gbc); gbc.gridx = 1;
        add(comboServicio, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Nueva Fecha:"), gbc); gbc.gridx = 1;
        add(agrupar(comboDia, comboMes, comboAño), gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Hora de la Cita:"), gbc); gbc.gridx = 1;
        add(agrupar(comboHora, comboMinuto), gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(errorLabel, gbc);

        gbc.gridy++;
        JButton btnModificar = new JButton("Modificar Cita");
        btnModificar.addActionListener(e -> modificarCita());
        add(btnModificar, gbc);

        gbc.gridy++;
        JButton btnCancelar = new JButton("Cancelar Cita");
        btnCancelar.addActionListener(e -> {
            String seleccion = (String) comboCitas.getSelectedItem();
            if (seleccion == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una cita para cancelar.");
                return;
            }

            try {
                int idCita = Integer.parseInt(seleccion.split(":")[0].trim());
                cancelarCita(idCita);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Formato de cita inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(btnCancelar, gbc);

        gbc.gridy++;
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        JButton btnMenu = new JButton("Menú Principal");
        btnMenu.addActionListener(e -> {
            new Inicio.PortadaFrame().setVisible(true);
            dispose();
        });
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(e -> {
            new InicioFrame().setVisible(true);
            dispose();
        });
        botonesPanel.add(btnMenu);
        botonesPanel.add(btnRegresar);
        add(botonesPanel, gbc);

        setVisible(true);
    }

    private void modificarCita() {
        String seleccion = (String) comboCitas.getSelectedItem();
        if (seleccion == null) return;

        int idCita = Integer.parseInt(seleccion.split(":")[0].trim());
        String idPaciente = campoID.getText().trim();
        String servicio = (String) comboServicio.getSelectedItem();
        int dia = Integer.parseInt((String) comboDia.getSelectedItem());
        int mes = comboMes.getSelectedIndex() + 1;
        int año = Integer.parseInt((String) comboAño.getSelectedItem());
        String hora = (String) comboHora.getSelectedItem();
        String minuto = (String) comboMinuto.getSelectedItem();

        if (!ValidacionesCita.esIdValido(idPaciente)) {
            errorLabel.setText("ID inválido (solo números)");
            return;
        }

        if (!ValidacionesCita.esFechaValida(dia, mes, año)) {
            errorLabel.setText("Fecha inválida (debe ser futura)");
            return;
        }

        String nuevaFecha = String.format("%04d-%02d-%02d", año, mes, dia);
        String nuevaHora = hora + ":" + minuto;

        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String checkSql = "SELECT COUNT(*) FROM CitasMedicas WHERE fecha = ? AND hora = ? AND servicio = ? AND idCita != ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, nuevaFecha);
            checkStmt.setString(2, nuevaHora);
            checkStmt.setString(3, servicio);
            checkStmt.setInt(4, idCita);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                errorLabel.setText("Ya existe una cita para ese servicio en la misma fecha y hora.");
                return;
            }

            String sql = "UPDATE CitasMedicas SET fecha = ?, hora = ?, servicio = ? WHERE idCita = ? AND idPaciente = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevaFecha);
            stmt.setString(2, nuevaHora);
            stmt.setString(3, servicio);
            stmt.setInt(4, idCita);
            stmt.setString(5, idPaciente);
            stmt.executeUpdate();

            ListaEsperaDAO.generarNotificacionParaListaEspera(nuevaFecha, nuevaHora, servicio);


            errorLabel.setForeground(Color.GREEN);
            errorLabel.setText("Cita modificada correctamente.");
        } catch (Exception ex) {
            errorLabel.setText("Error: " + ex.getMessage());
        }
    }

    private void cancelarCita(int idCita) {
        final int MAX_REINTENTOS = 3;
        int intentos = 0;
        boolean completado = false;

        String fecha = "";
        String hora = "";
        String servicio = "";

        while (intentos < MAX_REINTENTOS && !completado) {
            try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
                String consulta = "SELECT fecha, hora, servicio FROM CitasMedicas WHERE idCita = ?";
                try (PreparedStatement stmtConsulta = conn.prepareStatement(consulta)) {
                    stmtConsulta.setInt(1, idCita);
                    ResultSet rs = stmtConsulta.executeQuery();
                    if (rs.next()) {
                        fecha = rs.getString("fecha");
                        hora = rs.getString("hora");
                        servicio = rs.getString("servicio");
                    } else {
                        JOptionPane.showMessageDialog(this, "Cita no encontrada.");
                        return;
                    }
                }

                String sql = "DELETE FROM CitasMedicas WHERE idCita = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idCita);
                    stmt.executeUpdate();
                }

                ListaEsperaDAO.generarNotificacionParaListaEspera(fecha, hora, servicio);


                completado = true;
                JOptionPane.showMessageDialog(this, "Cita cancelada correctamente.");
                cargarCitas();

            } catch (SQLException ex) {
                if (ex.getMessage().contains("database is locked")) {
                    intentos++;
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al cancelar cita: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
        }

        if (!completado) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cancelar la cita. Intenta más tarde.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarDatosPersonales() {
        String idText = campoID.getText().trim();
        if (idText.isEmpty()) return;
        try {
            int id = Integer.parseInt(idText);
            try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
                String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    campoNombre.setText(rs.getString("Nombre"));
                    campoNombre.setForeground(Color.BLACK);
                    campoApellidos.setText(rs.getString("ApellidoPaterno") + " " + rs.getString("ApellidoMaterno"));
                    campoApellidos.setForeground(Color.BLACK);
                } else {
                    campoNombre.setText(NO_EDITABLE);
                    campoNombre.setForeground(Color.GRAY);
                    campoApellidos.setText(NO_EDITABLE);
                    campoApellidos.setForeground(Color.GRAY);
                }
            }
        } catch (Exception ex) {
            campoNombre.setText(NO_EDITABLE);
            campoNombre.setForeground(Color.GRAY);
            campoApellidos.setText(NO_EDITABLE);
            campoApellidos.setForeground(Color.GRAY);
        }
    }

    private void cargarCitas() {
        comboCitas.removeAllItems();
        String idPaciente = campoID.getText().trim();
        if (idPaciente.isEmpty()) {
            errorLabel.setText("Debe ingresar ID para buscar citas.");
            return;
        }

        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String sql = "SELECT idCita, fecha || ' ' || hora || ' - ' || servicio AS cita FROM CitasMedicas WHERE idPaciente = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPaciente);
            ResultSet rs = stmt.executeQuery();

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                comboCitas.addItem(rs.getInt("idCita") + ": " + rs.getString("cita"));
            }

            errorLabel.setText(hayResultados ? "" : "No se encontraron citas para este usuario.");
        } catch (Exception ex) {
            errorLabel.setText("Error cargando citas: " + ex.getMessage());
        }
    }

    private JPanel agrupar(JComboBox<String>... combos) {
        JPanel panel = new JPanel();
        for (JComboBox<String> c : combos) panel.add(c);
        return panel;
    }

    private String[] crearRango(int inicio, int fin) {
        String[] datos = new String[fin - inicio + 1];
        for (int i = 0; i < datos.length; i++)
            datos[i] = String.format("%02d", i + inicio);
        return datos;
    }
}
