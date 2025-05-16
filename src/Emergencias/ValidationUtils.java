package Emergencias;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ValidationUtils {
    public static boolean validate(Accidente a) {
        // ID no vacío
        if (a.getIdEmergencia().isEmpty())
            return false;
        // Fecha válida (no futura)
        try {
            String[] p = a.getFecha().split("/");
            LocalDate f = LocalDate.of(
                    Integer.parseInt(p[2]),
                    Integer.parseInt(p[1]),
                    Integer.parseInt(p[0]));
            if (f.isAfter(LocalDate.now()))
                return false;
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException ex) {
            return false;
        }
        // Lugar no vacío
        if (a.getLugar().isEmpty())
            return false;
        // Descripción al menos 10 caracteres
        if (a.getDescripcion() == null || a.getDescripcion().length() < 10)
            return false;
        // Testigos opcional
        return true;
    }
}
