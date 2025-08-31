# Etapa de build Angular
FROM node:20-alpine AS build
WORKDIR /app

# Copiar package.json y package-lock.json
COPY package*.json ./

# Instalar dependencias de forma reproducible
RUN npm ci --legacy-peer-deps

# Copiar todo el código del frontend
COPY . .

# Compilar Angular en modo producción
RUN npm run build -- --configuration production && ls -la /app/dist

# Etapa de runtime con Nginx
FROM nginx:1.27-alpine

# Copiar build de Angular a Nginx
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

# Copiar configuración de nginx (SPA Angular)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
