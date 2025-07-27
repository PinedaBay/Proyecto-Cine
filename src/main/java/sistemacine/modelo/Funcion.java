
package sistemacine.modelo;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

public class Funcion {
    private int id;
    private String pelicula;
    private Date fecha;
    private Time hora;
    private String sala;
    private String promociones;

    public Funcion() {}

    public Funcion(int id, String pelicula, Date fecha, Time hora, String sala, String promociones) {
        this.id = id;
        this.pelicula = pelicula;
        this.fecha = fecha;
        this.hora = hora;
        this.sala = sala;
        this.promociones = promociones;        
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getpelicula() { return pelicula; }
    public void setpelicula(String pelicula) { this.pelicula = pelicula; }

    public Date getfecha() { return fecha; }
    public void setfecha(Date fecha) { this.fecha = fecha; }

    public Time gethora() { return hora; }
    public void sethora(Time hora) { this.hora = hora; }

    public String getsala() { return sala; }
    public void setsala(String sala) { this.sala = sala; }
    
    public String getpromociones() { return promociones; }
    public void setpromociones(String promociones) { this.promociones = promociones; }   

    public void setFecha(LocalDate fecha) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setHora(Time horaSQL) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

