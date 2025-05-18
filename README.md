# ServiciosMedicos-POO
Proyecto final de la materia Programación Orientada a Objetos en el cual se creó un sistema desde cero enfocado para otimizar servicios médicos UDLAP


# Estructura del Proyecto
  De dividó el proyecto en diferentes áreas, la cual se le asignó a cada uno de los integrantes del equipo.
  
| Package               | Descripción                                                                                                                                                                                               | Integrante |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------- |
| BaseDeDatos           | Se incluyen los archivos de la base de datos, como la conexión con SQLite y la clase que, al ingresar el ID de un alumno, automáticamente busca sus otros datos.                                       | Mariana    |
| Consultas             | Códigos del área de consultas destinados a cuando el médico está en consulta y se registran los síntomas, el diagnóstico y la información relevante de la consulta.                                      | Mariana, Robbie, Serenity(inicialmente)           |
| Emergencias           | Área destinada a emergencias: botón de pánico, registro de llamada de emergencia y llenado del reporte de un accidente.                                                                                   | Mariana    |
| GestiónCitas          | Perspectiva del paciente para agendar citas de Consulta, Examen médico y Enfermería; permite modificar, cancelar y, en caso de fecha ocupada, agregarse a lista de espera con notificación al liberarse. | Arlette    |
| GestionEnfermedades   | Permite al médico acceder al historial médico de un paciente específico y al paciente visualizar su propio historial, con información de registros y consultas realizadas.                               | Robbie     |
| Inicio                | Interfaces iniciales (Login y base) con paneles fijos (título, bienvenida y cerrar sesión), menú dinámico según rol (médico o paciente) y área central que muestra el `JFrame` correspondiente.           | Mariana    |
| Justificantes         | Gestión de solicitudes de justificantes médicos (externos e internos); el médico puede aprobar, rechazar o generar justificantes desde consultas internas.                                             | Eduardo    |
| Modelo                | Código para gestionar la lista de espera de citas.                                                                                                                                                         | Arlette    |
| Registro              | Código para el registro de nuevos pacientes, creación de expediente médico y altas de pacientes.                                                                                                           | Sebastián  |
| Utilidades            | Clases base (colores de interfaces, barra de controles, etc.) y componentes reutilizables empleados en toda la aplicación.                                                                               | Mariana    |

