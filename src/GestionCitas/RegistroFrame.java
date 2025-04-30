package GestionCitas;

import javax.swing.*;
import java.awt.*;

public class RegistroFrame extends JFrame {
    public RegistroFrame() {
        setTitle("Registro de Usuarios");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Nombre:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Correo:");
        JTextField emailField = new JTextField();
        JButton registerBtn = new JButton("Registrar");

        registerBtn.addActionListener(e -> {
            if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (!emailField.getText().contains("@")) {
                JOptionPane.showMessageDialog(this, "Ingrese un email válido", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente");
            }
        });

        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(registerBtn);

        setVisible(true);
    }
}
