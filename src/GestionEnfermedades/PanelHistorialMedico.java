package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import BaseDeDatos.ConexionSQLite;
import Utilidades.ColoresUDLAP;
import Utilidades.PanelProvider;

public class PanelHistorialMedico extends JPanel implements PanelProvider {
    private final int idPaciente;

    public PanelHistorialMedico(int idPaciente) {
        this.idPaciente = idPaciente;
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        JLabel titulo = new JLabel("Historial Médico del Paciente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(ColoresUDLAP.VERDE_SOLIDO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel contenedorItems = new JPanel();
        contenedorItems.setLayout(new BoxLayout(contenedorItems, BoxLayout.Y_AXIS));
        contenedorItems.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(contenedorItems);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        for (HistorialMedicoItem item : obtenerHistorial()) {
            contenedorItems.add(item);
            contenedorItems.add(Box.createVerticalStrut(10));
        }
    }

    private ArrayList<HistorialMedicoItem> obtenerHistorial() {
        ArrayList<HistorialMedicoItem> lista = new ArrayList<>();
        String sql = """
                    SELECT FechaConsulta, diagnostico, observaciones
                    FROM Consultas
                    INNER JOIN diagnosticos ON Consultas.IDexpedienteMedico = diagnosticos.id_justificante
                    WHERE IDPaciente = ?
                    ORDER BY FechaConsulta DESC
                """;

        try (Connection con = ConexionSQLite.conectar();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String fecha = rs.getString("FechaConsulta");
                String diag = rs.getString("diagnostico");
                String obs = rs.getString("observaciones");
                lista.add(new HistorialMedicoItem(fecha, diag, obs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener el historial médico:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return lista;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "historialMedico";
    }
}
