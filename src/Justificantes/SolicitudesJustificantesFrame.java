package Justificantes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SolicitudesJustificantesFrame extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField buscador;
    private TableRowSorter<DefaultTableModel> sorter;

    public SolicitudesJustificantesFrame() {
        setTitle("Solicitudes de Justificantes");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top panel con búsqueda
        JPanel topPanel = new JPanel(new BorderLayout());
        buscador = new JTextField();
        buscador.setToolTipText("Buscar por ID o Nombre...");
        topPanel.add(new JLabel(" Buscar: "), BorderLayout.WEST);
        topPanel.add(buscador, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(new String[]{
            "Folio", "ID", "Nombre", "Motivo", "Inicio", "Fin"
        }, 0);
        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        cargarDatos();

        // Filtro en vivo
        buscador.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
        });

        // Botones
        JButton btnVer = new JButton("Ver Seleccionado");
        btnVer.addActionListener(e -> abrirSeleccionado());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        JButton btnMenu = new JButton("Menú Principal");
        btnMenu.addActionListener(e -> {
            new Inicio.MenuMedicosFrame().setVisible(true);
            dispose();
        });

        JPanel botones = new JPanel();
        botones.add(btnVer);
        botones.add(btnEliminar);
        botones.add(btnMenu);
        add(botones, BorderLayout.SOUTH);

        // Doble clic
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirSeleccionado();
                }
            }
        });
    }

    private void filtrar() {
        String texto = buscador.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1, 2)); // Filtra columnas ID y Nombre
        }
    }

    private void cargarDatos() {
        List<Justificante> lista = JustificanteDAO.obtenerTodos();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Justificante j : lista) {
            modelo.addRow(new Object[]{
                j.getFolio(),
                j.getIdPaciente(),
                j.getNombrePaciente(),
                j.getMotivo(),
                j.getFechaInicio().format(fmt),
                j.getFechaFin().format(fmt)
            });
        }
    }

    private void abrirSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro.");
            return;
        }

        int folio = (int) tabla.getValueAt(fila, 0);
        new RevisarSolicitudFrame(folio).setVisible(true);
        dispose();
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro para eliminar.");
            return;
        }

        int folio = (int) tabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar el justificante folio " + folio + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = JustificanteDAO.eliminarPorFolio(folio);
            if (eliminado) {
                ((DefaultTableModel) tabla.getModel()).removeRow(fila);
                JOptionPane.showMessageDialog(this, "Justificante eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el justificante.");
            }
        }
    }

    public static void main(String[] args) {
        new SolicitudesJustificantesFrame().setVisible(true);
    }
}
