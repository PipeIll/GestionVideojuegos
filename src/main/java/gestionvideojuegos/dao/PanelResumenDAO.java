package gestionvideojuegos.dao;

import gestionvideojuegos.config.ConexionDB;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

// Esta clase centraliza todas las consultas estadisticas del sistema
// Se usa exclusivamente por PanelResumen para mostrar el dashboard
public class PanelResumenDAO {

    // ─── Conteos generales ────────────────────────────────────────────────────

    public int contarVideojuegos() {
        return contarTabla("videojuego");
    }

    public int contarGeneros() {
        return contarTabla("genero");
    }

    public int contarPlataformas() {
        return contarTabla("plataforma");
    }

    public int contarDesarrolladoras() {
        return contarTabla("desarrolladora");
    }

    public int contarDlcs() {
        return contarTabla("dlc");
    }

    public int contarPremios() {
        return contarTabla("premio");
    }

    private int contarTabla(String tabla) {
        String sql = "SELECT COUNT(*) FROM " + tabla;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error al contar " + tabla + ": " + e.getMessage());
        }
        return 0;
    }

    // ─── Estadísticas de videojuegos ──────────────────────────────────────────

    public double obtenerPrecioPromedio() {
        String sql = "SELECT ROUND(AVG(precio)::numeric, 2) FROM videojuego";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Error al calcular precio promedio: " + e.getMessage());
        }
        return 0.0;
    }

    public double obtenerRatingPromedio() {
        String sql = "SELECT ROUND(AVG(rating)::numeric, 2) FROM videojuego";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Error al calcular rating promedio: " + e.getMessage());
        }
        return 0.0;
    }

    // ─── Rankings ─────────────────────────────────────────────────────────────

    // Retorna hasta 5 generos con mas videojuegos asociados
    public Map<String, Integer> topGenerosPorJuegos() {
        String sql = """
                SELECT g.nombre, COUNT(v.id_videojuego) AS total
                FROM genero g
                LEFT JOIN videojuego v ON g.id_genero = v.id_genero
                GROUP BY g.nombre
                ORDER BY total DESC
                LIMIT 5
                """;
        return ejecutarRanking(sql);
    }

    // Retorna hasta 5 desarrolladoras con mas videojuegos
    public Map<String, Integer> topDesarrolladorasPorJuegos() {
        String sql = """
                SELECT d.nombre, COUNT(v.id_videojuego) AS total
                FROM desarrolladora d
                LEFT JOIN videojuego v ON d.id_desarrolladora = v.id_desarrolladora
                GROUP BY d.nombre
                ORDER BY total DESC
                LIMIT 5
                """;
        return ejecutarRanking(sql);
    }

    // Retorna distribucion de juegos por dificultad
    public Map<String, Integer> distribucionPorDificultad() {
        String sql = """
                SELECT COALESCE(dificultad, 'Sin definir') AS dificultad, COUNT(*) AS total
                FROM videojuego
                GROUP BY dificultad
                ORDER BY total DESC
                """;
        return ejecutarRanking(sql);
    }

    // Retorna distribucion de juegos por modo de juego
    public Map<String, Integer> distribucionPorModo() {
        String sql = """
                SELECT COALESCE(modo_juego, 'Sin definir') AS modo, COUNT(*) AS total
                FROM videojuego
                GROUP BY modo_juego
                ORDER BY total DESC
                """;
        return ejecutarRanking(sql);
    }

    // Retorna el top 3 de juegos con mejor rating
    public Map<String, Double> topJuegosPorRating() {
        String sql = """
                SELECT nombre, ROUND(rating::numeric, 2)
                FROM videojuego
                ORDER BY rating DESC
                LIMIT 3
                """;
        Map<String, Double> resultado = new LinkedHashMap<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.put(rs.getString(1), rs.getDouble(2));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener top juegos por rating: " + e.getMessage());
        }
        return resultado;
    }

    // Retorna cuantos DLCs tiene en promedio cada juego
    public double promedioDlcsPorJuego() {
        String sql = """
                SELECT ROUND(AVG(cnt)::numeric, 2)
                FROM (
                    SELECT id_videojuego, COUNT(*) AS cnt
                    FROM dlc
                    GROUP BY id_videojuego
                ) sub
                """;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Error al calcular promedio DLCs: " + e.getMessage());
        }
        return 0.0;
    }

    // Retorna cuantos premios tiene en promedio cada juego
    public double promedioPremiosPorJuego() {
        String sql = """
                SELECT ROUND(AVG(cnt)::numeric, 2)
                FROM (
                    SELECT id_videojuego, COUNT(*) AS cnt
                    FROM premio
                    GROUP BY id_videojuego
                ) sub
                """;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Error al calcular promedio premios: " + e.getMessage());
        }
        return 0.0;
    }

    // ─── Utilidad ─────────────────────────────────────────────────────────────

    private Map<String, Integer> ejecutarRanking(String sql) {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException e) {
            System.err.println("Error en ranking: " + e.getMessage());
        }
        return resultado;
    }
}
