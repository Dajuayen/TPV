/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author David
 */
public class Info implements Serializable{
    
    
    private HashMap<Integer, Vector> lineas; // Aqui se almacenan los productos pedidos

    public Info() {
        this.lineas = new HashMap<>();
    }

    public void rellenaDatos (JLabel label, DefaultTableModel modeloTabla ){
        System.out.println("Entramos a rellenar datos");
        Vector total = new Vector();
        total.add(label);
        this.getLineas().put(0, total);
        for (int i = 0; i<modeloTabla.getRowCount();i++){
             System.out.println(i);
            this.getLineas().put(i+1, (Vector) modeloTabla.getDataVector().elementAt(i));
             System.out.println(modeloTabla.getDataVector().elementAt(i).toString());
        }
    }
   
    public int size (){
        return this.getLineas().size();
    }
    public void vaciar (){
        this.getLineas().clear();
    }

   
    
    public HashMap<Integer, Vector> getLineas() {
        return lineas;
    }

    public void setLineas(HashMap<Integer, Vector> lineas) {
        this.lineas = lineas;
    }
    
    
    
    
}
