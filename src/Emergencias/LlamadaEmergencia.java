package Emergencias;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import java.sql.Timestamp;
import javax.swing.SpinnerDateModel;

public class LlamadaEmergencia extends JFrame {
    private final JTextField campoNombrePaciente;
    private final JTextField campoIDPaciente;
    private final JTextField campoUbicacion;
    private final JTextField campoTelefonoContacto;
    private final JComboBox<String> comboTipoEmergencia;
    private final JRadioButton rbRojo, rbNaranja, rbAmarillo, rbVerde, rbAzul;
    private final ButtonGroup grupoGravedad;
    private final JTextArea areaDescripcion;
    private final JLabel errorLabel;
    private final JComboBox<ResponsableItem> comboResponsable;
    private final JSpinner spinnerFechaIncidente;

    public LlamadaEmergencia() {
        setTitle("Emergencias UDLAP");
        setSize(550, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // 1) Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Formulario de Emergencia", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitulo, gbc);

        // 2) Nombre del paciente (no editable, se carga por ID)
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Nombre del Paciente:"), gbc);
        gbc.gridx = 1;
        campoNombrePaciente = new JTextField(20);
        campoNombrePaciente.setEditable(false);
        campoNombrePaciente.setForeground(Color.GRAY);
        add(campoNombrePaciente, gbc);

        // 3) ID del Paciente
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("ID del Paciente:"), gbc);
        gbc.gridx = 1;
        campoIDPaciente = new JTextField(10);
        add(campoIDPaciente, gbc);
        // Cuando pierda foco, cargamos el nombre
        campoIDPaciente.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatosPaciente();
            }
        });

        // 4) Médico Responsable
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Médico Responsable:"), gbc);
        gbc.gridx = 1;
        comboResponsable = new JComboBox<>();
        add(comboResponsable, gbc);
        cargarResponsables();

        // 5) Ubicación
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1;
        campoUbicacion = new JTextField(20);
        add(campoUbicacion, gbc);

        // 6) Tipo de Emergencia
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Tipo de Emergencia:"), gbc);
        gbc.gridx = 1;
        comboTipoEmergencia = new JComboBox<>(new String[] {
                "Caída/Golpe", "Corte", "Quemadura", "Atragantamiento",
                "Intoxicación Alimentaria", "Accidente Eléctrico",
                "Accidente Deportivo", "Accidente Automovilístico"
        });
        add(comboTipoEmergencia, gbc);

        // 7) Gravedad
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Gravedad:"), gbc);
        gbc.gridx = 1;
        JPanel panelGravedad = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbRojo = new JRadioButton("Rojo");
        rbNaranja = new JRadioButton("Naranja");
        rbAmarillo = new JRadioButton("Amarillo");
        rbVerde = new JRadioButton("Verde");
        rbAzul = new JRadioButton("Azul");
        grupoGravedad = new ButtonGroup();
        grupoGravedad.add(rbRojo);
        grupoGravedad.add(rbNaranja);
        grupoGravedad.add(rbAmarillo);
        grupoGravedad.add(rbVerde);
        grupoGravedad.add(rbAzul);
        panelGravedad.add(rbRojo);
        panelGravedad.add(rbNaranja);
        panelGravedad.add(rbAmarillo);
        panelGravedad.add(rbVerde);
        panelGravedad.add(rbAzul);
        add(panelGravedad, gbc);

        // 8) Fecha del Incidente
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Fecha del Incidente:"), gbc);
        gbc.gridx = 1;
        spinnerFechaIncidente = new JSpinner(new SpinnerDateModel());
        spinnerFechaIncidente.setEditor(
                new JSpinner.DateEditor(spinnerFechaIncidente, "yyyy-MM-dd HH:mm:ss"));
        add(spinnerFechaIncidente, gbc);

        // 9) Teléfono de Contacto (opcional)
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Teléfono de Contacto:"), gbc);
        gbc.gridx = 1;
        campoTelefonoContacto = new JTextField(15);
        add(campoTelefonoContacto, gbc);

        // 10) Descripción
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weighty = 1.0;
        add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        areaDescripcion = new JTextArea(6, 20);
        add(new JScrollPane(areaDescripcion), gbc);

        // 11) Label de error/confirmación
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        add(errorLabel, gbc);

        // 12) Botón Registrar
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        JButton btnRegistrar = new JButton("Registrar Emergencia");
        add(btnRegistrar, gbc);
        btnRegistrar.addActionListener(e -> registrarEmergencia());

        // 13) Botones Menú y Regresar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnMenu = new JButton("Menú Principal");
        JButton btnRegresar = new JButton("Regresar");
        panelBotones.add(btnMenu);
        panelBotones.add(btnRegresar);
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(panelBotones, gbc);

        btnMenu.addActionListener(e -> {
            new Inicio.PortadaFrame().setVisible(true);
            dispose();
        });
        btnRegresar.addActionListener(e -> {
            new Inicio.MenuMedicosFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    /**
     * Carga el nombre completo del paciente desde InformacionAlumno.
     */
    private void cargarDatosPaciente() {
        String txt = campoIDPaciente.getText().trim();
        if (!txt.matches("\\d+")) {
            campoNombrePaciente.setText("");
            return;
        }
        int id = Integer.parseInt(txt);
        String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno "
                + "FROM InformacionAlumno WHERE ID = ?";
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campoNombrePaciente.setText(
                            rs.getString("Nombre") + " "
                                    + rs.getString("ApellidoPaterno") + " "
                                    + rs.getString("ApellidoMaterno"));
                } else {
                    campoNombrePaciente.setText("");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar paciente:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga la lista de médicos responsables desde InformacionMedico.
     */
    private void cargarResponsables() {
        String sql = "SELECT ID, Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionMedico";
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String n = rs.getString("Nombre") + " "
                        + rs.getString("ApellidoPaterno") + " "
                        + rs.getString("ApellidoMaterno");
                comboResponsable.addItem(new ResponsableItem(id, n));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar médicos:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida campos y guarda la emergencia en la tabla Emergencias.
     */
    private void registrarEmergencia() {
        // 1) Validaciones
        String txtID = campoIDPaciente.getText().trim();
        if (!txtID.matches("\\d+")) {
            errorLabel.setText("ID de paciente inválido.");
            return;
        }
        int idPaciente = Integer.parseInt(txtID);

        ResponsableItem resp = (ResponsableItem) comboResponsable.getSelectedItem();
        if (resp == null) {
            errorLabel.setText("Seleccione un médico responsable.");
            return;
        }
        int idResp = resp.id;

        String ubic = campoUbicacion.getText().trim();
        String desc = areaDescripcion.getText().trim();
        String tel = campoTelefonoContacto.getText().trim();
        if (ubic.isEmpty()) {
            errorLabel.setText("La ubicación es obligatoria.");
            return;
        }
        if (desc.isEmpty()) {
            errorLabel.setText("La descripción es obligatoria.");
            return;
        }

        String gravedad = null;
        if (rbRojo.isSelected())
            gravedad = "Rojo";
        if (rbNaranja.isSelected())
            gravedad = "Naranja";
        if (rbAmarillo.isSelected())
            gravedad = "Amarillo";
        if (rbVerde.isSelected())
            gravedad = "Verde";
        if (rbAzul.isSelected())
            gravedad = "Azul";
        if (gravedad == null) {
            errorLabel.setText("Seleccione la gravedad.");
            return;
        }

        String tipo = (String) comboTipoEmergencia.getSelectedItem();
        Date f = (Date) spinnerFechaIncidente.getValue();
        Timestamp ts = new Timestamp(f.getTime());

        // 2) Comprobar existencia del paciente
        String chk = "SELECT 1 FROM InformacionAlumno WHERE ID = ?";
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                PreparedStatement psChk = conn.prepareStatement(chk)) {
            psChk.setInt(1, idPaciente);
            try (ResultSet rs = psChk.executeQuery()) {
                if (!rs.next()) {
                    errorLabel.setText("No existe paciente con ese ID.");
                    return;
                }
            }

            // 3) Insertar emergencia
            String ins = "INSERT INTO Emergencias "
                    + "(IDPaciente, IDResponsable, Ubicacion, TipoDeEmergencia, Gravedad, "
                    + "Descripcion, FechaIncidente, TelefonoContacto) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psIns = conn.prepareStatement(ins)) {
                psIns.setInt(1, idPaciente);
                psIns.setInt(2, idResp);
                psIns.setString(3, ubic);
                psIns.setString(4, tipo);
                psIns.setString(5, gravedad);
                psIns.setString(6, desc);
                psIns.setTimestamp(7, ts);
                psIns.setString(8, tel.isEmpty() ? null : tel);

                int filas = psIns.executeUpdate();
                if (filas > 0) {
                    errorLabel.setForeground(Color.GREEN);
                    errorLabel.setText("Emergencia registrada con éxito.");
                } else {
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText("No se pudo registrar la emergencia.");
                }
            }

        } catch (SQLException ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error al registrar: " + ex.getMessage());
        }
    }

    /**
     * Elemento para JComboBox de médicos responsables.
     */
    private static class ResponsableItem {
        final int id;
        final String nombre;

        ResponsableItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LlamadaEmergencia::new);
    }
}
