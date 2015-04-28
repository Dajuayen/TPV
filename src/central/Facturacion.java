/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package central;

import datos.Factura;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author David
 */
public class Facturacion {

    private Factura factura;

    private File fichero;

    private TreeMap<Integer, Factura> registro;

    private DefaultTableModel modeloTabla;

    private JLabel total;

    public Facturacion(File fichero, DefaultTableModel modeloTabla, JLabel total) throws FileNotFoundException, IOException {
        this.fichero = fichero;
        this.modeloTabla = modeloTabla;
        this.total = total;

        this.factura = new Factura();

    }

    public void rellenarFactura() throws IOException {
        //Introducimos primero los datos que ya tenemos
        this.getFactura().setFecha(new Date());
        this.getFactura().setTotal(Integer.parseInt(this.getTotal().getText()));
        //Recorremos el modeloTabla y vamos introduciendo los datos en las lineas de la factura
        for (int i = 0; i < this.getModeloTabla().getRowCount(); i++) {
            Vector vector = (Vector) this.getModeloTabla().getDataVector().get(i);
            StringBuilder linea = new StringBuilder();
            linea.append("" + (i + 1));
            for (int j = 2; j < 3; j++) {
                linea.append("   ||   ");
                linea.append(vector.get(j));
            }
            this.getFactura().getLineas().add(linea);
        }
        //Introducimos el numero de la factura
        this.getFactura().setNumFactura(leerFichero() + 1);

    }

    public int leerFichero() throws IOException {
        int totalCompras = 0;
        ObjectInputStream in = null;
        this.getRegistro().clear();
        try {
            in = new ObjectInputStream(new FileInputStream(fichero));
            Factura aux = new Factura();
            while (true) {
                aux = (Factura) in.readObject();
                totalCompras++;
                this.getRegistro().put(totalCompras, aux);
            }
        } catch (IOException | ClassNotFoundException e) {
            in.close();
        }
        return totalCompras;

    }

    public boolean guardarFactura() throws IOException {
        ObjectOutputStream out = null;
        out = new ObjectOutputStream(new FileOutputStream(fichero));
        out.writeUnshared(this.getFactura());
        return false;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public File getFichero() {
        return fichero;
    }

    public void setFichero(File fichero) {
        this.fichero = fichero;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public void setModeloTabla(DefaultTableModel modeloTabla) {
        this.modeloTabla = modeloTabla;
    }

    public JLabel getTotal() {
        return total;
    }

    public void setTotal(JLabel total) {
        this.total = total;
    }

    public TreeMap<Integer, Factura> getRegistro() {
        return registro;
    }

    public void setRegistro(TreeMap<Integer, Factura> registro) {
        this.registro = registro;
    }

}
