package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import BaseDeDatos.ConexionSQLite;  
import Inicio.MenuPacientesFrame;

public class CorreosProfesoresFrame extends JFrame {
    private final JComboBox<Integer> countBox = new JComboBox<>();
    private final JPanel correosPanel = new JPanel();
    private final JButton enviarBtn = new JButton("Enviar Justificante");
    private final JButton menuBtn  = new JButton("Menú Principal");
    private final JButton backBtn  = new JButton("Regresar");
    private final int folio;

    public CorreosProfesoresFrame(int folio) {
        super("Agregar Correos de Profesores");
        this.folio = folio;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500,400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Top
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Cantidad de profesores:"));
        for(int i=1;i<=12;i++) countBox.addItem(i);
        top.add(countBox);

        // Center
        correosPanel.setLayout(new BoxLayout(correosPanel, BoxLayout.Y_AXIS));
        actualizarCampos(3);

        countBox.addActionListener(e -> actualizarCampos((int)countBox.getSelectedItem()));

        // Bottom
        JPanel bot = new JPanel();
        bot.add(menuBtn);
        bot.add(backBtn);
        bot.add(enviarBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(correosPanel), BorderLayout.CENTER);
        add(bot, BorderLayout.SOUTH);

        menuBtn .addActionListener(e -> { new MenuPacientesFrame().setVisible(true); dispose(); });
        backBtn .addActionListener(e -> { new FormularioJustificanteFrame().setVisible(true); dispose(); });
        enviarBtn.addActionListener(e -> {
            if(guardarCorreos()) {
                JOptionPane.showMessageDialog(this, "Correos registrados para folio " + folio);
                new MenuPacientesFrame().setVisible(true);
                dispose();
            }
        });
    }

    private void actualizarCampos(int n) {
        correosPanel.removeAll();
        for(int i=1;i<=n;i++){
            correosPanel.add(new JLabel("Correo profesor " + i + ":"));
            correosPanel.add(new JTextField(30));
            correosPanel.add(Box.createRigidArea(new Dimension(0,10)));
        }
        correosPanel.revalidate();
        correosPanel.repaint();
    }

    private boolean guardarCorreos() {
        String sql = "INSERT INTO JustificanteProfesores(folio, correo) VALUES (?,?)";
        try (Connection c = ConexionSQLite.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for(Component comp: correosPanel.getComponents()) {
                if(comp instanceof JTextField) {
                    String mail = ((JTextField)comp).getText().trim();
                    if(!mail.isEmpty()) {
                        ps.setInt(1, folio);
                        ps.setString(2, mail);
                        ps.addBatch();
                    }
                }
            }
            ps.executeBatch();
            return true;
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar correos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CorreosProfesoresFrame(0).setVisible(true));
    }
}
