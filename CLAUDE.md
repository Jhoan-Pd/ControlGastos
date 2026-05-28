# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Proyecto

**control-gastos** — aplicación de gestión financiera personal desarrollada como proyecto universitario.  
Backend Spring Boot 3.2 (Java 17) + frontend React 18/TypeScript/Vite.  
La API REST corre en `http://localhost:8080/api`; el frontend en `http://localhost:3001`.

---

## Comandos

### Backend
```bash
mvn spring-boot:run          # compilar y ejecutar
mvn clean package -DskipTests  # solo compilar
java -jar target/control-gastos-*.jar
```

### Frontend
```bash
cd frontend
npm install     # primera vez
npm run dev     # http://localhost:3001
npm run build
npm run lint
```

**Prerequisito:** base de datos PostgreSQL corriendo en `localhost:5432` con la BD `controlgastos` creada:
```sql
CREATE DATABASE controlgastos;
```

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Spring Boot 3.2, Java 17, Maven |
| Persistencia | Spring Data JPA (Hibernate), PostgreSQL |
| API | REST JSON, CORS habilitado para `localhost:3001` y `localhost:5173` |
| Frontend | React 18, TypeScript, Vite (puerto 3001) |
| Estilos | Tailwind CSS |
| HTTP client | Axios (`frontend/services/api.ts` → `http://localhost:8080/api`) |
| Autenticación | Sin JWT; sesión basada en `localStorage` (clave `usuario_cg`) |

---

## Arquitectura del backend

### Estructura de paquetes (`src/main/java/com/finanzas/controlgastos/`)

```
ControGastosApplication.java   (@SpringBootApplication)
config/
  CorsConfig.java              (permite localhost:3001 y localhost:5173)
  DataInitializer.java         (CommandLineRunner — inserta categorías globales al arrancar si tabla vacía)
model/
  Transaccion.java             (@Entity @Inheritance(JOINED) — clase abstracta base)
  Gasto.java                   (@Entity — extiende Transaccion, añade esFijo)
  Ingreso.java                 (@Entity — extiende Transaccion, añade fuenteIngreso)
  Categoria.java               (@Entity — árbol auto-referenciado con padre/subcategorias)
  Presupuesto.java             (@Entity — tiene descripcion, saldoDisponible, fechaInicio, fechaFin)
  Usuario.java                 (@Entity — nombre, email, contrasena; tiene @OneToMany transacciones)
repository/                    (JpaRepository<Entidad, Integer> por entidad)
service/
  IXxxService.java             (interfaz por entidad)
  XxxService.java              (@Service — implementación con mapeo entidad↔DTO)
controller/                    (@RestController + @RequestMapping("/api/xxx") por entidad)
dto/
  request/                     (XxxRequest — payload de entrada sin exponer entidades)
  response/                    (XxxResponse — payload de salida; GastoResponse e IngresoResponse tienen CategoriaResumen anidada)
exception/
  RecursoNoEncontradoException.java  (@ResponseStatus 404)
  GlobalExceptionHandler.java        (@RestControllerAdvice — respuestas de error uniformes)
```

### Jerarquía de herencia (núcleo del diseño POO)

```
Transaccion  (@Entity @Inheritance JOINED — abstracta)
├── id: Integer
├── monto: BigDecimal
├── fecha: LocalDate
├── esActiva: boolean
├── @ManyToOne → Usuario
├── @ManyToOne → Categoria  (nullable)
└── @ManyToOne → Presupuesto (nullable)
    │
    ├── Gasto     (tabla: gastos)
    │   └── esFijo: boolean
    │
    └── Ingreso   (tabla: ingresos)
        └── fuenteIngreso: String
```

**Regla clave:** nunca convertir `Transaccion` en `@MappedSuperclass` ni romper la herencia — `Usuario` y `Presupuesto` tienen `@OneToMany` a `Transaccion`.

### Relaciones entre entidades

