package gestionvideojuegos.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Esta clase se encarga de gestionar la conexion con la base de datos PostgreSQL
// Se utilizan variables de entorno para no exponer las credenciales en el codigo
public class ConexionDB {

    // Carga las variables de entorno desde el archivo .env
    // Si el archivo no existe o las variables no están definidas, la app no puede conectarse
    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASSWORD");

    // Este metodo devuelve una conexion activa a la base de datos remota
    // Es importante usar try-with-resources al llamarlo en los DAOs
    // para asegurar que la conexion se cierre correctamente y no consumir memoria de mas
    public static Connection getConnection() throws SQLException {
        if (URL == null) {
            throw new SQLException("No se pudo cargar las credenciales correctas de DB desde el archivo .env");
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}