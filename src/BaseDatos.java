import java.sql.*;
import java.util.ArrayList;

public class BaseDatos {

    private static final String URL = "jdbc:mysql://bf1lvlxpjgtkyjbph2ko-mysql.services.clever-cloud.com:3306/bf1lvlxpjgtkyjbph2ko";

    private static final String USER = "ubg9psiqlptr95jk";
    private static final String PASSWORD = "uNfiqm82lHOOyGTrTUKW";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }

    public static boolean registrarUsuario(String nombre, String cedula, String email, String contra) {
        String sql = "INSERT INTO usuarios (nombre, cedula, email, contrasena, saldo) VALUES (?, ?, ?, ?, 0.00)";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, cedula);
            pstmt.setString(3, email);
            pstmt.setString(4, contra);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario validarLogin(String inputUser, String password) {
        String sql = "SELECT * FROM usuarios WHERE (nombre = ? OR email = ?) AND contrasena = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputUser);
            pstmt.setString(2, inputUser);
            pstmt.setString(3, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("cedula"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getDouble("saldo")
                );
                usuario.setHistorial(cargarHistorial(usuario.getId()));
                return usuario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void actualizarSaldo(int usuarioId, double nuevoSaldo) {
        String sql = "UPDATE usuarios SET saldo = ? WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setInt(2, usuarioId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void guardarMovimiento(int usuarioId, String tipo, double monto, String fecha) {
        String sql = "INSERT INTO movimientos (usuario_id, tipo, monto, fecha) VALUES (?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, tipo);
            pstmt.setDouble(3, monto);
            pstmt.setString(4, fecha);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Usuario buscarUsuario(String criterio) {
        String sql = "SELECT * FROM usuarios WHERE nombre = ? OR email = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, criterio);
            pstmt.setString(2, criterio);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("cedula"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getDouble("saldo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Movimiento> cargarHistorial(int usuarioId) {
        ArrayList<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos WHERE usuario_id = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                String tipo = rs.getString("tipo");
                double monto = rs.getDouble("monto");
                String fecha = rs.getString("fecha");

                Movimiento mov = new Movimiento(tipo, monto);
                mov.setFecha(fecha);

                lista.add(mov);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean eliminarHistorial(int usuarioId) {
        String sql = "DELETE FROM movimientos WHERE usuario_id = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}