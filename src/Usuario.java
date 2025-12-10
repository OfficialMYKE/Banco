public class Usuario {
    private String nombre;
    private String cedula;
    private String email;
    private String contrasena;
    private double saldo;

    public Usuario(String nombre, String cedula, String email, String contrasena, double saldo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.email = email;
        this.contrasena = contrasena;
        this.saldo = 0.00;
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
}
