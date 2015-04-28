/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package central;

import datos.Factura;
import java.io.EOFException;
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
    
    private JLabel total;
    private StringBuilder linea;

    public Facturacion(File fichero) throws FileNotFoundException, IOException {
        this.fichero = fichero;             
        this.registro = new TreeMap<Integer, Factura>();
        this.linea = new StringBuilder();
        this.factura = new Factura();
    }

    public void rellenarFactura(DefaultTableModel modeloTabla, String total) throws IOException, ClassNotFoundException {

        //Introducimos primero los datos que ya tenemos
        this.getFactura().setFecha(new Date());
        this.getFactura().setTotal(total);

        //Recorremos el modeloTabla y vamos introduciendo los datos en las lineas de la factura        
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if (linea.length() > 0) {
                linea.delete(0, linea.length());
            }
            Vector vector = (Vector) modeloTabla.getDataVector().get(i);
            linea.append("" + (i + 1));
            for (int j = 0; j < vector.size(); j++) {
                linea.append("   ||   ");
                linea.append(vector.get(j));
                if (j == 0) {
                    linea.setLength(40);
                }
            }
            System.out.println(linea.toString());
            this.getFactura().getLineas().add(linea.toString());

        }
        //Introducimos el numero de la factura
        //leerFichero();
        int num = 1 + this.getRegistro().size();
        this.getFactura().setNumFactura(num);

    }

    public int leerFichero() throws IOException, ClassNotFoundException {
        int totalCompras = 0;
        FileInputStream fileIn = null;
        ObjectInputStream in = null;

        if (!this.getRegistro().isEmpty()) {
            this.getRegistro().clear();
        }

        try {
            System.out.println(this.getFichero().getName());
            System.out.println(this.getFichero().getPath());
            String file = fichero.getAbsolutePath();
            System.out.println(file);
            
            fileIn = new FileInputStream(file);
            in = new ObjectInputStream(fileIn);

            Factura aux = new Factura();
            while (true) {
                aux = (Factura) in.readObject();
                totalCompras++;
                this.getRegistro().put(totalCompras, aux);
            }
        } catch (FileNotFoundException e) {
            fileIn.close();
            in.close();
            return totalCompras;
        } catch (EOFException e) {
            fileIn.close();
            in.close();
            return totalCompras;
        }

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

    public TreeMap<Integer, Factura> getRegistro() {
        return registro;
    }

    public void setRegistro(TreeMap<Integer, Factura> registro) {
        this.registro = registro;
    }

}
