package GestionCitas;

import BaseDeDatos.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO {

    // Modelo interno para representar una notificación
    public static class Notificacion {
        public int idNotificacion;
        public int idPaciente;
        public String fecha;
        public String hora;
        public String servicio;

        public Notificacion(int idNotificacion, int idPaciente, String fecha, String hora, String servicio) {
            this.idNotificacion = idNotificacion;
            this.idPaciente = idPaciente;
            this.fecha = fecha;
            this.hora = hora;
            this.servicio = servicio;
        }
    }

    // Devuelve todas las notificaciones de un paciente
    public static List<Notificacion> obtenerNotificaciones(int idPaciente) {
        List<Notificacion> notificaciones = new ArrayList<>();

        try (Connection conn = ConexionSQLite.conectar()) {
            String sql = "SELECT * FROM Notificaciones WHERE idPaciente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idPaciente);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        notificaciones.add(new Notificacion(
                                rs.getInt("idNotificacion"),
                                rs.getInt("idPaciente"),
                                rs.getString("fecha"),
                                rs.getString("hora"),
                                rs.getString("servicio")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener notificaciones: " + e.getMessage());
        }

        return notificaciones;
    }

    // Elimina una notificación por ID
    public static void eliminarNotificacion(int idNotificacion) {
        try (Connection conn = ConexionSQLite.conectar()) {
            String sql = "DELETE FROM Notificaciones WHERE idNotificacion = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idNotificacion);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar notificación: " + e.getMessage());
        }
    }

    // Verifica si un paciente tiene al menos una notificación pendiente
    public static boolean tieneNotificacionesNoLeidas(int idPaciente) {
        boolean tiene = false;
        String sql = "SELECT COUNT(*) FROM Notificaciones WHERE idPaciente = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt(1);
                    tiene = total > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar notificaciones no leídas: " + e.getMessage());
        }

        return tiene;
    }
}
