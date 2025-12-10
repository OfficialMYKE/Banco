import javax.swing.*;

public class Banco extends JFrame {

    private JPanel Pantalla_Principal;

    public Banco(){
        setContentPane(Pantalla_Principal);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Pantalla Principal");
        setLocationRelativeTo(null);
        setSize(700, 700);
    }
}
