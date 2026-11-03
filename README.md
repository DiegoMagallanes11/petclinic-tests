# PetClinic pruebas del laboratorio

## Features

### 1.  Unit Test  --> Tag v1.0.0

mvn test -Dspring.profiles.active=h2

## Alcance confirmado por el profesor

El equipo desarrolla el **caso 5 del Word** y el **grupo 5 de TASKS.md**. Son dos bloques diferentes; no corresponden a los seis casos completos ni al grupo 6 de tipos.

| Integrante | Responsabilidad | Estado |
|---|---|---|
| Diego Magallanes, DiegoMagallanes11 | Caso 5 del Word: CRUD de tipos | Implementado en feature/diego-caso-5; PR #1 revisado y aprobado por Ángel |
| Ángel Rosales, AngelRosales78 | Grupo 5: relacion veterinario especialidad | Implementado en feature/angel-grupo-5; pendiente revision de Diego |

## Ejecutar en Windows

Requisitos: JDK 21, Git e Internet para la primera descarga de Maven. Configura JAVA_HOME apuntando al JDK 21 instalado.

```powershell
git clone https://github.com/DiegoMagallanes11/petclinic-tests.git
cd petclinic-tests
git switch main
./mvnw.cmd clean test
```

En Linux o macOS: `./mvnw clean test`. El perfil predeterminado es H2 en memoria; no requiere instalar MySQL ni configurar credenciales.

Solo las pruebas de Diego: `./mvnw.cmd test "-Dtest=TypeServiceTest,TypeServiceMockitoTest"`.

## Parte de Diego

Se agregan Type, TypeRepository, TypeService, TypeServiceImpl y TypeNotFoundException porque el proyecto original solo implementaba el servicio de mascotas.

TypeServiceTest contiene las cuatro pruebas del Word: crear, actualizar, buscar por ID y eliminar. Usa H2, datos propios y rollback. TypeServiceMockitoTest agrega ocho pruebas aisladas del CRUD y errores. No implementa el reporte del grupo 6, fuera del alcance confirmado. No se modifican schema.sql ni data.sql.

- [Explicacion de Diego](docs/EXPLICACION_DIEGO.md).
- [Prompt para Angel](docs/PROMPT_ANGEL.md).
- [Reglas](docs/REGLAS_DE_TRABAJO.md).
- [Resultados](docs/RESULTADOS.md).

Basado en [jgomezz/petclinic_test](https://github.com/jgomezz/petclinic_test). Se conserva el historial original y la autoria de sus aportes.

## Aportes y conflictos del equipo

Diego: servicio de tipos, cuatro pruebas H2 y ocho pruebas con Mockito, documentacion y preparacion del repositorio.

Ángel: VetSpecialtyService y soporte JPA para veterinarios, especialidades y asignaciones con clave compuesta. Doce escenarios del grupo 5 cubiertos con Mockito y H2, mas validaciones y restricciones reales de base de datos. Consulta [la explicacion y distribucion de pruebas](docs/EXPLICACION_ANGEL.md) y [los resultados de Ángel](docs/RESULTADOS_ANGEL.md).

Se integraron el [PR #1](https://github.com/DiegoMagallanes11/petclinic-tests/pull/1) y el [PR #2](https://github.com/DiegoMagallanes11/petclinic-tests/pull/2), con revisiones cruzadas y commits propios. Angel ya es colaborador. La suite conjunta contiene 58 pruebas y GitHub Actions valido main correctamente.

Se realizo una [practica controlada de conflicto](docs/EJERCICIO_CONFLICTO.md), con conflicto real y merge de resolucion 6513637. Los dos cambios de practica fueron realizados por Diego; no se atribuyen a Angel. Los PR de implementacion se integraron sin conflicto. La aceptacion de esta practica como requisito de equipo corresponde al docente.

Consulta el [informe](docs/INFORME_LABORATORIO.md) y las [evidencias originales](docs/evidencias/).

Para la entrega: enlace al repositorio, captura real de `git log --all --graph --oneline`, README con aportes y conflictos, e informe con evidencias y conclusiones.

## Parte de Ángel

Angel desarrollo su grupo 5 desde la rama de Diego y lo publico inicialmente en su fork. Tras integrar el PR #1 se cambio la base del PR #2 a main y se integro con aprobacion de Diego. Ambos aportes estan disponibles en main.

No se modifican el servicio, entidad, repositorio ni pruebas de tipos de Diego. Se conservan schema.sql y data.sql. La regla de una sola especialidad principal se aplica en una transaccion con bloqueo del veterinario; el esquema original no tiene una restriccion adicional para esa regla.

```powershell
git switch main
./mvnw.cmd test "-Dtest=VetSpecialtyServiceMockitoTest,VetSpecialtyServiceTest"
./mvnw.cmd clean test
```

VetSpecialtyServiceMockitoTest prueba la logica de forma aislada. VetSpecialtyServiceTest usa H2, datos propios, flush y clear para verificar persistencia, y rollback automatico en cada prueba. Los informes reales se generan en target/surefire-reports.