```
Usuario       1 ──< Transaccion   (mappedBy="usuario", cascade ALL)
Usuario       1 ──< Presupuesto   (mappedBy="usuario")
Presupuesto   1 ──< Transaccion   (mappedBy="presupuesto", nullable)
Categoria     1 ──< Categoria     (auto-referencia: padre/subcategorias, cascade ALL)
Transaccion  >── 1 Categoria      (nullable)
```

### Tablas generadas por Hibernate (ddl-auto=update)

| Tabla | Columnas clave |
|---|---|
| `usuarios` | id, nombre, email, contrasena |
| `transacciones` | id, monto, fecha, es_activa, usuario_id, categoria_id, presupuesto_id |
| `gastos` | id (FK transacciones), es_fijo |
| `ingresos` | id (FK transacciones), fuente_ingreso |
| `categorias` | id, nombre, es_global, padre_id (auto-referencia) |
| `presupuestos` | id, descripcion, saldo_disponible, fecha_inicio, fecha_fin, usuario_id |

---

## API REST — endpoints completos

### Usuarios `/api/usuarios`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/usuarios` | Listar todos |
| GET | `/api/usuarios/{id}` | Obtener por ID |
| GET | `/api/usuarios/email/{email}` | Obtener por email |
| POST | `/api/usuarios/login` | **Login** — body: `{email, contrasena}` → `UsuarioResponse` o 401 |
| POST | `/api/usuarios` | Registrar usuario |
| PUT | `/api/usuarios/{id}` | Actualizar |
| DELETE | `/api/usuarios/{id}` | Eliminar |

### Gastos `/api/gastos`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/gastos` | Listar todos |
| GET | `/api/gastos/{id}` | Obtener por ID |
| GET | `/api/gastos/usuario/{usuarioId}` | Gastos de un usuario |
| POST | `/api/gastos` | Crear — body: `GastoRequest` |
| PUT | `/api/gastos/{id}` | Actualizar |
| DELETE | `/api/gastos/{id}` | Eliminar |

### Ingresos `/api/ingresos`
Mismo patrón que gastos — incluye `/usuario/{usuarioId}`.

### Categorías `/api/categorias`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/categorias` | Lista categorías raíz con subcategorías anidadas |
| GET | `/api/categorias/{id}` | Obtener por ID |
| POST | `/api/categorias` | Crear — body: `{nombre, esGlobal, padreId?}` |
| PUT | `/api/categorias/{id}` | Actualizar |
| DELETE | `/api/categorias/{id}` | Eliminar (cascade a subcategorías) |

### Presupuestos `/api/presupuestos`
Mismo patrón — incluye `/usuario/{usuarioId}`.

---

## Arquitectura del frontend

```
frontend/
├── src/
│   ├── index.html         (root: src/ en vite.config.ts → script="/main.tsx")
│   ├── main.tsx           (ReactDOM.createRoot, importa ../index.css)
│   └── App.tsx            (AuthProvider + Router + rutas protegidas)
├── context/
│   └── AuthContext.tsx    (createContext — usuario:{id,nombre,email}, login(), logout())
├── components/
│   └── Navbar.tsx         (teal-700, enlaces activos, logout, nombre usuario)
├── pages/
│   ├── Login.tsx          (tabs: Iniciar sesión / Registrarse)
│   ├── Dashboard.tsx      (resumen: ingresos, gastos, balance en COP)
│   ├── Expenses.tsx       (tabla + formulario inline para crear gastos)
│   ├── Income.tsx         (tabla + formulario inline para crear ingresos)
│   ├── Categories.tsx     (grid de tarjetas + formulario para crear categorías)
│   └── Budgets.tsx        (grid + formulario para crear presupuestos)
└── services/
    └── api.ts             (axios baseURL=http://localhost:8080/api, timeout=10s)
```

### Flujo de autenticación

