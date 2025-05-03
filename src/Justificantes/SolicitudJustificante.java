package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Inicio.MenuPacientesFrame;
import Inicio.PortadaFrame;

public class SolicitudJustificante extends JFrame {

    public SolicitudJustificante() {
        setTitle("Justificante por Solicitud");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton btnMenu = new JButton("Menú Principal");
        JButton btnRegresar = new JButton("Regresar");

        btnMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MenuPacientesFrame().setVisible(true);
                dispose();
            }
        });

        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PortadaFrame().setVisible(true);
                dispose();
            }
        });

        add(btnMenu);
        add(btnRegresar);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SolicitudJustificante();
    }
}

