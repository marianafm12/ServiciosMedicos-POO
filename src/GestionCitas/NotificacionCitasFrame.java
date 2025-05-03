package GestionCitas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class NotificacionCitasFrame extends JFrame {
    private JTextArea mensajeNotificacion;
    private JButton aceptarCitaButton, rechazarCitaButton;
    private JLabel estadoLabel;

    private final String fecha;
    private final String hora;
    private final String servicio;
    private final String idPaciente;

    public NotificacionCitasFrame(String fecha, String hora, String servicio, String idPaciente) {
        this.fecha = fecha;
        this.hora = hora;
        this.servicio = servicio;
        this.idPaciente = idPaciente;

        setTitle("Notificación de Cita Disponible");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mensajeNotificacion = new JTextArea("Se ha liberado una cita:\n"
                + "Fecha: " + fecha + "\n"
                + "Hora: " + hora + "\n"
                + "Servicio: " + servicio + "\n\n"
                + "¿Desea agendar esta cita?");
        mensajeNotificacion.setEditable(false);
        mensajeNotificacion.setLineWrap(true);
        mensajeNotificacion.setWrapStyleWord(true);
        add(mensajeNotificacion, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        aceptarCitaButton = new JButton("Aceptar");
        add(aceptarCitaButton, gbc);

        gbc.gridx = 1;
        rechazarCitaButton = new JButton("Rechazar");
        add(rechazarCitaButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        estadoLabel = new JLabel("");
        estadoLabel.setForeground(Color.BLUE);
        add(estadoLabel, gbc);

        aceptarCitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aceptarCita();
            }
        });

        rechazarCitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                estadoLabel.setText("Has rechazado esta cita.");
                aceptarCitaButton.setEnabled(false);
                rechazarCitaButton.setEnabled(false);
            }
        });

        setVisible(true);
    }

    private void aceptarCita() {
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            // Insertar la cita para el paciente
            String sql = "INSERT INTO CitasMedicas(idPaciente, fecha, hora, servicio) VALUES(?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPaciente);
            stmt.setString(2, fecha);
            stmt.setString(3, hora);
            stmt.setString(4, servicio);
            stmt.executeUpdate();

            estadoLabel.setForeground(new Color(0, 128, 0));
            estadoLabel.setText("Cita agendada exitosamente.");
            aceptarCitaButton.setEnabled(false);
            rechazarCitaButton.setEnabled(false);
        } catch (Exception ex) {
            estadoLabel.setForeground(Color.RED);
            estadoLabel.setText("Error al agendar: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // Para pruebas aisladas
        new NotificacionCitasFrame("2025-05-10", "14:00", "Consulta", "123456");
    }
}
