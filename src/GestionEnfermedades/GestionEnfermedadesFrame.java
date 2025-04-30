package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestionEnfermedadesFrame extends JFrame {
    public GestionEnfermedadesFrame() {
        setTitle("Gestión de Enfermedades");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel para el formulario de la enfermedad
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel lblEnfermedad = new JLabel("Enfermedad:");
        JTextField txtEnfermedad = new JTextField();

        JLabel lblSintomas = new JLabel("Síntomas:");
        JTextField txtSintomas = new JTextField();

        JLabel lblTratamiento = new JLabel("Tratamiento:");
        JTextField txtTratamiento = new JTextField();

        panelFormulario.add(lblEnfermedad);
        panelFormulario.add(txtEnfermedad);
        panelFormulario.add(lblSintomas);
        panelFormulario.add(txtSintomas);
        panelFormulario.add(lblTratamiento);
        panelFormulario.add(txtTratamiento);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        // Agregar con enter
        getRootPane().setDefaultButton(btnAgregar);

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Enfermedad registrada con éxito!");
            }
        });

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        JPanel panelBotonesAbajo = new JPanel();
        panelBotonesAbajo.setLayout(new BorderLayout());
        panelBotonesAbajo.add(panelBotones, BorderLayout.CENTER);

        // Panel para los botones "Menú Principal" y "Regresar"
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton menuPrincipalButton = new JButton("Menú Principal");
        JButton regresarButton = new JButton("Regresar");
        bottomPanel.add(menuPrincipalButton);
        bottomPanel.add(regresarButton);

        // Agregar los botones de "Menú Principal" y "Regresar" en la parte inferior
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panelBotonesAbajo, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.SOUTH);

        // Evento para botón "Menú Principal"
        menuPrincipalButton.addActionListener(e -> {
            new Inicio.PortadaFrame().setVisible(true);
            dispose();
        });

        // Evento para botón "Regresar"
        regresarButton.addActionListener(e -> {
            new Inicio.MenuMedicosFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new GestionEnfermedadesFrame();
    }
}
