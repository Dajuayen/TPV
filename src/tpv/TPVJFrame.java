/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpv;

import datos.Info;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
// Clase frame
public class TPVJFrame extends JFrame {

    //----------CONSTANTES
    private static final Color AZUL_CLARO = new Color(169, 198, 218);
    private static final Color AZUL_OSCURO = new Color(53, 79, 98);
    //--------- CAMPOS
    private JPanel jPanelTPV; //EL panel donde se encuentran todos los elementos
    private DefaultTableModel modeloTabla; //Modelo de la tabla que contiene la factura
    private JLabel jLabelTotal; //JLabel donde se muestra el total de la factura
    private HashMap<String, ProductoPedido> listaPedidos; // Aqui se almacenan los productos pedidos
    private JPanel jPanelListaProductos; // Panel donde van apareciendo los productos de las distintas familias
    private JTable tabla;
    //----------- Socket cliente
    private Socket cliente;
    private ObjectOutputStream out;
    private Info info;

    //---------- CONSTRUCTOR
    /**
     * Crea una vista del TPV, iniciando todos sus componentes.
     */
    public TPVJFrame() {
        super("TPV");
        //this.setUndecorated(true);
        crearVentana();
        
        this.info = new Info();
        
        try {
            this.cliente = new Socket("localhost", 65000);
            this.out = new ObjectOutputStream(this.cliente.getOutputStream());
            DataInputStream in = new DataInputStream(this.cliente.getInputStream());
            String titulo = in.readUTF();
            if (titulo.equals("ocupado")) {
                System.out.println("Terminal no conectado a CTPV y finalizado");
                System.exit(0);
            } else {
                setTitle(titulo);
                setVisible(true);
            }
        } catch (IOException ex) {
            Logger.getLogger(TPVJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //----------METODOS
    /**
     * Crea la ventana de la aplicación
     */
    private void crearVentana() {
        jPanelTPV = new JPanel(new BorderLayout(10, 10));
        jPanelTPV.setBorder(new EmptyBorder(15, 15, 15, 15));
        jPanelTPV.setBackground(AZUL_CLARO);
        crearEncabezado();
        crearZonaProductos();
        crearZonaFactura();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(jPanelTPV);
        pack();
    }

    /**
     * Crea los componentes dentro del panel del encabezado, este se situa en la
     * zona norte del BorderLayout del JFrame
     */
    private void crearEncabezado() {
        JPanel jPanelEncabezado = new JPanel(new BorderLayout());
        jPanelEncabezado.setBackground(AZUL_CLARO);
        jPanelEncabezado.setBorder(new LineBorder(AZUL_OSCURO));

        JPanel jPanelIzquierdo = new JPanel(new FlowLayout(10));
        jPanelIzquierdo.setBackground(AZUL_CLARO);

        JButton jButtonAyuda = new JButton("Ayuda");
        jButtonAyuda.setBorder(null);
        jButtonAyuda.setContentAreaFilled(false);
        jButtonAyuda.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAyuda.setIconTextGap(-3);
        jButtonAyuda.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAyuda.setIcon(new ImageIcon("..\\TPV\\src\\imagenes\\Menus\\Ayuda2.png"));
        jButtonAyuda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null, "PO Interfaz grafica de un TPV",
                        "Ayuda", JOptionPane.INFORMATION_MESSAGE);
            }
        });;
        jPanelIzquierdo.add(jButtonAyuda);

        JButton jButtonSalir = new JButton("Salir");
        jButtonSalir.setBorder(null);
        jButtonSalir.setContentAreaFilled(false);
        jButtonSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSalir.setIconTextGap(-3);
        jButtonSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSalir.setIcon(new ImageIcon("..\\TPV\\src\\imagenes\\Menus\\Salir2.png"));
        jButtonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    getInfo().getLineas().clear();
                    Vector vacio = new Vector();
                    getInfo().getLineas().put(-1, vacio);

                    getOut().writeUnshared(getInfo());
                    getOut().flush();

                    getOut().close();
                    getCliente().close();

