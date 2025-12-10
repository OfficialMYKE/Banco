import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame{
    private JPanel Login_Panel;
    private JTextField text_Usuario;
    private JButton iniciarButton;
    private JLabel Registrate;
    private JPasswordField pass_Contrasena;

    public Login(){
        setContentPane(Login_Panel);
        setVisible(true);
        setSize(700, 500);
        setTitle("Login");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        Registrate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Registro pantallaRegistro = new Registro();
                pantallaRegistro.setVisible(true);

                dispose();
            }
        });


        iniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });
    }

    private void validarLogin(){
        String inputUsuario = text_Usuario.getText();
        String contrasenaIngresada = new String(pass_Contrasena.getPassword());

        boolean UsuarioEncontrado = false;

        for (Usuario usua : BaseDatos.ListaUsuarios){

            boolean coincideUsuario = usua.getNombre().equals(inputUsuario) || usua.getEmail().equals(inputUsuario);

            if (coincideUsuario && usua.getContrasena().equals(contrasenaIngresada)){
                UsuarioEncontrado = true;
                JOptionPane.showMessageDialog(null, "Bienvenido");

                Banco pantallaPricipal = new Banco();
                pantallaPricipal.setVisible(true);

                dispose();
                break;
            }
        }

        if (!UsuarioEncontrado){
            JOptionPane.showMessageDialog(null,"Usuario o contraseña incorrecta");
        }
    }
}
