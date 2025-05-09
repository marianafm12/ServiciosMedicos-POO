package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import BaseDeDatos.ConexionSQLite;
import Inicio.MenuPacientesFrame;

public class SolicitudesJustificantesFrame extends JFrame {
    private final DefaultListModel<Integer> model = new DefaultListModel<>();
    private final JList<Integer> list = new JList<>(model);
    private final JButton revisarBtn = new JButton("Revisar");

    public SolicitudesJustificantesFrame() {
        super("Justificante por Solicitud");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Cargar folios
        try (Connection c = ConexionSQLite.conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT folio FROM JustificantePaciente ORDER BY folio DESC")) {
            while(rs.next()) model.addElement(rs.getInt("folio"));
        } catch(SQLException e){ e.printStackTrace(); }

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        revisarBtn.setEnabled(false);
        list.addListSelectionListener(e -> revisarBtn.setEnabled(!list.isSelectionEmpty()));

        JPanel top = new JPanel();
        JButton menu = new JButton("Menú Principal"), back = new JButton("Regresar");
        menu.addActionListener(e -> { new MenuPacientesFrame().setVisible(true); dispose(); });
        back.addActionListener(e -> { new SeleccionarPacienteFrame().setVisible(true); dispose(); });
        top.add(menu); top.add(back);

        JPanel bot = new JPanel();
        bot.add(revisarBtn);
        revisarBtn.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Función de revisión pendiente.")
        );

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
        add(bot, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SolicitudesJustificantesFrame().setVisible(true));
    }
}
