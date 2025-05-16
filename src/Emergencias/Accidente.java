package Emergencias;

public class Accidente {
    private final String idEmergencia, fecha, lugar, descripcion, testigos;

    public Accidente(String idEmergencia, String fecha, String lugar, String descripcion, String testigos) {
        this.idEmergencia = idEmergencia;
        this.fecha = fecha;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.testigos = testigos;
    }

    public String id() {
        return idEmergencia;
    }

    public String fecha() {
        return fecha;
    }

    public String lugar() {
        return lugar;
    }

    public String descripcion() {
        return descripcion;
    }

    public String testigos() {
        return testigos;
    }

    public int dia() {
        try {
            return Integer.parseInt(fecha.split("/")[0]);
        } catch (Exception e) {
            return 1;
        }
    }

    public int mes() {
        try {
            return Integer.parseInt(fecha.split("/")[1]);
        } catch (Exception e) {
            return 1;
        }
    }

    public int anio() {
        try {
            return Integer.parseInt(fecha.split("/")[2]);
        } catch (Exception e) {
            return 2024;
        }
    }
}
