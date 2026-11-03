# Pruebas de tipos y asignaciones de especialidades en PetClinic

**Curso:** Construcción y Pruebas de Software  
**Integrantes:** Diego Magallanes y Ángel Rosales  
**Fecha:** 3 de noviembre de 2026  
**Repositorio:** https://github.com/DiegoMagallanes11/petclinic-tests

## Objetivo

Verificar las operaciones del caso 5 del Word, tipos de mascotas, y del grupo 5 de TASKS.md, relación entre veterinarios y especialidades. El trabajo combina pruebas unitarias con Mockito y pruebas de integración con una base H2, conservando el esquema y los datos originales del proyecto.

## Entorno y metodología

Se utilizó Java 21, Maven Wrapper 3.9.11, Spring Boot 3.5.14, JUnit 5, Mockito y H2. El proyecto parte de https://github.com/jgomezz/petclinic_test y conserva su historial.

Las pruebas siguen la secuencia preparar datos, ejecutar una operación y comprobar el resultado. Mockito simula los repositorios para aislar la lógica del servicio. Las pruebas H2 ejecutan la persistencia real y usan datos propios y rollback por prueba. Las operaciones flush y clear permiten verificar los datos después de enviarlos a la base y limpiar la caché de entidades.

## Distribución del trabajo

| Integrante | Alcance | Pruebas nuevas |
|---|---|---:|
| Diego Magallanes | Caso 5 del Word: CRUD de tipos | 12 |
| Ángel Rosales | Grupo 5 de TASKS.md: relación veterinario especialidad | 31 |

Diego trabajó en feature/diego-caso-5. Ángel partió de esa rama y publicó feature/angel-grupo-5 desde su fork. El PR de Ángel apunta temporalmente a la rama de Diego para mostrar únicamente sus cambios.

## Caso 5 tipos de mascotas

Se implementaron Type, TypeRepository, TypeService, TypeServiceImpl y TypeNotFoundException. Type representa las columnas existentes de types. El servicio crea, actualiza, busca por ID y elimina. Al actualizar comprueba que el ID exista; la creación utiliza un ID generado.

TypeServiceTest contiene cuatro pruebas de persistencia: creación con ID generado, actualización de todos los campos, búsqueda del registro creado y eliminación confirmada mediante ausencia del registro y excepción al buscarlo. TypeServiceMockitoTest contiene ocho pruebas aisladas del CRUD y de los casos de error.

La eliminación se verifica con un tipo sin mascotas asociadas, puesto que el esquema restringe la eliminación de tipos utilizados por mascotas.

## Grupo 5 relación veterinario especialidad

Se implementó VetSpecialtyService con soporte JPA para vet_specialties y una clave compuesta de veterinario y especialidad. Se cubren los doce escenarios asignados: asignar, quitar, rechazar duplicados, consultar en ambas direcciones, consulta vacía, elegir principal, conservar una sola principal, rechazar principal no asignada, filtrar por experiencia y actualizar experiencia y certificación.

Ángel añadió 17 pruebas con Mockito y 14 con H2. Las pruebas reales comprueban también que la clave compuesta rechace duplicados y que una clave foránea rechace una especialidad inexistente. La eliminación conserva los registros de veterinario y especialidad.

El cambio de principal se realiza en una transacción con bloqueo del veterinario. La revisión confirma el comportamiento secuencial probado; no se afirma haber probado concurrencia ni una restricción adicional de principal única en el esquema.

## Resultados verificados

Se descargó y revisó el commit de Ángel 79bf739a34c7f0571c1b5448690ae79d85eff422. Se ejecutó Maven con JDK 21 y dependencias locales: `mvnw.cmd -o -B test`. La compilación y la suite conjunta finalizaron con BUILD SUCCESS.

| Clase | Pruebas | Fallos | Errores |
|---|---:|---:|---:|
| TypeServiceTest | 4 | 0 | 0 |
| TypeServiceMockitoTest | 8 | 0 | 0 |
| VetSpecialtyServiceTest | 14 | 0 | 0 |
| VetSpecialtyServiceMockitoTest | 17 | 0 | 0 |
| Pruebas originales de mascotas y contexto | 15 | 0 | 0 |
| **Total** | **58** | **0** | **0** |

No hubo pruebas omitidas. Los resultados corresponden al código combinado de la rama de Ángel; GitHub Actions tambien valido satisfactoriamente main despues de ambas integraciones.

## Evidencias y revisión del equipo

- PR de Diego: https://github.com/DiegoMagallanes11/petclinic-tests/pull/1. Ángel registró su aprobación.
- PR de Ángel: https://github.com/DiegoMagallanes11/petclinic-tests/pull/2. Diego registró su aprobación después de revisar el código y ejecutar las 58 pruebas.
- Se autorizó la ejecución de los workflows del fork de Ángel.
- El archivo revision-equipo.log conserva la salida local real de la ejecución conjunta.
- El archivo historial_git.txt conserva el historial local del código revisado.

## Integracion verificada

AngelRosales78 acepto la invitacion y figura como colaborador. Se integraron ambos PR mediante merge, conservando los commits y las revisiones:

- PR #1: merge eee03cdb78adf3cd94b01293cfb2ee920d8419b7, revisado por Angel.
- PR #2: se cambio su base a main y se integro en 80dd195953c001fb6c98050106343e99991d5e8a, revisado por Diego.
- GitHub Actions sobre main, commit 80dd195, termino con resultado success.

La repeticion local posterior al merge se detuvo por bloqueo del entorno; la validacion local conjunta de 58 pruebas y la ejecucion automatica de main si finalizaron correctamente.

## Practica controlada de conflicto

Los PR #1 y #2 se integraron automaticamente, sin conflictos. Posteriormente Diego realizo una practica deliberada en las ramas feature/cierre-laboratorio y practica/validacion-mockito. Ambas modificaron la misma linea de docs/EJERCICIO_CONFLICTO.md: una describia H2 y la otra Mockito. Git detecto un conflicto real de contenido y detuvo el merge.

La resolucion conservo las dos descripciones y se registro en el merge 6513637. Se guardaron la salida original de Git, los marcadores y el historial en docs/evidencias. Ambos commits de practica tienen la identidad de Diego; no se presentan como aportes de Angel ni como un conflicto surgido durante la implementacion. El docente debe confirmar si esta practica satisface el requisito de conflicto en equipo.

Los cambios documentales finales se proponen en un PR separado y requieren revision de Angel. No se simula esa aprobacion.

## Conclusiones

1. Las pruebas con Mockito permiten verificar la lógica del servicio de forma aislada, mientras que H2 comprueba la persistencia y las restricciones reales; ambos enfoques aportan evidencia diferente.
2. La clave compuesta identifica de forma conjunta al veterinario y la especialidad. Las pruebas comprobaron el rechazo de duplicados y la conservación de los datos de una asignación existente.
3. La ejecución conjunta de 58 pruebas sin fallos respalda el comportamiento del alcance implementado. Las revisiones y las integraciones quedaron registradas; la practica de conflicto se documento de manera separada y transparente, pendiente de valoracion por el docente.
