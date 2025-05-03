package GestionCitas;

import java.util.List;

public class NotificadorListaEspera {

    public static void notificarDisponibilidad(String fecha, String hora, String servicio) {
        try {
            List<ListaEsperaDAO.Espera> solicitudes = ListaEsperaDAO.obtenerSolicitudesPara(fecha, hora, servicio);

            for (ListaEsperaDAO.Espera espera : solicitudes) {
                // Mostrar ventana de notificación (una por paciente que coincida)
                new NotificacionCitasFrame(fecha, hora, servicio, espera.idPaciente); // ✅ Corregido: 4 parámetros

                // Marcar como notificada para no mostrar de nuevo
                ListaEsperaDAO.marcarNotificada(espera.idPaciente, fecha, hora, servicio);
            }

        } catch (Exception e) {
            System.err.println("Error al notificar disponibilidad: " + e.getMessage());
        }
    }
}
