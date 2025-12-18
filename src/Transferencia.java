import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class Transferencia extends JFrame {
    private JPanel Pantalla_Transferencia;
    private JTextField text_Destinatario;
    private JTextField text_Monto;
    private JButton aceptarButton;
    private JButton cancelarButton;

    // Guardamos quien es la persona que esta usando la ventana
    private Usuario usuarioOrigen;

    public Transferencia(Usuario usuario) {
        // Recibimos al usuario logueado
        this.usuarioOrigen = usuario;

        setContentPane(Pantalla_Transferencia);
        setVisible(true);
        setSize(500, 400);
        setTitle("Realizar Transferencia");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Solo cierra esta ventana, no la app completa
                dispose();
            }
        });

        aceptarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarTransferencia();
            }
        });
    }

    //LOGICA TRANSFERENCIA
    private void confirmarTransferencia() {
        String destinatarioInput = text_Destinatario.getText();
        String montoInput = text_Monto.getText();

        if (destinatarioInput.isEmpty() || montoInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Llene todos los campos");
            return; // Detiene la función aquí
        }

        // Validacion logica: No te puedes transferir a ti mismo
        if (destinatarioInput.equals(usuarioOrigen.getNombre()) || destinatarioInput.equals(usuarioOrigen.getEmail())) {
            JOptionPane.showMessageDialog(null, "No puedes transferirte dinero a ti mismo.");
            return;
        }

        try {
            // Convertir texto a numero
            double monto = Double.parseDouble(montoInput);

            // Validaciones de dinero
            if (monto <= 0) {
                JOptionPane.showMessageDialog(null, "El monto debe ser mayor a 0");
                return;
            }
            if (monto > usuarioOrigen.getSaldo()) {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente para esta transferencia");
                return;
            }

            // Antes de restar dinero, verificamos que la otra persona exista
            Usuario usuarioDestino = BaseDatos.buscarUsuario(destinatarioInput);

            if (usuarioDestino != null) {
                String fechaHoy = LocalDate.now().toString();

                double nuevoSaldoOrigen = usuarioOrigen.getSaldo() - monto;
                double nuevoSaldoDestino = usuarioDestino.getSaldo() + monto;

                // Actualizamos el objeto local (para que se vea en pantalla al volver)
                usuarioOrigen.setSaldo(nuevoSaldoOrigen);
                // Actualizamos en la BD
                BaseDatos.actualizarSaldo(usuarioOrigen.getId(), nuevoSaldoOrigen);
                // Guardamos en Historial
                BaseDatos.guardarMovimiento(usuarioOrigen.getId(), "Transferencia enviada", -monto, fechaHoy);

                BaseDatos.actualizarSaldo(usuarioDestino.getId(), nuevoSaldoDestino);
                BaseDatos.guardarMovimiento(usuarioDestino.getId(), "Transferencia recibida", monto, fechaHoy);

                JOptionPane.showMessageDialog(null, "Transferencia exitosa a " + usuarioDestino.getNombre());
                dispose();// Cerramos la ventana al terminar con exito
            } else {
                JOptionPane.showMessageDialog(null, "El usuario destinatario no existe.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un monto numérico válido");
        }
    }
}