import java.util.ArrayList;
import java.io.*;

public class BaseDatos {
    public static ArrayList<Usuario> ListaUsuarios = new ArrayList<>();
    private static final String Archivo = "usuarios.dat";

    public static void guardarUsuarios(){
        try {
            FileOutputStream fos = new FileOutputStream(Archivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.close();
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void cargarUsuarios(){
        try {
            File ARCHIVO = new File(Archivo);
            if (!ARCHIVO.exists()) {
                return;
            }

            FileInputStream fis = new FileInputStream(Archivo);
            ObjectInput ois = new ObjectInputStream(fis);

            ListaUsuarios = (ArrayList<Usuario>) ois.readObject();

            ois.close();
            fis.close();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
