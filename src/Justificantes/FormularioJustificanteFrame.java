package Justificantes;

import BaseDeDatos.ConexionSQLite;
import Utilidades.ColoresUDLAP;
import Utilidades.PanelProvider;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PanelSolicitarJustificante extends JPanel implements PanelProvider {
    private final int userId;
    private final JTextField campoProfesor;
    private final JTextArea campoMotivo;
    private final JTextField campoFechaInicio;
    private final JTextField campoFechaFin;

    public PanelSolicitarJustificante(int userId) {
        this.userId = userId;
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        JLabel titulo = new JLabel("Solicitud de Justificante", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel();
        formulario.setLayout(new GridLayout(5, 2, 10, 15));
        formulario.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        formulario.setBackground(ColoresUDLAP.BLANCO);

        campoProfesor = new JTextField();
        campoMotivo = new JTextArea(3, 20);
        campoMotivo.setLineWrap(true);
        campoMotivo.setWrapStyleWord(true);
        campoFechaInicio = new JTextField();
        campoFechaFin = new JTextField();

        formulario.add(new JLabel("Profesor:"));
        formulario.add(campoProfesor);

        formulario.add(new JLabel("Motivo:"));
        formulario.add(new JScrollPane(campoMotivo));

        formulario.add(new JLabel("Fecha Inicio (yyyy-mm-dd):"));
        formulario.add(campoFechaInicio);

        formulario.add(new JLabel("Fecha Fin (yyyy-mm-dd):"));
        formulario.add(campoFechaFin);

        JButton btnEnviar = new JButton("Enviar Solicitud");
        btnEnviar.setBackground(ColoresUDLAP.VERDE);
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 16));
        btnEnviar.setFocusPainted(false);
        btnEnviar.addActionListener(e -> enviarSolicitud());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(ColoresUDLAP.BLANCO);
        panelBoton.add(btnEnviar);

        add(formulario, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void enviarSolicitud() {
        String profesor = campoProfesor.getText().trim();
        String motivo = campoMotivo.getText().trim();
        String inicio = campoFechaInicio.getText().trim();
        String fin = campoFechaFin.getText().trim();

        if (profesor.isEmpty() || motivo.isEmpty() || inicio.isEmpty() || fin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate.parse(inicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate.parse(fin, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Usa yyyy-mm-dd.", "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO SolicitudesJustificantes (IDAlumno, Profesor, Motivo, FechaInicio, FechaFin) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, profesor);
            ps.setString(3, motivo);
            ps.setString(4, inicio);
            ps.setString(5, fin);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Solicitud enviada correctamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            campoProfesor.setText("");
            campoMotivo.setText("");
            campoFechaInicio.setText("");
            campoFechaFin.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la solicitud: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "solicitarJustificante";
    }
}
