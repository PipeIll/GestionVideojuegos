package gestionvideojuegos.dao;

import gestionvideojuegos.config.ConexionDB;
import gestionvideojuegos.model.Dlc;
import gestionvideojuegos.model.Videojuego;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DlcDAO {

    public void insertar(Dlc dlc) {
        String sql = "INSERT INTO dlc (id_videojuego, nombre, precio, fecha_lanzamiento, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dlc.getVideojuego().getIdVideojuego());
            ps.setString(2, dlc.getNombre());
            ps.setDouble(3, dlc.getPrecio());
            ps.setDate(4, Date.valueOf(dlc.getFechaLanzamiento()));
            ps.setString(5, dlc.getDescripcion());

            ps.executeUpdate();
            System.out.println("DLC insertado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al insertar DLC: " + e.getMessage());
        }
    }

    public List<Dlc> buscarTodos() {
        String sql = "SELECT * FROM dlc";
        List<Dlc> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar DLCs: " + e.getMessage());
        }
        return lista;
    }

    public Dlc buscarPorId(int id) {
        String sql = "SELECT * FROM dlc WHERE id_dlc = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar DLC por ID: " + e.getMessage());
        }
        return null;
    }
    public List<Dlc> buscarPorPrecioRango(double precioMin, double precioMax) {
        // BETWEEN filtra los DLCs cuyo precio esta dentro del rango dado
        // Es mas eficiente que usar dos condiciones separadas >= y <=
        String sql = "SELECT * FROM dlc WHERE precio BETWEEN ? AND ?";
        List<Dlc> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, precioMin);
            ps.setDouble(2, precioMax);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error de búsqueda por rango de precio en DLC: " + e.getMessage());
        }

        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM dlc WHERE id_dlc = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("DLC eliminado correctamente.");
            } else {
                System.out.println("No se encontró el DLC con ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar DLC: " + e.getMessage());
        }
    }

    public void actualizar(Dlc dlc) {
        String sql = "UPDATE dlc SET id_videojuego = ?, nombre = ?, precio = ?, fecha_lanzamiento = ?, descripcion = ? WHERE id_dlc = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dlc.getVideojuego().getIdVideojuego());
            ps.setString(2, dlc.getNombre());
            ps.setDouble(3, dlc.getPrecio());
            ps.setDate(4, Date.valueOf(dlc.getFechaLanzamiento()));
            ps.setString(5, dlc.getDescripcion());
            ps.setInt(6, dlc.getIdDlc());

            ps.executeUpdate();
            System.out.println("DLC actualizado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al actualizar DLC: " + e.getMessage());
        }
    }

    private Dlc mapear(ResultSet rs) throws SQLException {
        Videojuego videojuegoId = new Videojuego(rs.getInt("id_videojuego"), null, null, null, null, 0, 0, null, null, null, null);

        return new Dlc(
                rs.getInt("id_dlc"),
                videojuegoId,
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getDate("fecha_lanzamiento").toLocalDate(),
                rs.getString("descripcion")
        );
    }
}
