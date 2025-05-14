package Inicio;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.sql.*;

public class LoginPacientesFrame extends JFrame {

    public LoginPacientesFrame() {
        setTitle("Inicio de sesión pacientes UDLAP");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //
        // Campo ID con placeholder
        //
        final String idPlaceholder = "Ingrese ID";
        JTextField idField = new JTextField(idPlaceholder, 15);
        idField.setForeground(Color.GRAY);
        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (idField.getText().equals(idPlaceholder)) {
                    idField.setText("");
                    idField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (idField.getText().isEmpty()) {
                    idField.setText(idPlaceholder);
                    idField.setForeground(Color.GRAY);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        add(idField, gbc);

        //
        // Campo Contraseña con placeholder
        //
        final String passPlaceholder = "Ingrese Contraseña";
        JPasswordField passwordField = new JPasswordField(passPlaceholder, 15);
        char defaultEcho = passwordField.getEchoChar();
        passwordField.setEchoChar((char) 0); // mostrar texto en vez de puntos
        passwordField.setForeground(Color.GRAY);
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String curr = new String(passwordField.getPassword());
                if (curr.equals(passPlaceholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar(defaultEcho);
                    passwordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setEchoChar((char) 0);
                    passwordField.setText(passPlaceholder);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        //
        // Botones y lógica de autenticación
        //
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Iniciar Sesión");
        add(loginButton, gbc);
        getRootPane().setDefaultButton(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton recoverButton = new JButton("Recuperar Contraseña");
        add(recoverButton, gbc);

        loginButton.addActionListener((ActionEvent e) -> {
            String idText = idField.getText();
            String password = new String(passwordField.getPassword());

            // No permitir enviar el placeholder como credencial
            if (idText.equals(idPlaceholder) || password.equals(passPlaceholder)) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, ingrese sus credenciales.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(idText);
                try (Connection conexion = DriverManager.getConnection(
                        "jdbc:sqlite:Servicios medicos.db")) {
                    String sql = "SELECT * FROM InformacionAlumno WHERE ID = ? AND Contraseña = ?";
                    try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                        stmt.setInt(1, id);
                        stmt.setString(2, password);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                new MenuPacientesFrame(id).setVisible(true); // Pasar el ID al menú
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(this,
                                        "Credenciales incorrectas",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID inválido",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error en la base de datos: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        recoverButton.addActionListener(e -> {
            new RecuperarContrasenaFrame();
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPacientesFrame();
    }
}
