
import javax.swing.JFrame;

public class EmpezarServidor {

    public static void main(String[] args) {
        Servidor sr = new Servidor();
        sr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sr.iniciarServidor();
    }
}
