/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IVAN
 */
public class Calculadora extends JFrame {

    private static final Color AZUL_CLARO = new Color(169, 198, 218);

    JTextField jTextResultado;
    TPVJFrame tpv;

    public Calculadora(TPVJFrame tpw) {
        this.tpv = tpw;
        setLayout(new BorderLayout());
        setBackground(AZUL_CLARO);
        JPanel jPanelNumeros = new JPanel(new GridLayout(0, 3, 5, 5));
        jPanelNumeros.setBackground(AZUL_CLARO);
        
        for (int i = 1;
                i
                <= 9; i++) {
            final JButton numero = new JButton("" + i);
            numero.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    añadirDigito(numero.getText());
                }
            });
            jPanelNumeros.add(numero);
        }

        final JButton jButtonPunto = new JButton(".");
        jButtonPunto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                añadirDigito(jButtonPunto.getText());
            }
        });
        jPanelNumeros.add(jButtonPunto);

        final JButton jButton0 = new JButton("0");
        jButton0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                añadirDigito(jButton0.getText());
            }
        });
        jPanelNumeros.add(jButton0);

        JButton jButtonBorrar = new JButton("Del");
        jButtonBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrar();
            }
        });
        jPanelNumeros.add(jButtonBorrar);

        add(jPanelNumeros, BorderLayout.CENTER);

        jTextResultado = new JTextField();
        add(jTextResultado,BorderLayout.NORTH);

        JButton jButtonEnter = new JButton("Enter");
        jButtonEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                añadirFactura();
            }
        });
        add(jButtonEnter, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private void añadirDigito(String digito) {
        jTextResultado.setText(jTextResultado.getText() + digito);
    }

    private void añadirFactura() {
        String cadena = jTextResultado.getText();
        if (!cadena.equals("")) {
            try {
                Float cantidad;
                cantidad = Float.parseFloat(cadena);
                tpv.insertarAFactura("Otros", cantidad);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El preico no se indico de forma correcta",
                        "Precio incorrecto", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void borrar() {
        String cadena = jTextResultado.getText();
        if (!cadena.equals("")) {
            jTextResultado.setText(cadena.substring(0, cadena.length() - 1));
        }
    }

}
