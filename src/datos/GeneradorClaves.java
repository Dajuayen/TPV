/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class GeneradorClaves {

    public static void main(String[] args) {
        try {
            KeyPairGenerator k = KeyPairGenerator.getInstance("RSA");
            SecureRandom s = SecureRandom.getInstance("SHA1PRNG");
            k.initialize(1024, s);
            KeyPair par = k.genKeyPair();
            //
            PrivateKey privada = par.getPrivate();
            PublicKey publica = par.getPublic();
            //guardarClavePrivadaFichero(privada, "Privada");
             PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(privada.getEncoded());
            //
            //la guardamos en un fichero
            FileOutputStream fos = new FileOutputStream("MV" + ".privada");
            fos.write(pkcs8.getEncoded());
            fos.close();            
            //
            System.out.println("Clave privada guardada ");
            
            //guardarClavePublicaFichero(publica, "Publica");
             X509EncodedKeySpec X509Pub = new X509EncodedKeySpec(publica.getEncoded());
            //
            //la guardamos en un fichero
            FileOutputStream fos2 = new FileOutputStream("CTPV" + ".publica");
            fos2.write(X509Pub.getEncoded());
            fos2.close();
            //
            System.out.println("Clave publica guardada");
            //


        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GeneradorClaves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeneradorClaves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeneradorClaves.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
