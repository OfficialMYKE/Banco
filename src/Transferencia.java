import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Transferencia extends JFrame {
    private JPanel Pantalla_Transferencia;
    private JTextField text_Destinatario;
    private JTextField text_Monto;
    private JButton aceptarButton;
    private JButton cancelarButton;

    private Usuario usuarioOrigen;

    public Transferencia(Usuario usuario) {
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

    private void confirmarTransferencia() {
        String destinatarioInput = text_Destinatario.getText();
        String montoInput = text_Monto.getText();

        if (destinatarioInput.isEmpty() || montoInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Llene todos los campos");
            return;
        }

        if (destinatarioInput.equals(usuarioOrigen.getNombre()) || destinatarioInput.equals(usuarioOrigen.getEmail())) {
            JOptionPane.showMessageDialog(null, "No puedes transferirte dinero a ti mismo.");
            return;
        }

        try {
            double monto = Double.parseDouble(montoInput);

            if (monto <= 0) {
                JOptionPane.showMessageDialog(null, "El monto debe ser mayor a 0");
                return;
            }
            if (monto > usuarioOrigen.getSaldo()) {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente para esta transferencia");
                return;
            }

            Usuario usuarioDestino = null;

            for (Usuario u : BaseDatos.ListaUsuarios) {
                if (u.getNombre().equals(destinatarioInput) || u.getEmail().equals(destinatarioInput)) {
                    usuarioDestino = u;
                    break;
                }
            }

            if (usuarioDestino != null) {
                usuarioOrigen.setSaldo(usuarioOrigen.getSaldo() - monto);
                usuarioOrigen.registrarMovimiento("Transferencia enviada", -monto);

                usuarioDestino.setSaldo(usuarioDestino.getSaldo() + monto);
                usuarioDestino.registrarMovimiento("Transferencia recibida", monto);
                BaseDatos.guardarUsuarios();

                JOptionPane.showMessageDialog(null, "Transferencia exitosa a " + usuarioDestino.getNombre());
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "El usuario destinatario no existe.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un monto numérico válido");
        }
    }
}