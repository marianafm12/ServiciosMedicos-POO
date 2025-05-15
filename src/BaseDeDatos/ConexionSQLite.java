package BaseDeDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLite {
    // Asegúrate de usar el nombre (y path) correcto de tu fichero .db
    private static final String RUTA_BD = "jdbc:sqlite:Servicios medicos.db";

    /**
     * Conecta a SQLite y lanza SQLException si algo falla.
     */
    public static Connection conectar() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // Convertimos el ClassNotFound en SQLException
            throw new SQLException("Driver SQLite no encontrado", e);
        }
        // Si falla aquí (fichero no existe, permisos, etc.), saltará SQLException
        return DriverManager.getConnection(RUTA_BD);
    }

    public static void main(String[] args) {
        try (Connection c = conectar()) {
            System.out.println("Conexión exitosa a SQLite: " + c.getMetaData().getURL());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
