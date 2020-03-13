
import javax.swing.JFrame;

public class EmpezarCliente {

    public static void main(String[] args) {
        Cliente cl;
        cl = new Cliente("192.168.100.68");
        cl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cl.iniciarCliente();
    }
}
