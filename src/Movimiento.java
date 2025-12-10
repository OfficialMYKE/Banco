import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimiento implements Serializable {
    private String tipo;
    private double monto;
    private String fecha;

    public Movimiento(String tipo, double monto) {
        this.tipo = tipo;
        this.monto = monto;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        this.fecha = dtf.format(LocalDateTime.now());
    }

    public String getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public String getFecha() { return fecha; }
}