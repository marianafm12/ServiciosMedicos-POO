package GestionCitas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotificacionCitasFrame extends JFrame {
    private JTextArea mensajeNotificacion;
    private JButton aceptarCitaButton, rechazarCitaButton;
    private JLabel estadoLabel;

    public NotificacionCitasFrame(String fecha, String hora, String tipoServicio) {
        setTitle("Notificación de Cita Disponible");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mensaje de notificación
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mensajeNotificacion = new JTextArea("Se ha liberado una cita: \n" +
                "Fecha: " + fecha + "\n" +
                "Hora: " + hora + "\n" +
                "Servicio: " + tipoServicio + "\n" +
                "¿Desea agendar esta cita?");
        mensajeNotificacion.setEditable(false);
        add(mensajeNotificacion, gbc);

        // Botón para aceptar la cita
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        aceptarCitaButton = new JButton("Aceptar Cita");
        add(aceptarCitaButton, gbc);

        // Botón para rechazar la cita
        gbc.gridx = 1;
        rechazarCitaButton = new JButton("Rechazar Cita");
        add(rechazarCitaButton, gbc);

        // Etiqueta de estado
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        estadoLabel = new JLabel("");
        estadoLabel.setForeground(Color.RED);
        add(estadoLabel, gbc);

        // Acción al aceptar la cita
        aceptarCitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                estadoLabel.setText("Cita confirmada. Se ha actualizado el sistema.");
                aceptarCitaButton.setEnabled(false);
                rechazarCitaButton.setEnabled(false);
            }
        });

        // Acción al rechazar la cita
        rechazarCitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                estadoLabel.setText("Cita rechazada. El horario sigue disponible para otros pacientes.");
                aceptarCitaButton.setEnabled(false);
                rechazarCitaButton.setEnabled(false);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new NotificacionCitasFrame("10/04/2025", "14:00", "Consulta General");
    }
}
