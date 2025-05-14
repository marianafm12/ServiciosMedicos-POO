package GestionCitas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;
import java.time.LocalDate;

public class AgendaCitaFrame extends JFrame {
    private JTextField campoNombre, campoApellidos, campoID;
    private JComboBox<String> comboMes, comboHora, comboMinuto, comboServicio;
    private JComboBox<Integer> comboDia, comboAño;
    private JLabel errorLabel;

    private static final String NO_EDITABLE = "No editable";

    public AgendaCitaFrame() {
        setTitle("Agendar Citas");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Registro de Cita Médica", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        campoNombre = new JTextField(NO_EDITABLE, 12);
        campoNombre.setEditable(false);
        campoNombre.setForeground(Color.GRAY);
        add(campoNombre, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 1;
        campoApellidos = new JTextField(NO_EDITABLE, 12);
        campoApellidos.setEditable(false);
        campoApellidos.setForeground(Color.GRAY);
        add(campoApellidos, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        campoID = new JTextField(12);
        add(campoID, gbc);
        campoID.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatosPersonales();
            }
        });

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Servicio:"), gbc);
        gbc.gridx = 1;
        comboServicio = new JComboBox<>(new String[]{"Consulta", "Enfermería", "Examen Médico"});
        add(comboServicio, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Fecha de la Cita:"), gbc);
        comboMes = new JComboBox<>(new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"});
        comboDia = new JComboBox<>();
        comboAño = new JComboBox<>();
        for (int i = 1; i <= 31; i++) comboDia.addItem(i);
        for (int i = LocalDate.now().getYear(); i <= 2030; i++) comboAño.addItem(i);
        JPanel fechaPanel = new JPanel();
        fechaPanel.add(comboDia);
        fechaPanel.add(comboMes);
        fechaPanel.add(comboAño);
        gbc.gridx = 1;
        add(fechaPanel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Hora de la Cita:"), gbc);
        comboHora = new JComboBox<>(new String[]{"08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"});
        comboMinuto = new JComboBox<>(new String[]{"00", "30"});
        JPanel horaPanel = new JPanel();
        horaPanel.add(comboHora);
        horaPanel.add(comboMinuto);
        gbc.gridx = 1;
        add(horaPanel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        add(errorLabel, gbc);

        gbc.gridy++;
        JButton btnConfirmar = new JButton("Confirmar Cita");
        add(btnConfirmar, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JButton btnMenu = new JButton("Menú Principal");
        gbc.gridx = 0;
        add(btnMenu, gbc);
        JButton btnRegresar = new JButton("Regresar");
        gbc.gridx = 1;
        add(btnRegresar, gbc);

        btnConfirmar.addActionListener(e -> validarYConfirmarCita());
        btnMenu.addActionListener(e -> {
            new Inicio.PortadaFrame().setVisible(true);
            dispose();
        });
        btnRegresar.addActionListener(e -> {
            new InicioFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    private boolean validarSesionActiva(String idCampo) {
        return idCampo.equals(String.valueOf(Inicio.SesionUsuario.getPacienteActual()));
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
                    campoNombre.setText("ID inválido");
                    campoNombre.setForeground(Color.GRAY);
                    campoApellidos.setText("ID inválido");
                    campoApellidos.setForeground(Color.GRAY);
                    System.out.println("[ERROR] ID inválido - no encontrado en base de datos.");
                }
            }
        } catch (Exception ex) {
            campoNombre.setText("ID inválido");
            campoNombre.setForeground(Color.GRAY);
            campoApellidos.setText("ID inválido");
            campoApellidos.setForeground(Color.GRAY);
            System.out.println("[ERROR] ID inválido - excepción al consultar: " + ex.getMessage());
        }
    }

    private void validarYConfirmarCita() {
        String id = campoID.getText().trim();

        if (!validarSesionActiva(id)) {
            errorLabel.setText("Error: El ID no coincide con el usuario en sesión.");
            System.out.println("[ERROR] ID ingresado no coincide con sesión activa.");
            return;
        }

        String servicio = (String) comboServicio.getSelectedItem();
        int dia = (int) comboDia.getSelectedItem();
        int mes = comboMes.getSelectedIndex() + 1;
        int año = (int) comboAño.getSelectedItem();
        String hora = (String) comboHora.getSelectedItem();
        String minuto = (String) comboMinuto.getSelectedItem();

        if (!ValidacionesCita.esIdValido(id)) {
            errorLabel.setText("ID inválido (solo números)");
            System.out.println("[ERROR] ID inválido - caracteres no permitidos.");
            return;
        }
        if (!ValidacionesCita.esFechaValida(dia, mes, año)) {
            errorLabel.setText("Fecha inválida (debe ser futura)");
            System.out.println("[ERROR] Fecha inválida - no es futura.");
            return;
        }

        String fecha = String.format("%04d-%02d-%02d", año, mes, dia);
        String horaFinal = hora + ":" + minuto;

        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String sqlOcupado = "SELECT COUNT(*) FROM CitasMedicas WHERE fecha=? AND hora=? AND servicio=?";
            PreparedStatement checkStmt = conn.prepareStatement(sqlOcupado);
            checkStmt.setString(1, fecha);
            checkStmt.setString(2, horaFinal);
            checkStmt.setString(3, servicio);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                conn.close();
                Object[] opciones = {"Sí", "No"};
                int opcion = JOptionPane.showOptionDialog(this,
                        "La cita está ocupada. ¿Deseas registrarte en la lista de espera?",
                        "Horario ocupado",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]);


                if (opcion == JOptionPane.YES_OPTION) {
                    try {
                        ListaEsperaDAO.registrarEnEspera(id, fecha, horaFinal, servicio);
                        errorLabel.setForeground(Color.ORANGE);
                        errorLabel.setText("Registrado en lista de espera.");
                        System.out.println("[INFO] Paciente registrado en lista de espera para " + fecha + " " + horaFinal);
                    } catch (SQLException ex) {
                        errorLabel.setForeground(Color.RED);
                        errorLabel.setText("Error: No se pudo registrar en lista de espera tras varios intentos.");
                        System.err.println("[ERROR] Registro en lista de espera fallido: " + ex.getMessage());
                    }
                }
                return;
            }

            String sqlDup = "SELECT COUNT(*) FROM CitasMedicas WHERE idPaciente=? AND servicio=?";
            PreparedStatement dupStmt = conn.prepareStatement(sqlDup);
            dupStmt.setString(1, id);
            dupStmt.setString(2, servicio);
            ResultSet rsDup = dupStmt.executeQuery();
            if (rsDup.next() && rsDup.getInt(1) > 0) {
                errorLabel.setText("Ya tienes una cita para este servicio.");
                System.out.println("[ERROR] Cita duplicada - paciente ya tiene cita para servicio " + servicio);
                return;
            }

            String sqlInsert = "INSERT INTO CitasMedicas(idPaciente, fecha, hora, servicio) VALUES(?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sqlInsert);
            stmt.setString(1, id);
            stmt.setString(2, fecha);
            stmt.setString(3, horaFinal);
            stmt.setString(4, servicio);
            stmt.executeUpdate();

            errorLabel.setForeground(Color.GREEN);
            errorLabel.setText("Cita agendada exitosamente.");
            System.out.println("[INFO] Cita registrada correctamente para paciente " + id);

        } catch (Exception ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error: " + ex.getMessage());
            System.err.println("[ERROR] Excepción al registrar cita: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new AgendaCitaFrame();
    }
}
