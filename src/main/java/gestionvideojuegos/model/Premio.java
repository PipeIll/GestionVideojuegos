package gestionvideojuegos.model;

public class Premio {

    private int idPremio;
    private Videojuego videojuego;
    private String nombrePremio;
    private String organizacion;
    private short año;
    private String categoria;

    public Premio(int idPremio, Videojuego videojuego, String nombrePremio,
                  String organizacion, short año, String categoria) {
        this.idPremio = idPremio;
        this.videojuego = videojuego;
        this.nombrePremio = nombrePremio;
        this.organizacion = organizacion;
        this.año = año;
        this.categoria = categoria;
    }

    public int getIdPremio() { return idPremio; }
    public Videojuego getVideojuego() { return videojuego; }
    public String getNombrePremio() { return nombrePremio; }
    public String getOrganizacion() { return organizacion; }
    public short getAño() { return año; }
    public String getCategoria() { return categoria; }

    public void setIdPremio(int idPremio) { this.idPremio = idPremio; }
    public void setVideojuego(Videojuego videojuego) { this.videojuego = videojuego; }
    public void setNombrePremio(String nombrePremio) { this.nombrePremio = nombrePremio; }
    public void setOrganizacion(String organizacion) { this.organizacion = organizacion; }
    public void setAño(short año) { this.año = año; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return "Premio{" +
                "idPremio=" + idPremio +
                ", videojuego=" + videojuego.getNombre() +
                ", nombrePremio='" + nombrePremio + '\'' +
                ", organizacion='" + organizacion + '\'' +
                ", año=" + año +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}