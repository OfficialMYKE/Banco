import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Deposito extends JFrame {
    private JPanel Pantalla_Deposito;
    private JButton aceptarButton;
    private JButton cancelarButton;
    private JTextField text_Monto;

    private Usuario usuarioActual;

    public Deposito(Usuario usuario) {
        this.usuarioActual = usuario;

        setContentPane(Pantalla_Deposito);
        setVisible(true);
        setSize(500, 300);
        setTitle("Realizar Depósito");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        aceptarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarDeposito();
            }
        });
    }

    private void confirmarDeposito() {
        String textoMonto = text_Monto.getText();

        if (textoMonto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un monto");
            return;
        }

        try {
            double cantidadMonto = Double.parseDouble(textoMonto);

            if (cantidadMonto > 0) {
                double saldoActual = usuarioActual.getSaldo();

                usuarioActual.setSaldo(saldoActual + cantidadMonto);
                usuarioActual.registrarMovimiento("Deposito", cantidadMonto);
                BaseDatos.guardarUsuarios();

                JOptionPane.showMessageDialog(null, "Depósito Realizado con Éxito");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "El monto debe ser mayor a 0");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido");
        }
    }
}