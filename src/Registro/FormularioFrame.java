package Registro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class FormularioFrame extends JFrame {
    private static Map<Integer, String[]> datosPacientes = new HashMap<>();
    private boolean esPaciente;
    private int idPaciente;

    public FormularioFrame(int idUsuario) {
        setTitle("Formulario Médico");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título del formulario
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Formulario Médico", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, gbc);

        // Campo ID Paciente
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("ID Paciente:"), gbc);
        gbc.gridx = 1;
        JTextField idPacienteField = new JTextField();
        add(idPacienteField, gbc);

        if (esPaciente) {
            idPacienteField.setText(String.valueOf(idUsuario));
            idPacienteField.setEditable(false);
        }

        // Campo Nombre
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        JTextField nombreField = new JTextField();
        add(nombreField, gbc);

        // Campo Enfermedades
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Enfermedades:"), gbc);
        gbc.gridx = 1;
        JTextField enfermedadesField = new JTextField();
        add(enfermedadesField, gbc);

        // Campo Medicamentos
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Medicamentos:"), gbc);
        gbc.gridx = 1;
        JTextField medicamentosField = new JTextField();
        add(medicamentosField, gbc);

        // Campo Alergias
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Alergias:"), gbc);
        gbc.gridx = 1;
        JTextField alergiasField = new JTextField();
        add(alergiasField, gbc);

        // Campo Altura
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Altura (m):"), gbc);
        gbc.gridx = 1;
        JTextField alturaField = new JTextField();
        add(alturaField, gbc);

        // Campo Peso
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Peso (kg):"), gbc);
        gbc.gridx = 1;
        JTextField pesoField = new JTextField();
        add(pesoField, gbc);

        // Botones "Buscar Paciente" y "Guardar"
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        JPanel buttonsPanel = new JPanel();
        JButton buscarButton = new JButton("Buscar Paciente");
        JButton guardarButton = new JButton("Guardar");
        buttonsPanel.add(buscarButton);
        buttonsPanel.add(guardarButton);
        add(buttonsPanel, gbc);

        // Acción para el botón "Buscar Paciente"
        buscarButton.addActionListener(e -> {
            try {
                idPaciente = Integer.parseInt(idPacienteField.getText());
                if (datosPacientes.containsKey(idPaciente)) {
                    String[] datos = datosPacientes.get(idPaciente);
                    nombreField.setText(datos[0]);
                    enfermedadesField.setText(datos[1]);
                    medicamentosField.setText(datos[2]);
                    alergiasField.setText(datos[3]);
                    alturaField.setText(datos[4]);
                    pesoField.setText(datos[5]);
                } else {
                    JOptionPane.showMessageDialog(this, "Paciente no encontrado", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Guardar con enter
        getRootPane().setDefaultButton(guardarButton);

        // Acción para el botón "Guardar"
        guardarButton.addActionListener((ActionEvent e) -> {
            try {
                int pacienteID = Integer.parseInt(idPacienteField.getText());
                datosPacientes.put(pacienteID, new String[] {
                        nombreField.getText(),
                        enfermedadesField.getText(),
                        medicamentosField.getText(),
                        alergiasField.getText(),
                        alturaField.getText(),
                        pesoField.getText()
                });
                JOptionPane.showMessageDialog(this, "Registro exitoso", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                // new LoginFrame();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID Paciente inválido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Agregar los botones "Menú Principal" y "Regresar"
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton menuPrincipalButton = new JButton("Menú Principal");
        JButton regresarButton = new JButton("Regresar");
        bottomPanel.add(menuPrincipalButton);
        bottomPanel.add(regresarButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(bottomPanel, gbc);

        // Evento para el botón "Menú Principal"
        menuPrincipalButton.addActionListener(e -> {
            new Inicio.PortadaFrame().setVisible(true);
            dispose();
        });

        // Evento para el botón "Regresar"
        regresarButton.addActionListener(e -> {
            new Inicio.MenuMedicosFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }
}