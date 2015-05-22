/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package central;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class StartInforme implements Runnable {

    private CTPV_Frame ctpv;

    private DatagramSocket escuchador;

    private TreeMap<String, Informe> registro;

    private byte[] buffer;
    private DatagramPacket datagrama;

    public StartInforme(CTPV_Frame ctpv) {
        this.ctpv = ctpv;
        this.registro = ctpv.getRegistro();

        this.buffer = new byte[10];
        this.datagrama = new DatagramPacket(buffer, 10);
        try {
            this.escuchador = new DatagramSocket(64000);

        } catch (SocketException ex) {
            Logger.getLogger(StartInforme.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {

        try {

            while (true) {

                String aux;
                System.out.println("Esperando para conexión");
                escuchador.receive(datagrama);
                aux = new String(datagrama.getData());

                aux = aux.trim();

                //Forma de cerrar un hilo Informe que esta corriendo
                if (aux.contains("f")) {
                    System.out.println("Pasamos el estado del Informe a false");
                    aux = (String) aux.subSequence(1, aux.length());
                    System.out.println(aux);
                    Informe temp = registro.get(aux);
                    temp.setEstado(false);
//                    registro.remove(aux);
                } else {
                    //Si comprobamos si ha sido antes registrado, su estado o lo registramos
                    if (!registro.containsKey(aux)) {//El puerto no esta registrado
                        System.out.println("Registramos el Informe");
                        System.out.println("Otro intento de conexion");
                        Informe informe = new Informe(ctpv, aux, datagrama.getAddress());
                        Thread hilo = new Thread(informe);
                        hilo.start();
                        registro.put(aux, informe);
                    } else {
                        //El puerto ya esta registrado
                        Informe temp = registro.get(aux);
                        if (!temp.isEstado()) {//Si es un hilo que se paro
                            System.out.println("Sustituimos el Informe del puerto registrado");
                            //Lo sustituimos por si ha cambiado la dirección IP
                            Informe informe = new Informe(ctpv, aux, datagrama.getAddress());
                            Thread hilo = new Thread(informe);
                            hilo.start();
                            registro.put(aux, informe);
                        }
                    }
                }
            }
        } catch (IllegalThreadStateException ex) {
            Logger.getLogger(StartInforme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StartInforme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("DatagramSocket StartInforme cerrado");
            escuchador.close();

        }
    }

//***************************************************************************
    public DatagramSocket getEscuchador() {
        return escuchador;
    }

    public CTPV_Frame getCtpv() {
        return ctpv;
    }

}
