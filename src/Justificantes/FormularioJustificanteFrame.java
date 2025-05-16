package Justificantes;

import Inicio.MenuMedicosFrame;
//import Inicio.MenuPacientesFrame;
import Utilidades.ColoresUDLAP;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

public class FormularioJustificanteFrame extends JFrame {
    private JTextField idField, nombreField, motivoField;
    private JComboBox<String> diaInicio, mesInicio, anioInicio;
    private JComboBox<String> diaFin, mesFin, anioFin;
    private File archivoPDF = null;

    public FormularioJustificanteFrame() {
        setTitle("Solicitud de Justificante Médico");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setSize(600, 400);
        setBackground(ColoresUDLAP.BLANCO);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2, 5, 5));

        // Campos de texto
        add(new JLabel("ID:"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        add(nombreField);

        add(new JLabel("Motivo:"));
        motivoField = new JTextField();
        add(motivoField);

        // Fecha de inicio
        add(new JLabel("Inicio de Reposo:"));
        JPanel panelInicio = new JPanel();
        diaInicio = new JComboBox<>(generarDias());
        mesInicio = new JComboBox<>(generarMeses());
        anioInicio = new JComboBox<>(generarAnios());
        panelInicio.add(diaInicio);
        panelInicio.add(mesInicio);
        panelInicio.add(anioInicio);
        add(panelInicio);

        // Fecha de fin
        add(new JLabel("Fin de Reposo:"));
        JPanel panelFin = new JPanel();
        diaFin = new JComboBox<>(generarDias());
        mesFin = new JComboBox<>(generarMeses());
        anioFin = new JComboBox<>(generarAnios());
        panelFin.add(diaFin);
        panelFin.add(mesFin);
        panelFin.add(anioFin);
        add(panelFin);

        // Botón para subir PDF
        JButton subirPDF = new JButton("Subir receta (PDF)");
        subirPDF.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                archivoPDF = chooser.getSelectedFile();
                JOptionPane.showMessageDialog(this,
                        "Archivo cargado: " + archivoPDF.getName());
            }
        });

        // Botón para guardar y pasar al siguiente paso
        JButton siguienteBtn = new JButton("Siguiente");
        siguienteBtn.addActionListener(e -> guardarJustificante());

        // Botón para volver al Menú Médico
        JButton menuBtn = new JButton("Menú Principal");
        menuBtn.addActionListener(e -> {
            new MenuMedicosFrame().setVisible(true);
            dispose();
        });

        // --- Aquí estaba el error: faltaba declarar 'regresarBtn' ---
        JButton regresarBtn = new JButton("Volver a Pacientes");
        regresarBtn.addActionListener(e -> {
            int idPaciente = 1; // Reemplaza con el ID real cuando proceda
            // new MenuPacientesFrame(idPaciente).setVisible(true);
            dispose();
        });

        // Panel que agrupa los botones al final
        JPanel panelBotones = new JPanel();
        panelBotones.add(subirPDF);
        panelBotones.add(siguienteBtn);
        panelBotones.add(menuBtn);
        panelBotones.add(regresarBtn);

        // Relleno de la cuadrícula
        add(new JLabel()); // celda vacía de separación
        add(panelBotones); // panel con todos los botones

        setVisible(true);
    }

    private void guardarJustificante() {
        String id = idField.getText().trim();
        String nombre = nombreField.getText().trim();
        String motivo = motivoField.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Completa todos los campos obligatorios.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate inicio = construirFecha(diaInicio, mesInicio, anioInicio);
        LocalDate fin = construirFecha(diaFin, mesFin, anioFin);

        if (inicio.isAfter(fin)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha de inicio no puede ser posterior a la de fin.",
                    "Error de fechas",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Justificante nuevo = new Justificante(
                id, nombre, motivo, inicio, fin, "", archivoPDF);

        boolean exito = JustificanteDAO.guardarJustificante(nuevo);
        if (exito) {
            JOptionPane.showMessageDialog(
                    this,
                    "Justificante guardado exitosamente.");
            dispose();
            new SeleccionarPacienteFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar justificante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String[] generarDias() {
        String[] d = new String[31];
        for (int i = 1; i <= 31; i++) {
            d[i - 1] = String.valueOf(i);
        }
        return d;
    }

    private String[] generarMeses() {
        return new String[] {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
    }

    private String[] generarAnios() {
        int base = LocalDate.now().getYear();
        String[] a = new String[6];
        for (int i = 0; i < 6; i++) {
            a[i] = String.valueOf(base + i);
        }
        return a;
    }

    private LocalDate construirFecha(
            JComboBox<String> dia,
            JComboBox<String> mes,
            JComboBox<String> anio) {
        int d = Integer.parseInt((String) dia.getSelectedItem());
        int m = mes.getSelectedIndex() + 1;
        int y = Integer.parseInt((String) anio.getSelectedItem());
        return LocalDate.of(y, m, d);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FormularioJustificanteFrame::new);
    }
}
