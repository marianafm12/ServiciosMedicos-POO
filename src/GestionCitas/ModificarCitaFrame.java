package GestionCitas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;
import java.time.LocalDate;

import BaseDeDatos.ConexionSQLite;
import Inicio.SesionUsuario;

public class ModificarCitaFrame extends JFrame {
    private static final String NO_EDITABLE = "No editable";

    private JTextField campoNombre;
    private JTextField campoApellidos;
    private JTextField campoID;
    private JComboBox<String> comboCitas;
    private JComboBox<String> comboServicio;
    private JComboBox<Integer> comboDia;
    private JComboBox<String> comboMes;
    private JComboBox<Integer> comboAño;
    private JComboBox<String> comboHora;
    private JComboBox<String> comboMinuto;
    private JLabel errorLabel;

    /** Constructor principal: toma automáticamente el ID del paciente en sesión */
    public ModificarCitaFrame() {
        super("Modificar Cita");
        int idPaciente = SesionUsuario.getPacienteActual();

        // Configuración de la ventana
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // Inicialización de componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoNombre = new JTextField(NO_EDITABLE, 12);
        campoNombre.setEditable(false);
        campoNombre.setForeground(Color.GRAY);

        campoApellidos = new JTextField(NO_EDITABLE, 12);
        campoApellidos.setEditable(false);
        campoApellidos.setForeground(Color.GRAY);

        // CAMBIO ID SE LLAMA

        campoID = new JTextField(String.valueOf(idPaciente), 12);
        campoID.setEditable(false);
        campoID.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatosPersonales();
            }
        });

        comboCitas    = new JComboBox<>();
        comboServicio = new JComboBox<>(new String[] { "Consulta", "Enfermería", "Examen Médico" });
        comboDia      = new JComboBox<>(crearRango(1, 31));
        comboMes      = new JComboBox<>(new String[] {
            "Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"
        });
        comboAño      = new JComboBox<>(crearRango(LocalDate.now().getYear(), 2030));
        comboHora     = new JComboBox<>(new String[] {
            "08","09","10","11","12","13","14","15","16","17","18","19","20","21"
        });
        comboMinuto   = new JComboBox<>(new String[] { "00", "30" });

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);

        // Añadir componentes al layout
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        add(campoNombre, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 1;
        add(campoApellidos, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        add(campoID, gbc);

        gbc.gridy++; gbc.gridx = 0;
        JButton btnBuscar = new JButton("Buscar Citas");
        btnBuscar.addActionListener(e -> cargarCitas());
        add(btnBuscar, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Selecciona tu cita:"), gbc);
        gbc.gridx = 1;
        add(comboCitas, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Servicio:"), gbc);
        gbc.gridx = 1;
        add(comboServicio, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Nueva Fecha:"), gbc);
        gbc.gridx = 1;
        add(agrupar(comboDia, comboMes, comboAño), gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Hora de la Cita:"), gbc);
        gbc.gridx = 1;
        add(agrupar(comboHora, comboMinuto), gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        add(errorLabel, gbc);

        gbc.gridy++;
        JButton btnModificar = new JButton("Modificar Cita");
        btnModificar.addActionListener(e -> modificarCita());
        add(btnModificar, gbc);

        gbc.gridy++;
        JButton btnCancelar = new JButton("Cancelar Cita");
        btnCancelar.addActionListener(e -> cancelarCita());
        add(btnCancelar, gbc);

        setVisible(true);

        // Al iniciar, cargar datos personales y citas
        cargarDatosPersonales();
        cargarCitas();
    }

    /** Carga nombre y apellidos del paciente basado en campoID */
    private void cargarDatosPersonales() {
        String idText = campoID.getText().trim();
        if (idText.isEmpty()) return;
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?"
             )) {
            ps.setInt(1, Integer.parseInt(idText));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campoNombre.setText(rs.getString("Nombre"));
                    campoNombre.setForeground(Color.BLACK);
                    campoApellidos.setText(
                        rs.getString("ApellidoPaterno") + " " + rs.getString("ApellidoMaterno")
                    );
                    campoApellidos.setForeground(Color.BLACK);
                }
            }
        } catch (SQLException ex) {
            campoNombre.setText(NO_EDITABLE);
            campoApellidos.setText(NO_EDITABLE);
        }
    }

    /** Llena el combo de citas pendientes del paciente */
    private void cargarCitas() {
        comboCitas.removeAllItems();
        String idText = campoID.getText().trim();
        if (idText.isEmpty()) {
            errorLabel.setText("ID de paciente faltante.");
            return;
        }
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT idCita, fecha || ' ' || hora || ' - ' || servicio AS desc FROM CitasMedicas WHERE idPaciente = ?"
             )) {
            ps.setInt(1, Integer.parseInt(idText));
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    comboCitas.addItem(
                        rs.getInt("idCita") + ": " + rs.getString("desc")
                    );
                }
                if (!found) errorLabel.setText("No hay citas para este paciente.");
                else       errorLabel.setText("");
            }
        } catch (SQLException ex) {
            errorLabel.setText("Error al cargar citas.");
        }
    }

    /** Modifica la cita seleccionada con los nuevos datos */
    private void modificarCita() {
        String sel = (String) comboCitas.getSelectedItem();
        if (sel == null) {
            errorLabel.setText("Debe seleccionar una cita.");
            return;
        }
        int idCita = Integer.parseInt(sel.split(":")[0].trim());
        String servicio = (String) comboServicio.getSelectedItem();
        int dia   = (Integer) comboDia.getSelectedItem();
        int mes   = comboMes.getSelectedIndex() + 1;
        int año   = (Integer) comboAño.getSelectedItem();
        String hora   = (String) comboHora.getSelectedItem();
        String minuto = (String) comboMinuto.getSelectedItem();

        if (!ValidacionesCita.esFechaValida(dia, mes, año)) {
            errorLabel.setText("Fecha inválida (debe ser futura).");
            return;
        }

        String nuevaFecha = String.format("%04d-%02d-%02d", año, mes, dia);
        String nuevaHora  = hora + ":" + minuto;
        String idText     = campoID.getText().trim();

        try (Connection conn = ConexionSQLite.conectar()) {
            // Verificar conflicto
            try (PreparedStatement ps = conn.prepareStatement(
                 "SELECT COUNT(*) FROM CitasMedicas WHERE fecha=? AND hora=? AND servicio=? AND idCita<>?"
            )) {
                ps.setString(1, nuevaFecha);
                ps.setString(2, nuevaHora);
                ps.setString(3, servicio);
                ps.setInt(4, idCita);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        errorLabel.setText("Ya existe otra cita en ese horario.");
                        return;
                    }
                }
            }
            // Actualizar
            try (PreparedStatement ps = conn.prepareStatement(
                 "UPDATE CitasMedicas SET fecha=?, hora=?, servicio=? WHERE idCita=? AND idPaciente=?"
            )) {
                ps.setString(1, nuevaFecha);
                ps.setString(2, nuevaHora);
                ps.setString(3, servicio);
                ps.setInt(4, idCita);
                ps.setInt(5, Integer.parseInt(idText));
                ps.executeUpdate();
            }
            errorLabel.setForeground(Color.GREEN);
            errorLabel.setText("Cita modificada correctamente.");
            cargarCitas();
        } catch (SQLException ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error al modificar cita.");
        }
    }

    /** Cancela la cita seleccionada */
    private void cancelarCita() {
        String sel = (String) comboCitas.getSelectedItem();
        if (sel == null) {
            errorLabel.setText("Debe seleccionar una cita para cancelar.");
            return;
        }
        int idCita = Integer.parseInt(sel.split(":")[0].trim());
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM CitasMedicas WHERE idCita = ?"
             )) {
            ps.setInt(1, idCita);
            ps.executeUpdate();
            errorLabel.setForeground(Color.BLUE);
            errorLabel.setText("Cita cancelada correctamente.");
            cargarCitas();
        } catch (SQLException ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error al cancelar cita.");
        }
    }

    /** Agrupa varios JComboBox en un JPanel horizontal */
    private JPanel agrupar(JComponent... comps) {
        JPanel p = new JPanel();
        for (Component c : comps) p.add(c);
        return p;
    }

    /** Crea un array de Integer de inicio a fin inclusive */
    private Integer[] crearRango(int inicio, int fin) {
        int size = fin - inicio + 1;
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = inicio + i;
        }
        return arr;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ModificarCitaFrame::new);
    }
}
