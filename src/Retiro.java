import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class Retiro extends JFrame {
    private JPanel Pantalla_Retiro;
    private JTextField text_Monto;
    private JButton aceptarButton;
    private JButton cancelarButton;
    private Usuario usuarioActual;

    // Necesitamos guardar quien esta logueado para saber a quien quitarle el dinero
    public Retiro(Usuario usuario) {
        this.usuarioActual = usuario;

        setContentPane(Pantalla_Retiro);
        setVisible(true);
        setSize(500, 300);
        setTitle("Realizar Retiro");
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
                confirmarRetiro();
            }
        });
    }

    private void confirmarRetiro() {
        String textoMonto = text_Monto.getText();

        // Validacion, si el usuario escribio algo
        if (textoMonto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un monto");
            return;
        }

        try {
            // Conventir texto a numero
            double cantidadRetiro = Double.parseDouble(textoMonto);
            // No se puede retirar 0 ni negativos
            if (cantidadRetiro <= 0) {
                JOptionPane.showMessageDialog(null, "El monto debe ser mayor a 0");
                return;
            }

            // Si intenta sacar $100 y tiene $50, el banco debe detenerlo.
            if (cantidadRetiro > usuarioActual.getSaldo()) {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente. Su saldo es: $" + usuarioActual.getSaldo());
                return;
            }

            // Calculamos el nuevo saldo (Resta)
            double nuevoSaldo = usuarioActual.getSaldo() - cantidadRetiro;
            String fechaHoy = LocalDate.now().toString();

            // Actualizamos el objeto en memoria RAM (para que la app se vea actualizada rapido)
            usuarioActual.setSaldo(nuevoSaldo);
            // Guardamos el historial
            BaseDatos.actualizarSaldo(usuarioActual.getId(), nuevoSaldo);

            BaseDatos.guardarMovimiento(usuarioActual.getId(), "Retiro", -cantidadRetiro, fechaHoy);

            JOptionPane.showMessageDialog(null, "Retiro exitoso. Nuevo saldo: $" + usuarioActual.getSaldo());
            dispose();

        } catch (NumberFormatException e) {
            // Si el usuario escribe letras en vez de números, cae aquí
            JOptionPane.showMessageDialog(null, "Ingrese solo números válidos");
        }
    }
}