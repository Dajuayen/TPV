/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctpv;

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
    private Terminal_Frame terminal;

    private Socket miSocket;
    private ObjectInputStream in;

    public Venta(Socket miSocket, Terminal_Frame terminal) {
        this.terminal = terminal;
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

    @Override
    public void run() {
        try {
            
            
            //while (this.getMiSocket().isConnected()) {
            while (true) {
               // if (!this.getMiSocket().isClosed()) {
                Object obj = this.getIn().readObject();
                System.out.println("entro");
                
                rellenarTerminal(obj);
              
                this.terminal.repaint();
//                }else
//                    this.getTerminal().getjLabelFinal().setVisible(true);
//                    Thread.sleep(3000);
//                    
//                    this.getTerminal().reset();
//                    break;
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Venta.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                this.getIn().close();
                Thread.sleep(3000);
            } catch (IOException ex) {
                Logger.getLogger(Venta.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Venta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     *
     * @return
     */
    private void rellenarTerminal(Object obj) {
        Info aux = (Info) obj;
        System.out.println("Tamaño de lo recibido = "+ aux.size());
        Vector vTotal = aux.getLineas().get(0);
        JLabel total = (JLabel) vTotal.elementAt(0);
        this.getTerminal().getjLabelTotal().setText(total.getText());
        
        //DefaultTableModel modeloTabla = (DefaultTableModel) aux.get(0); //Modelo de la tabla que contiene la factura
        
        //Borramos los datos de la tabla
        if (this.getTerminal().getModeloTabla().getRowCount() != 0) {
            for (int i = 0; i < this.getTerminal().getModeloTabla().getRowCount(); i++) {
                this.getTerminal().getModeloTabla().removeRow(i);
            }
        }
        
    
        
        //Recorremos los datos de la tabla origen y los copiamos a la de destino
        for (int i = 1; i < aux.size(); i++) {
            
                Vector linea = (Vector) aux.getLineas().get(i);
                
                System.out.println(linea.toString());
                
                this.getTerminal().getModeloTabla().addRow(linea);
                
            
        }
        this.getTerminal().getjTableLineasCompra().removeAll();
        this.getTerminal().getjTableLineasCompra().setModel(this.getTerminal().getModeloTabla());
        this.getTerminal().getjTableLineasCompra().repaint();
        
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

    
}
