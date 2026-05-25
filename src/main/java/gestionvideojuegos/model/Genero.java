package gestionvideojuegos.model;

public class Genero {

    private int idGenero;
    private String nombre;
    private String descripcion;

    public Genero(int idGenero, String nombre, String descripcion) {
        this.idGenero = idGenero;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdGenero() { return idGenero; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setIdGenero(int idGenero) { this.idGenero = idGenero; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return String.format("%-5d %-20s %s", idGenero, nombre, descripcion);
    }
}
