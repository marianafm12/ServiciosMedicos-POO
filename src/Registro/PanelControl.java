package Registro;

import javax.swing.*;
import java.awt.*;

public class PanelControl extends JPanel {
    public PanelControl(JTextField[] campos) {
        setLayout(new GridLayout(1, 3, 5, 5));

        JButton botonAgregar = new JButton("Agregar");
        botonAgregar.addActionListener(new AgregarRegistro(campos));
        add(botonAgregar);

        JButton botonBuscar = new JButton("Buscar");
        botonBuscar.addActionListener(new Buscar(campos));
        add(botonBuscar);

        JButton botonLimpiar = new JButton("Limpiar");
        botonLimpiar.addActionListener(new LimpiarCampos(campos));
        add(botonLimpiar);
    }
}
