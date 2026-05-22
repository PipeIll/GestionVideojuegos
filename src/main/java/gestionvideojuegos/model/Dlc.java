package gestionvideojuegos.model;

import java.time.LocalDate;

public class Dlc {

    private int idDlc;
    private Videojuego videojuego;
    private String nombre;
    private double precio;
    private LocalDate fechaLanzamiento;
    private String descripcion;

    public Dlc(int idDlc, Videojuego videojuego, String nombre,
               double precio, LocalDate fechaLanzamiento, String descripcion) {
        this.idDlc = idDlc;
        this.videojuego = videojuego;
        this.nombre = nombre;
        this.precio = precio;
        this.fechaLanzamiento = fechaLanzamiento;
        this.descripcion = descripcion;
    }

    public int getIdDlc() { return idDlc; }
    public Videojuego getVideojuego() { return videojuego; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public String getDescripcion() { return descripcion; }

    public void setIdDlc(int idDlc) { this.idDlc = idDlc; }
    public void setVideojuego(Videojuego videojuego) { this.videojuego = videojuego; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "Dlc{" +
                "idDlc=" + idDlc +
                ", videojuego=" + videojuego.getNombre() +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", fechaLanzamiento=" + fechaLanzamiento +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}