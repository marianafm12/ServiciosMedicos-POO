package Inicio;

import GestionEnfermedades.EditarDatosPaciente;
import GestionEnfermedades.VerDatosPaciente;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MenuMedicosFrame extends JFrame {

    private boolean hasNewNotification = false;
    private JButton notificationButton;
    private ImageIcon iconDefault;
    private ImageIcon iconNew;

    public MenuMedicosFrame() {
        super("Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        loadIcons();
        initToolbar();
        initCenterButtons();
        setSize(600, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Carga los iconos de la campana desde el classpath */
    private void loadIcons() {
        URL u1 = getClass().getResource("/icons/bell.png");
        URL u2 = getClass().getResource("/icons/bell_new.png");
        if (u1 == null || u2 == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se encontraron /icons/bell.png o /icons/bell_new.png en el classpath.",
                    "Recursos no encontrados",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        Image img1 = new ImageIcon(u1).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        Image img2 = new ImageIcon(u2).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        iconDefault = new ImageIcon(img1);
        iconNew = new ImageIcon(img2);
    }

    /** Crea la barra superior con título centrado y campana a la derecha */
    private void initToolbar() {
        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setBackground(new Color(245, 245, 245));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Bienvenido al Sistema de Servicios Médicos UDLAP", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        toolbar.add(title, gbc);

        // Botón: Editar Datos del Paciente
        gbc.gridy = 1;
        gbc.gridx = 1;
        JButton editarDatosButton = new JButton("Editar Datos del Paciente");
        editarDatosButton.setPreferredSize(buttonSize);
        add(editarDatosButton, gbc);

        // Botón: Justificante Médico
        gbc.gridy = 2;
        gbc.gridx = 0;
        JButton justificantesMedicosButton = new JButton("Justificantes Médicos");
        justificantesMedicosButton.setPreferredSize(buttonSize);
        add(justificantesMedicosButton, gbc);

        // Botón: Llamada de Emergencia Nueva
        gbc.gridy = 2;
        gbc.gridx = 1;
        JButton llamadaEmergenciaNueva = new JButton("Registrar Llamada de Emergencia");
        llamadaEmergenciaNueva.setPreferredSize(buttonSize);
        add(llamadaEmergenciaNueva, gbc);

        // Botón: Formulario Reporte de Accidente
        gbc.gridy = 3;
        gbc.gridx = 0;
        JButton reporteAccidente = new JButton("Llenar Reporte de accidente");
        reporteAccidente.setPreferredSize(buttonSize);
        add(reporteAccidente, gbc);

        // Evento para botón "Consulta Nueva"
        consultaButton.addActionListener(e -> {
            Consultas.ConsultasFrame.launchApp();
            dispose();
        });

        // Evento para botón "Editar Datos del Paciente"
        editarDatosButton.addActionListener(e -> {
            String idPacienteStr = JOptionPane.showInputDialog(this, "Ingrese el ID del Paciente:");
            if (idPacienteStr != null && !idPacienteStr.isEmpty()) {
                try {
                    int idPaciente = Integer.parseInt(idPacienteStr);
                    new EditarDatosPaciente(idPaciente).setVisible(true); // Usa el nuevo constructor
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID inválido. Debe ser un número.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        justificantes.addActionListener(e -> {
            new Justificantes.SeleccionarPacienteFrame().setVisible(true);
            dispose();
        });
        emitir.addActionListener(e -> {
            new Justificantes.EmitirJustificanteDesdeConsultaFrame().setVisible(true);
            dispose();
        });
        emergencia.addActionListener(e -> {
            new Emergencias.LlamadaEmergencia().setVisible(true);
            dispose();
        });

        // Evento para botón "Reporte de Accidente"
        reporteAccidente.addActionListener(e -> {
            new Emergencias.AccidenteFrame().setVisible(true);
            dispose();
        });

        add(center, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuMedicosFrame::new);
    }
}
