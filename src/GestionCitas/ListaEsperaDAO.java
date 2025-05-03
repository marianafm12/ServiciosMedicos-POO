package GestionCitas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListaEsperaDAO {

    public static class Espera {
        public final String idPaciente;
        public final String fecha;
        public final String hora;
        public final String servicio;

        public Espera(String idPaciente, String fecha, String hora, String servicio) {
            this.idPaciente = idPaciente;
            this.fecha = fecha;
            this.hora = hora;
            this.servicio = servicio;
        }
    }

    public static void registrarEnEspera(String idPaciente, String fecha, String hora, String servicio) throws SQLException {
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String sql = "INSERT INTO ListaEsperaCitas(idPaciente, fechaDeseada, horaDeseada, servicio) VALUES (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPaciente);
            stmt.setString(2, fecha);
            stmt.setString(3, hora);
            stmt.setString(4, servicio);
            stmt.executeUpdate();
        }
    }

    public static List<Espera> obtenerSolicitudesPara(String fecha, String hora, String servicio) throws SQLException {
        List<Espera> lista = new ArrayList<>();
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String sql = "SELECT idPaciente FROM ListaEsperaCitas WHERE fechaDeseada = ? AND horaDeseada = ? AND servicio = ? AND estado = 'pendiente'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fecha);
            stmt.setString(2, hora);
            stmt.setString(3, servicio);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Espera(rs.getString("idPaciente"), fecha, hora, servicio));
            }
        }
        return lista;
    }

    public static void marcarNotificada(String idPaciente, String fecha, String hora, String servicio) throws SQLException {
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String sql = "UPDATE ListaEsperaCitas SET estado = 'notificada' WHERE idPaciente = ? AND fechaDeseada = ? AND horaDeseada = ? AND servicio = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPaciente);
            stmt.setString(2, fecha);
            stmt.setString(3, hora);
            stmt.setString(4, servicio);
            stmt.executeUpdate();
        }
    }
}
