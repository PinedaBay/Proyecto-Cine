package sistemacine.modelo;
import java.sql.Date;
import java.sql.Time;
public class Funcion {
    private int funcion_id;
    private int peliculaId;    
    private String pelicula;
    private Date fecha;
    private Time hora;
    private String sala;
    private int salaId;
    private String promociones;
    private int promocion_id;


    public Funcion() {}

    public Funcion(int funcion_id, int peliculaId, String pelicula, Date fecha, Time hora, String sala, int salaId, String promociones, int promocion_Id) {
        this.funcion_id = funcion_id;
        this.peliculaId = peliculaId;
        this.pelicula = pelicula;
        this.fecha = fecha;
        this.hora = hora;
        this.sala = sala;
        this.salaId = salaId;
        this.promociones = promociones; 
        this.promocion_id = promocion_id;
    }

    public void setPromocion_id(int promocion_id) {
        this.promocion_id = promocion_id;
    }

    public int getPromocion_id() {
        return promocion_id;
    }

    public int getPeliculaId() {
        return peliculaId;
    }
    public void setPeliculaId(int peliculaId) {
        this.peliculaId = peliculaId;
    }

    public int getSalaId() {
        return salaId;
    }
    public void setSalaId(int salaId) {
        this.salaId = salaId;
    }

    public int getfuncion_Id() {
        return funcion_id;
    }

    public void setfuncion_Id(int id) {
        this.funcion_id = id;
    }

    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getPromociones() {
        return promociones;
    }

    public void setPromociones(String promociones) {
        this.promociones = promociones;
    }
 
}
