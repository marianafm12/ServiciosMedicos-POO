package Inicio;

public class SesionUsuario {
    private static int idPaciente;

    public static void iniciarSesion(int id) {
        idPaciente = id;
    }

    public static int getPacienteActual() {
        return idPaciente;
    }
}
