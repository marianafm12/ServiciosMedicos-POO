package Emergencias;

import javax.swing.*;

public class ValidadorAccidente {
    public static boolean validar(JTextField id, JTextField lugar, JTextArea descripcion, JTextField testigos) {
        if (id.getText().trim().isEmpty()) {
            showError("El ID de emergencia es obligatorio.");
            return false;
        }
        if (lugar.getText().trim().isEmpty()) {
            showError("El lugar del accidente es obligatorio.");
            return false;
        }
        if (descripcion.getText().trim().isEmpty()) {
            showError("La descripción es obligatoria.");
            return false;
        }
        if (testigos.getText().trim().isEmpty()) {
            showError("Debe especificar al menos un testigo.");
            return false;
        }
        return true;
    }

    private static void showError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Validación", JOptionPane.WARNING_MESSAGE);
    }
}
