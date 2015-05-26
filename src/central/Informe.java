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
import java.net.InetAddress;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author David
 */
public class Informe implements Runnable {

    private CTPV_Frame ctpv;
    private InetAddress destino;
    private int puerto;
    private DatagramSocket socket;
    
    private boolean estado;
        
    public Informe(CTPV_Frame ctpv, String puerto, InetAddress direccion) {
        this.ctpv = ctpv;
        this.puerto = Integer.parseInt(puerto);
        this.destino = direccion; 
        this.estado = true;
        
        try {          
                     
            this.socket = new DatagramSocket();
            
        } catch (SocketException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("El hilo del Informe arrancó");
            System.out.println("Conexión realizada");
            
            while (estado) {

                String informacion = recopilarDatos();
                
                byte[]infoEncriptada = encriptar(informacion.getBytes());
                                
                DatagramPacket paqueteEnvio = new DatagramPacket(infoEncriptada, infoEncriptada.length, destino, puerto);
               
                socket.send(paqueteEnvio);
                
                Thread.sleep(2500);
            }

        } catch (IllegalThreadStateException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("DatagramSocket Informe cerrado");
            socket.close();
            ctpv.getRegistro().remove(String.valueOf(puerto));
            
        }
    }

    private synchronized String recopilarDatos() throws FileNotFoundException, IOException {
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

        aux = contador + ";" + decimales.format(mañana) + ";" + decimales.format(tarde);
        return aux;
    }

    private double devuelveTotal(String linea) {

        double cifra;
        linea = linea.replace("Total :", "");
        linea = linea.replace("€", "");
        linea = linea.replace(",", ".");
        
        cifra = Double.parseDouble(linea.trim());

        return cifra;
    }

       private byte[] encriptar(byte[] textoPlano) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        //Cifro el texto.
        Cipher ci = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        ci.init(Cipher.ENCRYPT_MODE, ctpv.getPublica());

        System.out.println("Texto a enviar : " + new String(textoPlano));
        byte[] textoCifrado = ci.doFinal(textoPlano);
        return textoCifrado;

    }
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

                  
}
