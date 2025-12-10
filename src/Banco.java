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

    private Usuario usuarioActual;
    private DefaultTableModel modeloTabla;
    private DecimalFormat df = new DecimalFormat("$#,##0.00");

    public Banco(Usuario usuarioRecibido){
        this.usuarioActual = usuarioRecibido;

        setContentPane(Pantalla_Principal);
        setVisible(true);
        setSize(700, 500);
        setTitle("Sistema Bancario");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configurarTabla();
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
                Transferencia pantallaTransf = new Transferencia(usuarioActual);

                pantallaTransf.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        actualizarDatosEnPantalla();
                    }
                });
            }
        });
    }

    private void configurarTabla() {
        String[] columnas = {"Tipo Operación", "Monto", "Fecha"};
        modeloTabla = new DefaultTableModel(null, columnas);
        table1.setModel(modeloTabla);
    }

    private void actualizarDatosEnPantalla() {
        if (Usuario != null && Saldo != null) {
            Usuario.setText("Bienvenido: " + usuarioActual.getNombre());
            Saldo.setText("Saldo Actual: " + df.format(usuarioActual.getSaldo()));
        }
        if (usuarioActual.getHistorial() !=null){
            modeloTabla.setRowCount(0);
            ArrayList<Movimiento> lista = usuarioActual.getHistorial();

            for (Movimiento mov : lista){
                Object[] fila ={
                        mov.getTipo(),
                        df.format(mov.getMonto()),
                        mov.getFecha()
                };
                modeloTabla.addRow(fila);
            }
        }
    }
}