                    System.exit(0);

                } catch (IOException ex) {
                    Logger.getLogger(TPVJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        jPanelIzquierdo.add(jButtonSalir);

        JPanel jPanelDerecho = new JPanel(new FlowLayout(10));
        jPanelDerecho.setBackground(AZUL_CLARO);

        JButton jButtonEspera = new JButton("Espera");
        jButtonEspera.setBorder(null);
        jButtonEspera.setContentAreaFilled(false);
        jButtonEspera.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonEspera.setIconTextGap(-3);
        jButtonEspera.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonEspera.setIcon(new ImageIcon("..\\TPV\\src\\imagenes\\Menus\\Espera2.png"));
        jButtonEspera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
            }
        });
        jPanelDerecho.add(jButtonEspera);

        JButton jButtonEliminar = new JButton("Eliminar");
        jButtonEliminar.setBorder(null);
        jButtonEliminar.setContentAreaFilled(false);
        jButtonEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonEliminar.setIconTextGap(-3);
        jButtonEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonEliminar.setIcon(new ImageIcon("..\\TPV\\src\\imagenes\\Menus\\Eliminar.png"));
        jButtonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                eliminar();
            }
        });
        jPanelDerecho.add(jButtonEliminar);

        JButton jButtonSubTotal = new JButton("SubTotal");
        jButtonSubTotal.setBorder(null);
        jButtonSubTotal.setContentAreaFilled(false);
        jButtonSubTotal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSubTotal.setIconTextGap(-3);
        jButtonSubTotal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSubTotal.setIcon(new ImageIcon("..\\TPV\\src\\imagenes\\Menus\\SubTotal.png"));
        jButtonSubTotal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
            }
        });
        jPanelDerecho.add(jButtonSubTotal);

        JButton jButtonTotal = new JButton("Total");
        jButtonTotal.setBorder(null);
        jButtonTotal.setContentAreaFilled(false);
        jButtonTotal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonTotal.setIconTextGap(-3);
        jButtonTotal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonTotal.setIcon(new ImageIcon("..\\TPV\\src\\imagenes\\Menus\\Total.png"));
        jButtonTotal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
            }
        });
        jPanelDerecho.add(jButtonTotal);

        jPanelEncabezado.add(jPanelIzquierdo, BorderLayout.WEST);
        jPanelEncabezado.add(jPanelDerecho, BorderLayout.EAST);
        jPanelTPV.add(jPanelEncabezado, BorderLayout.NORTH);
    }

    /**
     * Crea el panel de la zona de productos, esta se situa en la zona center
     * del BorderLayout del JFrame
     */
    private void crearZonaProductos() {
        //Panel que alberga los dos sub-paneles de fomilias y productos
        JPanel jPanelZonaProductos = new JPanel(new BorderLayout(10, 10));
        jPanelZonaProductos.setBackground(AZUL_CLARO);

        //Panel que alberga los productos de la familia.
        jPanelListaProductos = new JPanel(new GridLayout(0, 5, 5, 5));
        jPanelListaProductos.setBorder(new BevelBorder(WIDTH));
        jPanelListaProductos.setBackground(AZUL_OSCURO);

        //Sub-panel con las familias de productos
        JPanel jPanelFamilias = new JPanel(new GridLayout(0, 4, 5, 5));
        jPanelFamilias.setBorder(new BevelBorder(WIDTH));
        jPanelFamilias.setBackground(AZUL_OSCURO);

        //Añadimos dinamicamente las familias de productos
        ArrayList<Familia> familias = LeerFamilias.leerFamilias(this);
        for (Familia familia : familias) {
            final Familia nuevaFamilia = familia;
            nuevaFamilia.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nuevaFamilia.mostrarProductos();
                }
            });
            jPanelFamilias.add(familia);
        }
        familias.get(5).mostrarProductos();
        jPanelZonaProductos.add(jPanelFamilias, BorderLayout.NORTH);
        jPanelZonaProductos.add(jPanelListaProductos, BorderLayout.CENTER);
        jPanelTPV.add(jPanelZonaProductos, BorderLayout.CENTER);
    }

    /**
     * Crea la zona con la factura. Esto incluye la tabla con los articulos
     * pedidos, el resultado total y el teclado para introducir otros productos.
     */
    private void crearZonaFactura() {
        listaPedidos = new HashMap<>();

        //Panel donde se introducen los dos sub-paneles el de la tabal y el de el teclado
        JPanel jPanelFactura = new JPanel(new BorderLayout());

        //Sub-panel de la tabla de productos solicitados
        JPanel jPanelTicket = new JPanel(new BorderLayout());

        modeloTabla = new DefaultTableModel();

        //Añadimos las columnas a la tabla
        modeloTabla.addColumn("Productos");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Sub-total");

        tabla = new JTable(modeloTabla); // Añadimos el modelo a la tabla
        //Panel deslizante por si la tabla contiene muchos elementos
        JScrollPane jPanelScroll = new JScrollPane();
        jPanelScroll.setViewportView(tabla); // Metemos tabla en el panel
        jPanelTicket.add(jPanelScroll);
        jPanelFactura.add(jPanelTicket, BorderLayout.NORTH);

        //Sub-panel total de la factura
        JPanel jPanelTotal = new JPanel(new FlowLayout());
        jPanelTotal.setBackground(AZUL_CLARO);
        JLabel jLabelTotalTexto = new JLabel("Total: ");
        jLabelTotalTexto.setFont(new Font("Tahoma", 0, 30));
        jLabelTotal = new JLabel("0");
        jLabelTotal.setFont(new Font("Tahoma", 0, 30));
        jPanelTotal.add(jLabelTotalTexto);
        jPanelTotal.add(jLabelTotal);
        jPanelFactura.add(jPanelTotal, BorderLayout.CENTER);

        JButton jButtonOtros = new JButton("Otros Productos");
        jButtonOtros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                crearCalculadora();
            }
        });;
        jPanelFactura.add(jButtonOtros, BorderLayout.SOUTH);
        jPanelTPV.add(jPanelFactura, BorderLayout.EAST);
    }

    /**
     * Muestra el panel de la familia de productos seleccionada
     *
     * @param productos
     */
    public void mostrarPanel(ArrayList<Producto> productos) {
        jPanelListaProductos.removeAll();

        for (Producto producto : productos) {
            jPanelListaProductos.add(producto);
        }
        jPanelListaProductos.updateUI();
        jPanelListaProductos.repaint();
    }

    /**
     * Añade un nuevo producto solicitado a la factura
     *
     * @param nombre Nombre del producto seleccionado
     * @param precio Precio de producto
     */
    public void insertarAFactura(String nombre, float precio) {
        int cantidad = 1;
        float total = precio;
        if (listaPedidos.containsKey(nombre)) {
            if (nombre.equals(nombre)) {
                total = listaPedidos.get(nombre).getTotal() + precio;
            }
            cantidad = listaPedidos.get(nombre).getCantidad() + 1;
        }
        ProductoPedido nuevoPedido;
        if (nombre.equals("Otros")) {
            nuevoPedido = new ProductoOtros(nombre, precio, cantidad, total);
        } else {
            nuevoPedido = new ProductoPedido(nombre, precio, cantidad);
        }
        listaPedidos.put(nombre, nuevoPedido);
        actualizarTabla();
        actualizarTotal();
        mandarInfo();
    }

    /**
     * Método que borra la tabla para rellenarla nuevamente con los datos
     * actualizados de la compra.
     *
     */
    public void actualizarTabla() {

        int a = modeloTabla.getRowCount() - 1;
        for (int i = a; i >= 0; i--) {
            modeloTabla.removeRow(i);
        }

        for (String string : listaPedidos.keySet()) {
            modeloTabla.addRow(listaPedidos.get(string).getProducto());
        }
    }

    /**
     * Método que suma el gasto en cada producto mostrando el total de la
     * compra.
     *
     */
    public void actualizarTotal() {
        float total = 0;
        for (String string : listaPedidos.keySet()) {
            total += listaPedidos.get(string).getTotal();
        }
        String val = total + "";
        BigDecimal big = new BigDecimal(val);
        big = big.setScale(2, RoundingMode.HALF_UP);
        jLabelTotal.setText("" + big);
    }

    public void mandarInfo() {
        if (this.getInfo().getLineas().size() > 0) {
            this.getInfo().getLineas().clear();
            System.out.println("Ha vaciado los datos de info");
        }

        this.getInfo().rellenaDatos(jLabelTotal, modeloTabla);

        try {

            System.out.println("Tamaño de lo mandado = " + this.getInfo().getLineas().size());
            //this.getOut().writeObject(this.getInfo());
            this.getOut().writeUnshared(this.getInfo());
            this.getOut().flush();
            this.getOut().reset();

        } catch (IOException ex) {
            Logger.getLogger(TPVJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void eliminar() {
        int[] indices = tabla.getSelectedRows();
        for (int i = 0; i < indices.length; i++) {
            listaPedidos.remove((String) modeloTabla.getValueAt(indices[i], 0));
        }
        actualizarTabla();
        actualizarTotal();
        mandarInfo();
    }

    private void crearCalculadora() {
        Calculadora calculadora = new Calculadora(this);
    }

    public static void main(String[] args) {
        TPVJFrame ventana = new TPVJFrame();
    }

//*****************************************************************************
//Getters & Setters
    public Socket getCliente() {
        return cliente;
    }

    public void setCliente(Socket cliente) {
        this.cliente = cliente;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

}
