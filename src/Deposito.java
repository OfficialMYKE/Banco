import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class Deposito extends JFrame {
    private JPanel Pantalla_Deposito;
    private JButton aceptarButton;
    private JButton cancelarButton;
    private JTextField text_Monto;

    // Guardamos al usuario que inicio sesion para saber a quien sumarle el dinero
    private Usuario usuarioActual;

    public Deposito(Usuario usuario) {
        // Recibimos el objeto usuario desde la ventana principal
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
                double nuevoSaldo = usuarioActual.getSaldo() + cantidadMonto;
                String fechaHoy = LocalDate.now().toString();

                // Actualizamos el objeto local (para que la pantalla del Banco se actualice visualmente)
                usuarioActual.setSaldo(nuevoSaldo);

                // Actualizamos el saldo en la Base de Datos
                BaseDatos.actualizarSaldo(usuarioActual.getId(), nuevoSaldo);

                // Guardamos el movimiento en la tabla de la Base de Datos
                BaseDatos.guardarMovimiento(usuarioActual.getId(), "Depósito", cantidadMonto, fechaHoy);

                // ELIMINADO: BaseDatos.guardarUsuarios(); (Ya no es necesario)

                JOptionPane.showMessageDialog(null, "Depósito Realizado con Éxito");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "El monto debe ser mayor a 0");
            }

        } catch (NumberFormatException e) {
            // Si el usuario escribe letras en lugar de números
            JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido");
        }
    }
}