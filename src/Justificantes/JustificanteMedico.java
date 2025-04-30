package Justificantes;

public class JustificanteMedico {
    private String motivo;
    private int dias;

    public JustificanteMedico(String motivo, int dias) {
        this.motivo = motivo;
        this.dias = dias;
    }

    @Override
    public String toString() {
        return "Motivo: " + motivo + "\nDías de ausencia: " + dias;
    }
}
