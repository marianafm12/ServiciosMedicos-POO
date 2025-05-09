package Justificantes;

import java.io.File;
import java.time.LocalDate;

public class Justificante {
    private int folio;
    private String idPaciente;
    private String nombrePaciente;
    private String motivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String diagnostico;
    private File archivoReceta;

    public Justificante(int folio, String idPaciente, String nombrePaciente, String motivo, LocalDate fechaInicio,
                        LocalDate fechaFin, String diagnostico, File archivoReceta) {
        this.folio = folio;
        this.idPaciente = idPaciente;
        this.nombrePaciente = nombrePaciente;
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diagnostico = diagnostico;
        this.archivoReceta = archivoReceta;
    }

    public Justificante(String idPaciente, String nombrePaciente, String motivo, LocalDate fechaInicio,
                        LocalDate fechaFin, String diagnostico, File archivoReceta) {
        this(-1, idPaciente, nombrePaciente, motivo, fechaInicio, fechaFin, diagnostico, archivoReceta);
    }

    // Getters y Setters
    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public File getArchivoReceta() {
        return archivoReceta;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setArchivoReceta(File archivoReceta) {
        this.archivoReceta = archivoReceta;
    }
}
