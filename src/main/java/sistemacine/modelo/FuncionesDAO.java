package sistemacine.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionesDAO {

    public List<Funcion> obtenerTodas() throws SQLException {
        List<Funcion> lista = new ArrayList<>();
        String sql = """
        SELECT f.funcion_id,
               p.titulo AS pelicula,
               f.fecha,
               f.hora,
               s.nombre AS sala,
               pr.descripcion AS promocion
        FROM funciones f
        JOIN peliculas p ON f.pelicula_id = p.pelicula_id
        JOIN salas s ON f.sala_id = s.sala_id
        LEFT JOIN promociones pr ON f.promocion_id = pr.promocion_id
    """;

        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Funcion f = new Funcion();
                f.setId(rs.getInt("funcion_id"));
                f.setpelicula(rs.getString("pelicula"));
                f.setfecha(rs.getDate("fecha"));
                f.sethora(rs.getTime("hora"));                
                f.setsala(rs.getString("salas"));
                f.setpromociones(rs.getString("promociones"));
                lista.add(f);
            }
        }
        return lista;
    }

    public int obtenerSiguienteId() throws SQLException {
        String sql = "SELECT IFNULL(MAX(funcion_id), 0) + 1 AS siguienteId FROM funciones";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("siguienteId");
            }
        }
        return 1;
    }
    public boolean guardar(Funcion f) throws SQLException {
        String sql = "INSERT INTO funciones (pelicula, fecha, hora, sala, promociones) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getpelicula());
            ps.setDate(2, f.getfecha());
            ps.setTime(3, f.gethora());
            ps.setString(4, f.getsala());
            ps.setString(5, f.getpromociones());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        f.setId(rs.getInt(1));  
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean actualizar(Funcion f) throws SQLException {
        String sql = "UPDATE funciones SET pelicula = ?, fecha = ?, hora = ?, sala = ?, promociones = ? WHERE funcion_id = ?";
        try (Connection conn = Conexion.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getpelicula());
            ps.setDate(2, f.getfecha());
            ps.setTime(3, f.gethora());
            ps.setString(4, f.getsala());
            ps.setString(5, f.getpromociones());
            ps.setInt(6, f.getId());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

   public boolean eliminar(int id) throws SQLException {
    try (Connection conn = Conexion.getConexion()) {
        conn.setAutoCommit(false);
        String sqlBoletos = "DELETE B FROM boletos B JOIN funciones F ON B.funcion_id = F.funcion_id WHERE F.pelicula_id = ?";
        String sqlFunciones = "DELETE FROM funciones WHERE pelicula_id = ?";
        String sqlPeliculasGenero = "DELETE FROM peliculas_genero WHERE pelicula_id = ?";
        String sqlPelicula = "DELETE FROM peliculas WHERE pelicula_id = ?";

        try (PreparedStatement ps1 = conn.prepareStatement(sqlBoletos);
             PreparedStatement ps2 = conn.prepareStatement(sqlFunciones);
             PreparedStatement ps3 = conn.prepareStatement(sqlPeliculasGenero);
             PreparedStatement ps4 = conn.prepareStatement(sqlPelicula)) {

            ps1.setInt(1, id); ps1.executeUpdate();
            ps2.setInt(1, id); ps2.executeUpdate();
            ps3.setInt(1, id); ps3.executeUpdate();
            ps4.setInt(1, id);
            int filas = ps4.executeUpdate();

            conn.commit();
            return filas > 0;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
} 
}
