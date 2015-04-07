/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ctpv;

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
        try {
            this.setServidor(new ServerSocket(65000));
            while (true) {
                Socket socket = this.getServidor().accept();
                
                Venta cliente = new Venta(socket, this.getApp().devuelveTerminal());
                cliente.start();
                
                this.getApp().repaint();
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
