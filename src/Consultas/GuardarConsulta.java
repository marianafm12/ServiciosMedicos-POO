package Consultas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import BaseDeDatos.ConexionSQLite;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GuardarConsulta implements ActionListener {
    private final JTextField[] campos;
    private final JTextArea areaTexto;

    public GuardarConsulta(JTextField[] campos, JTextArea areaTexto) {
        this.campos = campos;
        this.areaTexto = areaTexto;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (validarCampos()) {
            try {
                guardarConsultaEnBD();
                mostrarConfirmacion();
                limpiarCampos();
            } catch (SQLException | ParseException ex) {
                JOptionPane.showMessageDialog(null,
                        "Error al guardar consulta:\n" + ex.getMessage(),
                        "Error de BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarCampos() {
        // Implementar validación similar a la original
        return true;
    }

    private void guardarConsultaEnBD() throws SQLException, ParseException {
        String sql = "INSERT INTO Consultas (IDPaciente, IDMedico, Sintomas, Medicamentos, Diagnostico, " +
                "FechaConsulta, FechaUltimaConsulta, FechaInicioSintomas, Receta) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(campos[0].getText().trim()));
            ps.setInt(2, /* ID del médico */ 1); // Deberías obtener esto de la sesión
            ps.setString(3, campos[4].getText().trim());
            ps.setString(4, campos[5].getText().trim());
            ps.setString(5, campos[6].getText().trim());

            SimpleDateFormat srcFmt = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat dstFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            ps.setString(6, dstFmt.format(srcFmt.parse(campos[7].getText().trim())));
            ps.setString(7, dstFmt.format(srcFmt.parse(campos[8].getText().trim())));
            ps.setString(8, dstFmt.format(srcFmt.parse(campos[9].getText().trim())));

            ps.setString(9, areaTexto.getText().trim());

            ps.executeUpdate();
        }
    }

    private void mostrarConfirmacion() {
        // Implementar diálogo de confirmación
    }

    private void limpiarCampos() {
        for (int i = 4; i < campos.length; i++) { // Limpiar solo campos de consulta
            campos[i].setText("");
        }
        areaTexto.setText("");
    }
}