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
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class Conexion implements Runnable {

    private InetAddress direccionCTPV;
    private int puertoCTPV;

    private DatagramSocket receptor;

    private DatagramPacket datagrama;
    private static final int MAX_LEN = 128;
    private byte[] buffer;

    private MV mv;

    public Conexion(int puerto, String direccion, MV mv) throws SocketException, UnknownHostException {
        this.receptor = new DatagramSocket();
        this.puertoCTPV = puerto;
        this.direccionCTPV = InetAddress.getByName(direccion);
        this.buffer = new byte[MAX_LEN];
        this.datagrama = new DatagramPacket(buffer, MAX_LEN);
        this.mv = mv;
    }

    @Override
    public void run() {
        try {
            String aux = "a";
            DatagramPacket clave = new DatagramPacket(aux.getBytes(), aux.getBytes().length, direccionCTPV, puertoCTPV);
        //StringBuilder resultados = new StringBuilder(MAX_LEN);
            System.out.println("Enviando: "+new String(clave.getData()));
            receptor.send(clave);
            System.out.println("Enviada la clave de conexión");
            while (true) {
                System.out.println("Esperando paquete del CTPV");
                receptor.receive(datagrama);
                System.out.println("Paquete recibido");
                StringTokenizer paquete = new StringTokenizer(new String(datagrama.getData()),";");               
                
                mv.getjTextFieldLineas().setText(paquete.nextToken());
                mv.getjTextFieldMananas().setText(paquete.nextToken());
                mv.getjTextFieldTardes().setText(paquete.nextToken());

            }
            
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            receptor.close();
            System.out.println("DatagramSocket Conexión cerrado");
        }
    }

}
