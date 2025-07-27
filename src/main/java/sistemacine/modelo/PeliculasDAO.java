package sistemacine.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculasDAO {

    public List<Pelicula> obtenerTodas() throws SQLException {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT pelicula_id, titulo, duracion, sinopsis, clasificacion FROM peliculas";

        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pelicula p = new Pelicula();
                p.setId(rs.getInt("pelicula_id"));
                p.setTitulo(rs.getString("titulo"));
                p.setDuracion(rs.getInt("duracion"));
                p.setSinopsis(rs.getString("sinopsis"));
                p.setClasificacion(rs.getString("clasificacion"));
                lista.add(p);
            }
        }
        return lista;
    }

    public int obtenerSiguienteId() throws SQLException {
        String sql = "SELECT IFNULL(MAX(pelicula_id), 0) + 1 AS siguienteId FROM peliculas";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("siguienteId");
            }
        }
        return 1;
    }

    public boolean guardar(Pelicula p) throws SQLException {
        String sql = "INSERT INTO peliculas (titulo, duracion, sinopsis, clasificacion) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getTitulo());
            ps.setInt(2, p.getDuracion());
            ps.setString(3, p.getSinopsis());
            ps.setString(4, p.getClasificacion());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1));  // Actualiza el id en el objeto
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean actualizar(Pelicula p) throws SQLException {
        String sql = "UPDATE peliculas SET titulo = ?, duracion = ?, sinopsis = ?, clasificacion = ? WHERE pelicula_id = ?";
        try (Connection conn = Conexion.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setInt(2, p.getDuracion());
            ps.setString(3, p.getSinopsis());
            ps.setString(4, p.getClasificacion());
            ps.setInt(5, p.getId());
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

                ps1.setInt(1, id);
                ps1.executeUpdate();

                ps2.setInt(1, id);
                ps2.executeUpdate();

                ps3.setInt(1, id);
                ps3.executeUpdate();

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

