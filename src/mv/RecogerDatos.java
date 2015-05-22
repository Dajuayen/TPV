/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mv;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.StringTokenizer;
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
public class RecogerDatos implements Runnable {

    private MV mv;

    private DatagramSocket receptor;

    private DatagramPacket datagrama;
    private static final int MAX_LEN = 128;
    private byte[] buffer;

    private boolean activo;


    public RecogerDatos(int puerto, MV mv) {
        this.mv = mv;
        this.buffer = new byte[MAX_LEN];
        this.activo = true;
        
        try {
            this.receptor = new DatagramSocket(puerto);
            this.datagrama = new DatagramPacket(buffer, MAX_LEN);

        } catch (SocketException ex) {
            Logger.getLogger(RecogerDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("El hilo del RecogerDatos arrancó");
            while (activo) {

                receptor.receive(datagrama);

                byte[] mensajeDesencriptado = desencriptar(datagrama.getData());
                String respuesta = new String(mensajeDesencriptado);

                StringTokenizer paquete = new StringTokenizer(respuesta, ";");

                mv.getjTextFieldLineas().setText(paquete.nextToken());
                mv.getjTextFieldMananas().setText(paquete.nextToken());
                mv.getjTextFieldTardes().setText(paquete.nextToken());

            }

        } catch (IllegalThreadStateException ex) {
            Logger.getLogger(RecogerDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecogerDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RecogerDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(RecogerDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(RecogerDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(RecogerDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(RecogerDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("DatagramSocket Receptor de datos cerrado");
            receptor.close();

        }
    }

    private byte[] desencriptar(byte[] textoCifrado) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        c.init(Cipher.DECRYPT_MODE, mv.getPrivada());
        byte[] desencriptado = c.doFinal(textoCifrado);
        return desencriptado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
