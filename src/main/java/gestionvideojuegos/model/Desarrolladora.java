package gestionvideojuegos.model;

public class Desarrolladora {

    private int idDesarrolladora;
    private String nombre;
    private String paisOrigen;
    private int añoFundacion;

    public Desarrolladora(int idDesarrolladora, String nombre, String paisOrigen, int añoFundacion) {
        this.idDesarrolladora = idDesarrolladora;
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
        this.añoFundacion = añoFundacion;
    }

    public int getIdDesarrolladora() { return idDesarrolladora; }
    public String getNombre() { return nombre; }
    public String getPaisOrigen() { return paisOrigen; }
    public int getAñoFundacion() { return añoFundacion; }

    public void setIdDesarrolladora(int idDesarrolladora) { this.idDesarrolladora = idDesarrolladora; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPaisOrigen(String paisOrigen) { this.paisOrigen = paisOrigen; }
    public void setAñoFundacion(int añoFundacion) { this.añoFundacion = añoFundacion; }

    @Override
    public String toString() {
        return String.format("%-5s %-30s %-20s %-6s", idDesarrolladora, nombre, paisOrigen, añoFundacion);
    }
}
