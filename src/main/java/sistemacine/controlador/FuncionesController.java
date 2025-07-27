
package sistemacine.controlador;

import sistemacine.modelo.*;
import java.sql.SQLException;
import java.util.List;
import sistemacine.vista.FrmFunciones;

public class FuncionesController{
    private final FrmFunciones vista;
    private final FuncionesDAO modelo;

    public FuncionesController(FrmFunciones vista, FuncionesDAO modelo) {
        this.vista = vista;
        this.modelo = modelo;
        inicializar();
    }

    public void inicializar() {
        cargarFunciones();
    }

    public void cargarFunciones() {
        try {
            List<Funcion> Funciones = modelo.obtenerTodas();
            vista.llenarTabla(Funciones);
        } catch (SQLException e) {
            vista.mostrarMensaje("Error cargando funciones: " + e.getMessage());
        }
    }

    public void guardarFunciones() {
        Funcion f = vista.getFuncionDesdeFormulario();
        try {
            if (modelo.guardar(f)) {
                vista.mostrarMensaje("Funcion guardada con éxito.");
                cargarFunciones();
                vista.limpiarCampos();
            } else {
                vista.mostrarMensaje("No se pudo guardar la Funcion.");
            }
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al guardar: " + e.getMessage());
        }
    }

    public void actualizarFunciones() {
         Funcion f = vista.getFuncionDesdeFormulario();
        try {
            if (modelo.actualizar(f)) {
                vista.mostrarMensaje("Funcion actualizada con éxito.");
                cargarFunciones();
                vista.limpiarCampos();
            } else {
                vista.mostrarMensaje("No se pudo actualizar la funcion.");
            }
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarFunciones(int id) {
        try {
            if (modelo.eliminar(id)) {
                vista.mostrarMensaje("Funcion eliminada.");
                cargarFunciones();
                vista.limpiarCampos();
            } else {
                vista.mostrarMensaje("No se pudo eliminar la funcion.");
            }
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al eliminar: " + e.getMessage());
        }
    }
    
}