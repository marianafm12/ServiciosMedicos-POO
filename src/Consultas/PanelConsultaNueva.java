package Consultas;

import Utilidades.PanelProvider;
import Utilidades.ColoresUDLAP;
import Utilidades.FormularioMedicoBase;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelConsultaNueva extends FormularioMedicoBase implements PanelProvider {

    private final JButton btnGuardar, btnBuscar, btnLimpiar;

    public PanelConsultaNueva(int idMedico, String nombreMedico) {
        super("Consulta Médica - Dr. " + nombreMedico, new String[] {
                "ID Paciente:", "Nombre", "Edad", "Correo",
                "Síntomas", "Medicamentos", "Diagnóstico",
                "Fecha Consulta", "Última Consulta", "Inicio de Síntomas",
                "Receta Médica"
        });

        // Inicializar fecha por defecto
        JTextField fecha = (JTextField) campos[7];
        fecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        // Botones
        btnGuardar = new JButton("Guardar");
        btnBuscar = new JButton("Buscar");
        btnLimpiar = new JButton("Limpiar");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botones.setBackground(Color.WHITE);
        Dimension size = new Dimension(120, 40);

        configurarBoton(btnGuardar, ColoresUDLAP.VERDE_SOLIDO, size);
        configurarBoton(btnBuscar, ColoresUDLAP.NARANJA_BARRA, size);
        configurarBoton(btnLimpiar, ColoresUDLAP.ROJO_SOLIDO, size);

        botones.add(btnGuardar);
        botones.add(btnBuscar);
        botones.add(btnLimpiar);
        add(botones, BorderLayout.SOUTH);

        // Conexion lógica
        JTextField[] camposConsulta = getCamposTexto(0, 1, 2, 3, 7, 8, 9);
        JTextField sintomas = new JTextField(((JTextArea) campos[4]).getText());
        JTextField medicamentos = new JTextField(((JTextArea) campos[5]).getText());
        JTextField diagnostico = new JTextField(((JTextArea) campos[6]).getText());
        JTextArea receta = getAreaTexto(10);

        JTextField[] consultaFinal = new JTextField[] {
                camposConsulta[0], camposConsulta[1], camposConsulta[2], camposConsulta[3],
                sintomas, medicamentos, diagnostico,
                camposConsulta[4], camposConsulta[5], camposConsulta[6]
        };

        btnGuardar.addActionListener(new GuardarConsulta(consultaFinal, receta));
        btnBuscar.addActionListener(new BuscarPaciente(new JTextField[] {
                (JTextField) campos[0], (JTextField) campos[1],
                (JTextField) campos[2], (JTextField) campos[3]
        }));
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    @Override
    protected boolean isTextArea(int index) {
        return index >= 4 && index <= 6 || index == 10;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "consultaNueva";
    }

    private void configurarBoton(JButton boton, Color color, Dimension size) {
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false); // ✅ Quita borde negro
        boton.setContentAreaFilled(true);
        boton.setOpaque(true);
        boton.setPreferredSize(size);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
    }

}
