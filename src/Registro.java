import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Registro extends JFrame{
    private JPanel Registro_Panel;
    private JTextField text_Usuario;
    private JTextField text_Cedula;
    private JTextField text_Email;
    private JButton crearCuentaButton;
    private JLabel IniciarSesion;
    private JPasswordField pass_ContrasenaVerificar;
    private JPasswordField pass_contrasena;

    public Registro(){
        setContentPane(Registro_Panel);
        setVisible(true);
        setTitle("Registro");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        IniciarSesion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Login pantallaLogin = new Login();
                pantallaLogin.setVisible(true);

                dispose();
            }
        });


        crearCuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistarUsuario();

            }
        });

    }
    private void RegistarUsuario(){
        String nombre = text_Usuario.getText();
        String cedula = text_Cedula.getText();
        String email = text_Email.getText();
        String contrasena = new String(pass_contrasena.getPassword());
        String confirmasrContrasena = new String(pass_ContrasenaVerificar.getPassword());

        if (nombre.isEmpty() || cedula.isEmpty() || email.isEmpty() || contrasena.isEmpty()){
            JOptionPane.showMessageDialog(null, "llene todos los campos");
            return;
        }
        if (!contrasena.equals(confirmasrContrasena)){
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
            return;
        }

        Usuario usuarioNuevo = new Usuario(nombre,cedula,email,contrasena,0);
        BaseDatos.ListaUsuarios.add(usuarioNuevo);

        JOptionPane.showMessageDialog(null, "Cuenta creada exitosamente");

        Login pantallaLogin = new Login();
        pantallaLogin.setVisible(true);

        dispose();
    }
}

