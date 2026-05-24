package gestionvideojuegos.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (URL == null) {
            throw new SQLException("No se pudo cargar las credenciales correctas de DB desde el archivo .env");
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}