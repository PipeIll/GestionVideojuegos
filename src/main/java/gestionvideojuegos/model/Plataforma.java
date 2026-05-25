package gestionvideojuegos.model;

public class Plataforma {

    private int idPlataforma;
    private String nombre;
    private String fabricante;
    private int añoLanzamiento;

    public Plataforma(int idPlataforma, String nombre, String fabricante, int añoLanzamiento) {
        this.idPlataforma = idPlataforma;
        this.nombre = nombre;
        this.fabricante = fabricante;
        this.añoLanzamiento = añoLanzamiento;
    }

    public int getIdPlataforma() { return idPlataforma; }
    public String getNombre() { return nombre; }
    public String getFabricante() { return fabricante; }
    public int getAñoLanzamiento() { return añoLanzamiento; }

    public void setIdPlataforma(int idPlataforma) { this.idPlataforma = idPlataforma; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
    public void setAñoLanzamiento(int añoLanzamiento) { this.añoLanzamiento = añoLanzamiento; }

    @Override
    public String toString() {
        return String.format("%-5s %-20s %-20s %-6s", idPlataforma, nombre, fabricante, añoLanzamiento);
    }
}
