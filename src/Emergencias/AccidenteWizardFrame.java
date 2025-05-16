package Emergencias;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AccidenteWizardFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cards;
    private JButton btnNext, btnBack, btnFinish;

    public AccidenteWizardFrame() {
        super("Registro de Accidente");
        initUI();
    }

    private void initUI() {
        // --- Configuración básica del JFrame ---
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        // --- Paneles “wizard” en CardLayout ---
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Importante: cada panel debe tener un nombre
        AccidentePage1Panel page1 = new AccidentePage1Panel();
        page1.setName("PAGE1");
        AccidentePage2Panel page2 = new AccidentePage2Panel();
        page2.setName("PAGE2");

        cards.add(page1, "PAGE1");
        cards.add(page2, "PAGE2");

        // --- Botones de navegación ---
        btnBack = new JButton("« Atrás");
        btnNext = new JButton("Siguiente »");
        btnFinish = new JButton("Finalizar");
        btnBack.setEnabled(false);
        btnFinish.setEnabled(false);

        btnBack.addActionListener(e -> navigate(-1));
        btnNext.addActionListener(e -> navigate(+1));
        btnFinish.addActionListener(e -> finish());

        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navBar.add(btnBack);
        navBar.add(btnNext);
        navBar.add(btnFinish);

        // --- Disposición final ---
        add(cards, BorderLayout.CENTER);
        add(navBar, BorderLayout.SOUTH);
    }

    /**
     * Navega en el CardLayout: +1 = siguiente, -1 = anterior.
     */
    private void navigate(int dir) {
        if (dir > 0) {
            cardLayout.next(cards);
        } else {
            cardLayout.previous(cards);
        }
        actualizarBotones();
    }

    /**
     * Habilita/deshabilita los botones según la página actual.
     */
    private void actualizarBotones() {
        String current = getCurrentCardName();
        // En la primera página no hay “Atrás”
        btnBack.setEnabled(!"PAGE1".equals(current));
        // En la última página no hay “Siguiente”
        btnNext.setEnabled(!"PAGE2".equals(current));
        // El botón “Finalizar” sólo en la segunda página
        btnFinish.setEnabled("PAGE2".equals(current));
    }

    /**
     * Recorre los componentes del cardLayout y devuelve el nombre
     * del panel que está visible en este momento.
     */
    private String getCurrentCardName() {
        for (Component comp : cards.getComponents()) {
            if (comp.isVisible()) {
                return comp.getName();
            }
        }
        return null;
    }

    /**
     * Acción al pulsar “Finalizar”: valida y guarda.
     */
    private void finish() {
        // Combinar modelos de las dos páginas
        Accidente a = new AccidentePage1Panel().getModel()
                .merge(new AccidentePage2Panel().getModel());

        if (!ValidationUtils.validate(a)) {
            JOptionPane.showMessageDialog(this,
                    "Corrija los errores antes de guardar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            new AccidenteDAO().save(a);
            JOptionPane.showMessageDialog(this,
                    "Accidente registrado con éxito.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccidenteWizardFrame().setVisible(true));
    }
}
