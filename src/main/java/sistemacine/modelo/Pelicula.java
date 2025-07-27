
package sistemacine.modelo;

public class Pelicula {
    private int id;
    private String titulo;
    private int duracion;
    private String sinopsis;
    private String clasificacion;

    public Pelicula() {}

    public Pelicula(int id, String titulo, int duracion, String sinopsis, String clasificacion) {
        this.id = id;
        this.titulo = titulo;
        this.duracion = duracion;
        this.sinopsis = sinopsis;
        this.clasificacion = clasificacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }
    
    
}
