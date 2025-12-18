import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Banco extends JFrame {

    private JPanel Pantalla_Principal;
    private JPanel Panel_Info;
    private JLabel Usuario;
    private JLabel Saldo;
    private JButton DEPÓSITOButton;
    private JButton RETIROButton;
    private JButton TRANSFERENCIAButton;
    private JButton SALIRButton;
    private JTable table1;
    private JButton ELIMINARREGISTROSButton;

    // Objeto 'Usuario': Mantiene la sesion de quien esta logueado actualmente.
    private Usuario usuarioActual;
    // Controla que datos se muestran en las filas/columnas.
    private DefaultTableModel modeloTabla;
    private DecimalFormat df = new DecimalFormat("$#,##0.00");

    public Banco(Usuario usuarioRecibido){
        // Recibimos al usuario que paso al logon
        this.usuarioActual = usuarioRecibido;

        setContentPane(Pantalla_Principal);
        setVisible(true);
        setSize(700, 500);
        setTitle("Sistema Bancario");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configurarTabla();
        // Llenamos los datos (Nombre, Saldo y Tabla) por primera vez
        actualizarDatosEnPantalla();

        SALIRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login pantallaLogin = new Login();
                pantallaLogin.setVisible(true);
                dispose();
            }
        });

        DEPÓSITOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la ventanita de deposito
                Deposito pantallaDeposito = new Deposito(usuarioActual);

                pantallaDeposito.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        actualizarDatosEnPantalla();
                    }
                });
            }
        });

        RETIROButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la ventanita de retiro
                Retiro pantallaRetiro = new Retiro(usuarioActual);

                pantallaRetiro.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        actualizarDatosEnPantalla();
                    }
                });
            }
        });

        TRANSFERENCIAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la ventanita de trnasferencia
                Transferencia pantallaTransf = new Transferencia(usuarioActual);

                pantallaTransf.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        actualizarDatosEnPantalla();
                    }
                });
            }
        });


        ELIMINARREGISTROSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas borrar todo tu historial?",
                        "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION){
                    boolean eliminado = BaseDatos.eliminarHistorial(usuarioActual.getId());
                    if (eliminado){
                        JOptionPane.showMessageDialog(null, "Historila eliminado correctamente.");
                        actualizarDatosEnPantalla();
                    } else {
                        JOptionPane.showMessageDialog(null, "No hay registros para eliminar o hubo un error.");
                    }
                }
            }
        });
    }

    private void configurarTabla() {
        String[] columnas = {"Tipo Operación", "Monto", "Fecha"};
        // Inicializamos el modelo con las columnas pero sin datos (null)
        modeloTabla = new DefaultTableModel(null, columnas);
        table1.setModel(modeloTabla);
    }

    //Se llama al inicio y cada vez que vuelves de hacer un depósito/retiro.
    private void actualizarDatosEnPantalla() {
        ArrayList<Movimiento> historialReciente = BaseDatos.cargarHistorial(usuarioActual.getId());
        // Actualizamos la lista interna del usuario
        usuarioActual.setHistorial(historialReciente);

        // Actualizamos las etiquetas de texto (Nombre y Saldo)
        if (Usuario != null && Saldo != null) {
            Usuario.setText("Bienvenido: " + usuarioActual.getNombre());
            Saldo.setText("SALDO ACTUAL: " + df.format(usuarioActual.getSaldo()));
        }

        //Limpiamos la tabla vieja
        modeloTabla.setRowCount(0);
        // Llenamos la tabla de nuevo fila por fila
        if (historialReciente != null) {
            for (Movimiento mov : historialReciente) {
                Object[] fila = {
                        mov.getTipo(),
                        // Formateamos el monto también en la tabla
                        df.format(mov.getMonto()),
                        mov.getFecha()
                };
                modeloTabla.addRow(fila);
            }
        }
    }
}