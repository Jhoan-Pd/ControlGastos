# Guía de despliegue — control-gastos

Backend en **Render** (gratis) · Frontend en **Vercel** (gratis)

---

## Requisitos previos

- Cuenta en [GitHub](https://github.com)
- Cuenta en [Render](https://render.com) (gratuita)
- Cuenta en [Vercel](https://vercel.com) (gratuita)
- Docker instalado (solo para prueba local opcional)

---

## Paso 1 — Subir el proyecto a GitHub

```bash
# Desde la raíz del proyecto
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/control-gastos.git
git push -u origin main
```

> El `.gitignore` ya excluye `target/`, `node_modules/`, `.env*` y archivos de IDE.

---

## Opción A — Deploy con render.yaml (recomendado)

Si tu repositorio de GitHub tiene el `render.yaml` en la raíz:

1. Ir a [dashboard.render.com](https://dashboard.render.com) → **New +** → **Blueprint**
2. Conectar el repositorio `control-gastos`
3. Render detectará el `render.yaml` y creará automáticamente la BD y el Web Service
4. Completar los Pasos 3 y 4 para las variables de entorno manuales

---

## Opción B — Deploy manual (paso a paso)

### Paso 2 — Crear la base de datos PostgreSQL en Render

1. **New +** → **PostgreSQL**
2. Configurar:
   - **Name:** `control-gastos-db`
   - **Database:** `controlgastos`
   - **Plan:** Free
   - **Version:** 15
3. Clic en **Create Database** y esperar ~1 minuto
4. Anotar los valores de: **Host**, **Port**, **Database**, **Username**, **Password**

### Paso 3 — Crear el Web Service en Render

1. **New +** → **Web Service**
2. Conectar el repositorio `control-gastos` de GitHub
3. Configurar:
   - **Name:** `control-gastos-backend`
   - **Branch:** `main`
   - **Runtime:** Docker
   - **Plan:** Free
4. En **Advanced** → **Health Check Path:** `/api/usuarios`

### Paso 4 — Configurar variables de entorno en Render

En la sección **Environment** del Web Service, agregar estas variables:

| Variable | Valor |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `DB_HOST` | Host de la BD PostgreSQL en Render |
| `DB_PORT` | `5432` |
| `DB_NAME` | `controlgastos` |
| `DB_USER` | Usuario de la BD en Render |
| `DB_PASSWORD` | Contraseña de la BD en Render |
| `CORS_ORIGINS` | `https://TU-APP.vercel.app` (actualizar en Paso 6) |

> **Nota:** Render entrega la URL de la BD en formato `postgresql://...` pero Spring Boot
> necesita el formato JDBC `jdbc:postgresql://...`. Por eso se usan variables separadas
> (DB_HOST, DB_PORT, etc.) que el `application-prod.properties` ensambla correctamente.

Hacer clic en **Create Web Service**. El primer build tarda ~5–10 minutos.

Verificar en: `https://control-gastos-backend.onrender.com/api/usuarios`
(debe devolver `[]` o una lista de usuarios)

---

## Paso 5 — Desplegar el frontend en Vercel

1. Ir a [vercel.com/new](https://vercel.com/new)
2. Importar el repositorio de GitHub `control-gastos`
3. Configurar:
   - **Root Directory:** `frontend`
   - **Framework Preset:** Vite
   - **Build Command:** `npm run build`
   - **Output Directory:** `dist`
4. En **Environment Variables**, agregar:

| Variable | Valor |
|---|---|
| `VITE_API_URL` | `https://control-gastos-backend.onrender.com/api` |

> Reemplaza la URL por la URL real de tu servicio en Render (Settings → URL).

5. Clic en **Deploy**. En ~1-2 minutos el frontend estará disponible.
6. Copiar la URL de Vercel (ejemplo: `https://control-gastos-abc123.vercel.app`).

---

## Paso 6 — Actualizar CORS_ORIGINS en Render

1. Render → tu Web Service → **Environment**
2. Actualizar `CORS_ORIGINS` con la URL real de Vercel (sin barra final):
   ```
   https://control-gastos-abc123.vercel.app
   ```
3. Render redesplegará automáticamente (~2 minutos).

---

## Paso 7 — Verificar que todo funciona

```bash
# 1. Backend responde
curl https://control-gastos-backend.onrender.com/api/usuarios

# 2. Categorías inicializadas por DataInitializer
curl https://control-gastos-backend.onrender.com/api/categorias

# 3. Frontend: abrir la URL de Vercel en el navegador
#    → intentar registrarse e iniciar sesión
```

Si el backend no responde la primera vez, espera 30-60 segundos (el servicio gratuito
de Render entra en modo sleep tras 15 minutos de inactividad).

---

## Prueba local con Docker (opcional)

```bash
chmod +x scripts/test-docker-local.sh
./scripts/test-docker-local.sh
```

---

## Notas importantes

### Render Free Tier
- El backend **duerme** después de 15 minutos sin peticiones.
- La primera petición después del sleep tarda **30–60 segundos**.
- Por eso `frontend/services/api.ts` tiene `timeout: 15000ms`.
- La base de datos PostgreSQL gratuita **expira a los 90 días** en Render.

### Seguridad
- Nunca subas contraseñas reales al repositorio — usa variables de entorno en Render.
- El perfil `prod` deshabilita `show-sql` y reduce el logging.
- La conexión a la BD usa `sslmode=require` (requerido por Render).

### Actualizar el deploy
Cualquier `git push` a `main` redesplegará automáticamente en Render y Vercel.

### Variables de entorno en Vercel vs .env.production
El archivo `frontend/.env.production` tiene la URL como placeholder. La URL real
se configura como variable de entorno directamente en el dashboard de Vercel,
lo que tiene prioridad sobre el archivo `.env.production`.
