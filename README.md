# EventosUY

## 📋 Descripción del Proyecto

**EventosUY** es una plataforma web para la gestión y organización de eventos en Uruguay. El proyecto permite a usuarios registrarse, crear eventos, seguir a otros organizadores, patrocinar eventos y registrarse como asistentes.

### Características Principales

- **Gestión de Eventos**: Crear, editar y consultar eventos
- **Categorías de Eventos**: Organización por categorías personalizables
- **Usuarios y Perfiles**: Sistema de usuarios con roles de organizadores y asistentes
- **Seguimiento**: Seguir a otros organizadores y eventos
- **Instituciones**: Gestión de instituciones relacionadas con los eventos
- **Patrocinios**: Sistema de gestión de patrocinadores
- **Reportes**: Generación de reportes en PDF

---

## 🏗️ Arquitectura del Proyecto

El proyecto está dividido en dos módulos principales:

### 1. **API (Backend)**
- **Ubicación**: `/api`
- **Tipo**: Servidor JAR independiente
- **Tecnología**: Java 21 + JAX-WS (Web Services)
- **Puerto**: 8080
- **Descripción**: Servidor central que expone servicios web para todas las operaciones CRUD y lógica de negocio

### 2. **Web (Frontend)**
- **Ubicación**: `/web`
- **Tipo**: Aplicación WAR (Servlet)
- **Tecnología**: Java 21 + Jakarta EE
- **Servidor**: Apache Tomcat 11.0.13
- **Puerto**: 8081
- **Descripción**: Interfaz web que consume los servicios del backend

---

## 🛠️ Requisitos Previos

- **Java**: JDK 21 o superior
- **Maven**: 3.8.0 o superior
- **Git**: Para clonar el repositorio

### Verificar instalación

```bash
java -version
mvn -version
```

---

## 📦 Build (Compilación)

### En Windows

```bash
build.bat
```

### En Linux/macOS

```bash
./build.sh
```

### Qué hace el build

1. **Compila el módulo `api`**:
   - Ejecuta `mvn clean install`
   - Genera `ServidorWS-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

2. **Compila el módulo `web`**:
   - Ejecuta `mvn clean install`
   - Genera `tarea2-0.0.1-SNAPSHOT.war`

3. **Copia datos de prueba**:
   - Copia archivos CSV a `target/datosPrueba/`
   - Copia recursos de upload a `target/src/webservices/uploads/`

### Salida esperada

```
✓ api/target/ServidorWS-0.0.1-SNAPSHOT-jar-with-dependencies.jar
✓ web/target/tarea2-0.0.1-SNAPSHOT.war
```

---

## 🚀 Deploy (Despliegue)

### En Windows

```bash
deploy.bat
```

### En Linux/macOS

```bash
./deploy.sh
```

### Qué hace el deploy

1. **Inicia el servidor API**:
   - Ejecuta el JAR en background
   - Puerto: 8080
   - URL: `http://localhost:8080`

2. **Detiene Tomcat** (si está corriendo)

3. **Despliega la aplicación web**:
   - Copia el WAR a `apache-tomcat-11.0.13/webapps/`
   - Inicia Tomcat

4. **Abre la aplicación**:
   - URL: `http://localhost:8081/tarea2-0.0.1-SNAPSHOT/HomeServlet`

---

## 🔧 Configuración

### Archivo: `application.properties`

```properties
server.url = http://localhost
server.port = 8080
```

Modifica estos valores si necesitas cambiar el puerto o la URL base del servidor.

---

## 📂 Estructura de Directorios

```
eventosUy/
├── api/                              # Servidor backend (JAR)
│   ├── src/
│   │   ├── org/                      # Código generado automáticamente
│   │   ├── logica/                   # Lógica de negocio
│   │   ├── webservices/              # Servicios web expuestos
│   │   ├── excepciones/              # Excepciones personalizadas
│   │   ├── casosPrueba/              # Casos de prueba
│   │   └── adminStation/             # Administración
│   ├── datosPrueba/                  # Datos iniciales (CSV)
│   ├── pom.xml                       # Configuración Maven
│   └── target/                       # Archivos compilados
│
├── web/                              # Cliente web (WAR)
│   ├── src/main/
│   │   ├── java/                     # Código Java (Servlets)
│   │   └── webapp/                   # Recursos web
│   ├── pom.xml                       # Configuración Maven
│   └── target/                       # Archivos compilados
│
├── apache-tomcat-11.0.13/            # Servidor Tomcat
│   ├── bin/                          # Scripts de inicio/parada
│   ├── conf/                         # Configuración
│   ├── webapps/                      # Aplicaciones desplegadas
│   └── logs/                         # Registros de eventos
│
├── build.sh / build.bat              # Script de compilación
├── deploy.sh / deploy.bat            # Script de despliegue
├── application.properties            # Configuración de la app
└── README.md                         # Este archivo
```

---

## 🔄 Flujo de Desarrollo

### 1. Clonar y preparar

```bash
git clone <repositorio>
cd eventosUy
```

### 2. Compilar

```bash
# Windows
build.bat

# Linux/macOS
./build.sh
```

### 3. Desplegar

```bash
# Windows
deploy.bat

# Linux/macOS
./deploy.sh
```

### 4. Acceder

- **Aplicación Web**: http://localhost:8081/tarea2-0.0.1-SNAPSHOT/HomeServlet
- **Servidor API**: http://localhost:8080

---

## 🧪 Datos de Prueba

Los datos de prueba están en formato CSV en `/api/datosPrueba/`:

- `2025Usuarios.csv` - Usuarios registrados
- `2025Eventos.csv` - Eventos disponibles
- `2025Categorias.csv` - Categorías de eventos
- `2025Instituciones.csv` - Instituciones participantes
- `2025Patrocinios.csv` - Patrocinio de eventos
- `2025Registros.csv` - Registros de asistencia
- Y más...

Estos archivos se copian automáticamente durante el build a `target/datosPrueba/`.

---

## 📝 Dependencias Principales

### API (Backend)
- **Jakarta XML Web Services** - 4.0.0
- **JAX-WS Runtime** - 4.0.0
- **iText7 Core** - 7.2.5 (Generación de PDF)
- **JUnit** - 4.13.2 (Testing)

### Web (Frontend)
- **Jakarta XML Web Services API** - 4.0.0
- **JAX-WS Runtime** - 4.0.0
- **Tomcat Servlet API** - 10.1.11

---

## 🐛 Solución de Problemas

### El API no inicia

- Verifica que el puerto 8080 esté disponible
- Comprueba que Java 21+ esté instalado
- Revisa los logs del build en `target/`

### Tomcat no inicia

- Verifica que el puerto 8081 esté disponible
- Asegúrate de que la aplicación WAR se copió correctamente
- Revisa los logs en `apache-tomcat-11.0.13/logs/`

### Problemas de compilación Maven

```bash
# Limpia la caché de Maven
mvn clean

# Reintenta el build
mvn install
```


---

**Última actualización**: 4 de diciembre de 2025
