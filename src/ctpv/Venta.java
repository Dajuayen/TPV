/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctpv;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

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
            
            
            while (this.getMiSocket().isConnected()) {
                
                
                Object obj = this.getIn().readObject();
                System.out.println("entro");
                
                rellenarTerminal(obj);
                //this.getIn().close();
                this.terminal.repaint();

               // this.getIn().close();
            }

        } catch (IOException ex) {
            Logger.getLogger(Venta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
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
        ArrayList aux = (ArrayList) obj;

        DefaultTableModel modeloTabla = (DefaultTableModel) aux.get(0); //Modelo de la tabla que contiene la factura
        //Borramos los datos de la tabla
        if (this.getTerminal().getModeloTabla().getRowCount() != 0) {
            for (int i = 0; i < this.getTerminal().getModeloTabla().getRowCount(); i++) {
                this.getTerminal().getModeloTabla().removeRow(i);
            }
        }
        //Recorremos los datos de la tabla origen y los copiamos a la de destino
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            for (int j = 0; j < 3; j++) {
                String contenido = (String) modeloTabla.getValueAt(1, j);
                System.out.println(contenido);
                this.getTerminal().getModeloTabla().setValueAt(contenido, i, j);
            }
            
        }
            
            
            
       // this.getTerminal().getModeloTabla().setDataVector(modeloTabla.getDataVector(), this.getColumnas());

        JLabel total = (JLabel) aux.get(1);
        this.getTerminal().getjLabelTotal().setText(total.getText());
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
