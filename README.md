# Cuadernos y Notas — Tarea Evaluativa AD05

Aplicación web para gestionar cuadernos de estudio y las notas asociadas a cada
uno. Spring Boot + Spring Data JPA + Spring MVC + Thymeleaf + Bootstrap, con
persistencia en Oracle.

## Tecnologías

- Java 21
- Spring Boot 4.0.9 (Spring Web MVC, Spring Data JPA, Thymeleaf, Validation)
- Oracle XE 21c (driver `ojdbc17`)
- Bootstrap 5.3.8 + Bootstrap Icons (por CDN)
- Maven

## Preparar la base de datos

Se necesita un usuario propio de Oracle (no se usa `SYSTEM` para conectar la
app). Conectado a `XEPDB1` con una cuenta administradora, crea el usuario
`TE5` y dale los permisos necesarios:

```sql
CREATE USER TE5 IDENTIFIED BY "tu_contrasena_aqui";
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO TE5;
ALTER USER TE5 QUOTA 50M ON USERS;
```

No hace falta crear las tablas a mano: la aplicación arranca con
`spring.jpa.hibernate.ddl-auto=update`, así que Hibernate crea (o actualiza)
las tablas `cuaderno` y `nota` la primera vez que se ejecuta. Los datos se
conservan entre arranques.

## Configuración

La conexión se define en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XEPDB1
spring.datasource.username=TE5
spring.datasource.password=${DB_PASSWORD}
```

La contraseña **no** está en el repositorio: se lee de la variable de entorno
`DB_PASSWORD`, que hay que definir antes de arrancar con la contraseña real
del usuario `TE5` creado en el paso anterior.

La aplicación escucha en el puerto **8086** (`server.port=8086`).

## Arrancar la aplicación

Con Maven, definiendo la contraseña en la misma línea:

```bash
DB_PASSWORD=tu_contrasena_aqui mvn spring-boot:run
```

Desde un IDE (p.ej. Eclipse): configurar `DB_PASSWORD` como variable de
entorno de la configuración de ejecución y lanzar la clase
`Te5CuadernosNotasApplication`.

Con Oracle XE arrancado y el usuario `TE5` ya creado, la app queda disponible
en [http://localhost:8086](http://localhost:8086) (redirige automáticamente
al listado de cuadernos).

## Funcionalidad

- **Cuadernos**: alta, listado, edición y borrado (borra también sus notas,
  mediante cascada JPA).
- **Notas**: alta, listado (con filtro opcional por cuaderno), edición y
  borrado. El formulario incluye un selector con los cuadernos existentes,
  obtenidos siempre de la base de datos.
