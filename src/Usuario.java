import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable{
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;
    private String cedula;
    private String email;
    private String contrasena;
    private double saldo;
    private ArrayList<Movimiento>historial;

    public Usuario(int id, String nombre, String cedula, String email, String contrasena, double saldo) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.email = email;
        this.contrasena = contrasena;
        this.saldo = saldo;
        this.historial = new ArrayList<>();
    }

    public Usuario(String nombre, String cedula, String email, String contrasena, double saldo, ArrayList<Movimiento> historial) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.email = email;
        this.contrasena = contrasena;
        this.saldo = saldo;
        this.historial = historial;
        this.id = 0;
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

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setHistorial(ArrayList<Movimiento> historial) {
        this.historial = historial;
    }

    public void registrarMovimiento(String tipo, double monto){
        Movimiento nuevoMov = new Movimiento(tipo, monto);
        historial.add(nuevoMov);
    }

}
