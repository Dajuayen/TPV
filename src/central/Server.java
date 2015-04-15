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

    @Override
    public void run() {
        DataOutputStream out = null;
        int index;
        try {
            this.setServidor(new ServerSocket(65000));
            while (true) {
                index = this.getApp().primeroLibre();
                if ( index != -1) {

                    Socket socket = this.getServidor().accept();
                    
                    Terminal_Frame terminal = new Terminal_Frame();
                    terminal.setTitle("Terminal Nº "+(index+1));
                    
                    this.getApp().insertarTerminal(terminal);
                    
                    Venta cliente = new Venta(socket, terminal);

                    out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(cliente.getTerminal().getTitle());

                    cliente.start();

                    this.getApp().repaint();
                } else {

                }
                Thread.sleep(200);
            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

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
