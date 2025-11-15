# 🍽️ Sistema de Reservas - Sabor Gourmet

> Aplicación web moderna para gestionar reservas de restaurante con roles de administrador y cliente.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Tecnologías](#tecnologías)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Credenciales](#credenciales)
- [Estructura](#estructura)
- [API](#api)
- [Troubleshooting](#troubleshooting)

---

## ✨ Características

### 👨‍💼 Panel Administrador
- ✅ Gestión completa de clientes (CRUD)
- ✅ Gestión de mesas con disponibilidad
- ✅ Gestión de reservas (crear, editar, eliminar)
- ✅ Dashboard con estadísticas
- ✅ Interfaz intuitiva y profesional

### 👤 Panel Cliente
- ✅ Ver mis reservas
- ✅ Crear nuevas reservas
- ✅ Cancelar reservas propias
- ✅ Ver perfil personalizado
- ✅ Registro automático con RUT

### 🎨 Diseño
- ✅ Paleta profesional y moderna
- ✅ Interfaz responsiva (Bootstrap 5)
- ✅ Navegación intuitiva
- ✅ Iconos Bootstrap Icons
- ✅ Tema consistente en todos los módulos

---

## 🛠️ Tecnologías

| Tecnología | Versión | Propósito |
|-----------|---------|----------|
| Java | 21 | Backend |
| Spring Boot | 3.5.7 | Framework |
| Spring Data JPA | - | ORM |
| Thymeleaf | - | Plantillas |
| Bootstrap | 5.3.0 | Frontend |
| H2 Database | - | Base de datos |
| Maven | 3.6+ | Build |
| Tomcat | 10.1.48 | Servidor |

---

## 📋 Requisitos

- **Java 21** o superior
- **Maven 3.6** o superior
- **Puerto 8080** disponible
- Navegador web moderno (Chrome, Firefox, Edge, Safari)

### Verificar versiones:
```bash
java -version
mvn -version
```

---

## 🚀 Instalación

### 1. Clonar o descargar el proyecto
```bash
cd eva2
```

### 2. Compilar
```bash
mvn clean package -DskipTests
```

### 3. Ejecutar

**Opción A: Desde terminal**
```bash
java -jar target/eva2-0.0.1-SNAPSHOT.jar
```

**Opción B: Desde VS Code**
- Presionar `F5` o `Ctrl+Shift+D`
- Seleccionar "Run Eva2Application"

### 4. Acceder
```
http://localhost:8080
```

---

## 💻 Uso

### 🔐 Seleccionar Rol

1. **Administrador**: 
   - Click en "Administrador"
   - Contraseña: `Admin123.-`
   - Click "Ingresar"

2. **Cliente**:
   - Click en "Cliente"
   - Ingresar RUT (ej: `12345678-9`)
   - Sistema crea cuenta automáticamente si es primera vez
   - Click "Ingresar"

### 👨‍💼 Como Administrador

#### Gestionar Clientes
1. Click en "Clientes" → Ver lista de clientes
2. "Nuevo Cliente" → Completar formulario → Guardar
3. Acciones: Editar, eliminar

#### Gestionar Mesas
1. Click en "Mesas" → Ver lista de mesas
2. "Nueva Mesa" → Ingresar número y capacidad
3. Cambiar disponibilidad

#### Gestionar Reservas
1. Click en "Reservas" → Ver todas las reservas
2. "Nueva Reserva" → Seleccionar cliente, mesa y fecha
3. Editar o eliminar reservas existentes

### 👤 Como Cliente

#### Mi Panel
1. Acceder con tu RUT
2. Ver 3 opciones principales:
   - **Nueva Reserva** (botón teal)
   - **Mis Reservas** (botón azul)
   - **Mis Datos** (botón gris)

#### Hacer una Reserva
1. Click "Nueva Reserva"
2. Seleccionar mesa disponible
3. Elegir fecha y hora
4. Click "Confirmar Reserva"

#### Ver y Cancelar Reservas
1. Click "Mis Reservas"
2. Ver todas tus reservas
3. Cancelar si deseas

---

## 🔐 Credenciales

### Administrador
```
Rol: Administrador
Contraseña: Admin123.-
```

### Clientes Precargados
```
RUT: 12345678-9 | Nombre: Juan Pérez
RUT: 98765432-1 | Nombre: María García
RUT: 11223344-5 | Nombre: Carlos López
RUT: 55667788-9 | Nombre: Ana Martínez
RUT: 22334455-6 | Nombre: Roberto Silva
```

**Crear nuevo cliente:**
- Ingresar cualquier RUT válido (formato: XX.XXX.XXX-X o XXXXXXXX-X)
- Sistema crea cuenta automáticamente

---

## 📂 Estructura del Proyecto

```
eva2/
├── src/
│   ├── main/
│   │   ├── java/eva2/eva2/
│   │   │   ├── controllers/
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── ClienteController.java
│   │   │   │   ├── MesaController.java
│   │   │   │   └── ReservaController.java
│   │   │   ├── models/
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── Mesa.java
│   │   │   │   └── Reserva.java
│   │   │   ├── services/
│   │   │   │   ├── ClienteService.java
│   │   │   │   ├── MesaService.java
│   │   │   │   └── ReservaService.java
│   │   │   ├── repository/
│   │   │   │   ├── ClienteRepository.java
│   │   │   │   ├── MesaRepository.java
│   │   │   │   └── ReservaRepository.java
│   │   │   ├── config/
│   │   │   │   └── DataLoader.java
│   │   │   └── Eva2Application.java
│   │   └── resources/
│   │       ├── templates/ (13 archivos HTML)
│   │       ├── static/css/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
├── docs/
│   └── screenshots/
├── pom.xml
├── mvnw
├── readme.md
└── target/
```

---

## 🔌 API REST

### Endpoints Administrador

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/admin/dashboard` | Panel principal |
| GET | `/clientes` | Listar clientes |
| GET | `/clientes/formulario` | Formulario nuevo cliente |
| POST | `/clientes/guardar` | Guardar cliente |
| GET | `/clientes/editar/{id}` | Editar cliente |
| GET | `/clientes/eliminar/{id}` | Eliminar cliente |
| GET | `/mesas` | Listar mesas |
| GET | `/mesas/formulario` | Formulario nueva mesa |
| POST | `/mesas/guardar` | Guardar mesa |
| GET | `/reservas` | Listar reservas |
| GET | `/reservas/nuevo` | Formulario nueva reserva |
| POST | `/reservas/guardar` | Guardar reserva |

### Endpoints Cliente

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/cliente/dashboard/{id}` | Mi panel |
| GET | `/reservas/mis-reservas/{clienteId}` | Mis reservas |
| GET | `/reservas/nueva/{clienteId}` | Nueva reserva |
| POST | `/reservas/guardar-cliente/{clienteId}` | Guardar reserva |
| GET | `/reservas/cancelar/{id}/{clienteId}` | Cancelar reserva |

---

## 🎨 Paleta de Colores

```
🟦 Primario:    #2c3e50  (Azul-gris oscuro)
🟩 Teal:        #16a085  (Clientes/Reservas)
🟦 Azul:        #3498db  (Mesas)
🟧 Naranja:     #e67e22  (Administración)
⬜ Fondo:       #ecf0f1  (Gris claro)
⬛ Texto:       Colores contrastados
```

---

## 📊 Base de Datos

### Esquema

#### CLIENTE
```sql
id (PK)      - Identificador único
rut          - RUT del cliente (único)
nombre       - Nombre completo
email        - Email (único)
telefono     - Teléfono de contacto
```

#### MESA
```sql
id (PK)      - Identificador único
numero       - Número de mesa
capacidad    - Capacidad de personas
disponible   - Estado disponibilidad
```

#### RESERVA
```sql
id (PK)      - Identificador único
cliente_id (FK) - Referencia a cliente
mesa_id (FK)    - Referencia a mesa
fecha_hora      - Fecha y hora de reserva
```

### Datos Iniciales

- **5 clientes** precargados
- **8 mesas** (capacidades: 2, 4, 6, 8 personas)
- **5 reservas** de ejemplo

---

## ⚙️ Configuración

### application.properties
```properties
# Servidor
server.port=8080

# Base de datos H2
spring.datasource.url=jdbc:h2:mem:sabor_gourmet
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.h2.console.enabled=true

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.defer-datasource-initialization=true

# SQL inicial
spring.sql.init.mode=never
```

### DataLoader
- Se ejecuta automáticamente al iniciar
- Solo carga datos si tabla está vacía
- Crea clientes, mesas y reservas de ejemplo

---

## 🐛 Troubleshooting

### ❌ "Port 8080 already in use"
```bash
# Windows - Encontrar y matar proceso
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### ❌ "Java 21 not found"
```bash
# Descargar desde: https://www.oracle.com/java/technologies/downloads/
# Verificar: java -version
```

### ❌ "Maven not found"
```bash
# Usar mvnw incluido:
./mvnw clean package
```

### ❌ "Violación de clave primaria"
- **Solución**: Ya resuelta. DataLoader verifica duplicados.
- Reiniciar aplicación

### ❌ "Required parameter not present"
- **Solución**: Ya resuelta en formularios de reserva.
- Limpiar caché del navegador: `Ctrl+Shift+Del`

### ❌ Aplicación no carga
1. Limpiar caché: `mvn clean`
2. Compilar de nuevo: `mvn package`
3. Reiniciar servidor

---

## 🔄 Desarrollo

### Agregar nueva funcionalidad

1. **Crear modelo** en `models/`
2. **Crear repositorio** en `repository/`
3. **Crear servicio** en `services/`
4. **Crear controlador** en `controllers/`
5. **Crear vista** en `templates/`
6. **Compilar y probar**

### Compilación y Testing

```bash
# Compilar sin tests
mvn clean package -DskipTests

# Compilar con tests
mvn clean package

# Solo compilar (sin JAR)
mvn clean compile
```

---

## 📸 Capturas de Pantalla

### Panel de Administración
![Admin Dashboard](docs/screenshots/admin-dashboard.png)

### Panel de Cliente
![Cliente Dashboard](docs/screenshots/cliente-dashboard.png)

### Nueva Reserva
![Nueva Reserva](docs/screenshots/nueva-reserva.png)

---

## 📝 Documentación Adicional

Consultar archivos en la raíz del proyecto:
- `DOCUMENTACION_CONTROLADORES.md` - Detalles de endpoints
- `DOCUMENTACION_SERVICIOS.md` - Lógica de negocio

---

## 📄 Licencia

Proyecto educativo - Libre para modificación y distribución.

---

## 👨‍💻 Autor

Desarrollado por: **Equipo de Desarrollo**

---

## 📞 Contacto y Soporte

Para reportar bugs, sugerencias o preguntas, contactar al equipo de desarrollo.

**Estado**: ✅ En funcionamiento
**Versión**: 0.0.1-SNAPSHOT
**Última actualización**: 15 de noviembre de 2025

---

⭐ Si te fue útil, deja una estrella en el repositorio!