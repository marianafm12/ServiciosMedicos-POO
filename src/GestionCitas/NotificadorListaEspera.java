package GestionCitas;

import java.util.List;
import Modelo.Espera;

public class NotificadorListaEspera {

    public static void notificarDisponibilidad(String fecha, String hora, String servicio) {
        try {
            List<Espera> solicitudes = ListaEsperaDAO.obtenerSolicitudesPara(fecha, hora, servicio);

            for (Espera espera : solicitudes) {
                // Mostrar ventana de notificación (una por paciente que coincida)
                new NotificacionCitasFrame(fecha, hora, servicio, espera.getIdPaciente());

                // Marcar como notificada para no mostrar de nuevo
                ListaEsperaDAO.marcarNotificada(espera.getIdPaciente(), fecha, hora, servicio);
            }
        } catch (Exception e) {
            System.err.println("Error al notificar disponibilidad: " + e.getMessage());
        }
    }
}
