/*
 * package Inicio;
 * 
 * import javax.swing.*;
 * import java.awt.*;
 * import java.awt.event.FocusAdapter;
 * import java.awt.event.FocusEvent;
 * import java.sql.*;
 * 
 * import BaseDeDatos.ConexionSQLite;
 * 
 * public class LoginPacientesFrame extends JFrame {
 * private final JTextField idField;
 * private final JPasswordField passwordField;
 * 
 * public LoginPacientesFrame() {
 * super("Inicio de sesión pacientes UDLAP");
 * setSize(300, 250);
 * setDefaultCloseOperation(EXIT_ON_CLOSE);
 * setLayout(new GridBagLayout());
 * setLocationRelativeTo(null);
 * 
 * GridBagConstraints gbc = new GridBagConstraints();
 * gbc.insets = new Insets(10, 10, 10, 10);
 * gbc.fill = GridBagConstraints.HORIZONTAL;
 * 
 * // ─── Campo ID con placeholder ───
 * final String idPlaceholder = "Ingrese ID";
 * idField = new JTextField(idPlaceholder, 15);
 * idField.setForeground(Color.GRAY);
 * idField.addFocusListener(new FocusAdapter() {
 * 
 * @Override
 * public void focusGained(FocusEvent e) {
 * if (idField.getText().equals(idPlaceholder)) {
 * idField.setText("");
 * idField.setForeground(Color.BLACK);
 * }
 * }
 * 
 * @Override
 * public void focusLost(FocusEvent e) {
 * if (idField.getText().isEmpty()) {
 * idField.setText(idPlaceholder);
 * idField.setForeground(Color.GRAY);
 * }
 * }
 * });
 * gbc.gridx = 0;
 * gbc.gridy = 0;
 * add(new JLabel("ID:"), gbc);
 * gbc.gridx = 1;
 * add(idField, gbc);
 * 
 * // ─── Campo Contraseña con placeholder ───
 * final String passPlaceholder = "Ingrese Contraseña";
 * passwordField = new JPasswordField(passPlaceholder, 15);
 * char defaultEcho = passwordField.getEchoChar();
 * passwordField.setEchoChar((char) 0);
 * passwordField.setForeground(Color.GRAY);
 * passwordField.addFocusListener(new FocusAdapter() {
 * 
 * @Override
 * public void focusGained(FocusEvent e) {
 * String curr = new String(passwordField.getPassword());
 * if (curr.equals(passPlaceholder)) {
 * passwordField.setText("");
 * passwordField.setEchoChar(defaultEcho);
 * passwordField.setForeground(Color.BLACK);
 * }
 * }
 * 
 * @Override
 * public void focusLost(FocusEvent e) {
 * if (passwordField.getPassword().length == 0) {
 * passwordField.setEchoChar((char) 0);
 * passwordField.setText(passPlaceholder);
 * passwordField.setForeground(Color.GRAY);
 * }
 * }
 * });
 * gbc.gridy = 1;
 * gbc.gridx = 0;
 * add(new JLabel("Contraseña:"), gbc);
 * gbc.gridx = 1;
 * add(passwordField, gbc);
 * 
 * // ─── Botones ───
 * gbc.gridy = 2;
 * gbc.gridx = 0;
 * gbc.gridwidth = 2;
 * JButton loginButton = new JButton("Iniciar Sesión");
 * JButton recoverButton = new JButton("Recuperar Contraseña");
 * add(loginButton, gbc);
 * 
 * gbc.gridy = 3;
 * add(recoverButton, gbc);
 * 
 * // Hacer que ENTER dispare el login
 * getRootPane().setDefaultButton(loginButton);
 * 
 * loginButton.addActionListener(e -> {
 * String idTxt = idField.getText().trim();
 * String pass = new String(passwordField.getPassword());
 * // validaciones de placeholder…
 * int idPaciente;
 * try {
 * idPaciente = Integer.parseInt(idTxt);
 * } catch (NumberFormatException ex) {
 * JOptionPane.showMessageDialog(
 * this, "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
 * return;
 * }
 * 
 * String sql =
 * "SELECT ID FROM InformacionAlumno WHERE ID = ? AND Contraseña = ?";
 * try (Connection conn = ConexionSQLite.conectar();
 * PreparedStatement ps = conn.prepareStatement(sql)) {
 * 
 * ps.setInt(1, idPaciente);
 * ps.setString(2, pass);
 * try (ResultSet rs = ps.executeQuery()) {
 * if (rs.next()) {
 * SesionUsuario.iniciarSesion(idPaciente);
 * new MenuPacientesFrame(idPaciente).setVisible(true);
 * dispose();
 * } else {
 * JOptionPane.showMessageDialog(
 * this, "ID o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
 * }
 * }
 * } catch (SQLException ex) {
 * ex.printStackTrace();
 * JOptionPane.showMessageDialog(
 * this,
 * "Error en la base de datos:\n" + ex.getMessage(),
 * "Error de BD",
 * JOptionPane.ERROR_MESSAGE);
 * }
 * });
 * 
 * // ─── Recuperar contraseña ───
 * recoverButton.addActionListener(e -> {
 * new RecuperarContrasenaFrame().setVisible(true);
 * dispose();
 * });
 * 
 * setVisible(true);
 * }
 * 
 * public static void main(String[] args) {
 * SwingUtilities.invokeLater(LoginPacientesFrame::new);
 * }
 * }
 */