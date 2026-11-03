# Prompt para Angel Rosales

Soy Angel Rosales, GitHub AngelRosales78. Trabaja en mi parte del laboratorio en https://github.com/DiegoMagallanes11/petclinic-tests, basado en https://github.com/jgomezz/petclinic_test.

El profesor confirmo dos bloques para la pareja: Diego desarrolla el caso 5 del Word (CRUD de tipos); yo desarrollo el grupo 5 de TASKS.md (relacion veterinario especialidad). No debo desarrollar los seis casos ni el grupo 6.

Lee primero las instrucciones y el codigo real, schema.sql, data.sql y TASKS.md. Implementa VetSpecialtyService y su soporte. El proyecto base no implementa servicios de veterinarios y especialidades: agrega solo las entidades y repositorios necesarios para mi bloque. No modifiques el servicio de tipos de Diego.

Respeta vet_specialties: clave compuesta vet_id y specialty_id, certification_date, years_experience, is_primary y notes. Usa LocalDate para fechas y mapeo JPA correcto. Reutiliza las tablas vets y specialties existentes.

Completa los cuatro bloques del grupo 5:

1. Asignacion: testAssignSpecialtyToVet, testRemoveSpecialtyFromVet, testAssignDuplicate_ShouldFail.
2. Consultas: testFindSpecialtiesByVet, testFindVetsBySpecialty, testFindSpecialtiesByVet_Empty.
3. Principal: testSetPrimarySpecialty, testSetPrimarySpecialty_OnlyOnePrimary, testSetPrimarySpecialty_NotAssigned.
4. Experiencia y certificaciones: testFindVetsByMinExperience, testUpdateYearsExperience, testUpdateCertificationDate.

Usa JUnit 5 y Mockito para pruebas unitarias. Agrega pruebas H2 de asignacion, consultas y eliminacion con datos propios y rollback. Comprueba excepciones, rechaza duplicados sin sobrescribir datos y verifica que solo una relacion quede como principal. No confundas mocks con evidencia de restricciones reales de base de datos.

Preparacion en PowerShell, con JDK 21 y JAVA_HOME configurado:

```powershell
git clone https://github.com/DiegoMagallanes11/petclinic-tests.git
cd petclinic-tests
git fetch origin
git switch -c feature/angel-grupo-5 origin/feature/diego-caso-5
git config user.name "Angel Rosales"
# Sustituye por tu propio correo asociado a GitHub.
git config user.email "TU_CORREO_DE_GITHUB"
./mvnw.cmd clean test
```

Si el PR de Diego ya esta integrado, crea la rama desde origin/main. Si sigue abierto, documenta que tu rama depende del suyo. Para subir ramas Diego debe darte acceso como colaborador y debes aceptar la invitacion; como alternativa usa un fork y abre un PR.

Usa commits propios en español sin prefijos fix:, feat:, test: ni docs:. No uses la identidad de Diego ni hagas force push. Ejecuta tus pruebas y la suite completa, sube tu rama y abre un PR para revision de Diego. Revisa su PR desde tu cuenta.

Completa el README con tus aportes. Coordinen el conflicto requerido y documenten lo ocurrido sin inventarlo. Conserven capturas reales del historial, pruebas y revisiones para el informe final.
