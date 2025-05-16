package Emergencias;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import Utilidades.ColoresUDLAP;

public class FormularioLlamadaEmergencia extends JPanel {
    private final JTextField campoIDPaciente, campoNombrePaciente, campoTelefonoContacto;
    private final JComboBox<String> comboTipoEmergencia;
    private final JRadioButton rbRojo, rbNaranja, rbAmarillo, rbVerde, rbAzul;
    private final ButtonGroup grupoGravedad;
    private final JTextArea areaDescripcion;
    private final JComboBox<ResponsableItem> comboResponsable;

    public FormularioLlamadaEmergencia() {
        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuración de fuentes
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Formulario de Llamada de Emergencia", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        add(lblTitulo, gbc);

        // Campos del formulario
        gbc.gridwidth = 1;
        gbc.gridy++;

        // ID Paciente
        gbc.gridx = 0;
        JLabel lblID = new JLabel("ID del Paciente:");
        lblID.setFont(labelFont);
        lblID.setForeground(ColoresUDLAP.NEGRO);
        add(lblID, gbc);

        gbc.gridx = 1;
        campoIDPaciente = new JTextField(20);
        campoIDPaciente.setFont(fieldFont);
        campoIDPaciente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(campoIDPaciente, gbc);

        // Nombre Paciente
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblNombre = new JLabel("Nombre del Paciente:");
        lblNombre.setFont(labelFont);
        lblNombre.setForeground(ColoresUDLAP.NEGRO);
        add(lblNombre, gbc);

        gbc.gridx = 1;
        campoNombrePaciente = new JTextField(20);
        campoNombrePaciente.setFont(fieldFont);
        campoNombrePaciente.setEditable(false);
        campoNombrePaciente.setForeground(Color.GRAY);
        campoNombrePaciente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(campoNombrePaciente, gbc);

        // Cargar paciente por ID
        campoIDPaciente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                cargarDatosPaciente();
            }
        });

        // Tipo de emergencia
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblTipo = new JLabel("Tipo de Emergencia:");
        lblTipo.setFont(labelFont);
        lblTipo.setForeground(ColoresUDLAP.NEGRO);
        add(lblTipo, gbc);

        gbc.gridx = 1;
        comboTipoEmergencia = new JComboBox<>(new String[] {
                "Caída/Golpe", "Corte", "Quemadura", "Atragantamiento", "Intoxicación Alimentaria",
                "Accidente Eléctrico", "Accidente Deportivo", "Accidente Automovilístico"
        });
        comboTipoEmergencia.setFont(fieldFont);
        comboTipoEmergencia.setBackground(Color.WHITE);
        add(comboTipoEmergencia, gbc);

        // Gravedad
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblGravedad = new JLabel("Nivel de Gravedad:");
        lblGravedad.setFont(labelFont);
        lblGravedad.setForeground(ColoresUDLAP.NEGRO);
        add(lblGravedad, gbc);

        gbc.gridx = 1;
        JPanel panelGravedad = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelGravedad.setBackground(ColoresUDLAP.BLANCO);

        rbRojo = crearRadioButton("Rojo", ColoresUDLAP.ROJO);
        rbNaranja = crearRadioButton("Naranja", ColoresUDLAP.NARANJA);
        rbAmarillo = crearRadioButton("Amarillo", ColoresUDLAP.AMARILLO);
        rbVerde = crearRadioButton("Verde", ColoresUDLAP.VERDE);
        rbAzul = crearRadioButton("Azul", ColoresUDLAP.AZUL);

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

        // Teléfono de contacto
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblTelefono = new JLabel("Teléfono de Contacto:");
        lblTelefono.setFont(labelFont);
        lblTelefono.setForeground(ColoresUDLAP.NEGRO);
        add(lblTelefono, gbc);

        gbc.gridx = 1;
        campoTelefonoContacto = new JTextField(20);
        campoTelefonoContacto.setFont(fieldFont);
        campoTelefonoContacto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(campoTelefonoContacto, gbc);

        // Descripción
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(labelFont);
        lblDescripcion.setForeground(ColoresUDLAP.NEGRO);
        add(lblDescripcion, gbc);

        gbc.gridx = 1;
        areaDescripcion = new JTextArea(4, 20);
        areaDescripcion.setFont(fieldFont);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        areaDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(new JScrollPane(areaDescripcion), gbc);

        // Médico Responsable
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblResponsable = new JLabel("Médico Responsable:");
        lblResponsable.setFont(labelFont);
        lblResponsable.setForeground(ColoresUDLAP.NEGRO);
        add(lblResponsable, gbc);

        gbc.gridx = 1;
        comboResponsable = new JComboBox<>();
        comboResponsable.setFont(fieldFont);
        comboResponsable.setBackground(Color.WHITE);
        add(comboResponsable, gbc);
        cargarResponsables();

        // Espacio flexible al final
        gbc.gridy++;
        gbc.weighty = 1.0;
        add(Box.createGlue(), gbc);

        // Botones de acción
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRegistrar = new JButton("Registrar Emergencia");
        JButton btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        add(panelBotones, gbc);

        // Acción de los botones
        btnRegistrar.addActionListener(e -> registrarEmergencia());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private JRadioButton crearRadioButton(String text, Color color) {
        JRadioButton radio = new JRadioButton(text);
        radio.setFont(new Font("Arial", Font.PLAIN, 13));
        radio.setBackground(ColoresUDLAP.BLANCO);
        radio.setForeground(color.darker());
        return radio;
    }

    // Método para cargar los datos del paciente
    private void cargarDatosPaciente() {
        String txt = campoIDPaciente.getText().trim();
        if (!txt.matches("\\d+")) {
            campoNombrePaciente.setText("");
            return;
        }
        int id = Integer.parseInt(txt);
        String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?";
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campoNombrePaciente.setText(
                            rs.getString("Nombre") + " " + rs.getString("ApellidoPaterno") + " "
                                    + rs.getString("ApellidoMaterno"));
                } else {
                    campoNombrePaciente.setText("");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar paciente:\n" + ex.getMessage(), "Error de BD",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para cargar los médicos responsables
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

    // Método para registrar la emergencia
    private void registrarEmergencia() {
        // Validaciones previas
        String txtID = campoIDPaciente.getText().trim();
        if (!txtID.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "ID de paciente inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validaciones para los otros campos
        String telefono = campoTelefonoContacto.getText().trim();
        if (telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El teléfono de contacto es obligatorio", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Guardar la emergencia en la base de datos
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String query = "INSERT INTO Emergencias (IDPaciente, TipoEmergencia, Descripcion, Telefono) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, Integer.parseInt(txtID));
                ps.setString(2, (String) comboTipoEmergencia.getSelectedItem());
                ps.setString(3, areaDescripcion.getText());
                ps.setString(4, telefono);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Emergencia registrada con éxito", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar la emergencia:\n" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpiar el formulario
    private void limpiarFormulario() {
        campoIDPaciente.setText("");
        campoNombrePaciente.setText("");
        campoTelefonoContacto.setText("");
        areaDescripcion.setText("");
        comboTipoEmergencia.setSelectedIndex(0);
        grupoGravedad.clearSelection();
        comboResponsable.setSelectedIndex(0);
    }

    public JTextField[] obtenerCampos() {
        return new JTextField[] { campoIDPaciente, campoNombrePaciente, campoTelefonoContacto };
    }

    public JTextArea getAreaDescripcion() {
        return areaDescripcion;
    }

    // Clase interna ResponsableItem se mantiene igual
    private static class ResponsableItem {
        private final int id;
        private final String nombre;

        public ResponsableItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }

        public int getId() {
            return id;
        }
    }

    private void limpiarCampos() {
        campoIDPaciente.setText("");
        campoNombrePaciente.setText("");
        campoTelefonoContacto.setText("");
        areaDescripcion.setText("");
        comboTipoEmergencia.setSelectedIndex(0); // Resetea el JComboBox a su primer valor
        grupoGravedad.clearSelection(); // Desmarcar cualquier opción de gravedad
        comboResponsable.setSelectedIndex(0); // Resetea el JComboBox de médicos responsables
    }

}
