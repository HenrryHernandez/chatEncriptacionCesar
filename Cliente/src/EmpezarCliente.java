
import javax.swing.JFrame;

public class EmpezarCliente {

    public static void main(String[] args) {
        Cliente cl;
        cl = new Cliente("192.168.100.68"); //poner ip de la computador donde estara el servidor
        cl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cl.iniciarCliente();
    }
}
