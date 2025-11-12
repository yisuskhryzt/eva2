# 📋 Documentación de Controladores - Sistema de Reservas de Restaurante

## 🏗️ Arquitectura del Sistema

Este sistema gestiona reservas de un restaurante con **dos roles principales**:
- **ADMINISTRADOR**: Gestión completa de clientes, mesas y reservas
- **CLIENTE**: Solo puede crear y cancelar sus propias reservas

---

## 1️⃣ ClienteController

### ✅ Correcciones Realizadas:

1. **Ruta de guardado corregida**: Cambié de `/nuevo` a `/guardar` en el POST
2. **Uso de `@ModelAttribute`**: Para capturar correctamente los datos del formulario
3. **Validación mejorada**: Verificar que el cliente existe antes de editar/eliminar
4. **Validación de relaciones**: No permite eliminar clientes con reservas activas
5. **Mensajes flash**: Implementados para feedback al usuario
6. **Manejo de excepciones**: Try-catch en todas las operaciones críticas

### 📍 Endpoints (Solo ADMIN):

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/clientes` | Lista todos los clientes |
| GET | `/clientes/nuevo` | Formulario para nuevo cliente |
| POST | `/clientes/guardar` | Guarda un nuevo cliente |
| GET | `/clientes/editar/{id}` | Formulario para editar cliente |
| GET | `/clientes/eliminar/{id}` | Elimina un cliente |

---

## 2️⃣ MesaController

### ✨ Funcionalidades Implementadas:

1. **CRUD completo** de mesas
2. **Gestión de disponibilidad**: Toggle para cambiar disponibilidad
3. **Validación de relaciones**: No permite eliminar mesas con reservas
4. **Filtrado por disponibilidad**: Endpoint para ver solo mesas disponibles
5. **Mensajes flash y manejo de errores**

### 📍 Endpoints (ADMIN):

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/mesas` | Lista todas las mesas |
| GET | `/mesas/nuevo` | Formulario para nueva mesa |
| POST | `/mesas/guardar` | Guarda una mesa |
| GET | `/mesas/editar/{id}` | Formulario para editar mesa |
| GET | `/mesas/eliminar/{id}` | Elimina una mesa |
| GET | `/mesas/cambiar-disponibilidad/{id}` | Toggle disponibilidad |
| GET | `/mesas/disponibles` | Lista solo mesas disponibles |

### 🔧 Correcciones en Modelo Mesa:

- Agregado **constructor vacío** (requerido por JPA)
- Constructor simplificado sin el parámetro `reservas`

### 🔧 Mejoras en MesaRepository:

```java
List<Mesa> findByDisponible(boolean disponible);
List<Mesa> findByCapacidadGreaterThanEqual(int capacidad);
```

---

## 3️⃣ ReservaController

### ✨ Funcionalidades Implementadas:

#### Para ADMINISTRADOR:
1. **CRUD completo** de reservas
2. **Validaciones exhaustivas**:
   - Cliente y mesa existen
   - Mesa está disponible
   - Fecha no es pasada
   - Mesa no está reservada en esa fecha/hora
3. **Vista completa** de todas las reservas

#### Para CLIENTE:
1. **Ver sus propias reservas**: `/mis-reservas/{clienteId}`
2. **Crear nueva reserva**: `/nueva/{clienteId}`
3. **Cancelar su propia reserva**: Con validación de propiedad
4. **Solo puede ver mesas disponibles**

### 📍 Endpoints - ADMINISTRADOR:

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/reservas` | Lista todas las reservas |
| GET | `/reservas/nuevo` | Formulario nueva reserva (ADMIN) |
| POST | `/reservas/guardar` | Guarda reserva (ADMIN) |
| GET | `/reservas/editar/{id}` | Formulario editar reserva |
| POST | `/reservas/actualizar/{id}` | Actualiza reserva |
| GET | `/reservas/eliminar/{id}` | Elimina reserva |

### 📍 Endpoints - CLIENTE:

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/reservas/mis-reservas/{clienteId}` | Ver mis reservas |
| GET | `/reservas/nueva/{clienteId}` | Formulario nueva reserva (CLIENTE) |
| POST | `/reservas/guardar-cliente/{clienteId}` | Crear reserva (CLIENTE) |
| GET | `/reservas/cancelar/{id}/{clienteId}` | Cancelar mi reserva |

