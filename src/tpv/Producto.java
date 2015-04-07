/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tpv;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Esta clase modela los productos que serviremos en nuetro negocio y cada uno de ellos
 * pertenecera a una familia de productos.
 * @author IVAN
 */
@SuppressWarnings("serial")
public class Producto extends JButton {
	//----------CAMPOS
	private final String nombre;    // Nombre del producto con el que se identificara en la factura
	private final float precio;     // Precio unitario del producto.
	private int unidadesStock;      // Sin implementar - Para llevar un contro de existencias. 
	private TPVJFrame tpv;          // El TPV al que pertenece el producto 

        //----------CONSTRUCTORES
	/**
	 * Constructor de un producto
	 * @param nombre El nombre del producto
	 * @param precio  El precio del producto
	 */
	public Producto(String nombre, float precio) {
		super(nombre);
		this.nombre = nombre;
		this.precio = precio;

	}
	
	/**
	 * Constructor de un producto
	 * @param nombre El nombre del producto
	 * @param imagen El icono del boton
	 * @param precio El precio del producto
	 */
	public Producto(String nombre,float precio, ImageIcon imagen) {
		super();
                setToolTipText(nombre);
                this.nombre=nombre;
		this.precio = precio;
                
                //Evitamos que la imagen salga rodeada por el espacio en blanco del boton
                setBorder(null);
                setBorderPainted(false);
                setContentAreaFilled(false);
                setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                setIconTextGap(-3);
                setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		setIcon(imagen);
	}
	
        //----------METODOS
	/**
         * Añade el producto a la factura
         * @param producto El producto a añadir a la factura 
         */
	public void añadirAFactura(Producto producto) {
		tpv.añadirAFactura(producto.getNombre(), producto.getPrecio());
		actualizarTabla();
		actualizarTotal();
                tpv.mandarInfo();
	}
	
	/**
	 * Actualiza la tabla tras añadir un producto o eliminarlo
	 */
	private void actualizarTabla(){
		tpv.actualizarTabla();
	}
	/**
         * Actualiza el total de la factura tras añadir un producto o eliminarlo
         */
	private void actualizarTotal (){
		tpv.actualizarTotal();
	}

        /**
         * Retorna el nombre del producto
         * @return El nombre
         */
	public String getNombre() {
		return nombre;
	}
        /**
         * Retorna lo que cuesta el producto
         * @return El precio unitario del producto
         */
	public float getPrecio() {
		return precio;
	}
        /**
         * Permite asignar un TPV al producto
         * @param tpv El TPV que se desa asignar al producto
         */
	public void setTpv(TPVJFrame tpv) {
		this.tpv = tpv;
	}
}

