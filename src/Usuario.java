import java.io.Serializable;
import java.util.ArrayList;

// 'implements Serializable':
// permite que este objeto se pueda convertir en una secuencia de bytes.
public class Usuario implements Serializable{
    // Evita errores si modificas la clase y tratas de leer un objeto guardado con una version vieja.
    private static final long serialVersionUID = 1L;
    private int id; //(Primary Key)
    private String nombre;
    private String cedula;
    private String email;
    private String contrasena;
    private double saldo;
    // Relacion "Uno a Muchos": Un Usuario tiene MUCHOS movimientos.
    private ArrayList<Movimiento>historial;
    // Este constructor pide el 'id'. Lo usamos cuando traemos un usuario que YA EXISTE en MySQL.
    public Usuario(int id, String nombre, String cedula, String email, String contrasena, double saldo) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.email = email;
        this.contrasena = contrasena;
        this.saldo = saldo;
        // Inicializamos la lista vacía para evitar errores de "NullPointerException"
        this.historial = new ArrayList<>();
    }

    // Este NO pide 'id'. Lo usamos cuando creamos un usuario NUEVO en la pantalla de Registro.
    // El ID se queda en 0 porque MySQL se encargara de asignarle un automatico despues.
    public Usuario(String nombre, String cedula, String email, String contrasena, double saldo, ArrayList<Movimiento> historial) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.email = email;
        this.contrasena = contrasena;
        this.saldo = saldo;
        this.historial = historial;
        this.id = 0; // 0 indica que aun no tiene ID en la base de datos
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public double getSaldo() {
        return saldo;
    }

    public ArrayList<Movimiento> getHistorial() {
        return historial;
    }

    // Usado para actualizar el saldo en la memoria RAM tras un deposito/retiro
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    // Usado al hacer Login para rellenar la lista con los datos que vienen de la BD
    public void setHistorial(ArrayList<Movimiento> historial) {
        this.historial = historial;
    }

    // Método auxiliar para agregar movimientos uno por uno a la lista local
    public void registrarMovimiento(String tipo, double monto){
        Movimiento nuevoMov = new Movimiento(tipo, monto);
        historial.add(nuevoMov);
    }

}
