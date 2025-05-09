package Justificantes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MisJustificantesPacienteFrame extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private String idPaciente;

    public MisJustificantesPacienteFrame(String idPaciente) {
        this.idPaciente = idPaciente;

        setTitle("Mis Justificantes Médicos");
        setSize(850, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new String[]{
            "Folio", "Motivo", "Inicio", "Fin", "Estado", "Médico"
        }, 0);

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        cargarJustificantes();

        JButton btnAbrirPDF = new JButton("Abrir Justificante PDF");
        btnAbrirPDF.addActionListener(e -> abrirSeleccionado());

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(e -> {
            // Aquí iría el menú paciente
            dispose();
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAbrirPDF);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarJustificantes() {
        List<Justificante> lista = JustificanteDAO.obtenerTodos().stream()
                .filter(j -> j.getIdPaciente().equals(idPaciente))
                .collect(Collectors.toList());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Justificante j : lista) {
            modelo.addRow(new Object[]{
                j.getFolio(),
                j.getMotivo(),
                j.getFechaInicio().format(fmt),
                j.getFechaFin().format(fmt),
                j.getEstado(),
                j.getResueltoPor() != null ? j.getResueltoPor() : "-"
            });
        }
    }

    private void abrirSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un justificante.");
            return;
        }

        String estado = (String) modelo.getValueAt(fila, 4);
        if (!estado.equalsIgnoreCase("Aprobado")) {
            JOptionPane.showMessageDialog(this, "Solo puedes abrir justificantes aprobados.");
            return;
        }

        int folio = (int) modelo.getValueAt(fila, 0);
        Justificante j = JustificanteDAO.obtenerPorFolio(folio).orElse(null);
        if (j != null) {
            File pdf = GeneradorPDFJustificante.generar(j);
            if (pdf != null && pdf.exists()) {
                try {
                    Desktop.getDesktop().open(pdf);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al abrir el PDF.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "El justificante no tiene PDF generado.");
            }
        }
    }

    public static void main(String[] args) {
        new MisJustificantesPacienteFrame("12345").setVisible(true); // para prueba
    }
}
