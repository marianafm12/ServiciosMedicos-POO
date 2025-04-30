package GestionCitas;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestValidacionesCita {

    @Test
    public void testNombreValido() {
        assertTrue(ValidacionesCita.esNombreValido("Juan Perez"));
    }

    @Test
    public void testNombreInvalido() {
        assertFalse(ValidacionesCita.esNombreValido("Juan123"));
    }

    @Test
    public void testIdValido() {
        assertTrue(ValidacionesCita.esIdValido("12345"));
    }

    @Test
    public void testIdInvalido() {
        assertFalse(ValidacionesCita.esIdValido("123A5"));
    }

    @Test
    public void testFechaValida() {
        assertTrue(ValidacionesCita.esFechaValida(25, 12, 2025));
    }

    @Test
    public void testFechaInvalida() {
        assertFalse(ValidacionesCita.esFechaValida(1, 1, 2024));
    }
}