### 🔒 Seguridad Implementada:

- **Validación de propiedad**: Un cliente solo puede cancelar sus propias reservas
- **Verificación en backend**: No se confía solo en el frontend
- **Control de permisos**: Rutas separadas para ADMIN y CLIENTE

### 🔧 Mejoras en ReservaRepository:

```java
// Buscar por cliente
List<Reserva> findByCliente(Cliente cliente);
List<Reserva> findByClienteId(Long clienteId);

// Buscar por mesa
List<Reserva> findByMesa(Mesa mesa);

// Buscar por fecha
List<Reserva> findByFechaHoraAfter(LocalDateTime fechaHora);
List<Reserva> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

// Verificar disponibilidad
boolean existsByMesaAndFechaHora(Long mesaId, LocalDateTime fechaHora);
```

---

## 🎨 Vistas HTML Necesarias

### Para Clientes:
- ✅ `clientes-lista.html` - Ya creada
- ✅ `cliente-formulario.html` - Ya creada

### Para Mesas:
- ⚠️ `mesas-lista.html` - **Necesita crearse**
- ⚠️ `mesa-formulario.html` - **Necesita crearse**
- ⚠️ `mesas-disponibles.html` - **Necesita crearse**

### Para Reservas:
- ⚠️ `reservas-lista.html` - **Necesita crearse** (ADMIN)
- ⚠️ `reserva-formulario.html` - **Necesita crearse** (ADMIN)
- ⚠️ `mis-reservas.html` - **Necesita crearse** (CLIENTE)
- ⚠️ `reserva-cliente-formulario.html` - **Necesita crearse** (CLIENTE)

---

## 📦 Características Comunes en Todos los Controladores

### ✅ Mensajes Flash:
```java
redirectAttributes.addFlashAttribute("mensaje", "Operación exitosa");
redirectAttributes.addFlashAttribute("tipoMensaje", "success"); // success, danger, warning, info
```

### ✅ Manejo de Errores:
- Try-catch en todas las operaciones
- Validaciones antes de operaciones críticas
- Mensajes de error descriptivos

### ✅ Validaciones:
- Verificación de existencia de entidades
- Validación de relaciones antes de eliminar
- Validación de fechas (no permitir pasadas)
- Validación de disponibilidad

---

## 🚀 Próximos Pasos Recomendados

1. **Crear las vistas HTML faltantes** usando Bootstrap (como las de clientes)
2. **Implementar Spring Security** para gestión de roles real
3. **Agregar campo de estado** a Reserva (pendiente, confirmada, cancelada)
4. **Implementar servicios** para lógica de negocio más compleja
5. **Agregar validaciones con Bean Validation** (@Valid, @NotNull, etc.)
6. **Crear un dashboard** principal con estadísticas
7. **Implementar búsqueda y filtrado** en las listas
8. **Agregar paginación** para listas largas

---

## 📝 Notas Importantes

- Todos los controladores usan **redirects** después de operaciones POST
- Los mensajes flash se muestran en la vista destino
- Las validaciones están tanto en backend como deberían estar en frontend
- El sistema asume que `clienteId` se obtiene de la sesión en producción
- Actualmente no hay autenticación real, se usa el ID en la URL (solo para desarrollo)

---

## 🔐 Consideraciones de Seguridad para Producción

⚠️ **IMPORTANTE**: Este código es para desarrollo/educación. Para producción:

1. Implementar **Spring Security** con roles ADMIN y USER
2. Obtener el usuario autenticado desde la sesión, no desde URL
3. Usar **@PreAuthorize** para proteger endpoints
4. Implementar **CSRF protection**
5. Validar **todas las entradas** con Bean Validation
6. Implementar **rate limiting** para prevenir abuso
7. Agregar **logging** de todas las operaciones críticas

---

**Fecha de creación**: Noviembre 11, 2025
**Autor**: Sistema de Gestión de Restaurante
**Versión**: 1.0
