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

        if (textoMonto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un monto");
            return;
        }

        try {
            double cantidadRetiro = Double.parseDouble(textoMonto);

            if (cantidadRetiro <= 0) {
                JOptionPane.showMessageDialog(null, "El monto debe ser mayor a 0");
                return;
            }

            if (cantidadRetiro > usuarioActual.getSaldo()) {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente. Su saldo es: $" + usuarioActual.getSaldo());
                return;
            }
            double nuevoSaldo = usuarioActual.getSaldo() - cantidadRetiro;
            String fechaHoy = LocalDate.now().toString();

            usuarioActual.setSaldo(nuevoSaldo);

            BaseDatos.actualizarSaldo(usuarioActual.getId(), nuevoSaldo);

            BaseDatos.guardarMovimiento(usuarioActual.getId(), "Retiro", -cantidadRetiro, fechaHoy);

            JOptionPane.showMessageDialog(null, "Retiro exitoso. Nuevo saldo: $" + usuarioActual.getSaldo());
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese solo números válidos");
        }
    }
}