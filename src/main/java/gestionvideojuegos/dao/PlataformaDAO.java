package gestionvideojuegos.dao;

import gestionvideojuegos.config.ConexionDB;
import gestionvideojuegos.model.Genero;
import gestionvideojuegos.model.Plataforma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO encargado de las operaciones CRUD para la entidad Plataforma (consolas, PC, etc)
public class PlataformaDAO {

    public void insertar(Plataforma plataforma) {
        String sql = "INSERT INTO plataforma (nombre, fabricante, año_lanzamiento) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plataforma.getNombre());
            ps.setString(2, plataforma.getFabricante());
            ps.setInt(3, plataforma.getAñoLanzamiento());

            ps.executeUpdate();
            System.out.println("Plataforma insertada con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al insertar: " + e.getMessage());
        }
    }

    public List<Plataforma> buscarTodos() {
        String sql = "SELECT * FROM plataforma";
        List<Plataforma> plataformas = new ArrayList<>();
        try(Connection conn = ConexionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                plataformas.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos: " + e.getMessage());
        }
        return plataformas;
    }

    public Plataforma buscarPorId(int id) {
        String sql = "SELECT * FROM plataforma WHERE id_plataforma = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error en búsqueda por ID: " + e.getMessage());
        }
        return null;
    }

    // Busca las plataformas que coincidan con la cadena dada, usando busqueda parcial insensible a mayusculas
    public List<Plataforma> buscarPorNombre(String nombre){
        String sql = "SELECT * FROM plataforma WHERE nombre ILIKE ?";
        List<Plataforma> plataformas = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                plataformas.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error de busqueda por Nombre: "+ e.getMessage());
        }
        return plataformas;
    }

    public void actualizar(Plataforma plataforma) {
        String sql = "UPDATE plataforma SET nombre = ?, fabricante = ?, año_lanzamiento = ? WHERE id_plataforma = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plataforma.getNombre());
            ps.setString(2, plataforma.getFabricante());
            ps.setInt(3, plataforma.getAñoLanzamiento());
            ps.setInt(4, plataforma.getIdPlataforma());
            ps.executeUpdate();
            System.out.println("Plataforma actualizado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
        }
    }


    public void eliminar(int id) {
        String sql = "DELETE FROM plataforma WHERE id_plataforma = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Plataforma eliminada correctamente.");
            } else {
                System.out.println("No se encontró ninguna plataforma con ese ID.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar plataforma: " + e.getMessage());
        }
    }

    // Metodo de apoyo para convertir cada fila de resultados SQL a un objeto Plataforma
    private Plataforma mapear(ResultSet rs) throws SQLException {
        return new Plataforma(
                rs.getInt("id_plataforma"),
                rs.getString("nombre"),
                rs.getString("fabricante"),
                rs.getInt("año_lanzamiento")
        );
    }
}