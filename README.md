# PetClinic pruebas del laboratorio

## Features

### 1.  Unit Test  --> Tag v1.0.0

mvn test -Dspring.profiles.active=h2

## Alcance confirmado por el profesor

El equipo desarrolla el **caso 5 del Word** y el **grupo 5 de TASKS.md**. Son dos bloques diferentes; no corresponden a los seis casos completos ni al grupo 6 de tipos.

| Integrante | Responsabilidad | Estado |
|---|---|---|
| Diego Magallanes, DiegoMagallanes11 | Caso 5 del Word: CRUD de tipos | Implementado en feature/diego-caso-5; pendiente revision de Angel |
| Angel Rosales, AngelRosales78 | Grupo 5: relacion veterinario especialidad | Pendiente de desarrollar por Angel |

## Ejecutar en Windows

Requisitos: JDK 21, Git e Internet para la primera descarga de Maven. Configura JAVA_HOME apuntando al JDK 21 instalado.

```powershell
git clone https://github.com/DiegoMagallanes11/petclinic-tests.git
cd petclinic-tests
git switch feature/diego-caso-5
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

Angel: pendiente desarrollar el grupo 5 y registrar sus pruebas y commits propios.

Conflictos resueltos: ninguno por ahora. La revision del compaÃ±ero, los merges y el conflicto exigido deben documentarse cuando ocurran realmente. Cada integrante usa su rama y PR. Los commits se escriben en espaÃ±ol sin prefijos fix: ni feat:.

Para la entrega: enlace al repositorio, captura real de `git log --all --graph --oneline`, README con aportes y conflictos, e informe con evidencias y conclusiones.
