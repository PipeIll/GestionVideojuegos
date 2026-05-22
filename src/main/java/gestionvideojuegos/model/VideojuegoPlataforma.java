package gestionvideojuegos.model;

import java.time.LocalDate;

public class VideojuegoPlataforma {

    private Videojuego videojuego;
    private Plataforma plataforma;
    private LocalDate fechaLanzamientoPlataforma;

    public VideojuegoPlataforma(Videojuego videojuego, Plataforma plataforma,
                                LocalDate fechaLanzamientoPlataforma) {
        this.videojuego = videojuego;
        this.plataforma = plataforma;
        this.fechaLanzamientoPlataforma = fechaLanzamientoPlataforma;
    }

    public Videojuego getVideojuego() { return videojuego; }
    public Plataforma getPlataforma() { return plataforma; }
    public LocalDate getFechaLanzamientoPlataforma() { return fechaLanzamientoPlataforma; }

    public void setVideojuego(Videojuego videojuego) { this.videojuego = videojuego; }
    public void setPlataforma(Plataforma plataforma) { this.plataforma = plataforma; }
    public void setFechaLanzamientoPlataforma(LocalDate fechaLanzamientoPlataforma) { this.fechaLanzamientoPlataforma = fechaLanzamientoPlataforma; }

    @Override
    public String toString() {
        return "VideojuegoPlataforma{" +
                "videojuego=" + videojuego.getNombre() +
                ", plataforma=" + plataforma.getNombre() +
                ", fechaLanzamientoPlataforma=" + fechaLanzamientoPlataforma +
                '}';
    }
}
