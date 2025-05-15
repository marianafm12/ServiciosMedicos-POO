package GestionCitas;
//Cambio

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

import BaseDeDatos.ConexionSQLite;
import Inicio.PortadaFrame;
import Inicio.MenuPacientesFrame;
import Inicio.SesionUsuario;

public class AgendaCitaFrame extends JFrame {
    private final int idPaciente;

    private JTextField campoNombre;
    private JTextField campoApellidos;
    private JTextField campoID;
    private JComboBox<String> comboServicio;
    private JComboBox<Integer> comboDia;
    private JComboBox<String> comboMes;
    private JComboBox<Integer> comboAño;
    private JComboBox<String> comboHora;
    private JComboBox<String> comboMinuto;
    private JLabel errorLabel;

    /** Constructor principal: recibe el ID del paciente en sesión. */
    public AgendaCitaFrame(int idPaciente) {
        super("Agendar Citas");
        this.idPaciente = idPaciente;

        // ─── Ajustes básicos de la ventana ───
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        // ─── Construcción del formulario ───
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Registro de Cita Médica", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, gbc);

        // Nombre (no editable)
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        campoNombre = new JTextField("Cargando…", 12);
        campoNombre.setEditable(false);
        add(campoNombre, gbc);

        // Apellidos (no editable)
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 1;
        campoApellidos = new JTextField("Cargando…", 12);
        campoApellidos.setEditable(false);
        add(campoApellidos, gbc);

        // ID (prefill, no editable)
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        campoID = new JTextField(String.valueOf(idPaciente), 12);
        campoID.setEditable(false);
        add(campoID, gbc);

        // Carga datos personales desde la BD
        cargarDatosPersonales(idPaciente);

        // Servicio
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Servicio:"), gbc);
        gbc.gridx = 1;
        comboServicio = new JComboBox<>(new String[] {
                "Consulta", "Enfermería", "Examen Médico"
        });
        add(comboServicio, gbc);

        // Fecha
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Fecha de la Cita:"), gbc);
        comboDia = new JComboBox<>();
        for (int d = 1; d <= 31; d++) comboDia.addItem(d);
        comboMes = new JComboBox<>(new String[] {
                "Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
        });
        comboAño = new JComboBox<>();
        for (int a = LocalDate.now().getYear(); a <= 2030; a++) comboAño.addItem(a);
        JPanel fechaPanel = new JPanel();
        fechaPanel.add(comboDia);
        fechaPanel.add(comboMes);
        fechaPanel.add(comboAño);
        gbc.gridx = 1;
        add(fechaPanel, gbc);

        // Hora
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Hora de la Cita:"), gbc);
        comboHora = new JComboBox<>(new String[] {
                "08", "09", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21"
        });
        comboMinuto = new JComboBox<>(new String[] { "00", "30" });
        JPanel horaPanel = new JPanel();
        horaPanel.add(comboHora);
        horaPanel.add(comboMinuto);
        gbc.gridx = 1;
        add(horaPanel, gbc);

        // Error label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        add(errorLabel, gbc);

        // Botón Confirmar
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton btnConfirmar = new JButton("Confirmar Cita");
        add(btnConfirmar, gbc);

        // Botones Menú / Regresar
        gbc.gridwidth = 1;
        gbc.gridy++;
        JButton btnMenu = new JButton("Menú Principal");
        JButton btnRegresar = new JButton("Regresar");
        gbc.gridx = 0;
        add(btnMenu, gbc);
        gbc.gridx = 1;
        add(btnRegresar, gbc);

        // ─── Listeners ───
        btnConfirmar.addActionListener(e -> validarYConfirmarCita(idPaciente));
        btnMenu.addActionListener(e -> {
            new PortadaFrame().setVisible(true);
            dispose();
        });
        btnRegresar.addActionListener(e -> {
            new MenuPacientesFrame(idPaciente).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    /** Constructor sin parámetros, delega al ID de sesión */
    public AgendaCitaFrame() {
        this(SesionUsuario.getPacienteActual());
    }

    /** Carga nombre y apellidos desde la BD según el ID dado */
    private void cargarDatosPersonales(int id) {
        String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno "
                + "FROM InformacionAlumno WHERE ID = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campoNombre.setText(rs.getString("Nombre"));
                    campoApellidos.setText(
                        rs.getString("ApellidoPaterno") + " "
                      + rs.getString("ApellidoMaterno"));
                } else {
                    campoNombre.setText("Desconocido");
                    campoApellidos.setText("Desconocido");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Valida fecha, disponibilidad y registra la cita.
     */
    private void validarYConfirmarCita(int idPaciente) {
        String servicio = (String) comboServicio.getSelectedItem();
        int dia   = (int) comboDia.getSelectedItem();
        int mes   = comboMes.getSelectedIndex() + 1;
        int año   = (int) comboAño.getSelectedItem();
        String hora   = (String) comboHora.getSelectedItem();
        String minuto = (String) comboMinuto.getSelectedItem();

        if (!ValidacionesCita.esFechaValida(dia, mes, año)) {
            errorLabel.setText("Fecha inválida (debe ser futura)");
            return;
        }
        String fecha    = String.format("%04d-%02d-%02d", año, mes, dia);
        String horaFinal = hora + ":" + minuto;

        // ── 1) Verificar disponibilidad ──
        boolean ocupado;
        try (Connection conn = ConexionSQLite.conectar()) {
            String sqlO = "SELECT COUNT(*) FROM CitasMedicas WHERE fecha=? AND hora=? AND servicio=?";
            try (PreparedStatement psO = conn.prepareStatement(sqlO)) {
                psO.setString(1, fecha);
                psO.setString(2, horaFinal);
                psO.setString(3, servicio);
                try (ResultSet rs = psO.executeQuery()) {
                    ocupado = rs.next() && rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            errorLabel.setText("Error al verificar disponibilidad.");
            ex.printStackTrace();
            return;
        }

        // ── 2) Si está ocupado, preguntar por lista de espera ──
        if (ocupado) {
            String[] opciones = { "Sí", "No" };
            int opcion = JOptionPane.showOptionDialog(
                    this,
                    "La cita está ocupada. ¿Deseas registrarte en la lista de espera?",
                    "Horario ocupado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );
            if (opcion == 0) {
                try {
                    ListaEsperaDAO.registrarEnEspera(
                        String.valueOf(idPaciente),
                        fecha, horaFinal, servicio);
                    errorLabel.setForeground(Color.ORANGE);
                    errorLabel.setText("Registrado en lista de espera.");
                } catch (SQLException e) {
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText("Error al registrar en lista de espera.");
                    e.printStackTrace();
                }
            }
            return;
        }

        // ── 3) Insertar nueva cita ──
        try (Connection conn = ConexionSQLite.conectar()) {
            // 3.1) Duplicado en mismo servicio
            String sqlD = "SELECT COUNT(*) FROM CitasMedicas WHERE idPaciente=? AND servicio=?";
            try (PreparedStatement psD = conn.prepareStatement(sqlD)) {
                psD.setInt(1, idPaciente);
                psD.setString(2, servicio);
                try (ResultSet rs = psD.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        errorLabel.setText("Ya tienes una cita para este servicio.");
                        return;
                    }
                }
            }
            // 3.2) Insertar cita
            String sqlI = "INSERT INTO CitasMedicas(idPaciente,fecha,hora,servicio) VALUES(?,?,?,?)";
            try (PreparedStatement psI = conn.prepareStatement(sqlI)) {
                psI.setInt(1, idPaciente);
                psI.setString(2, fecha);
                psI.setString(3, horaFinal);
                psI.setString(4, servicio);
                psI.executeUpdate();
            }
        } catch (SQLException ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error al registrar cita.");
            ex.printStackTrace();
            return;
        }

        errorLabel.setForeground(Color.GREEN);
        errorLabel.setText("Cita agendada exitosamente.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AgendaCitaFrame::new);
    }
}
