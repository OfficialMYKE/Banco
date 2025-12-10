import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable{
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String cedula;
    private String email;
    private String contrasena;
    private double saldo;
    private ArrayList<Movimiento>historial;

    public Usuario(String nombre, String cedula, String email, String contrasena, double saldo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.email = email;
        this.contrasena = contrasena;
        this.saldo = 0.00;
        this.historial = new ArrayList<>();
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

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void registrarMovimiento(String tipo, double monto){
        Movimiento nuevoMov = new Movimiento(tipo, monto);
        historial.add(nuevoMov);
    }

    public ArrayList<Movimiento> getHistorial(){
        return historial;
    }
}
