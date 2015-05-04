/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package central;

import com.sun.jmx.snmp.BerDecoder;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import datos.Info;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
                    guardarVenta();
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

    /**
     * Método sincronizado que guarda en el fichero del objeto CTPV_Frame
     * la compra llevada a cabo en el terminal que controla el hilo del objeto Venta.
     * 
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private synchronized void guardarVenta() throws IOException, ClassNotFoundException {
        double total = 0.0;
        DecimalFormat decimales;
        decimales = new DecimalFormat("0.00");
        StringBuilder contenido = new StringBuilder();
        //Cierro el socket
        this.getMiSocket().close();
        
        //Coloco la fecha formateada para distinguir las compras
        SimpleDateFormat fecha = new SimpleDateFormat("HH:mm:ss EEEE d MMMM yyyy");        
        this.getApp().getOut().println(fecha.format(new Date()));
        this.getApp().getOut().flush();
        
        //cogo las lineas de la compra
        for (int i = 0; i < this.getTerminal().getModeloTabla().getRowCount(); i++) {
            Vector linea = (Vector) this.getTerminal().getModeloTabla().getDataVector().get(i);
            if (contenido.length() > 0) {
                contenido.delete(0, contenido.length());
            }
            contenido.append(String.valueOf(i + 1));
            System.out.println(contenido.toString());
            for (int j = 0; j < linea.size(); j++) {
                contenido.append("   ||   ");
                contenido.append(linea.get(j));
                if (j == 0) {
                    contenido.setLength(40);
                }
                if (j == 2) {
                    total = total + Double.parseDouble((String) linea.get(j));
                }
            }
            
            //Ecribimos en el archivo las linea de la compra
            this.getApp().getOut().println(contenido.toString());
            this.getApp().getOut().flush();

        }
        //Escribimos el total
        this.getApp().getOut().println("                                           Total : " + decimales.format(total) + " €");
        this.getApp().getOut().flush();
        //Escribimos el cierre de la compra 
        this.getApp().getOut().println("***********************************************************");
        this.getApp().getOut().flush();

        this.getTerminal().compraFinalizada();//Muestro la ventana emergente

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
