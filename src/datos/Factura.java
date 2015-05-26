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
    private ArrayList<String> lineas;
    private String total;

    public Factura(int numFactura, ArrayList<String> lineas) {
        this.fecha = new Date();
        this.numFactura = numFactura;
        this.lineas = lineas;
        this.total = "";
    }

    public Factura() {
        this.fecha = null;
        this.lineas = new ArrayList<>();

    }

    //**************************************************************************
    //GETTERS & SETTERS
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(int numFactura) {
        this.numFactura = numFactura;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<String> getLineas() {
        return lineas;
    }

    public void setLineas(ArrayList<String> lineas) {
        this.lineas = lineas;
    }

    @Override
    public String toString() {
        StringBuilder aux = new StringBuilder();
        aux.append("Número de factura : " + this.getNumFactura() + "\n");
        aux.append("Fecha de compra : " + this.getFecha()+ "\n");
        aux.append("-----------------------------------------"+ "\n");
        aux.append("NºLinea | Producto                        | Cantidad | Precio "+ "\n");
        for (String linea : this.getLineas()) {
            aux.append("-----------------------------------------"+ "\n");
            aux.append(linea+ "\n");
        }
        aux.append("-----------------------------------------"+ "\n");
        aux.append("                    Total : " + this.getTotal()+ "\n");

        return aux.toString();

    }
}