1. Usuario accede → `RutaProtegida` verifica `AuthContext.usuario`
2. Sin sesión → redirige a `/login`
3. Login: `POST /api/usuarios/login` → guarda `{id, nombre, email}` en `localStorage` (clave `usuario_cg`)
4. Logout: borra `localStorage` y redirige a `/login`
5. Recarga de página: el `AuthContext` lee `localStorage` y restaura la sesión

### Categorías por defecto (DataInitializer)

Al arrancar el backend por primera vez, se insertan automáticamente:
- Alimentación (Supermercado, Restaurantes, Cafetería)
- Transporte (Combustible, Transporte público, Taxi/App)
- Vivienda (Arriendo, Servicios públicos, Mantenimiento)
- Salud (Médico, Medicamentos, Gimnasio)
- Educación (Matrícula, Libros, Cursos)
- Entretenimiento (Streaming, Salidas, Hobbies)
- Ropa y calzado
- Ingresos laborales (Salario, Horas extra, Bonificaciones)
- Ingresos adicionales (Freelance, Arriendo, Inversiones)
- Otros

---

## Contexto para diagramas

### Diagrama de clases — relaciones clave

```
[Usuario] 1---* [Transaccion] {abstract}
[Usuario] 1---* [Presupuesto]
[Transaccion] *---1 [Categoria] {optional}
[Transaccion] *---1 [Presupuesto] {optional}
[Categoria] 1---* [Categoria] {subcategorias, auto-referencia}
[Transaccion] <|-- [Gasto]
[Transaccion] <|-- [Ingreso]
```

**Atributos para el diagrama de clases:**

`Transaccion (abstract)`: -id:Integer, -monto:BigDecimal, -fecha:LocalDate, -esActiva:boolean  
`Gasto`: -esFijo:boolean  
`Ingreso`: -fuenteIngreso:String  
`Categoria`: -id:Integer, -nombre:String, -esGlobal:boolean, -padre:Categoria, -subcategorias:List\<Categoria\>  
`Presupuesto`: -id:Integer, -descripcion:String, -saldoDisponible:BigDecimal, -fechaInicio:LocalDate, -fechaFin:LocalDate  
`Usuario`: -id:Integer, -nombre:String, -email:String, -contrasena:String

### Diagrama de contexto / arquitectura

```
[Usuario (navegador)]
       │ HTTP:3001
       ▼
[Frontend React/Vite]  ──── Axios HTTP:8080 ────►  [Backend Spring Boot]
  - Login / Registro                                  - REST API /api/**
  - Dashboard                                         - Spring Data JPA
  - CRUD Gastos                                       - Hibernate ORM
  - CRUD Ingresos                              │
  - CRUD Categorías                            ▼
  - CRUD Presupuestos                    [PostgreSQL :5432]
                                          BD: controlgastos
```

### Diagrama de capas (backend)

```
Controller (@RestController)
    │ usa DTOs (Request/Response)
    ▼
Service (IXxxService / XxxService)
    │ mapea entidades ↔ DTOs
    ▼
Repository (JpaRepository)
    │ queries JPA
    ▼
Model (Entidades JPA)
    │ herencia JOINED
    ▼
PostgreSQL
```

### Casos de uso principales

- Registrarse / Iniciar sesión / Cerrar sesión
- Registrar gasto (con categoría, fecha, tipo fijo/variable)
- Registrar ingreso (con fuente, categoría, fecha)
- Ver resumen financiero (dashboard con balance)
- Gestionar categorías (crear árbol jerárquico)
- Gestionar presupuestos (con rango de fechas y saldo)

---

## Convenciones del proyecto

- Comentarios en **español**
- Controllers retornan `ResponseEntity<?>`
- Endpoints por usuario: `/api/{recurso}/usuario/{usuarioId}`
- `BigDecimal` para montos (nunca `double`)
- Montos en frontend se formatean como COP: `toLocaleString('es-CO', {style:'currency', currency:'COP'})`
- Fechas: `LocalDate` en backend; el frontend suma `T00:00:00` al parsear (`new Date(fecha + 'T00:00:00')`) para evitar desfase horario
