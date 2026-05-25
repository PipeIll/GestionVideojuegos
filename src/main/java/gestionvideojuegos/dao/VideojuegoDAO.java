package gestionvideojuegos.dao;

import gestionvideojuegos.config.ConexionDB;
import gestionvideojuegos.model.Desarrolladora;
import gestionvideojuegos.model.Genero;
import gestionvideojuegos.model.Videojuego;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoDAO {

    public void insertar(Videojuego videojuego) {
        String sql = "INSERT INTO videojuego (nombre, id_genero, id_desarrolladora, fecha_lanzamiento, precio, rating, dificultad, clasificacion_edad, modo_juego, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, videojuego.getNombre());
            // Los IDs se extraen del objeto Genero y Desarrolladora
            ps.setInt(2, videojuego.getGenero().getIdGenero());
            ps.setInt(3, videojuego.getDesarrolladora().getIdDesarrolladora());
            ps.setDate(4, Date.valueOf(videojuego.getFechaLanzamiento()));
            ps.setDouble(5, videojuego.getPrecio());
            ps.setDouble(6, videojuego.getRating());
            ps.setString(7, videojuego.getDificultad());
            ps.setString(8, videojuego.getClasificacionEdad());
            ps.setString(9, videojuego.getModoJuego());
            ps.setString(10, videojuego.getDescripcion());
            ps.executeUpdate();
            System.out.println("Videojuego insertado correctamente");

        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
        }
    }

    public List<Videojuego> buscarTodos() {
        String sql = "SELECT * FROM videojuego";
        List<Videojuego> videojuegos = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                videojuegos.add(mapearVideojuego(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar todos: " + e.getMessage());
        }
        return videojuegos;
    }

    public Videojuego buscarPorId(int id) {
        String sql = "SELECT * FROM videojuego WHERE id_videojuego = ?";
        Videojuego videojuego = null;
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                videojuego = mapearVideojuego(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar por id: " + e.getMessage());
        }
        return videojuego;
    }

    // WHERE 1=1 permite agregar condiciones con AND sin preocuparse
    // por si es el primer filtro o no. Cada filtro es opcional.
    public List<Videojuego> filtrar(String nombre, Integer idGenero, Integer idDesarrolladora,
                                    LocalDate fechaLanzamiento, Double precioMin, Double precioMax, Double ratingMin,
                                    String dificultad, String clasificacionEdad,
                                    String modoJuego, String descripcion) {

        StringBuilder sql = new StringBuilder("SELECT * FROM videojuego WHERE 1=1");
        List<Object> filtros = new ArrayList<>();

        // Solo se agrega el filtro si el usuario proporciono un valor
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND nombre ILIKE ?");
            filtros.add("%" + nombre.trim() + "%");
        }
        if (idGenero != null) {
            sql.append(" AND id_genero = ?");
            filtros.add(idGenero);
        }
        if (idDesarrolladora != null) {
            sql.append(" AND id_desarrolladora = ?");
            filtros.add(idDesarrolladora);
        }
        if (fechaLanzamiento != null) {
            sql.append(" AND fecha_lanzamiento = ?");
            filtros.add(Date.valueOf(fechaLanzamiento));
        }
        if (precioMin != null && precioMax != null) {
            sql.append(" AND precio BETWEEN ? AND ?");
            filtros.add(precioMin);
            filtros.add(precioMax);
        } else if (precioMin != null) {
            sql.append(" AND precio >= ?");
            filtros.add(precioMin);
        } else if (precioMax != null) {
            sql.append(" AND precio <= ?");
            filtros.add(precioMax);
        }
        if (ratingMin != null) {
            sql.append(" AND rating >= ?");
            filtros.add(ratingMin);
        }
        if (dificultad != null && !dificultad.trim().isEmpty()) {
            sql.append(" AND dificultad ILIKE ?");
            filtros.add("%" + dificultad.trim() + "%");
        }
        if (clasificacionEdad != null && !clasificacionEdad.trim().isEmpty()) {
            sql.append(" AND clasificacion_edad ILIKE ?");
            filtros.add("%" + clasificacionEdad.trim() + "%");
        }
        if (modoJuego != null && !modoJuego.trim().isEmpty()) {
            sql.append(" AND modo_juego ILIKE ?");
            filtros.add("%" + modoJuego.trim() + "%");
        }
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            sql.append(" AND descripcion ILIKE ?");
            filtros.add("%" + descripcion.trim() + "%");
        }

        List<Videojuego> videojuegos = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // setObject permite manejar cualquier tipo de dato dinamicamente
            // ya que no sabemos de antemano cuantos o cuales filtros se usaran
            for (int i = 0; i < filtros.size(); i++) {
                ps.setObject(i + 1, filtros.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                videojuegos.add(mapearVideojuego(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al filtrar: " + e.getMessage());
        }
        return videojuegos;
    }

    public void actualizar(Videojuego videojuego) {
        String sql = "UPDATE videojuego SET nombre = ?, id_genero = ?, id_desarrolladora = ?, fecha_lanzamiento = ?, precio = ?, rating = ?, dificultad = ?, clasificacion_edad = ?, modo_juego = ?, descripcion = ? WHERE id_videojuego = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, videojuego.getNombre());
            ps.setInt(2, videojuego.getGenero().getIdGenero());
            ps.setInt(3, videojuego.getDesarrolladora().getIdDesarrolladora());
            ps.setDate(4, Date.valueOf(videojuego.getFechaLanzamiento()));
            ps.setDouble(5, videojuego.getPrecio());
            ps.setDouble(6, videojuego.getRating());
            ps.setString(7, videojuego.getDificultad());
            ps.setString(8, videojuego.getClasificacionEdad());
            ps.setString(9, videojuego.getModoJuego());
            ps.setString(10, videojuego.getDescripcion());
            ps.setInt(11, videojuego.getIdVideojuego());
            ps.executeUpdate();
            System.out.println("Videojuego actualizado correctamente");

        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM videojuego WHERE id_videojuego = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Videojuego eliminado correctamente");

        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    // Convierte una fila del ResultSet en un objeto Videojuego
    // Solo se mapean los IDs de genero y desarrolladora porque vienen
    // como FK en la tabla de la DB. El nombre se carga desde su propia tabla si se necesita.
    private Videojuego mapearVideojuego(ResultSet rs) throws SQLException {
        int idVideojuego = rs.getInt("id_videojuego");
        String nombre = rs.getString("nombre");
        int idGenero = rs.getInt("id_genero");
        int idDesarrolladora = rs.getInt("id_desarrolladora");
        LocalDate fechaLanzamiento = rs.getDate("fecha_lanzamiento").toLocalDate();
        double precio = rs.getDouble("precio");
        double rating = rs.getDouble("rating");
        String dificultad = rs.getString("dificultad");
        String clasificacionEdad = rs.getString("clasificacion_edad");
        String modoJuego = rs.getString("modo_juego");
        String descripcion = rs.getString("descripcion");

        Genero genero = new Genero(idGenero, null, null);
        Desarrolladora desarrolladora = new Desarrolladora(idDesarrolladora, null, null, 0);

        return new Videojuego(idVideojuego, nombre, genero, desarrolladora,
                fechaLanzamiento, precio, rating, dificultad,
                clasificacionEdad, modoJuego, descripcion);
    }
}
