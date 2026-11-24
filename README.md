# PetClinic Tests

Proyecto académico de **pruebas de software con Java, Spring Boot, JUnit 5, Mockito y H2**. Trabajo de Diego Magallanes y Ángel Rosales.

## Qué muestra

- Pruebas aisladas de servicios con Mockito.
- Pruebas de integración con JPA y H2.
- Escenarios CRUD y manejo de errores.
- Trabajo con ramas, revisiones y pull requests.

## Aportes del equipo

| Integrante | Alcance |
|---|---|
| Diego Magallanes (`DiegoMagallanes11`) | Servicio de tipos de mascota, pruebas CRUD con H2 y Mockito, documentación y preparación del repositorio. |
| Ángel Rosales (`AngelRosales78`) | Relación veterinario-especialidad, soporte JPA y pruebas de asignaciones con Mockito y H2. |

Los documentos de cada integrante contienen el detalle. El proyecto parte de una base académica existente; no se atribuye todo el código a un único autor.

## Ejecutar

Requisitos: JDK 21, Git e Internet para la primera descarga de dependencias. Configurar `JAVA_HOME`.

```powershell
git clone https://github.com/DiegoMagallanes11/petclinic-tests.git
cd petclinic-tests
.\mvnw.cmd clean test
```

Linux/macOS: `./mvnw clean test`. El perfil predeterminado usa H2; no requiere una base MySQL real.

Solo el bloque de tipos:

```powershell
.\mvnw.cmd test "-Dtest=TypeServiceTest,TypeServiceMockitoTest"
```

Los resultados se generan en `target/surefire-reports`. El estado de automatización está en [Actions](https://github.com/DiegoMagallanes11/petclinic-tests/actions). Esta actualización organiza la documentación; no añade una nueva ejecución de pruebas ni cambia código.

## Documentación

- [Aporte de Diego](docs/EXPLICACION_DIEGO.md)
- [Aporte de Ángel](docs/EXPLICACION_ANGEL.md)
- [Resultados de Diego](docs/RESULTADOS.md)
- [Resultados de Ángel](docs/RESULTADOS_ANGEL.md)
- [Reglas de trabajo](docs/REGLAS_DE_TRABAJO.md)
- [Pull requests y revisiones](https://github.com/DiegoMagallanes11/petclinic-tests/pulls?q=is%3Apr)

Los informes reflejan la entrega en que se escribieron. Para el estado de una integración, consultar el pull request correspondiente. No se presenta una simulación de conflicto como un conflicto real entre integrantes.

## Procedencia

Basado en [jgomezz/petclinic_test](https://github.com/jgomezz/petclinic_test), conservando el historial y la autoría. Se utilizaron herramientas de IA como apoyo al trabajo académico.
