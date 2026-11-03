# Resultados de validacion

Validacion local del 3 de noviembre de 2026 con JDK 21, Maven Wrapper 3.9.11, Spring Boot 3.5.14 y H2 en memoria.

Se ejecuto una compilacion limpia y la suite completa con `mvnw.cmd -o -B clean test`, usando una cache local de dependencias ya descargadas. Resultado: **BUILD SUCCESS; 27 pruebas, 0 fallos, 0 errores, 0 omitidas**.

| Clase | Pruebas | Fallos | Errores |
|---|---:|---:|---:|
| TypeServiceTest | 4 | 0 | 0 |
| TypeServiceMockitoTest | 8 | 0 | 0 |
| Pruebas originales de mascotas y contexto | 15 | 0 | 0 |
| Total | 27 | 0 | 0 |

Las 12 pruebas nuevas corresponden a Diego. Las 15 originales conservan la autoria del proyecto base. Los resultados detallados se generan en target/surefire-reports al ejecutar Maven; no se versionan archivos de compilacion.

Esta validacion no incluye el grupo 5 de Angel, que sigue pendiente. Tampoco sustituye las evidencias futuras de revision del PR, merge y resolucion del conflicto de equipo.
