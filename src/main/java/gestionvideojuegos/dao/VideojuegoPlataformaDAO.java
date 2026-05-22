package gestionvideojuegos.dao;

import gestionvideojuegos.config.ConexionDB;
import gestionvideojuegos.model.Plataforma;
import gestionvideojuegos.model.Videojuego;
import gestionvideojuegos.model.VideojuegoPlataforma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoPlataformaDAO {

    public void insertar(VideojuegoPlataforma vp) {
        String sql = "INSERT INTO videojuego_plataforma (id_videojuego, id_plataforma, fecha_lanzamiento_plataforma) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vp.getVideojuego().getIdVideojuego());
            ps.setInt(2, vp.getPlataforma().getIdPlataforma());
            ps.setDate(3, Date.valueOf(vp.getFechaLanzamientoPlataforma()));
            ps.executeUpdate();
            System.out.println("Relación Videojuego-Plataforma insertada.");

        } catch (SQLException e) {
            System.err.println("Error al insertar relación: " + e.getMessage());
        }
    }

    public List<VideojuegoPlataforma> buscarTodos() {
        String sql = "SELECT * FROM videojuego_plataforma";
        List<VideojuegoPlataforma> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar relaciones: " + e.getMessage());
        }
        return lista;
    }

    public void eliminar(int idVideojuego, int idPlataforma) {
        String sql = "DELETE FROM videojuego_plataforma WHERE id_videojuego = ? AND id_plataforma = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVideojuego);
            ps.setInt(2, idPlataforma);
            int filas = ps.executeUpdate();

            if (filas > 0) System.out.println("Relación eliminada.");
            else System.out.println("No se encontró la relación especificada.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar relación: " + e.getMessage());
        }
    }

    private VideojuegoPlataforma mapear(ResultSet rs) throws SQLException {
        Videojuego v = new Videojuego(rs.getInt("id_videojuego"), null, null, null, null, 0, 0, null, null, null, null);
        Plataforma p = new Plataforma(rs.getInt("id_plataforma"), null, null, 0);

        return new VideojuegoPlataforma(
                v,
                p,
                rs.getDate("fecha_lanzamiento_plataforma").toLocalDate()
        );
    }
}