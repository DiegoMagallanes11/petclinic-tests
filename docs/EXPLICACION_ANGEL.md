# Grupo 5 relacion veterinario especialidad

Autor: Ángel Rosales, GitHub AngelRosales78.

El servicio asigna especialidades a veterinarios, consulta la relacion, elige una especialidad principal y actualiza experiencia y certificacion. Corresponde al grupo 5 de TASKS.md y al caso 6 del Word. El caso 5 del Word pertenece a Diego y permanece intacto.

## Estructura

Vet y Specialty representan las tablas existentes vets y specialties. VetSpecialty representa vet_specialties y sus campos certification_date (LocalDate), years_experience, is_primary y notes. VetSpecialtyId usa EmbeddedId: vet_id y specialty_id forman juntos la identidad de una asignacion. No se agrega un ID artificial ni se cambia el esquema o los datos iniciales.

VetSpecialtyRepository implementa las consultas de la relacion. VetSpecialtyService define las operaciones; VetSpecialtyServiceImpl aplica las reglas y las transacciones. Las asignaciones nuevas usan persist, mediante Persistable, para que un ID compuesto asignado no convierta una insercion en una actualizacion silenciosa.

## Reglas

- Asignar requiere un veterinario y una especialidad existentes. Repetir la misma pareja lanza DuplicateVetSpecialtyException y conserva los datos anteriores. La clave primaria tambien rechaza duplicados directamente en H2.
- Eliminar borra solo la relacion; conserva veterinario y especialidad. Una relacion inexistente lanza VetSpecialtyNotFoundException.
- Las consultas devuelven entidades de especialidad o veterinario, ordenadas por ID; sin coincidencias devuelven listas vacias.
- Marcar una principal desmarca las otras del mismo veterinario, sin afectar a otros veterinarios. Primero se comprueba que la relacion exista. Una transaccion y un bloqueo pesimista del veterinario serializan los cambios realizados mediante el servicio. El esquema original no impide multiples principales escritas directamente por SQL; no se modifico esa estructura. Las pruebas verifican el cambio secuencial, no simulan concurrencia.
- El filtro de experiencia incluye el limite (mayor o igual) y se aplica a la especialidad solicitada. La experiencia admite cero y rechaza negativos. La certificacion requiere una fecha no nula. Los cambios conservan la clave y los restantes campos.

## Escenarios del laboratorio

Cada escenario se ejecuta tanto en VetSpecialtyServiceMockitoTest como en VetSpecialtyServiceTest.

| Prueba | Resultado comprobado |
|---|---|
| testAssignSpecialtyToVet | Asignacion con clave compuesta y valores iniciales |
| testRemoveSpecialtyFromVet | Relacion eliminada, padres y otras relaciones conservados |
| testAssignDuplicate_ShouldFail | Excepcion sin sobrescribir experiencia, fecha, nota o principal |
| testFindSpecialtiesByVet | Especialidades del veterinario solicitado |
| testFindVetsBySpecialty | Veterinarios de la especialidad solicitada |
| testFindSpecialtiesByVet_Empty | Lista vacia para veterinario sin asignaciones |
| testSetPrimarySpecialty | Relacion marcada como principal |
| testSetPrimarySpecialty_OnlyOnePrimary | Cambio de principal e independencia entre veterinarios |
| testSetPrimarySpecialty_NotAssigned | Excepcion sin cambiar la principal anterior |
| testFindVetsByMinExperience | Limite inclusivo y exclusion de otra especialidad |
| testUpdateYearsExperience | Experiencia actualizada y persistida, incluido cero |
| testUpdateCertificationDate | Fecha actualizada y persistida, incluida fecha bisiesta |

Mockito agrega cinco pruebas de validaciones: veterinario inexistente, especialidad inexistente, eliminacion no asignada, experiencia negativa y parametros faltantes. Se verifican respuestas y llamadas, incluida la ausencia de escrituras en errores.

H2 agrega dos pruebas de restricciones reales: duplicado de clave compuesta y clave foranea hacia una especialidad inexistente. Los datos propios usan veterinarios 81001 a 81003 y especialidades 82001 a 82002. BeforeEach los crea dentro de la transaccion de cada prueba; Transactional revierte todo al finalizar, aunque la prueba falle. flush envia cambios y clear elimina la cache antes de volver a leer. Mockito no usa ni altera la base.

## Trabajo en equipo

La rama feature/angel-grupo-5 nace de feature/diego-caso-5, cuyo PR #1 estaba abierto. Los commits de Ángel usan su identidad local del repositorio. Diego debe revisar el PR de Ángel antes de integrarlo. No se han fabricado conflictos, capturas, merges ni revisiones. Los resultados efectivos se registran en RESULTADOS_ANGEL.md.
