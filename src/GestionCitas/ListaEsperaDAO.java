package GestionCitas;
//Cambio
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import BaseDeDatos.ConexionSQLite;
import Modelo.Espera;

public class ListaEsperaDAO {

    public static void registrarEnEspera(String idPaciente, String fecha, String hora, String servicio)
            throws SQLException {
        final int MAX_REINTENTOS = 3;
        int intentos = 0;
        boolean completado = false;

        while (intentos < MAX_REINTENTOS && !completado) {
            try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
                String sql = "INSERT INTO ListaEsperaCitas(idPaciente, fechaDeseada, horaDeseada, servicio, estado) VALUES (?,?,?,?, 'pendiente')";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, idPaciente);
                stmt.setString(2, fecha);
                stmt.setString(3, hora);
                stmt.setString(4, servicio);
                stmt.executeUpdate();
                completado = true;
            } catch (SQLException e) {
                if (e.getMessage().contains("database is locked")) {
                    intentos++;
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    throw e;
                }
            }
        }

        if (!completado) {
            throw new SQLException("No se pudo registrar en lista de espera tras varios intentos.");
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

    public static void marcarNotificada(String idPaciente, String fecha, String hora, String servicio)
            throws SQLException {
        final int MAX_REINTENTOS = 3;
        int intentos = 0;
        boolean completado = false;

        while (intentos < MAX_REINTENTOS && !completado) {
            try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
                String sql = "UPDATE ListaEsperaCitas SET estado = 'notificada' WHERE idPaciente = ? AND fechaDeseada = ? AND horaDeseada = ? AND servicio = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, idPaciente);
                stmt.setString(2, fecha);
                stmt.setString(3, hora);
                stmt.setString(4, servicio);
                stmt.executeUpdate();
                completado = true;
            } catch (SQLException e) {
                if (e.getMessage().contains("database is locked")) {
                    intentos++;
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    throw e;
                }
            }
        }

        if (!completado) {
            throw new SQLException("No se pudo marcar como notificada tras varios intentos.");
        }
    }

    public static void generarNotificacionParaListaEspera(String fecha, String hora, String servicio) {
        try {
            List<Espera> solicitudes = obtenerSolicitudesPara(fecha, hora, servicio);
            for (Espera espera : solicitudes) {
                try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
                    String mensaje = "Se ha liberado una cita el " + fecha + " a las " + hora + " para " + servicio;
                    String sql = "INSERT INTO Notificaciones(idPaciente, mensaje, estado, fecha, hora, servicio) VALUES (?, ?, 'pendiente', ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, espera.getIdPaciente());
                    stmt.setString(2, mensaje);
                    stmt.setString(3, espera.getFecha());
                    stmt.setString(4, espera.getHora());
                    stmt.setString(5, espera.getServicio());
                    stmt.executeUpdate();
                }
                marcarNotificada(espera.getIdPaciente(), fecha, hora, servicio);
            }
        } catch (SQLException e) {
            System.err.println("Error al generar notificaciones para lista de espera: " + e.getMessage());
        }
    }
}
