package Emergencias;

public class Accidente {
    private String idEmergencia;
    private String fecha;
    private String lugar;
    private String descripcion;
    private String testigos;

    /**
     * Constructor vacío.
     */
    public Accidente() {
    }

    /**
     * Constructor con todos los campos.
     *
     * @param idEmergencia Identificador de la emergencia.
     * @param fecha        Fecha del accidente (formato DD/MM/AAAA).
     * @param lugar        Lugar donde ocurrió el accidente.
     * @param descripcion  Descripción detallada del accidente.
     * @param testigos     Nombres de testigos, separados por comas.
     */
    public Accidente(String idEmergencia, String fecha, String lugar,
            String descripcion, String testigos) {
        this.idEmergencia = idEmergencia;
        this.fecha = fecha;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.testigos = testigos;
    }

    // ——— Getters y setters ———

    public String getIdEmergencia() {
        return idEmergencia;
    }

    public void setIdEmergencia(String idEmergencia) {
        this.idEmergencia = idEmergencia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTestigos() {
        return testigos;
    }

    public void setTestigos(String testigos) {
        this.testigos = testigos;
    }

    // ——— Métodos auxiliares ———

    /**
     * Fusiona los datos de otra instancia en esta.
     * Útil para combinar la información recogida en distintas páginas del wizard.
     *
     * @param otro Otra instancia de Accidente con campos adicionales.
     * @return Esta misma instancia, con los nuevos datos incorporados.
     */
    public Accidente merge(Accidente otro) {
        if (otro == null) {
            return this;
        }
        if (otro.descripcion != null && !otro.descripcion.isEmpty()) {
            this.descripcion = otro.descripcion;
        }
        if (otro.testigos != null && !otro.testigos.isEmpty()) {
            this.testigos = otro.testigos;
        }
        return this;
    }

    @Override
    public String toString() {
        return "Accidente{" +
                "idEmergencia='" + idEmergencia + '\'' +
                ", fecha='" + fecha + '\'' +
                ", lugar='" + lugar + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", testigos='" + testigos + '\'' +
                '}';
    }
}
