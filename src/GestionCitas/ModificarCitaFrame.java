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

        // Campos Nombre y Apellidos con placeholder inicial
        campoNombre = new JTextField(NO_EDITABLE, 12);
        campoNombre.setEditable(false);
        campoNombre.setForeground(Color.GRAY);

        campoApellidos = new JTextField(NO_EDITABLE, 12);
        campoApellidos.setEditable(false);
        campoApellidos.setForeground(Color.GRAY);

        // Campo ID
        campoID = new JTextField(12);
        campoID.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatosPersonales();
            }
        });

        // Combos y demás componentes
        comboCitas = new JComboBox<>();
        comboServicio = new JComboBox<>(new String[] { "Consulta", "Enfermería", "Examen Médico" });

        String[] dias = crearRango(1, 31);
        String[] meses = { "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" };
        String[] años = crearRango(2025, 2030);
        String[] horas = { "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21" };
        String[] mins = { "00", "30" };

        comboDia = new JComboBox<>(dias);
        comboMes = new JComboBox<>(meses);
        comboAño = new JComboBox<>(años);
        comboHora = new JComboBox<>(horas);
        comboMinuto = new JComboBox<>(mins);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);

        // Posicionamiento
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        add(campoNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 1;
        add(campoApellidos, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        add(campoID, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JButton btnBuscar = new JButton("Buscar Cita");
        btnBuscar.addActionListener(e -> cargarCitas());
        add(btnBuscar, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Selecciona tu cita:"), gbc);
        gbc.gridx = 1;
        add(comboCitas, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Servicio:"), gbc);
        gbc.gridx = 1;
        add(comboServicio, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Nueva Fecha:"), gbc);
        gbc.gridx = 1;
        add(agrupar(comboDia, comboMes, comboAño), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Hora de la Cita:"), gbc);
        gbc.gridx = 1;
        add(agrupar(comboHora, comboMinuto), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(errorLabel, gbc);

        gbc.gridy++;
        JButton btnModificar = new JButton("Modificar Cita");
        btnModificar.addActionListener(e -> modificarCita());
        add(btnModificar, gbc);

        gbc.gridy++;
        JButton btnCancelar = new JButton("Cancelar Cita");
        btnCancelar.addActionListener(e -> cancelarCita());
        add(btnCancelar, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
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

    /**
     * Carga nombre y apellidos según el ID ingresado.
     * Si encuentra registro, muestra datos en negro.
     * Si no, restaura el placeholder “No editable” en gris.
     */
    private void cargarDatosPersonales() {
        String idText = campoID.getText().trim();
        if (idText.isEmpty()) {
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
                String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno "
                        + "FROM InformacionAlumno WHERE ID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    campoNombre.setText(rs.getString("Nombre"));
                    campoNombre.setForeground(Color.BLACK);
                    campoApellidos.setText(
                            rs.getString("ApellidoPaterno") + " " + rs.getString("ApellidoMaterno"));
                    campoApellidos.setForeground(Color.BLACK);
                } else {
                    campoNombre.setText(NO_EDITABLE);
                    campoNombre.setForeground(Color.GRAY);
                    campoApellidos.setText(NO_EDITABLE);
                    campoApellidos.setForeground(Color.GRAY);
                }
            }
        } catch (NumberFormatException ex) {
            campoNombre.setText(NO_EDITABLE);
            campoNombre.setForeground(Color.GRAY);
            campoApellidos.setText(NO_EDITABLE);
            campoApellidos.setForeground(Color.GRAY);
        } catch (SQLException ex) {
            System.err.println("Error al cargar datos personales: " + ex.getMessage());
            campoNombre.setText(NO_EDITABLE);
            campoNombre.setForeground(Color.GRAY);
            campoApellidos.setText(NO_EDITABLE);
            campoApellidos.setForeground(Color.GRAY);
        }
    }

    // Carga en el comboBox las citas asociadas al paciente (usa el ID del paciente)
    private void cargarCitas() {
        comboCitas.removeAllItems();
        String idPaciente = campoID.getText().trim();

        if (idPaciente.isEmpty()) {
            errorLabel.setForeground(Color.RED);
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
            if (!hayResultados) {
                errorLabel.setForeground(Color.RED);
                errorLabel.setText("No se encontraron citas para el usuario indicado.");
            } else {
                errorLabel.setText("");
            }
        } catch (Exception e) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error cargando citas: " + e.getMessage());
        }
    }

    // Actualiza la fecha, hora y servicio de una cita ya existente. Se hace uso
    // sólo del idPaciente para identintificar el registro

    private void modificarCita() {
        String seleccion = (String) comboCitas.getSelectedItem();
        if (seleccion == null)
            return;

        int idCita = Integer.parseInt(seleccion.split(":")[0].trim());
        String idPaciente = campoID.getText().trim();
        String servicio = (String) comboServicio.getSelectedItem();
        int dia = Integer.parseInt((String) comboDia.getSelectedItem());
        int mes = comboMes.getSelectedIndex() + 1;
        int año = Integer.parseInt((String) comboAño.getSelectedItem());
        String hora = (String) comboHora.getSelectedItem();
        String minuto = (String) comboMinuto.getSelectedItem();

        if (!ValidacionesCita.esIdValido(idPaciente)) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("ID inválido (solo números)");
            return;
        }
        if (!ValidacionesCita.esFechaValida(dia, mes, año)) {
            errorLabel.setForeground(Color.RED);
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
                errorLabel.setForeground(Color.RED);
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

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                errorLabel.setForeground(Color.GREEN);
                errorLabel.setText("Cita modificada exitosamente.");
            } else {
                errorLabel.setForeground(Color.RED);
                errorLabel.setText("No se pudo modificar la cita. Verifique los datos.");
            }
        } catch (Exception ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error: " + ex.getMessage());
        }
    }

    // Elimina la cita seleccionada.

    private void cancelarCita() {
        String seleccion = (String) comboCitas.getSelectedItem();
        if (seleccion == null)
            return;

        int idCita = Integer.parseInt(seleccion.split(":")[0].trim());

        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String sql = "DELETE FROM CitasMedicas WHERE idCita = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCita);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                errorLabel.setForeground(Color.GREEN);
                errorLabel.setText("Cita cancelada exitosamente.");
                comboCitas.removeItem(seleccion);
            } else {
                errorLabel.setForeground(Color.RED);
                errorLabel.setText("No se pudo cancelar la cita. Verifique los datos.");
            }
        } catch (Exception ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error: " + ex.getMessage());
        }
    }

    // Agrupar componentes JComboBox en un panel.

    private JPanel agrupar(JComboBox<String>... combos) {
        JPanel panel = new JPanel();
        for (JComboBox<String> combo : combos)
            panel.add(combo);
        return panel;
    }

    // Crea un arreglo de String con números en formato de dos dígitos a partir de
    // un rango.
    private String[] crearRango(int inicio, int fin) {
        String[] datos = new String[fin - inicio + 1];
        for (int i = 0; i < datos.length; i++)
            datos[i] = String.format("%02d", i + inicio);
        return datos;
    }
}