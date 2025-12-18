import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame{
    private JPanel Login_Panel;
    private JTextField text_Usuario;
    private JButton iniciarButton;
    private JLabel Registrate; // Este Label funciona como un boton/link
    private JPasswordField pass_Contrasena;

    public Login(){
        setContentPane(Login_Panel);
        setVisible(true);
        setSize(700, 500);
        setTitle("Login");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //BaseDatos.cargarUsuarios();

        // Usamos un MouseListener para que el texto "REGISTRATE" funcione como un enlace
        Registrate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // Abrir ventana de registro
                Registro pantallaRegistro = new Registro();
                pantallaRegistro.setVisible(true);

                //Cuando se abre la pantalla registro se cierra la pantalla LOGIN
                dispose();
            }
        });


        iniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Llamamos a la funcion para verificar los datos
                validarLogin();
            }
        });
    }

    /*private void validarLogin(){
        String inputUsuario = text_Usuario.getText();
        String contrasenaIngresada = new String(pass_Contrasena.getPassword());

        boolean UsuarioEncontrado = false;

        for (Usuario usua : BaseDatos.ListaUsuarios){

            boolean coincideUsuario = usua.getNombre().equals(inputUsuario) || usua.getEmail().equals(inputUsuario);

            if (coincideUsuario && usua.getContrasena().equals(contrasenaIngresada)){
                UsuarioEncontrado = true;
                JOptionPane.showMessageDialog(null, "Bienvenido " + usua.getNombre()); // Agregué el nombre para que se vea mejor

                Banco pantallaPricipal = new Banco(usua);
                pantallaPricipal.setVisible(true);

                dispose();
                break;
            }
        }

        if (!UsuarioEncontrado){
            JOptionPane.showMessageDialog(null,"Usuario o contraseña incorrecta");
        }*/

    // Validar logica con la base de datos
    private void validarLogin(){
        // Capturamos lo que el usuario escribio
        String inputUsuario = text_Usuario.getText();
        // Convertimos la contraseña en String porque devuelve un Char
        String contrasenaIngresada = new String(pass_Contrasena.getPassword());
        // Si existe, nos devuelve el objeto Usuario completo (con su ID, saldo, etc.)
        Usuario usuarioEncontrado = BaseDatos.validarLogin(inputUsuario, contrasenaIngresada);
        if (usuarioEncontrado != null){
            // Usuario existentente yu datos correctos
            JOptionPane.showMessageDialog(null, "Bienvenido" + usuarioEncontrado.getNombre());
            // Redirgimos al usuario a la pantalla de principal BANCO.
            Banco pantallaPrincipal = new Banco(usuarioEncontrado);
            pantallaPrincipal.setVisible(true);

            dispose();
        } else {
            //La base de datos no encontro coincidencias o la clave estaba mal
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        }


    }
}
