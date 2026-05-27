package gestionvideojuegos.dao;

import gestionvideojuegos.config.ConexionDB;
import gestionvideojuegos.model.Desarrolladora;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Esta clase se encarga del acceso a datos (DAO) para la entidad Desarrolladora
// Contiene todos los metodos para realizar el CRUD en la base de datos
public class DesarrolladoraDAO {

    public void insertar(Desarrolladora desarrolladora) {
        String sql = "INSERT INTO desarrolladora (nombre, pais_origen, año_fundacion) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, desarrolladora.getNombre());
            ps.setString(2, desarrolladora.getPaisOrigen());
            ps.setInt(3, desarrolladora.getAñoFundacion());
            ps.executeUpdate();
            System.out.println("Desarrolladora insertada correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al insertar: " + e.getMessage());
        }
    }

    public List<Desarrolladora> buscarTodos() {
        String sql = "SELECT * FROM desarrolladora";
        List<Desarrolladora> desarrolladoras = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                desarrolladoras.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos: " + e.getMessage());
        }
        return desarrolladoras;
    }

    public Desarrolladora buscarPorId(int id) {
        String sql = "SELECT * FROM desarrolladora WHERE id_desarrolladora = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por id: " + e.getMessage());
        }
        return null;
    }

    // Este metodo busca desarrolladoras cuyo nombre coincida parcialmente con el texto ingresado
    // Utiliza ILIKE para que la busqueda no distinga entre mayusculas y minusculas
    public List<Desarrolladora> buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM desarrolladora WHERE nombre ILIKE ?";
        List<Desarrolladora> desarrolladoras = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                desarrolladoras.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por nombre: " + e.getMessage());
        }
        return desarrolladoras;
    }

    public void actualizar(Desarrolladora desarrolladora) {
        String sql = "UPDATE desarrolladora SET nombre = ?, pais_origen = ?, año_fundacion = ? WHERE id_desarrolladora = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, desarrolladora.getNombre());
            ps.setString(2, desarrolladora.getPaisOrigen());
            ps.setInt(3, desarrolladora.getAñoFundacion());
            ps.setInt(4, desarrolladora.getIdDesarrolladora());
            ps.executeUpdate();
            System.out.println("Desarrolladora actualizada correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM desarrolladora WHERE id_desarrolladora = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Desarrolladora eliminada correctamente.");
            } else {
                System.out.println("No se encontró la desarrolladora con ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
        }
    }

    // Este metodo auxiliar convierte una fila del ResultSet en un objeto Desarrolladora
    // Ayuda a no repetir este codigo en cada metodo de busqueda
    private Desarrolladora mapear(ResultSet rs) throws SQLException {
        return new Desarrolladora(
                rs.getInt("id_desarrolladora"),
                rs.getString("nombre"),
                rs.getString("pais_origen"),
                rs.getInt("año_fundacion")
        );
    }
}
