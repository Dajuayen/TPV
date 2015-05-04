/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpv;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Esta clas emodela un articulo que a sido pedido y añadido a la factura
 * @author IVAN
 */
public class ProductoPedido {
    //----------CAMPOS
    private String nombre;
    private float precio;
    private int cantidad;

    //----------CONSTRUCTOR
    /**
     * Permite crear un producto para añadirlo a la factura
     * @param nombre    El nombre que identifica al producto
     * @param precio    El precio unitario del producto
     * @param cantidad  La cantidad de unidades pedidas del producto
     */
    public ProductoPedido(String nombre, float precio, int cantidad) {
        super();
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    //----------METODOS
    public String getNombre(){
        return nombre;
    } 
    
    /**
     * Retorna la cantidad de productos pedidos
     * @return La cantidad de unidades
     */
    public int getCantidad() {
        return cantidad;
    }
    
    /**
     * Retorna un array de string para poder introducirlo en la tabla de la factura
     * @return Un array formado por el nombre, la cantidad y el sub-total
     */
    public String[] getProducto() {
        String[] producto = {nombre, "" + cantidad, redondear(cantidad * precio)};
        return producto;
    }
    
    /**
     * Retorna el valor total de las unidades pedidas
     * @return El preci de todas las unidades pedidas
     */
    public float getTotal() {
        return cantidad * precio;
    }
    
    public String redondear(float num){
      String val = num +"";
      BigDecimal big = new BigDecimal(val);
      big = big.setScale(2, RoundingMode.HALF_UP);
      return "" + big;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
}
