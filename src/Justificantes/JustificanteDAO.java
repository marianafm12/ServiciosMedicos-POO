package Justificantes;

import BaseDeDatos.ConexionSQLite;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JustificanteDAO {

    public static boolean guardarJustificante(Justificante j) {
        String sql = "INSERT INTO JustificantePaciente (idPaciente, nombrePaciente, motivo, fechaInicio, fechaFin, diagnostico, rutaArchivo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, j.getIdPaciente());
            pst.setString(2, j.getNombrePaciente());
            pst.setString(3, j.getMotivo());
            pst.setString(4, j.getFechaInicio().toString());
            pst.setString(5, j.getFechaFin().toString());
            pst.setString(6, j.getDiagnostico());
            pst.setString(7, j.getArchivoReceta() != null ? j.getArchivoReceta().getAbsolutePath() : null);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Optional<Justificante> obtenerPorFolio(int folio) {
        String sql = "SELECT * FROM JustificantePaciente WHERE folio = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, folio);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String idPaciente = rs.getString("idPaciente");
                String nombrePaciente = rs.getString("nombrePaciente");
                String motivo = rs.getString("motivo");
                LocalDate fechaInicio = LocalDate.parse(rs.getString("fechaInicio"));
                LocalDate fechaFin = LocalDate.parse(rs.getString("fechaFin"));
                String diagnostico = rs.getString("diagnostico");
                File receta = rs.getString("rutaArchivo") != null ? new File(rs.getString("rutaArchivo")) : null;

                return Optional.of(new Justificante(folio, idPaciente, nombrePaciente, motivo,
                                                    fechaInicio, fechaFin, diagnostico, receta));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static boolean actualizarDiagnosticoYFechas(int folio, String diagnostico, LocalDate inicio, LocalDate fin) {
        String sql = "UPDATE JustificantePaciente SET diagnostico = ?, fechaInicio = ?, fechaFin = ? WHERE folio = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, diagnostico);
            pst.setString(2, inicio.toString());
            pst.setString(3, fin.toString());
            pst.setInt(4, folio);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Justificante> obtenerTodos() {
        List<Justificante> lista = new ArrayList<>();
        String sql = "SELECT * FROM JustificantePaciente ORDER BY folio DESC";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int folio = rs.getInt("folio");
                String idPaciente = rs.getString("idPaciente");
                String nombrePaciente = rs.getString("nombrePaciente");
                String motivo = rs.getString("motivo");
                LocalDate fechaInicio = LocalDate.parse(rs.getString("fechaInicio"));
                LocalDate fechaFin = LocalDate.parse(rs.getString("fechaFin"));
                String diagnostico = rs.getString("diagnostico");
                File receta = rs.getString("rutaArchivo") != null ? new File(rs.getString("rutaArchivo")) : null;

                lista.add(new Justificante(folio, idPaciente, nombrePaciente, motivo, fechaInicio, fechaFin, diagnostico, receta));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static boolean eliminarPorFolio(int folio) {
        String sql = "DELETE FROM JustificantePaciente WHERE folio = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, folio);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
