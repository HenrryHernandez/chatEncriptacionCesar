
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Cliente extends JFrame {

    private JLabel lblClave;
    private JTextField tfClave;
    private JTextField tfMensajeEnviar;
    private JTextArea txtChat;
    private JTextArea txtChatEncriptado;
    private JButton btnEnviar;
    private JButton btnDesencriptar;
    private JScrollPane largoMensaje;
    private JScrollPane largoEncriptado;

    private String mensaje = "";
    private String ip_servidor;
    //public String mensajeEncriptado;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket conexion;

    public Cliente(String host) {
        super("Cliente");
        ip_servidor = host;
        setBounds(300, 100, 500, 550);
        setLayout(null);

        lblClave = new JLabel("Inserte la clave para la encriptación: ");
        lblClave.setBounds(20, 20, 310, 25);
        add(lblClave);

        tfClave = new JTextField();
        tfClave.setBounds(240, 20, 150, 25);
        add(tfClave);

        txtChat = new JTextArea();
        txtChat.setEditable(false);

        largoMensaje = new JScrollPane(txtChat);
        largoMensaje.setBounds(20, 50, 440, 150);
        add(largoMensaje);

        txtChatEncriptado = new JTextArea();
        txtChatEncriptado.setEditable(false);

        largoEncriptado = new JScrollPane(txtChatEncriptado);
        largoEncriptado.setBounds(20, 280, 440, 150);
        add(largoEncriptado);

        tfMensajeEnviar = new JTextField();
        tfMensajeEnviar.setBounds(20, 220, 310, 25);
        tfMensajeEnviar.setEditable(true);
        add(tfMensajeEnviar);

        btnEnviar = new JButton("Enviar");
        btnEnviar.setBounds(340, 220, 100, 25);
        add(btnEnviar);
        btnEnviar.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (tfClave.getText().equals("") || malaClave(tfClave.getText())) {
                    JOptionPane.showMessageDialog(null, "Ingrese clave de desencriptación válida");
                } else {
                    enviar(tfMensajeEnviar.getText());
                    tfMensajeEnviar.setText("");
                }
            }
        }
        );

        btnDesencriptar = new JButton("Desencriptar");
        btnDesencriptar.setBounds(20, 450, 150, 25);
        add(btnDesencriptar);
        btnDesencriptar.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (tfClave.getText().equals("") || malaClave(tfClave.getText())) {
                    JOptionPane.showMessageDialog(null, "Ingrese clave válida");
                } else {
                    txtChatEncriptado.setText("");
                    txtChatEncriptado.setText(desencriptar(mensaje, Integer.parseInt(tfClave.getText())));
                }
            }
        }
        );

        setVisible(true);
    }

    boolean malaClave(String clave){
        for(int i = 0; i < clave.length(); i++){
            if(Character.isLetter(clave.charAt(i))){
                return true;
            }
        }
        return false;
    }
    
    public String encriptar(String palabra, int clave) {
        String caracteres = palabra.toUpperCase();
        char[] arreCar = caracteres.toCharArray();
        String mensaje = "";
        char aux = 0;

        for (int i = 0; i < arreCar.length; i++) {
            if (arreCar[i] != 32) {
                aux = (char) (((((int) arreCar[i] - 65) + clave) % 26) + 65);
            } else {
                aux = 32;
            }
            mensaje += aux;
        }
        return mensaje;
    }

    public String desencriptar(String palabra, int clave) {
        String caracteres = palabra.toUpperCase();
        char[] arreCar = caracteres.toCharArray();
        String mensaje = "";
        char aux = 0;

        for (int i = 0; i < arreCar.length; i++) {
            if (arreCar[i] != 32) {
                aux = (char) (((((int) arreCar[i] - 65) + (26 - (clave % 26))) % 26) + 65);
            } else {
                aux = 32;
            }
            mensaje += aux;
        }
        return mensaje;
    }

    public void iniciarCliente() {
        try {
            conectarAlServidor();
            configurarFlujosDeDatos();
            chateandoAndo();
        } catch (EOFException eofException) {
            imprimirMensaje("\nSe cerro la conexión");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            cerrarConexiones();
        }
    }

    void ponerClave(String clave) {
        tfClave.setText(clave);
    }

    private void conectarAlServidor() throws IOException {
        imprimirMensaje("Conectando... \n");
        conexion = new Socket(InetAddress.getByName(ip_servidor), 6789);
        imprimirMensaje("Conectado con: " + conexion.getInetAddress().getHostName() + "\n-----------------------------------------------------------------------------------------\n");
    }

    private void configurarFlujosDeDatos() throws IOException {
        output = new ObjectOutputStream(conexion.getOutputStream());
        output.flush();
        input = new ObjectInputStream(conexion.getInputStream());
    }

    private void chateandoAndo() throws IOException {
        do {
            try {
                mensaje = (String) input.readObject();
                //mensajeEncriptado = mensaje;
                imprimirMensaje("\nServidor: " + mensaje);
            } catch (ClassNotFoundException classNotFoundException) {
                imprimirMensaje("Paquete no identificado");
            }
        } while (true); //!mensaje.equals("Salir")
    }

    private void cerrarConexiones() {
        imprimirMensaje("\nCerrando la conexión...");
        try {
            output.close();
            input.close();
            conexion.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void enviar(String message) {
        try {
            output.writeObject(encriptar(message, Integer.parseInt(tfClave.getText())));
            output.flush();
            imprimirMensaje("\nCliente: " + message);
        } catch (IOException ioException) {
            txtChat.append("\nAlgo salió mal");
        }
    }

    private void imprimirMensaje(final String message) {
        SwingUtilities.invokeLater(
                new Runnable() {
            public void run() {
                txtChat.append(message);
            }
        }
        );
    }
}
