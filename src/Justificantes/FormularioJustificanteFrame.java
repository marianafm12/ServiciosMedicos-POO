package Justificantes;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import BaseDeDatos.ConexionSQLite;
import Inicio.MenuPacientesFrame;

public class FormularioJustificanteFrame extends JFrame {
    private final JTextField idField, nombreField, motivoField;
    private final JComboBox<String> diaInicioBox, mesInicioBox, anioInicioBox;
    private final JComboBox<String> diaFinBox, mesFinBox, anioFinBox;
    private final JButton subirRecetaBtn, siguienteBtn, menuBtn, regresarBtn;
    private String recetaPath;

    public FormularioJustificanteFrame() {
        super("Solicitud de Justificante Médico");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 320);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Panel de campos
        JPanel main = new JPanel(new GridLayout(5,2,10,10));
        main.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        main.add(new JLabel("ID:"));
        idField = new JTextField(); main.add(idField);

        main.add(new JLabel("Nombre:"));
        nombreField = new JTextField(); nombreField.setEditable(false);
        main.add(nombreField);

        main.add(new JLabel("Motivo:"));
        motivoField = new JTextField(); main.add(motivoField);

        main.add(new JLabel("Inicio de Reposo:"));
        diaInicioBox  = new JComboBox<>(generarDias());
        mesInicioBox  = new JComboBox<>(generarMeses());
        anioInicioBox = new JComboBox<>(generarAnios(2020,2030));
        JPanel pIni = new JPanel(); pIni.add(diaInicioBox); pIni.add(mesInicioBox); pIni.add(anioInicioBox);
        main.add(pIni);

        main.add(new JLabel("Fin de Reposo:"));
        diaFinBox  = new JComboBox<>(generarDias());
        mesFinBox  = new JComboBox<>(generarMeses());
        anioFinBox = new JComboBox<>(generarAnios(2020,2030));
        JPanel pFin = new JPanel(); pFin.add(diaFinBox); pFin.add(mesFinBox); pFin.add(anioFinBox);
        main.add(pFin);

        // Botones
        subirRecetaBtn = new JButton("Subir receta (PDF)");
        siguienteBtn    = new JButton("Siguiente");
        menuBtn        = new JButton("Menú Principal");
        regresarBtn    = new JButton("Regresar");

        JPanel buttons = new JPanel();
        buttons.add(subirRecetaBtn);
        buttons.add(siguienteBtn);
        buttons.add(menuBtn);
        buttons.add(regresarBtn);

        add(main, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        // Listeners
        idField.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                String id = idField.getText().trim();
                if (!id.isEmpty()) buscarNombre(id);
                else nombreField.setText("");
            }
        });

        subirRecetaBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
            if (fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                recetaPath = f.getAbsolutePath();
            }
        });

        siguienteBtn.addActionListener(e -> {
            int folio = guardarSolicitud();
            if (folio != -1) {
                new CorreosProfesoresFrame(folio).setVisible(true);
                dispose();
            }
        });

        menuBtn.addActionListener(e -> {
            new MenuPacientesFrame().setVisible(true);
            dispose();
        });
        regresarBtn.addActionListener(e -> {
            new SeleccionarPacienteFrame().setVisible(true);
            dispose();
        });
    }

    private String[] generarDias() {
        String[] d = new String[31];
        for(int i=1;i<=31;i++) d[i-1]=String.valueOf(i);
        return d;
    }
    private String[] generarMeses() {
        return new String[]{"Enero","Febrero","Marzo","Abril","Mayo","Junio",
                            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    }
    private String[] generarAnios(int desde,int hasta) {
        String[] a = new String[hasta-desde+1];
        for(int i=0;i<a.length;i++) a[i]=String.valueOf(desde+i);
        return a;
    }

    private void buscarNombre(String id) {
        String sql = "SELECT nombre FROM InformacionAlumno WHERE id = ?";
        try (Connection c = ConexionSQLite.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,id);
            ResultSet rs = ps.executeQuery();
            nombreField.setText(rs.next() ? rs.getString("nombre") : "");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int guardarSolicitud() {
        String sql = "INSERT INTO JustificantePaciente "
                   + "(alumno_id,motivo,fecha_inicio,fecha_fin,receta_path) VALUES(?,?,?,?,?)";
        try (Connection c = ConexionSQLite.conectar();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, Integer.parseInt(idField.getText().trim()));
            ps.setString(2, motivoField.getText().trim());
            String ini = diaInicioBox.getSelectedItem()+" "+mesInicioBox.getSelectedItem()+" "+anioInicioBox.getSelectedItem();
            String fin = diaFinBox.getSelectedItem()+" "+mesFinBox.getSelectedItem()+" "+anioFinBox.getSelectedItem();
            ps.setString(3, ini);
            ps.setString(4, fin);
            ps.setString(5, recetaPath);
            ps.executeUpdate();
            ResultSet k = ps.getGeneratedKeys();
            if(k.next()) return k.getInt(1);
        } catch(SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormularioJustificanteFrame().setVisible(true));
    }
}
