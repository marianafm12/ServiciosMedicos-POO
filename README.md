# ServiciosMedicos-POO
Proyecto final de la materia Programación Orientada a Objetos en el cual se creó un sistema desde cero enfocado para otimizar servicios médicos UDLAP


# Estructura del Proyecto
  De dividó el proyecto en diferentes áreas, la cual se le asignó a cada uno de los integrantes del equipo.
      | **Package | Descripción | Integrante** | 
      | ---- | -------- | --- | 
      | BaseDeDatos | Se incluyen los archivos de la base de datos como la conexión con SQLite y la clase con la que al ingresar el ID de un alumno automáticamnete se buscan sus otros datos | Mariana | 
      | Consultas | Códigos del área de consultas destinado a cunado el médico se encuentra en consulta y se hace un registro de los síntomas, diagnósitco e infomación importante de la consulta | - | 
      | Emergencias | Área destinada a las emergencias, tanto botón de pánico, registro de una llamada de mergencia y llenar el reporte de un accidente | Mariana
      | GestiónCitas | Área que se ejecuta desde el punto de vista del paciente en el que puede agendar una cita para diferentes usos como Consulta, Examen médico y Enfermería; estas sitas se pueden modificar o cancelar y si hay una fecha que ya está ocupada se puede agregar a una lista de espera y te notifica cuando ya está libre la fecha deseada | Arlette | 
      | GestionEnfermedades | äre en la que permite a un médico acceder al historial médico de un paciente en específico y desde el punto de vista del paciente le permite acceder a su propio hitoiral médico siendo visible la información del registro y las consultas que ha tenido | Robbie | 
      | Inicio | Interfáces inciales como lo son las de LogIn y la intefaz base en la que hay paneles fijos como lo son el del título, bienvenida al usuario y cerrar sesión, paneles semi-fijos como son el del menú, en el cual sólo cambia dependiendo del Id con el que se inicia sesión (personal médico o paciente) mostrando solo las opciones que son válidas para cada caso y el panel que se actualiza es el panel central en el que se muestra el JFraame dependiendo del botón del menú que se presiona | Mariana | 
      | Justificantes | Dentro de esto se puede iniciar la solicitud de un justificante médico si se tuvo consulta externa a la uniersidad y el médico puede aprobar el justificante o rechazarlo, por otro lado el médico puede generar un justificante médico desde una consult interna | Eduardo | 
      | Modelo | Código necesario para gestionar la lista de espera de gestión de citas | Arlette | 
      | Registro | Código correspondientes al registro de pacientes nuevos, se usa para crear inicialmente un nuevo expediente médico y dar de alta a pacientes | Sebastián | 
      | Utilidades | Carpeta que contiene las clases base como son los colores de las interfaces y JFrames, la barra de controles (minimizar, maximizar y cerrar), entre otras clases que se usaron en todo el programa | Mariana | 
     
      
