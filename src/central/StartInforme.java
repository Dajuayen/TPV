/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package central;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class StartInforme implements Runnable {

    private CTPV_Frame ctpv;

    private DatagramSocket escuchador;

    private DatagramPacket datagrama;
    private static final int MAX_LEN = 10;
    private byte[] buffer;

    private boolean activo;

    //private Informador informador;
    public StartInforme(CTPV_Frame ctpv) {
        this.ctpv = ctpv;
        this.buffer = new byte[MAX_LEN];

        //this.informador = new Informador(ctpv, escuchador);
        try {
            this.escuchador = new DatagramSocket(64000);
            this.datagrama = new DatagramPacket(buffer, MAX_LEN);
            this.activo = true;
        } catch (SocketException ex) {
            Logger.getLogger(StartInforme.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        System.out.println("El hilo del informe arrancó");

        try {
            System.out.println("Esperando para conexión");

            escuchador.receive(datagrama);
            String aux = new String(datagrama.getData());

            if (aux.trim().equals("a")) {
                System.out.println("Conexión realizada");
                int n = 0;
                while (true) {

                    String informe = recopilarDatos();
                    System.out.println("Información a enviar: " + informe);
                    DatagramPacket paqueteEnvio = new DatagramPacket(informe.getBytes(), informe.length(), datagrama.getAddress(), datagrama.getPort());
                    System.out.println("enviando:" + new String(paqueteEnvio.getData()));

                    escuchador.send(paqueteEnvio);
                    System.out.println("Paquete enviado");
                    Thread.sleep(2500);
                    //Thread.sleep(10*1000);
                }
            }
        } catch (IllegalThreadStateException ex) {
            Logger.getLogger(StartInforme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StartInforme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(StartInforme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            escuchador.close();
            System.out.println("DatagramSocket cerrado");
        }
    }

    private String recopilarDatos() throws FileNotFoundException, IOException {
        String aux;
        int contador = 0;
        double mañana = 0, tarde = 0;
        DecimalFormat decimales;
        decimales = new DecimalFormat("0.00");

        BufferedReader in = new BufferedReader(new FileReader(ctpv.getFichero()));
        String linea;
        boolean turno = true;//True = mañana, False = tarde

        while ((linea = in.readLine()) != null) {
            //Controlamos de que turno es la compra
            if (!linea.contains("|") && !linea.contains("€") && !linea.contains("*")) {
                int hr = Integer.parseInt(linea.trim().substring(0, 2));
                if (hr < 14) {
                    turno = true;
                } else {
                    turno = false;
                }
            }
            //Controlamos si hay que contabilizar la linea como la compra de un producto
            if (linea.contains("||")) {
                contador++;
            }
            //Controlamos los totales de los distintos turnos
            if (linea.contains("€")) {
                if (turno) {
                    mañana = mañana + devuelveTotal(linea);
                } else {
                    tarde = tarde + devuelveTotal(linea);
                }

            }

        }

        aux = contador + ";" +decimales.format(mañana) + ";" + decimales.format(tarde);
        return aux;
    }

    private double devuelveTotal(String linea) {
        
        double cifra;
        linea = linea.replace("Total :", "");
        linea = linea.replace("€", "");
        linea = linea.replace(",", ".");
        System.out.println(linea);
        cifra = Double.parseDouble(linea.trim());
        
        return cifra;
    }

    //***************************************************************************
    public DatagramSocket getEscuchador() {
        return escuchador;
    }

    public void setEscuchador(DatagramSocket escuchador) {
        this.escuchador = escuchador;
    }

    public DatagramPacket getDatagrama() {
        return datagrama;
    }

    public void setDatagrama(DatagramPacket datagrama) {
        this.datagrama = datagrama;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
