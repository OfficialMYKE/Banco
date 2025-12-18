import java.sql.*;
import java.util.ArrayList;

public class BaseDatos {

    //URL a la base de datos en la nube (Clever Cloud)
    private static final String URL = "jdbc:mysql://bf1lvlxpjgtkyjbph2ko-mysql.services.clever-cloud.com:3306/bf1lvlxpjgtkyjbph2ko";
    //Credenciales de accesp a la base de datos, Usuario y Contraseña
    private static final String USER = "ubg9psiqlptr95jk";
    private static final String PASSWORD = "uNfiqm82lHOOyGTrTUKW";

    //Método (Conectar) conecta java con MYSQL
    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }

    //Tratamos de evitar (SQL Inyection), utilizamos el caracter (?)
    public static boolean registrarUsuario(String nombre, String cedula, String email, String contra) {
        String sql = "INSERT INTO usuarios (nombre, cedula, email, contrasena, saldo) VALUES (?, ?, ?, ?, 0.00)";
        //Aseguramso que la conexecion de cierre sola, para evitar saturaciones en la base de datos
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            //Asignamos los valores a (?)
            pstmt.setString(1, nombre);
            pstmt.setString(2, cedula);
            pstmt.setString(3, email);
            pstmt.setString(4, contra);

            //executeUpdate() se usa para INSERT, UPDATE, DELETE (CRUD) en la base de datos
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // La consulta busca por Nombre O por Email, permitiendo iniciar sesion con cualquiera de los dos
    public static Usuario validarLogin(String inputUser, String password) {
        String sql = "SELECT * FROM usuarios WHERE (nombre = ? OR email = ?) AND contrasena = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputUser); // nombre ?
            pstmt.setString(2, inputUser); // email ?
            pstmt.setString(3, password); // contraseña ?

            // executeQuery(). se usa para LEER datos
            ResultSet rs = pstmt.executeQuery();

            // Si rs.next() es true, significa que encontro un usuario con esos datos
            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("cedula"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getDouble("saldo")
                );
                //Cargamos el historial de movimientos
                usuario.setHistorial(cargarHistorial(usuario.getId()));
                return usuario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Devuelve null si las credenciales son incorrectas
    }

    // Se llama cada vez que se hace un DEPOSITO, RETIRO, TRANSFERENCA (Actualiza el saldo)
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
    // Se guardan los movimientos en el h8istorial
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
    // Se busca al usuario para realizar la transferencia
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
        return null; // Si no se encuentra al usuario se devuel null
    }

    //Se recupera la lista de movimientos de un usuario en especifico
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

                lista.add(mov); // Añadimos cada movimiento a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Eliminamos los registros de la tabla de movientos
    public static boolean eliminarHistorial(int usuarioId) {
        String sql = "DELETE FROM movimientos WHERE usuario_id = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            // devuelve el numero de filas borradas
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}