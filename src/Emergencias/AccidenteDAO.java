package Emergencias;

import java.sql.*;
import BaseDeDatos.*;

public class AccidenteDAO {
    public void save(Accidente a) throws SQLException {
        String sql = "INSERT INTO Accidente "
                + "(ID_EMERGENCIA, FECHA, LUGAR, DESCRIPCION, TESTIGOS) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionSQLite.conectar();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getIdEmergencia());
            ps.setString(2, a.getFecha());
            ps.setString(3, a.getLugar());
            ps.setString(4, a.getDescripcion());
            ps.setString(5, a.getTestigos());
            ps.executeUpdate();
        }
    }
}
