package GestionCitas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                rechazarCita();
            }
        });

        setVisible(true);
    }

    private void aceptarCita() {
        final int MAX_REINTENTOS = 3;
        int intentos = 0;
        boolean exito = false;

        while (intentos < MAX_REINTENTOS && !exito) {
            try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
                conn.setAutoCommit(false);

                // Insertar cita nueva
                String sqlInsert = "INSERT INTO CitasMedicas(idPaciente, fecha, hora, servicio) VALUES(?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sqlInsert);
                stmt.setString(1, idPaciente);
                stmt.setString(2, fecha);
                stmt.setString(3, hora);
                stmt.setString(4, servicio);
                stmt.executeUpdate();

                // Eliminar de ListaEspera
                String sqlDelEspera = "DELETE FROM ListaEsperaCitas WHERE idPaciente = ? AND fechaDeseada = ? AND horaDeseada = ? AND servicio = ?";
                PreparedStatement delStmt = conn.prepareStatement(sqlDelEspera);
                delStmt.setString(1, idPaciente);
                delStmt.setString(2, fecha);
                delStmt.setString(3, hora);
                delStmt.setString(4, servicio);
                delStmt.executeUpdate();

                // Marcar notificación como atendida
                String sqlNotif = "UPDATE Notificaciones SET estado = 'atendida' WHERE idPaciente = ? AND fecha = ? AND hora = ? AND servicio = ?";
                PreparedStatement upStmt = conn.prepareStatement(sqlNotif);
                upStmt.setString(1, idPaciente);
                upStmt.setString(2, fecha);
                upStmt.setString(3, hora);
                upStmt.setString(4, servicio);
                upStmt.executeUpdate();

                conn.commit();

                estadoLabel.setForeground(new Color(0, 128, 0));
                estadoLabel.setText("Cita agendada exitosamente.");
                aceptarCitaButton.setEnabled(false);
                rechazarCitaButton.setEnabled(false);
                exito = true;

            } catch (SQLException ex) {
                if (ex.getMessage().contains("database is locked")) {
                    intentos++;
                    estadoLabel.setForeground(Color.RED);
                    estadoLabel.setText("Reintentando... (" + intentos + "/" + MAX_REINTENTOS + ")");
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    estadoLabel.setForeground(Color.RED);
                    estadoLabel.setText("Error al agendar: " + ex.getMessage());
                    break;
                }
            }
        }

        if (!exito) {
            estadoLabel.setForeground(Color.RED);
            estadoLabel.setText("No se pudo agendar la cita. Intenta más tarde.");
        }
    }

    private void rechazarCita() {
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String sql = "UPDATE Notificaciones SET estado = 'rechazada' WHERE idPaciente = ? AND fecha = ? AND hora = ? AND servicio = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPaciente);
            stmt.setString(2, fecha);
            stmt.setString(3, hora);
            stmt.setString(4, servicio);
            stmt.executeUpdate();

            estadoLabel.setText("Has rechazado esta cita.");
            aceptarCitaButton.setEnabled(false);
            rechazarCitaButton.setEnabled(false);
        } catch (SQLException ex) {
            estadoLabel.setForeground(Color.RED);
            estadoLabel.setText("Error al rechazar: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new NotificacionCitasFrame("2025-05-10", "14:00", "Consulta", "123456");
    }
}
