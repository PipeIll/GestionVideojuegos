package gestionvideojuegos.dao;

import gestionvideojuegos.config.ConexionDB;
import gestionvideojuegos.model.Premio;
import gestionvideojuegos.model.Videojuego;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Clase responsable de la comunicacion con la tabla premios de la base de datos
public class PremioDAO {

    public void insertar(Premio premio) {
        String sql = "INSERT INTO premio (id_videojuego, nombre_premio, organizacion, año, categoria) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, premio.getVideojuego().getIdVideojuego());
            ps.setString(2, premio.getNombrePremio());
            ps.setString(3, premio.getOrganizacion());
            ps.setShort(4, premio.getAño());
            ps.setString(5, premio.getCategoria());

            ps.executeUpdate();
            System.out.println("Premio insertado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al insertar premio: " + e.getMessage());
        }
    }

    public List<Premio> buscarTodos() {
        String sql = "SELECT * FROM premio";
        List<Premio> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar premios: " + e.getMessage());
        }
        return lista;
    }

    public Premio buscarPorId(int id) {
        String sql = "SELECT * FROM premio WHERE id_premio = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar premio por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Premio> buscarPorNombreDeVideojuego(String nombreVideojuego) {
        // Utilizamos INNER JOIN para unir la tabla premio con la tabla videojuego
        // Esto nos permite buscar los premios filtrando por el nombre del juego en lugar de requerir su ID
        String sql = "SELECT premio.* FROM premio " +
                "INNER JOIN videojuego ON premio.id_videojuego = videojuego.id_videojuego " +
                "WHERE videojuego.nombre ILIKE ?";

        List<Premio> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombreVideojuego + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar premios por nombre de juego: " + e.getMessage());
        }

        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM premio WHERE id_premio = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Premio eliminado correctamente.");
            else System.out.println("No se encontró el premio con ID: " + id);

        } catch (SQLException e) {
            System.err.println("Error al eliminar premio: " + e.getMessage());
        }
    }

    public void actualizar(Premio premio) {
        String sql = "UPDATE premio SET id_videojuego = ?, nombre_premio = ?, organizacion = ?, año = ?, categoria = ? WHERE id_premio = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, premio.getVideojuego().getIdVideojuego());
            ps.setString(2, premio.getNombrePremio());
            ps.setString(3, premio.getOrganizacion());
            ps.setShort(4, premio.getAño());
            ps.setString(5, premio.getCategoria());
            ps.setInt(6, premio.getIdPremio());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Premio actualizado correctamente.");
            } else {
                System.out.println("No se encontró el premio a actualizar.");
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar premio: " + e.getMessage());
        }
    }

    // Transforma una fila obtenida desde SQL a una instancia de Premio
    // Se inicializa un objeto Videojuego con atributos nulos excepto su ID para mantener la relacion
    private Premio mapear(ResultSet rs) throws SQLException {
        Videojuego videojuegoId = new Videojuego(rs.getInt("id_videojuego"), null, null, null, null, 0, 0, null, null, null, null);

        return new Premio(
                rs.getInt("id_premio"),
                videojuegoId,
                rs.getString("nombre_premio"),
                rs.getString("organizacion"),
                rs.getShort("año"),
                rs.getString("categoria")
        );
    }
}