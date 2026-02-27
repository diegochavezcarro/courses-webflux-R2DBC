# courses-webflux-R2DBC

    
## Comandos

### Variables sensibles por entorno

Puedes partir de la plantilla sin secretos:

```bash
cp .env.example .env
```

Luego exportar variables desde `.env`:

```bash
set -a && source .env && set +a
```

Definir credenciales y conexión DB antes de ejecutar la app/tests:

```bash
export DB_R2DBC_URL='r2dbc:postgresql://localhost:5432/coursesdb'
export DB_USERNAME='appuser'
export DB_PASSWORD='tu_password_real'
```

Opcionalmente para tests puedes sobrescribir con variables dedicadas:

```bash
export TEST_R2DBC_URL="$DB_R2DBC_URL"
export TEST_DB_USERNAME="$DB_USERNAME"
export TEST_DB_PASSWORD="$DB_PASSWORD"
```

```bash
./mvnw spring-boot:run
```

### Correr tests

Ejecutar suite rápida (sin tests de integración con DB):

```bash
./mvnw test
```

Ejecutar también tests de integración WebFlux/R2DBC con DB manual iniciada:

```bash
RUN_DB_TESTS=true ./mvnw test
```

Ejecutar una clase de test específica:

```bash
./mvnw -Dtest=CoursesMvcJdbcApplicationTests test
```

```bash
curl "http://localhost:8080/courses?limit=100"
```

```bash
BASE_URL=http://localhost:8080 LIMIT=100 MAX_VUS=1000 \
k6 run --summary-export=summary-mvc-loom-pool30-100l-1000vu-simple.json k6-simple.js | tee run-mvc-loom-pool30-100l-1000vu-simple.txt
```
