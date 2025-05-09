package Justificantes;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

public class GeneradorPDFJustificante {

    public static File generar(Justificante j) {
        try {
            // Carpeta base
            String carpetaBase = System.getProperty("user.home") + File.separator + "JustificantesGenerados";
            File carpeta = new File(carpetaBase);
            if (!carpeta.exists()) carpeta.mkdirs();

            // Nombre del archivo
            String nombreArchivo = String.format("Justificante_Folio%d_%s.pdf",
                    j.getFolio(), j.getNombrePaciente().replaceAll("\\s+", "_"));
            File file = new File(carpeta, nombreArchivo);

            // Crear documento
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            Font titulo = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font subtitulo = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font texto = new Font(Font.HELVETICA, 12);
            Font negrita = new Font(Font.HELVETICA, 12, Font.BOLD);

            doc.add(new Paragraph("Justificante Médico", titulo));
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("Folio: " + j.getFolio(), texto));
            doc.add(new Paragraph("ID del Paciente: " + j.getIdPaciente(), texto));
            doc.add(new Paragraph("Nombre del Paciente: " + j.getNombrePaciente(), texto));
            doc.add(new Paragraph("Motivo: " + j.getMotivo(), texto));

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            doc.add(new Paragraph("Inicio de Reposo: " + j.getFechaInicio().format(fmt), texto));
            doc.add(new Paragraph("Fin de Reposo: " + j.getFechaFin().format(fmt), texto));

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Diagnóstico y Observaciones:", subtitulo));
            doc.add(new Paragraph(j.getDiagnostico(), texto));

            doc.add(new Paragraph(" "));

            // Estado de resolución
            doc.add(new Paragraph("Estado del Justificante: " + j.getEstado(), negrita));
            if (j.getFechaResolucion() != null && j.getResueltoPor() != null) {
                doc.add(new Paragraph("Resuelto por: " + j.getResueltoPor(), texto));
                doc.add(new Paragraph("Fecha de Resolución: " + j.getFechaResolucion().format(fmt), texto));
            }

            doc.add(new Paragraph("\n\n________________________"));
            doc.add(new Paragraph(j.getResueltoPor() != null ? j.getResueltoPor() : "Médico Responsable", texto));

            doc.close();
            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
