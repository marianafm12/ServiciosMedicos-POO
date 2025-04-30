package Emergencias;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccidenteFrame extends JFrame {
    // 0) Folio de Emergencia
    private final JTextField campoIDEmergencia;

    // Datos del paciente (cargados desde Emergencias + InformacionAlumno)
    private final JTextField idPacienteField;
    private final JTextField nombrePacienteField;
    private final JTextField apellidosPacienteField;
    private final JTextField correoPacienteField;
    private final JTextField telefonoPacienteField;

    // Fecha y hora del accidente
    private final JComboBox<String> diaAccidenteBox;
    private final JComboBox<String> mesAccidenteBox;
    private final JComboBox<String> anoAccidenteBox;
    private final JComboBox<String> horaAccidenteBox;
    private final JComboBox<String> minutoAccidenteBox;

    // Signos vitales y consciencia
    private final JTextField presionArterialField;
    private final JTextField ritmoCardiacoField;
    private final JTextField ritmoRespiratorioField;
    private final JRadioButton alertaRadioButton;
    private final JRadioButton conscienteRadioButton;
    private final JRadioButton inconscienteRadioButton;
    private final ButtonGroup conscienciaGroup;

    // Género del paciente (para el informe)
    private final JComboBox<String> generoPacienteBox;

    // Contacto de emergencia
    private final JTextField idContactoField;
    private final JTextField nombreContactoField;
    private final JTextField apellidosContactoField;
    private final JTextField correoContactoField;
    private final JTextField telefonoContactoField;

    // Descripción y observaciones
    private final JTextArea descripcionAccidenteArea;
    private final JTextArea observacionesArea;

    // Estado de la emergencia
    private final JComboBox<String> comboEstadoEmergencia;

    // Mensajes
    private final JLabel errorLabel;

    // Botones de acción
    private final JButton btnConfirmar;
    private final JButton btnModificar;

    public AccidenteFrame() {
        setTitle("Formulario de Accidente");
        setSize(820, 1020);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- 0) Folio de Emergencia ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("*Folio de Emergencia:"), gbc);
        gbc.gridx = 1;
        campoIDEmergencia = new JTextField(10);
        add(campoIDEmergencia, gbc);
        campoIDEmergencia.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatosEmergencia();
            }
        });

        // --- 1) Fecha del Accidente ---
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("*Fecha del Accidente:"), gbc);
        gbc.gridx = 1;
        String[] meses = { "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" };
        diaAccidenteBox = new JComboBox<>();
        mesAccidenteBox = new JComboBox<>(meses);
        anoAccidenteBox = new JComboBox<>();
        horaAccidenteBox = new JComboBox<>();
        minutoAccidenteBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++)
            diaAccidenteBox.addItem(String.valueOf(i));
        for (int y = LocalDate.now().getYear(); y <= 2030; y++)
            anoAccidenteBox.addItem(String.valueOf(y));
        for (int h = 0; h < 24; h++)
            horaAccidenteBox.addItem(String.format("%02d", h));
        for (int m = 0; m < 60; m++)
            minutoAccidenteBox.addItem(String.format("%02d", m));
        JPanel fechaPanel = new JPanel();
        fechaPanel.add(diaAccidenteBox);
        fechaPanel.add(mesAccidenteBox);
        fechaPanel.add(anoAccidenteBox);
        fechaPanel.add(horaAccidenteBox);
        fechaPanel.add(minutoAccidenteBox);
        add(fechaPanel, gbc);

        // --- 2) Datos del Paciente (solo lectura) ---
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("*ID Paciente:"), gbc);
        gbc.gridx = 1;
        idPacienteField = new JTextField(10);
        idPacienteField.setEditable(false);
        add(idPacienteField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Nombre Paciente:"), gbc);
        gbc.gridx = 1;
        nombrePacienteField = new JTextField(20);
        nombrePacienteField.setEditable(false);
        add(nombrePacienteField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        add(new JLabel("Apellidos Paciente:"), gbc);
        gbc.gridx = 1;
        apellidosPacienteField = new JTextField(25);
        apellidosPacienteField.setEditable(false);
        add(apellidosPacienteField, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        add(new JLabel("Correo Paciente:"), gbc);
        gbc.gridx = 1;
        correoPacienteField = new JTextField(25);
        correoPacienteField.setEditable(false);
        add(correoPacienteField, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        add(new JLabel("Teléfono Paciente:"), gbc);
        gbc.gridx = 1;
        telefonoPacienteField = new JTextField(10);
        add(telefonoPacienteField, gbc);

        // --- 3) Signos Vitales & Consciencia ---
        gbc.gridy = 7;
        gbc.gridx = 0;
        add(new JLabel("*Presión Arterial:"), gbc);
        gbc.gridx = 1;
        presionArterialField = new JTextField(10);
        add(presionArterialField, gbc);

        gbc.gridy = 8;
        gbc.gridx = 0;
        add(new JLabel("*Ritmo Cardíaco:"), gbc);
        gbc.gridx = 1;
        ritmoCardiacoField = new JTextField(10);
        add(ritmoCardiacoField, gbc);

        gbc.gridy = 9;
        gbc.gridx = 0;
        add(new JLabel("*Ritmo Respiratorio:"), gbc);
        gbc.gridx = 1;
        ritmoRespiratorioField = new JTextField(10);
        add(ritmoRespiratorioField, gbc);

        gbc.gridy = 10;
        gbc.gridx = 0;
        add(new JLabel("*Consciencia:"), gbc);
        gbc.gridx = 1;
        alertaRadioButton = new JRadioButton("Alerta");
        conscienteRadioButton = new JRadioButton("Consciente");
        inconscienteRadioButton = new JRadioButton("Inconsciente");
        conscienciaGroup = new ButtonGroup();
        conscienciaGroup.add(alertaRadioButton);
        conscienciaGroup.add(conscienteRadioButton);
        conscienciaGroup.add(inconscienteRadioButton);
        JPanel conscienciaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        conscienciaPanel.add(alertaRadioButton);
        conscienciaPanel.add(conscienteRadioButton);
        conscienciaPanel.add(inconscienteRadioButton);
        add(conscienciaPanel, gbc);

        // --- 4) Género del Paciente ---
        gbc.gridy = 11;
        gbc.gridx = 0;
        add(new JLabel("*Género Paciente:"), gbc);
        gbc.gridx = 1;
        generoPacienteBox = new JComboBox<>(new String[] { "Masculino", "Femenino", "Otro" });
        add(generoPacienteBox, gbc);

        // --- 5) Descripción y Observaciones---
        gbc.gridy = 12;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("*Descripción del Accidente:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1; // toma todo el espacio horizontal restante
        gbc.weighty = 1; // para que al estirar la ventana también crezca en alto
        gbc.fill = GridBagConstraints.BOTH;
        descripcionAccidenteArea = new JTextArea(12, 30);
        JScrollPane scrollDesc = new JScrollPane(
                descripcionAccidenteArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollDesc, gbc);

        gbc.gridy = 13;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Observaciones Adicionales:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        observacionesArea = new JTextArea(12, 30);
        JScrollPane scrollObs = new JScrollPane(
                observacionesArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollObs, gbc);

        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- 6) Contacto de Emergencia ---
        gbc.gridy = 14;
        gbc.gridx = 0;
        add(new JLabel("ID Contacto:"), gbc);
        gbc.gridx = 1;
        idContactoField = new JTextField(10);
        add(idContactoField, gbc);

        gbc.gridy = 15;
        gbc.gridx = 0;
        add(new JLabel("Nombre Contacto:"), gbc);
        gbc.gridx = 1;
        nombreContactoField = new JTextField(20);
        add(nombreContactoField, gbc);

        gbc.gridy = 16;
        gbc.gridx = 0;
        add(new JLabel("Apellidos Contacto:"), gbc);
        gbc.gridx = 1;
        apellidosContactoField = new JTextField(25);
        add(apellidosContactoField, gbc);

        gbc.gridy = 17;
        gbc.gridx = 0;
        add(new JLabel("Correo Contacto:"), gbc);
        gbc.gridx = 1;
        correoContactoField = new JTextField(25);
        add(correoContactoField, gbc);

        gbc.gridy = 18;
        gbc.gridx = 0;
        add(new JLabel("Teléfono Contacto:"), gbc);
        gbc.gridx = 1;
        telefonoContactoField = new JTextField(10);
        add(telefonoContactoField, gbc);

        // --- 7) Estado de la Emergencia ---
        gbc.gridy = 19;
        gbc.gridx = 0;
        add(new JLabel("*Estado Emergencia:"), gbc);
        gbc.gridx = 1;
        comboEstadoEmergencia = new JComboBox<>(new String[] { "Pendiente", "Completo", "Transferido" });
        add(comboEstadoEmergencia, gbc);

        // --- 8) Botones de acción ---
        gbc.gridy = 20;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnConfirmar = new JButton("Confirmar Accidente");
        btnModificar = new JButton("Modificar Accidente");
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnModificar);
        add(panelBotones, gbc);

        // 9) Mensaje de estado
        gbc.gridy = 21;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        add(errorLabel, gbc);

        // Listeners
        btnConfirmar.addActionListener(e -> registrarAccidente());
        btnModificar.addActionListener(e -> modificarAccidente());

        setVisible(true);
    }

    /** Carga datos de la llamada y, si existe, del accidente asociado */
    private void cargarDatosEmergencia() {
        String txtId = campoIDEmergencia.getText().trim();
        if (!txtId.matches("\\d+"))
            return;
        int idEmerg = Integer.parseInt(txtId);

        // 1) Cargar llamada + paciente + estado
        String sqlE = "SELECT e.IDPaciente, e.Estado, a.Nombre, a.ApellidoPaterno, a.ApellidoMaterno, a.Correo FROM Emergencias e JOIN InformacionAlumno a ON e.IDPaciente = a.ID WHERE e.IDEmergencia = ? ";
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                PreparedStatement psE = conn.prepareStatement(sqlE)) {
            psE.setInt(1, idEmerg);
            try (ResultSet rs = psE.executeQuery()) {
                if (rs.next()) {
                    idPacienteField.setText(String.valueOf(rs.getInt("IDPaciente")));
                    nombrePacienteField.setText(rs.getString("Nombre"));
                    apellidosPacienteField.setText(
                            rs.getString("ApellidoPaterno") + " " + rs.getString("ApellidoMaterno"));
                    correoPacienteField.setText(rs.getString("Correo"));
                    comboEstadoEmergencia.setSelectedItem(rs.getString("Estado"));
                }
            }

            // 2) Cargar accidente si ya existe
            String sqlA = "SELECT FechaAccidente, GeneroPaciente, PresionArterial, RitmoCardiaco, RitmoRespiratorio, Consciencia, DescripcionAccidente, Observaciones, IDContacto, NombreContacto, ApellidosContacto, CorreoContacto, TelefonoContacto, EstadoEmergencia FROM Accidentes WHERE IDEmergencia = ?";
            try (PreparedStatement psA = conn.prepareStatement(sqlA)) {
                psA.setInt(1, idEmerg);
                try (ResultSet rsA = psA.executeQuery()) {
                    if (rsA.next()) {
                        // FechaAccidente → combo boxes
                        String ts = rsA.getString("FechaAccidente");
                        LocalDateTime dt = LocalDateTime.parse(
                                ts, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        diaAccidenteBox.setSelectedItem(String.valueOf(dt.getDayOfMonth()));
                        mesAccidenteBox.setSelectedIndex(dt.getMonthValue() - 1);
                        anoAccidenteBox.setSelectedItem(String.valueOf(dt.getYear()));
                        horaAccidenteBox.setSelectedItem(String.format("%02d", dt.getHour()));
                        minutoAccidenteBox.setSelectedItem(String.format("%02d", dt.getMinute()));

                        generoPacienteBox.setSelectedItem(rsA.getString("GeneroPaciente"));
                        presionArterialField.setText(rsA.getString("PresionArterial"));
                        ritmoCardiacoField.setText(String.valueOf(rsA.getInt("RitmoCardiaco")));
                        ritmoRespiratorioField.setText(String.valueOf(rsA.getInt("RitmoRespiratorio")));

                        String cons = rsA.getString("Consciencia");
                        alertaRadioButton.setSelected("Alerta".equals(cons));
                        conscienteRadioButton.setSelected("Consciente".equals(cons));
                        inconscienteRadioButton.setSelected("Inconsciente".equals(cons));

                        descripcionAccidenteArea.setText(rsA.getString("DescripcionAccidente"));
                        observacionesArea.setText(rsA.getString("Observaciones"));

                        idContactoField.setText(
                                rsA.getObject("IDContacto") != null
                                        ? String.valueOf(rsA.getInt("IDContacto"))
                                        : "");
                        nombreContactoField.setText(rsA.getString("NombreContacto"));
                        apellidosContactoField.setText(rsA.getString("ApellidosContacto"));
                        correoContactoField.setText(rsA.getString("CorreoContacto"));
                        telefonoContactoField.setText(rsA.getString("TelefonoContacto"));

                        comboEstadoEmergencia.setSelectedItem(rsA.getString("EstadoEmergencia"));
                    }
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Inserta un nuevo accidente */
    private void registrarAccidente() {
        if (!validarCamposBasicos())
            return;
        ejecutarInsertOUpdate(false);
    }

    /** Actualiza el accidente existente */
    private void modificarAccidente() {
        if (!validarCamposBasicos())
            return;
        ejecutarInsertOUpdate(true);
    }

    /** Valida formatos y campos obligatorios */
    private boolean validarCamposBasicos() {
        String txtIdEmerg = campoIDEmergencia.getText().trim();
        if (!txtIdEmerg.matches("\\d+")) {
            errorLabel.setText("Folio de emergencia inválido.");
            return false;
        }

        // Fecha
        int d = Integer.parseInt((String) diaAccidenteBox.getSelectedItem());
        int m = mesAccidenteBox.getSelectedIndex() + 1;
        int y = Integer.parseInt((String) anoAccidenteBox.getSelectedItem());

        if (!isDateValid(d, m, y)) {
            errorLabel.setText("La fecha del accidente no puede ser anterior a hoy.");
            return false;
        }

        // Signos vitales y consciencia
        if (presionArterialField.getText().trim().isEmpty()
                || ritmoCardiacoField.getText().trim().isEmpty()
                || ritmoRespiratorioField.getText().trim().isEmpty()
                || conscienciaGroup.getSelection() == null) {
            errorLabel.setText("Complete todos los signos vitales y nivel de consciencia.");
            return false;
        }

        // Descripción
        if (descripcionAccidenteArea.getText().trim().isEmpty()) {
            errorLabel.setText("La descripción del accidente es obligatoria.");
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    /**
     * Si esModificar==false hace INSERT; si esModificar==true hace UPDATE.
     * También actualiza Emergencias.Estado.
     */
    private void ejecutarInsertOUpdate(boolean esModificar) {
        int idEmerg = Integer.parseInt(campoIDEmergencia.getText().trim());
        String fechaAcc = String.format("%04d-%02d-%02d %02d:%02d:00",
                Integer.parseInt((String) anoAccidenteBox.getSelectedItem()),
                mesAccidenteBox.getSelectedIndex() + 1,
                Integer.parseInt((String) diaAccidenteBox.getSelectedItem()),
                Integer.parseInt((String) horaAccidenteBox.getSelectedItem()),
                Integer.parseInt((String) minutoAccidenteBox.getSelectedItem()));

        String genero = (String) generoPacienteBox.getSelectedItem();
        String presion = presionArterialField.getText().trim();
        int rc = Integer.parseInt(ritmoCardiacoField.getText().trim());
        int rr = Integer.parseInt(ritmoRespiratorioField.getText().trim());
        String cons = alertaRadioButton.isSelected() ? "Alerta"
                : conscienteRadioButton.isSelected() ? "Consciente"
                        : "Inconsciente";
        String desc = descripcionAccidenteArea.getText().trim();
        String obs = observacionesArea.getText().trim();

        String txtIdCont = idContactoField.getText().trim();
        Integer idCont = txtIdCont.matches("\\d+") ? Integer.parseInt(txtIdCont) : null;
        String nomCont = nombreContactoField.getText().trim();
        String apCont = apellidosContactoField.getText().trim();
        String mailCont = correoContactoField.getText().trim();
        String telCont = telefonoContactoField.getText().trim();

        String estadoNvo = (String) comboEstadoEmergencia.getSelectedItem();

        String sqlIns = "INSERT INTO Accidentes (IDEmergencia, FechaAccidente, GeneroPaciente, PresionArterial, RitmoCardiaco, RitmoRespiratorio, Consciencia, DescripcionAccidente, Observaciones, IDContacto, NombreContacto, ApellidosContacto, CorreoContacto, TelefonoContacto, EstadoEmergencia) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdA = "UPDATE Accidentes SET FechaAccidente=?, GeneroPaciente=?, PresionArterial=?, RitmoCardiaco=?, RitmoRespiratorio=?, Consciencia=?, DescripcionAccidente=?, Observaciones=?, IDContacto=?, NombreContacto=?, ApellidosContacto=?, CorreoContacto=?, TelefonoContacto=?, EstadoEmergencia=? WHERE IDEmergencia=?";
        String sqlUpdE = "UPDATE Emergencias SET Estado=? WHERE IDEmergencia=?";

        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                PreparedStatement psAE = conn.prepareStatement(esModificar ? sqlUpdA : sqlIns);
                PreparedStatement psEE = conn.prepareStatement(sqlUpdE)) {

            // Parámetros both INSERT and UPDATE Accidentes
            if (esModificar) {
                // UPDATE Accidentes: posicion 15 = IDEmergencia
                psAE.setString(1, fechaAcc);
                psAE.setString(2, genero);
                psAE.setString(3, presion);
                psAE.setInt(4, rc);
                psAE.setInt(5, rr);
                psAE.setString(6, cons);
                psAE.setString(7, desc);
                psAE.setString(8, obs.isEmpty() ? null : obs);
                if (idCont != null)
                    psAE.setInt(9, idCont);
                else
                    psAE.setNull(9, Types.INTEGER);
                psAE.setString(10, nomCont.isEmpty() ? null : nomCont);
                psAE.setString(11, apCont.isEmpty() ? null : apCont);
                psAE.setString(12, mailCont.isEmpty() ? null : mailCont);
                psAE.setString(13, telCont.isEmpty() ? null : telCont);
                psAE.setString(14, estadoNvo);
                psAE.setInt(15, idEmerg);

            } else {
                // INSERT Accidentes
                psAE.setInt(1, idEmerg);
                psAE.setString(2, fechaAcc);
                psAE.setString(3, genero);
                psAE.setString(4, presion);
                psAE.setInt(5, rc);
                psAE.setInt(6, rr);
                psAE.setString(7, cons);
                psAE.setString(8, desc);
                psAE.setString(9, obs.isEmpty() ? null : obs);
                if (idCont != null)
                    psAE.setInt(10, idCont);
                else
                    psAE.setNull(10, Types.INTEGER);
                psAE.setString(11, nomCont.isEmpty() ? null : nomCont);
                psAE.setString(12, apCont.isEmpty() ? null : apCont);
                psAE.setString(13, mailCont.isEmpty() ? null : mailCont);
                psAE.setString(14, telCont.isEmpty() ? null : telCont);
                psAE.setString(15, estadoNvo);
            }

            int filasAE = psAE.executeUpdate();
            if (filasAE == 0)
                throw new SQLException("No se afectó registro de Accidentes.");

            // Actualizar Emergencias
            psEE.setString(1, estadoNvo);
            psEE.setInt(2, idEmerg);
            int filasE = psEE.executeUpdate();
            if (filasE == 0)
                throw new SQLException("No se actualizó Estado en Emergencias.");

            errorLabel.setForeground(Color.GREEN);
            errorLabel.setText(esModificar
                    ? "Accidente modificado correctamente."
                    : "Accidente registrado correctamente.");

        } catch (SQLException ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error de BD: " + ex.getMessage());
        }
    }

    private boolean isDateValid(int d, int m, int y) {
        return !LocalDate.of(y, m, d).isBefore(LocalDate.now());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AccidenteFrame::new);
    }
}
