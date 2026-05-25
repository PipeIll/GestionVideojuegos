package gestionvideojuegos.model;

import java.time.LocalDate;

public class Videojuego {

    private int idVideojuego;
    private String nombre;
    private Genero genero;
    private Desarrolladora desarrolladora;
    private LocalDate fechaLanzamiento;
    private double precio;
    private double rating;
    private String dificultad;
    private String clasificacionEdad;
    private String modoJuego;
    private String descripcion;

    public Videojuego(int idVideojuego, String nombre, Genero genero,
                      Desarrolladora desarrolladora, LocalDate fechaLanzamiento,
                      double precio, double rating, String dificultad,
                      String clasificacionEdad, String modoJuego, String descripcion) {
        this.idVideojuego = idVideojuego;
        this.nombre = nombre;
        this.genero = genero;
        this.desarrolladora = desarrolladora;
        this.fechaLanzamiento = fechaLanzamiento;
        this.precio = precio;
        this.rating = rating;
        this.dificultad = dificultad;
        this.clasificacionEdad = clasificacionEdad;
        this.modoJuego = modoJuego;
        this.descripcion = descripcion;
    }

    public int getIdVideojuego() { return idVideojuego; }
    public String getNombre() { return nombre; }
    public Genero getGenero() { return genero; }
    public Desarrolladora getDesarrolladora() { return desarrolladora; }
    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public double getPrecio() { return precio; }
    public double getRating() { return rating; }
    public String getDificultad() { return dificultad; }
    public String getClasificacionEdad() { return clasificacionEdad; }
    public String getModoJuego() { return modoJuego; }
    public String getDescripcion() { return descripcion; }

    public void setIdVideojuego(int idVideojuego) { this.idVideojuego = idVideojuego; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setGenero(Genero genero) { this.genero = genero; }
    public void setDesarrolladora(Desarrolladora desarrolladora) { this.desarrolladora = desarrolladora; }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setRating(double rating) { this.rating = rating; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }
    public void setClasificacionEdad(String clasificacionEdad) { this.clasificacionEdad = clasificacionEdad; }
    public void setModoJuego(String modoJuego) { this.modoJuego = modoJuego; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        String nombreGenero = genero != null ? genero.getNombre() : " ";
        String nombreDesarrolladora = desarrolladora != null ? desarrolladora.getNombre() : " ";
        return String.format("%-5s %-25s %-15s %-20s %-12s %-8s %-6s %-12s %-8s %-12s %s",
                idVideojuego, nombre, genero.getNombre(), desarrolladora.getNombre(),
                fechaLanzamiento, precio, rating, dificultad, clasificacionEdad, modoJuego, descripcion);
    }
}