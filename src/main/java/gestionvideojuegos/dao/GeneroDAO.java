package gestionvideojuegos.dao;

import gestionvideojuegos.config.ConexionDB;
import gestionvideojuegos.model.Genero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneroDAO {

    public void insertar(Genero genero) {
        String sql = "INSERT INTO genero (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, genero.getNombre());
            ps.setString(2, genero.getDescripcion());
            ps.executeUpdate();
            System.out.println("Genero insertado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al insertar: " + e.getMessage());
        }
    }

    public List<Genero> buscarTodos() {
        String sql = "SELECT * FROM genero";
        List<Genero> generos = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                generos.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos: " + e.getMessage());
        }
        return generos;
    }

    public Genero buscarPorId(int id) {
        String sql = "SELECT * FROM genero WHERE id_genero = ?";
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

    public List<Genero> buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM genero WHERE nombre ILIKE ?";
        List<Genero> generos = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                generos.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por nombre: " + e.getMessage());
        }
        return generos;
    }

    public void actualizar(Genero genero) {
        String sql = "UPDATE genero SET nombre = ?, descripcion = ? WHERE id_genero = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, genero.getNombre());
            ps.setString(2, genero.getDescripcion());
            ps.setInt(3, genero.getIdGenero());
            ps.executeUpdate();
            System.out.println("Genero actualizado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM genero WHERE id_genero = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Genero eliminado correctamente.");
            } else {
                System.out.println("No se encontró el género con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
        }
    }

    // EL MÉTODO MAESTRO
    private Genero mapear(ResultSet rs) throws SQLException {
        return new Genero(
                rs.getInt("id_genero"),
                rs.getString("nombre"),
                rs.getString("descripcion")
        );
    }
}