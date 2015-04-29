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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author David
 */
public class Facturacion {

    private File fichero;
    private TreeMap<Integer, Factura> registro;

    public Facturacion(File fichero) throws FileNotFoundException, IOException {
        this.fichero = fichero;
        this.registro = new TreeMap<Integer, Factura>();
       
        
    }

    /**
     * Método que recibiendo como parametros un DeFaultTableModel de una compra y un string con su total
     * genera una Factura con la fecha actual del sistema, un número de factura, las distintas lineas de la compra
     * sacadas del DefaultTableModel y el total.
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
        //Introducimos el numero de la factura
        //leerFichero();
        int num = 1 + this.getRegistro().size();
        aux.setNumFactura(num);

        return aux;

    }

    /**
     * Método que recibe una Factura como parametro y la guarda tanto en la coleccion interna de la clase Facturación
     * como en el fichero introducido en el atributo fichero.
     * 
     * @param factura
     * @throws IOException 
     */
    public void guardarFactura(Factura factura) throws IOException {
        
        ObjectOutputStream out = null;
        out = new ObjectOutputStream(new FileOutputStream(fichero,true));
        
        //this.getRegistro().put(factura.getNumFactura(), factura);
        
        out.writeUnshared(factura);
        out.flush();
        
        out.close();
    }

    public int leerFichero() throws IOException, ClassNotFoundException {
        int totalCompras = 0;
        FileInputStream fileIn = null;
        ObjectInputStream in = null;

        //Como vamos a volcar los datos del fichero en la colección, si tiene datos los borramos
        if (!this.getRegistro().isEmpty()) {
            this.getRegistro().clear();
        }

        try {
//            System.out.println(this.getFichero().getName());
//            System.out.println(this.getFichero().getPath());
//            String file = fichero.getAbsolutePath();
//            System.out.println(file);

            fileIn = new FileInputStream(fichero);
            in = new ObjectInputStream(fileIn);

            Factura aux = new Factura();
            while (true) {
                aux = (Factura) in.readObject();
                in.reset();
                System.out.println(aux.toString());
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
    
    /**
     * Muestra por consola las distintas facturas registradas en la coleccion 
     * interna de la clase Facturacion
     */
    public void mostrarFacturacion(){
        
      for(Factura factura : this.getRegistro().values()){
          System.out.println("Número de factura : "+factura.getNumFactura());
          System.out.println("Fecha de compra : "+factura.getFecha());
          System.out.println("-----------------------------------------");
          System.out.println("NºLinea | Producto                        | Cantidad | Precio ");
          for (String linea: factura.getLineas()) {
              System.out.println("-----------------------------------------");
              System.out.println(linea);
          }
          System.out.println("-----------------------------------------");
          System.out.println("                    Total : "+factura.getTotal());
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
