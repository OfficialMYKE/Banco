import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 'implements Serializable'
// Permite que este objeto pueda (VIAJAR) por red o guardarse en archivos si fuera necesario.
public class Movimiento implements Serializable {
    private String tipo;
    private double monto;
    private String fecha;

    // Constructor solo pedimos TIPO y MONTO, NO PEDIMOS LA FECHA. la calculamos dentro del metodo
    public Movimiento(String tipo, double monto) {
        this.tipo = tipo;
        this.monto = monto;

        //Obtiene la fecha y hora del sistema
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"); //Fromato Año/Mes/Dia Hora:Minutos
        this.fecha = dtf.format(LocalDateTime.now());
    }


    public String getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public String getFecha() {
        return fecha;
    }

    // Cuando traemos el historial desde MySQL, la fecha YA existe (es vieja)
    // asi que necesitamos este metodo para sobrescribir la fecha automatica del constructor
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}