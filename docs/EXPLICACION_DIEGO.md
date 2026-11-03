# Caso 5 tipos de mascotas

Type representa types y sus siete columnas. TypeRepository accede a la tabla mediante Spring Data JPA. TypeService define crear, actualizar, buscar y eliminar. TypeServiceImpl ejecuta las operaciones. TypeNotFoundException indica un ID inexistente.

El proyecto base no implementaba este servicio, por eso se agrega el soporte necesario. La creacion requiere ID nulo; H2 lo genera. Si active no se indica se usa true. Actualizar comprueba que el ID exista para evitar crear otro registro por accidente.

## Pruebas del Word

| Prueba | Comprobacion |
|---|---|
| testCreateType | ID generado y todos los campos persistidos |
| testUpdateType | Cambios de todos los campos, conservando ID |
| testFindTypeById | Recuperacion del registro creado para la prueba |
| testDeleteType | Registro ausente y excepcion al buscarlo |

TypeServiceTest usa SpringBootTest, perfil h2 y Transactional. Cada prueba crea sus datos y hace rollback al terminar. flush envia los cambios a H2 y clear vacia la cache para que la siguiente busqueda lea desde la base de datos. Son pruebas de integracion, aunque el Word las denomine unitarias.

TypeServiceMockitoTest contiene ocho pruebas unitarias aisladas: el mismo CRUD, buscar, eliminar y actualizar un ID inexistente, y rechazar creacion con ID asignado. Mock simula el repositorio, InjectMocks prepara el servicio, when establece respuestas, verify comprueba llamadas y assertThrows comprueba errores.

Eliminar un tipo utilizado por una mascota esta restringido por la clave foranea. El CRUD prueba un tipo propio sin mascotas; no modifica datos compartidos. No se implementa el reporte del grupo 6 porque el profesor asigno el grupo 5.

Con JDK 21 ejecuta `./mvnw.cmd clean test`. Para explicarlo en clase: preparar datos, ejecutar una operacion y comprobar el resultado. Consulta RESULTADOS.md para la validacion real.
