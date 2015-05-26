/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package central;

import datos.Info;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author David
 */
public class Venta extends Thread {

    private Vector columnas;
    private CTPV_Frame app;
    private Terminal_Frame terminal;

    private Socket miSocket;
    private ObjectInputStream in;

    /**
     * Constructor que recibe el Socket de comunicación entre el TPV y el
     * servidor, el objeto CTPV_Frame, y el indice el terminal.
     *
     * Inicializa también el flujo de comunicación y el vector con los nombres
     * de las columnas de la tabla.
     *
     * @param miSocket
     * @param app
     * @param index
     */
    public Venta(Socket miSocket, CTPV_Frame app, int index) {
        this.app = app;
        this.terminal = app.getLista()[index];
        this.terminal.setVisible(true);

        this.miSocket = miSocket;
        try {
            this.in = new ObjectInputStream(miSocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Venta.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.columnas = new Vector();
        this.columnas.addElement("Productos");
        this.columnas.addElement("Cantidad");
        this.columnas.addElement("Sub-total");

    }

    /**
     * Método del arranca el hilo mientras este el socket no este cerrado, que
     * se encargará de la comunicación con el TPV.
     *
     * Si recibe datos los mostrará en el TPV del internal frame correspondiente
     * y sino cerrará la comunicación y guardará la compra.
     */
    @Override
    public void run() {
        try {
            while (!this.getMiSocket().isClosed()) {

                Object obj = this.getIn().readObject();
                System.out.println("entro");
                //Compruevo el Objeto que me ha llegado, defende lo que contenga relleno el terminal o cierro comunicacion
                if (rellenarTerminal(obj)) {
                    this.terminal.repaint();
                } else {
                    this.getApp().guardarVenta(getMiSocket(), getTerminal());
                }

            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Venta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("Error generico");
            Logger.getLogger(Venta.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                this.getIn().close();
                if (!this.getMiSocket().isClosed()) {
                    this.getMiSocket().close();
                }
                this.getTerminal().reset();
                this.getApp().borrarTerminal(getTerminal());
            } catch (IOException ex) {
                Logger.getLogger(Venta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Método que recibe un objeto como parametro, es casteado a Info que es una
     * Map, si contiene la clave -1 es que se ha cerrado el TPV y por lo tanto
     * devuelve False. Sino contiene la clave -1 borra los datos del TPV
     * internal frame, introduce el total, introduce los datos que recibe y
     * devuelve true. *
     *
     * @return boolean
     */
    private boolean rellenarTerminal(Object obj) {
        boolean b;

        Info aux = (Info) obj;

        if (aux.getLineas().containsKey(-1)) {
            b = false;
        } else {

            //Borramos los datos del terminal      
            this.getTerminal().vaciar();

            //En el indice 0 del objeto dato esta la label con el total de la factura
            Vector vTotal = aux.getLineas().get(0);
            JLabel total = (JLabel) vTotal.elementAt(0);
            this.getTerminal().getjLabelTotal().setText(total.getText() + " €");

            //Recorremos los datos de la tabla origen y los copiamos a la de destino
            for (int i = 1; i < aux.getLineas().size(); i++) {

                Vector linea = (Vector) aux.getLineas().get(i);

                System.out.println(linea.toString());

                this.getTerminal().getModeloTabla().addRow(linea);

            }

            this.getTerminal().getjTableLineasCompra().removeAll();
            this.getTerminal().getjTableLineasCompra().setModel(this.getTerminal().getModeloTabla());
            this.getTerminal().getjTableLineasCompra().repaint();

            b = true;
        }
        return b;
    }

    
//*******************************************************************************
//GETTERS & SETTERS

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public Terminal_Frame getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal_Frame terminal) {
        this.terminal = terminal;
    }

    public Vector getColumnas() {
        return columnas;
    }

    public Socket getMiSocket() {
        return miSocket;
    }

    public CTPV_Frame getApp() {
        return app;
    }

    public void setApp(CTPV_Frame app) {
        this.app = app;
    }

}
