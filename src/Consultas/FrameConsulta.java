package Consultas;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FrameConsulta extends JPanel {
    private final JTextField[] campos;
    private final JTextArea areaTexto;
    private final int idMedico;
    private final String nombreMedico;

    private final String[] etiquetas = {
            "ID Paciente:", "Nombre del Paciente:", "Edad:", "Correo Electrónico:",
            "Síntomas:", "Medicamentos:", "Diagnóstico:",
            "Fecha (dd/MM/yyyy):", "Última Consulta (dd/MM/yyyy):",
            "Inicio Síntomas (dd/MM/yyyy):"
    };

    public FrameConsulta(int idMedico, String nombreMedico) {
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;

        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);
        campos = new JTextField[etiquetas.length];

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuración de fuente
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titulo = new JLabel("Consulta Médica - Dr. " + nombreMedico, SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        add(titulo, gbc);

        gbc.gridwidth = 1;

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1; // +1 por el título
            gbc.weightx = 0.3;

            JLabel label = new JLabel(etiquetas[i]);
            label.setFont(labelFont);
            label.setForeground(ColoresUDLAP.NEGRO);
            add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;

            campos[i] = new JTextField(25);
            campos[i].setFont(fieldFont);
            campos[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            // Establecer fecha actual por defecto
            if (etiquetas[i].contains("Fecha (")) {
                campos[i].setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            }

            add(campos[i], gbc);
        }

        // Área de texto para la receta
        gbc.gridx = 0;
        gbc.gridy = etiquetas.length + 1;
        gbc.gridwidth = 1;
        JLabel lblReceta = new JLabel("Receta Médica:");
        lblReceta.setFont(labelFont);
        lblReceta.setForeground(ColoresUDLAP.NEGRO);
        add(lblReceta, gbc);

        gbc.gridx = 1;
        gbc.gridy = etiquetas.length + 1;
        areaTexto = new JTextArea(5, 25);
        areaTexto.setFont(fieldFont);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(scrollPane, gbc);

        // Espacio adicional al final
        gbc.gridy = etiquetas.length + 2;
        gbc.weighty = 1.0;
        add(Box.createGlue(), gbc);
    }

    public JTextField[] obtenerCampos() {
        return campos;
    }

    public JTextArea obtenerAreaTexto() {
        return areaTexto;
    }
}