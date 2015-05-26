/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mv;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class Conexion implements Runnable {

    private DatagramSocket conector;

    private DatagramPacket datagrama;

    private String info;

    private boolean estado;

    public Conexion(int puerto, String direccion, int puertoB) throws SocketException, UnknownHostException {
        this.conector = new DatagramSocket();

        this.info = String.valueOf(puertoB);

        this.datagrama = new DatagramPacket(info.getBytes(), info.getBytes().length, InetAddress.getByName(direccion), puerto);

        this.estado = true;
    }

    @Override
    public void run() {
        try {
            while (estado) {

                conector.send(datagrama);

                Thread.sleep(500);
            }

            String fin = info+"f" ;
            
            DatagramPacket cierre = new DatagramPacket(fin.getBytes(), fin.getBytes().length, datagrama.getAddress(), datagrama.getPort());
            setDatagrama(cierre);
            System.out.println(fin);
            
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//            String fin = "f" + info;
//            DatagramPacket cierre = new DatagramPacket(fin.getBytes(), fin.getBytes().length, datagrama.getAddress(), datagrama.getPort());
//            setDatagrama(cierre);
            System.out.println("DatagramSocket Conexión cerrado");
            conector.close();
        }
    }

    public DatagramPacket getDatagrama() {
        return datagrama;
    }

    public void setDatagrama(DatagramPacket datagrama) {
        this.datagrama = datagrama;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

}
