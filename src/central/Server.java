/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package central;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author David
 */
public class Server implements Runnable {

    private ServerSocket servidor;
    private CTPV_Frame app;

    public Server(CTPV_Frame app) {
        this.servidor = null;
        this.app = app;
    }

    /**
     * Método del arranca el hilo del servidor que permanecera abierto mientras lo este el CTPV,
     * se encargará de estar a la escucha para aceptar a TPV que se arranquen.
     *
     * Inicializa el hilo Venta que se encargar de atender al TPV que se conecta.
     * 
     */
    @Override
    public void run() {
        DataOutputStream out = null;
        int index;
        try {
            this.setServidor(new ServerSocket(65000));
            
            while (true) {
                
                Socket socket = this.getServidor().accept();
                index = this.getApp().primeroLibre();
                
                
                if ( index != -1) {                    
                    
                    Terminal_Frame terminal = new Terminal_Frame();
                    terminal.setTitle("Terminal Nº "+(index+1));
                    
                    this.getApp().insertarTerminal(terminal);
                    
                    Venta cliente = new Venta(socket, this.getApp(), index);

                    out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(cliente.getTerminal().getTitle());

                    cliente.start();

                    this.getApp().repaint();
                } else {
                    //Lanza la ventana emergente que avisa que se ha llegado al máximo de TPV conectados
                    JOptionPane.showMessageDialog(this.getApp(), "Limite de terminales alcanzado");
                     out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("ocupado");//Envia el mensaje para que se cierre el TPV que ha intentado conectarse
                    socket.close();
                }
                Thread.sleep(200);
            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

   
    //**************************************************************************
    //GETTERS & SETTERS
    
    public ServerSocket getServidor() {
        return servidor;
    }

    public void setServidor(ServerSocket servidor) {
        this.servidor = servidor;
    }

    public CTPV_Frame getApp() {
        return app;
    }

    public void setApp(CTPV_Frame app) {
        this.app = app;
    }

}
