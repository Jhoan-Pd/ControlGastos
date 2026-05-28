#!/usr/bin/env bash
set -e

# ================================================================
# Script para probar el backend con Docker localmente
# Requiere: Docker instalado y corriendo, Maven instalado
# ================================================================

IMAGE_NAME="control-gastos-backend"
DB_CONTAINER="control-gastos-test-db"
APP_CONTAINER="control-gastos-test-app"
DB_PASSWORD="testpass"
DB_NAME="controlgastos"
DB_USER="postgres"
APP_PORT="8080"
DB_PORT="5433"   # Puerto externo distinto al 5432 local para no colisionar

echo "======================================================"
echo " Test Docker local — control-gastos"
echo "======================================================"

# 1. Limpiar contenedores anteriores si existen
echo ""
echo "[1/5] Limpiando contenedores anteriores..."
docker rm -f "$DB_CONTAINER" "$APP_CONTAINER" 2>/dev/null || true

# 2. Compilar JAR
echo ""
echo "[2/5] Compilando el proyecto con Maven..."
cd "$(dirname "$0")/.."
mvn clean package -DskipTests -B -q
echo "     JAR generado en target/"

# 3. Construir imagen Docker
echo ""
echo "[3/5] Construyendo imagen Docker: $IMAGE_NAME..."
docker build -t "$IMAGE_NAME" .
echo "     Imagen lista: $IMAGE_NAME"

# 4. Levantar PostgreSQL temporal
echo ""
echo "[4/5] Levantando PostgreSQL temporal en puerto $DB_PORT..."
docker run -d \
  --name "$DB_CONTAINER" \
  -e POSTGRES_PASSWORD="$DB_PASSWORD" \
  -e POSTGRES_USER="$DB_USER" \
  -e POSTGRES_DB="$DB_NAME" \
  -p "${DB_PORT}:5432" \
  postgres:15-alpine

echo "     Esperando que PostgreSQL arranque (8 segundos)..."
sleep 8

# 5. Levantar el backend
echo ""
echo "[5/5] Levantando backend en puerto $APP_PORT..."
docker run -d \
  --name "$APP_CONTAINER" \
  --link "$DB_CONTAINER:db" \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=db \
  -e DB_PORT=5432 \
  -e DB_NAME="$DB_NAME" \
  -e DB_USER="$DB_USER" \
  -e DB_PASSWORD="$DB_PASSWORD" \
  -e CORS_ORIGINS="http://localhost:3001" \
  -p "${APP_PORT}:8080" \
  "$IMAGE_NAME"

echo ""
echo "======================================================"
echo " Backend corriendo en: http://localhost:${APP_PORT}"
echo " Endpoint de prueba:   http://localhost:${APP_PORT}/api/usuarios"
echo " Categorías:           http://localhost:${APP_PORT}/api/categorias"
echo " Logs del backend:     docker logs -f $APP_CONTAINER"
echo ""
echo " Para detener todo:"
echo "   docker rm -f $APP_CONTAINER $DB_CONTAINER"
echo "======================================================"
