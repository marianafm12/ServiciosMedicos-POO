package Justificantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import BaseDeDatos.ConexionSQLite;
import Justificantes.SolicitudJustificante;

public class SolicitudesJustificantesFrame extends JFrame {
    private final DefaultListModel<Integer> listModel = new DefaultListModel<>();
    private final JList<Integer> folioList = new JList<>(listModel);
    private final JButton revisarBtn = new JButton("Revisar");
    private final JButton menuButton = new JButton("Menú Principal");
    private final JButton regresarButton = new JButton("Regresar");

    public SolicitudesJustificantesFrame() {
        setTitle("Justificante por Solicitud");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        cargarFolios();

        JScrollPane scroll = new JScrollPane(folioList);
        folioList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        revisarBtn.setEnabled(false);
        folioList.addListSelectionListener(e ->
            revisarBtn.setEnabled(!folioList.isSelectionEmpty())
        );

        JPanel topPanel = new JPanel();
        topPanel.add(menuButton);
        topPanel.add(regresarButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(revisarBtn);

        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        revisarBtn.addActionListener(e -> {
            Integer folioSeleccionado = folioList.getSelectedValue();
            if (folioSeleccionado != null) {
                new SolicitudJustificante(folioSeleccionado).setVisible(true);
                dispose();
            }
        });

        menuButton.addActionListener(e -> {
            new Inicio.MenuMedicosFrame().setVisible(true);
            dispose();
        });

        regresarButton.addActionListener(e -> {
            new SeleccionarPacienteFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    private void cargarFolios() {
        String sql = "SELECT folio FROM JustificantePaciente ORDER BY folio DESC";
        try (Connection conn = ConexionSQLite.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                listModel.addElement(rs.getInt("folio"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SolicitudesJustificantesFrame::new);
    }
}
