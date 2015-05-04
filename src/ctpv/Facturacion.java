/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctpv;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author David
 */
public class Facturacion {

    private File fichero;
    private TreeMap<Integer, Factura> registro;

    private ObjectOutputStream out = null;

    private ObjectInputStream in = null;

    public Facturacion(File fichero) throws FileNotFoundException, IOException {
        this.fichero = fichero;
        this.fichero.setWritable(true);
        this.registro = new TreeMap<Integer, Factura>();
        this.out = new ObjectOutputStream(new FileOutputStream(fichero, true));
        this.in = new ObjectInputStream(new FileInputStream(fichero));

    }

    /**
     * Método que recibiendo como parametros un DeFaultTableModel de una compra
     * y un string con su total, genera una Factura con la fecha actual del
     * sistema, un número de factura, las distintas lineas de la compra sacadas
     * del DefaultTableModel y el total.
     *
     * @param modeloTabla
     * @param total
     * @return Factura
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Factura rellenarFactura(DefaultTableModel modeloTabla, String total) throws IOException, ClassNotFoundException {
        Factura aux = new Factura();
        StringBuilder linea = new StringBuilder();

        //Introducimos primero los datos que ya tenemos
        aux.setFecha(new Date());
        aux.setTotal(total);

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
            aux.getLineas().add(linea.toString());

        }

        int num = 1 + this.getRegistro().size();
        aux.setNumFactura(num);

        return aux;

    }

    /**
     * Método que recibe una Factura como parametro y la guarda tanto en la
     * coleccion interna de la clase Facturación como en el fichero introducido
     * en el atributo fichero.
     *
     * @param factura
     * @throws IOException
     */
    public void guardarFactura(Factura factura) throws IOException {

        out.writeUnshared(factura);
        out.flush();

    }

    /**
     * 
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void leerFichero() throws IOException, ClassNotFoundException {
        int totalCompras = 0;

        //Como vamos a volcar los datos del fichero en la colección, si tiene datos los borramos
        if (!this.getRegistro().isEmpty()) {
            this.getRegistro().clear();
        }
        
        long tamanioFichero = fichero.length();//Longitud del fichero

        //Si el tamaño del fichero es mayor a 0, es decir si no esta vacio
        if (tamanioFichero > 0) {

            try {

                Factura aux = new Factura();
                while (true) {
                    aux = (Factura) in.readUnshared();

                    System.out.println(aux.toString());
                    totalCompras++;
                    this.getRegistro().put(totalCompras, aux);
                }

            } catch (EOFException e) {
                System.out.println("Final del archivo");

            } catch (Exception edfdf) {
                System.out.println(edfdf.getMessage());
            }
         
        }
    }

    /**
     * Muestra por consola las distintas facturas registradas en la coleccion
     * interna de la clase Facturacion
     */
    public void mostrarFacturacion() {

        for (Factura factura : this.getRegistro().values()) {
            System.out.println("Número de factura : " + factura.getNumFactura());
            System.out.println("Fecha de compra : " + factura.getFecha());
            System.out.println("-----------------------------------------");
            System.out.println("NºLinea | Producto                        | Cantidad | Precio ");
            for (String linea : factura.getLineas()) {
                System.out.println("-----------------------------------------");
                System.out.println(linea);
            }
            System.out.println("-----------------------------------------");
            System.out.println("                    Total : " + factura.getTotal());
        }

    }

    /**
     * Método que cierra los canales de salida y entrada 
     * y marca el fichero como solo lectura
     */
    public void cerrarFacturacion(){
        try {
            this.fichero.setWritable(false);
            this.in.close();
            this.out.close();
        } catch (IOException ex) {
            Logger.getLogger(Facturacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    //**************************************************************************
    //GETTERS & SETTERS
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
