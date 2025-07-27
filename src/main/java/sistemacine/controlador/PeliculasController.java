
package sistemacine.controlador;

import sistemacine.modelo.*;
import sistemacine.vista.FrmPeliculas;
import java.sql.SQLException;
import java.util.List;

public class PeliculasController {
    private final FrmPeliculas vista;
    private final PeliculasDAO modelo;

    public PeliculasController(FrmPeliculas vista, PeliculasDAO modelo) {
        this.vista = vista;
        this.modelo = modelo;
        inicializar();
    }

    public void inicializar() {
        cargarPeliculas();
    }

    public void cargarPeliculas() {
        try {
            List<Pelicula> peliculas = modelo.obtenerTodas();
            vista.llenarTabla(peliculas);
        } catch (SQLException e) {
            vista.mostrarMensaje("Error cargando películas: " + e.getMessage());
        }
    }

    public void guardarPelicula() {
        Pelicula p = vista.getPeliculaDesdeFormulario();
        try {
            if (modelo.guardar(p)) {
                vista.mostrarMensaje("Película guardada con éxito.");
                cargarPeliculas();
                vista.limpiarCampos();
            } else {
                vista.mostrarMensaje("No se pudo guardar la película.");
            }
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al guardar: " + e.getMessage());
        }
    }

    public void actualizarPelicula() {
        Pelicula p = vista.getPeliculaDesdeFormulario();
        try {
            if (modelo.actualizar(p)) {
                vista.mostrarMensaje("Película actualizada con éxito.");
                cargarPeliculas();
                vista.limpiarCampos();
            } else {
                vista.mostrarMensaje("No se pudo actualizar la película.");
            }
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarPelicula(int id) {
        try {
            if (modelo.eliminar(id)) {
                vista.mostrarMensaje("Película eliminada.");
                cargarPeliculas();
                vista.limpiarCampos();
            } else {
                vista.mostrarMensaje("No se pudo eliminar la película.");
            }
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al eliminar: " + e.getMessage());
        }
    }
    
}

