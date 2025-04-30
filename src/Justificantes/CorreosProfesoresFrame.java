package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CorreosProfesoresFrame extends JFrame {
    private JComboBox<Integer> cantidadProfesoresBox;
    private JPanel correosPanel;
    private JButton enviarBtn;
    private int idJustificante;

    public CorreosProfesoresFrame(int idJustificante) {
        this.idJustificante = idJustificante;

        setTitle("Agregar Correos de Profesores");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Cantidad de profesores:"));
        cantidadProfesoresBox = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cantidadProfesoresBox.addItem(i);
        }
        topPanel.add(cantidadProfesoresBox);

        correosPanel = new JPanel();
        correosPanel.setLayout(new GridLayout(12, 1));
        actualizarCamposCorreos(1);

        cantidadProfesoresBox.addActionListener(e -> {
            int cantidad = (int) cantidadProfesoresBox.getSelectedItem();
            actualizarCamposCorreos(cantidad);
        });

        enviarBtn = new JButton("Enviar Justificante");
        enviarBtn.addActionListener(e -> {
            if (guardarCorreosEnBD()) {
                JOptionPane.showMessageDialog(this, "Justificante enviado a los profesores.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(correosPanel), BorderLayout.CENTER);
        add(enviarBtn, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void actualizarCamposCorreos(int cantidad) {
        correosPanel.removeAll();
        correosPanel.setLayout(new GridLayout(cantidad, 1));
        for (int i = 0; i < cantidad; i++) {
            correosPanel.add(new JTextField("Correo profesor " + (i + 1) + ": "));
        }
        correosPanel.revalidate();
        correosPanel.repaint();
    }

    private boolean guardarCorreosEnBD() {
        Component[] componentes = correosPanel.getComponents();
        try {
            Connection conn = BaseDeDatos.ConexionSQLite.conectar();
            String sql = "INSERT INTO correos_profesores (id_justificante, correo) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (Component comp : componentes) {
                if (comp instanceof JTextField) {
                    String correo = ((JTextField) comp).getText().trim();
                    if (!correo.isEmpty()) {
                        if (!correo.endsWith("@udlap.mx")) {
                            JOptionPane.showMessageDialog(this,
                                    "El correo \"" + correo + "\" no es válido.\nDebe terminar en @udlap.mx.",
                                    "Correo inválido", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        pstmt.setInt(1, idJustificante);
                        pstmt.setString(2, correo);
                        pstmt.addBatch();
                    }
                }
            }

            pstmt.executeBatch();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar correos:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Para pruebas
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CorreosProfesoresFrame(1));
    }
}
