/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author David
 */
public class Factura implements Serializable {

    private Date fecha;
    private int numFactura;
    private ArrayList<StringBuilder> lineas;
    private int total;

    public Factura(int numFactura, ArrayList<StringBuilder> lineas) {
        this.fecha = new Date();
        this.numFactura = numFactura;
        this.lineas = lineas;
        this.total = 0;
    }

    public Factura() {
        this.fecha = null;
        this.lineas = new ArrayList<>();

    }

    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha){
        this.fecha = fecha;
    }
    public int getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(int numFactura) {
        this.numFactura = numFactura;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<StringBuilder> getLineas() {
        return lineas;
    }

    public void setLineas(ArrayList<StringBuilder> lineas) {
        this.lineas = lineas;
    }

        @Override
    public String toString() {
        return fecha.toString() + "/n Número de factura : " + numFactura;

    }
}
