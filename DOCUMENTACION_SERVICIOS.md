# 📋 Documentación de Servicios - Sistema de Reservas de Restaurante ac-v1.1

## 🎯 Propósito de los Servicios

Los servicios contienen la **lógica de negocio** de la aplicación y actúan como intermediarios entre los controladores y los repositorios. Esto permite:

- ✅ **Separación de responsabilidades**: Controladores limpios, lógica en servicios
- ✅ **Reutilización de código**: Misma lógica usable desde múltiples controladores
- ✅ **Validaciones centralizadas**: Todas las reglas de negocio en un solo lugar
- ✅ **Transaccionalidad**: Uso de `@Transactional` para operaciones seguras
- ✅ **Testeo más fácil**: Los servicios se pueden probar independientemente

---

## 1️⃣ ClienteService

### 📌 Métodos Principales:

#### Operaciones CRUD:
| Método | Descripción | Validaciones |
|--------|-------------|--------------|
| `obtenerTodosLosClientes()` | Lista todos los clientes | - |
| `obtenerClientePorId(Long id)` | Busca cliente por ID | - |
| `guardarCliente(Cliente)` | Guarda/actualiza cliente | Nombre obligatorio, email válido |
| `actualizarCliente(Long, Cliente)` | Actualiza cliente existente | Cliente debe existir |
| `eliminarCliente(Long)` | Elimina cliente | No puede tener reservas |

#### Validaciones Especiales:
```java
// Validar nombre
if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
    throw new IllegalArgumentException("El nombre del cliente es obligatorio");
}

// Validar email
if (!cliente.getEmail().contains("@")) {
    throw new IllegalArgumentException("El email no tiene un formato válido");
}

// No eliminar si tiene reservas
if (cliente.getReservas() != null && !cliente.getReservas().isEmpty()) {
    throw new IllegalStateException("No se puede eliminar el cliente porque tiene reservas asociadas");
}
```

#### Métodos Útiles:
- `existeCliente(Long id)` - Verifica si existe
- `contarClientes()` - Cuenta total de clientes
- `buscarPorEmail(String email)` - Busca por email
- `emailExiste(String email)` - Verifica si email ya existe
- `emailExisteExcluyendoCliente(String, Long)` - Para validar en edición

---

## 2️⃣ MesaService

### 📌 Métodos Principales:

#### Operaciones CRUD:
| Método | Descripción | Validaciones |
|--------|-------------|--------------|
| `obtenerTodasLasMesas()` | Lista todas las mesas | - |
| `obtenerMesaPorId(Long id)` | Busca mesa por ID | - |
| `guardarMesa(Mesa)` | Guarda/actualiza mesa | Número y capacidad > 0, número único |
| `actualizarMesa(Long, Mesa)` | Actualiza mesa existente | Mesa debe existir, número único |
| `eliminarMesa(Long)` | Elimina mesa | No puede tener reservas |

#### Gestión de Disponibilidad:
```java
// Cambiar disponibilidad (toggle)
cambiarDisponibilidad(Long id)

// Marcar como disponible
marcarComoDisponible(Long id)

// Marcar como no disponible
marcarComoNoDisponible(Long id)
```

#### Métodos de Consulta:
- `obtenerMesasDisponibles()` - Solo mesas disponibles
- `obtenerMesasPorCapacidadMinima(int)` - Filtra por capacidad
- `obtenerMesasDisponiblesPorCapacidad(int)` - Disponibles con capacidad mínima

#### Validaciones Especiales:
```java
// Validar número de mesa
if (mesa.getNumero() <= 0) {
    throw new IllegalArgumentException("El número de mesa debe ser mayor a 0");
}

// Validar número único
if (numeroMesaExiste(mesa.getNumero())) {
    throw new IllegalArgumentException("Ya existe una mesa con el número " + mesa.getNumero());
}

// No eliminar si tiene reservas
if (mesa.getReservas() != null && !mesa.getReservas().isEmpty()) {
    throw new IllegalStateException("No se puede eliminar la mesa porque tiene reservas asociadas");
}
```

#### Estadísticas:
- `contarMesas()` - Total de mesas
- `contarMesasDisponibles()` - Mesas disponibles
- `contarMesasNoDisponibles()` - Mesas no disponibles
- `obtenerCapacidadTotal()` - Suma de todas las capacidades
- `obtenerCapacidadDisponible()` - Suma capacidades disponibles

---

## 3️⃣ ReservaService

### 📌 Métodos Principales:

#### Operaciones CRUD:
| Método | Descripción | Validaciones |
|--------|-------------|--------------|
| `obtenerTodasLasReservas()` | Lista todas las reservas | - |
| `obtenerReservaPorId(Long id)` | Busca reserva por ID | - |
| `crearReserva(Long, Long, LocalDateTime)` | Crea nueva reserva | **Múltiples validaciones** |
| `actualizarReserva(Long, Long, Long, LocalDateTime)` | Actualiza reserva | Mesa disponible, fecha válida |
| `cancelarReserva(Long)` | Cancela/elimina reserva | - |

#### Validaciones en Crear Reserva:
```java
// 1. Cliente existe
Cliente cliente = clienteRepository.findById(clienteId)
    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

// 2. Mesa existe
Mesa mesa = mesaRepository.findById(mesaId)
    .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));

// 3. Mesa está disponible
if (!mesa.isDisponible()) {
    throw new IllegalStateException("La mesa no está disponible");
}

// 4. Fecha no es pasada
if (fechaHora.isBefore(LocalDateTime.now())) {
    throw new IllegalArgumentException("No se puede crear una reserva en una fecha pasada");
}

// 5. Fecha no es muy lejana (máx 3 meses)
if (fechaHora.isAfter(LocalDateTime.now().plusMonths(3))) {
    throw new IllegalArgumentException("No se pueden hacer reservas con más de 3 meses de anticipación");
}

// 6. Mesa no está reservada en esa fecha/hora
if (reservaRepository.existsByMesaAndFechaHora(mesaId, fechaHora)) {
    throw new IllegalStateException("La mesa ya está reservada para esa fecha y hora");
}

// 7. No hay reserva cercana (dentro de 2 horas)
if (existeReservaCercana(mesaId, fechaHora)) {
    throw new IllegalStateException("Ya existe una reserva cercana a ese horario");
}
```

#### Métodos de Consulta por Cliente:
- `obtenerReservasPorCliente(Long)` - Todas las reservas del cliente
- `obtenerReservasFuturasDeCliente(Long)` - Solo futuras
- `obtenerReservasPasadasDeCliente(Long)` - Solo pasadas
- `contarReservasPorCliente(Long)` - Cuenta reservas del cliente

#### Métodos de Consulta por Mesa:
- `obtenerReservasPorMesa(Long)` - Todas las reservas de una mesa
- `mesaDisponibleEnFechaHora(Long, LocalDateTime)` - Verifica disponibilidad

#### Métodos de Consulta por Fecha:
- `obtenerReservasFuturas()` - Todas las futuras
- `obtenerReservasPorRangoFechas(inicio, fin)` - Por rango
- `obtenerReservasDeHoy()` - Reservas de hoy
- `obtenerProximasReservas()` - Próximas 24 horas

#### Cancelación de Reservas:
```java
// Cancelar con validación de propiedad (CLIENTE)
cancelarReservaPorCliente(Long reservaId, Long clienteId) {
    // Verifica que la reserva pertenece al cliente
    if (!reserva.getCliente().getId().equals(clienteId)) {
        throw new IllegalStateException("No tiene permiso para cancelar esta reserva");
    }
    
    // No permite cancelar reservas pasadas
    if (reserva.getFechaHora().isBefore(LocalDateTime.now())) {
        throw new IllegalStateException("No se pueden cancelar reservas pasadas");
    }
}
```

#### Métodos Especiales:
- `obtenerMesasDisponiblesParaFechaHora(LocalDateTime)` - Mesas libres en fecha
- `reservaPerteneceACliente(Long, Long)` - Verifica propiedad
- `existeReservaCercana(Long, LocalDateTime)` - Detecta conflictos (privado)

#### Estadísticas:
- `contarReservas()` - Total de reservas
- `contarReservasFuturas()` - Reservas futuras

---

## 🔄 Flujo de Uso en los Controladores

### Ejemplo: Crear Reserva (CLIENTE)

```java
@Controller
public class ReservaController {
    
    @Autowired
    private ReservaService reservaService;
    
    @PostMapping("/guardar-cliente/{clienteId}")
    public String guardarReservaCliente(
            @PathVariable Long clienteId,
            @RequestParam Long mesaId,
            @RequestParam LocalDateTime fechaHora,
            RedirectAttributes redirectAttributes) {
        
        try {
            // El servicio maneja TODAS las validaciones
            Reserva reserva = reservaService.crearReserva(clienteId, mesaId, fechaHora);
            
            redirectAttributes.addFlashAttribute("mensaje", "Reserva creada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            // Errores de validación
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (IllegalStateException e) {
            // Errores de estado (mesa ocupada, etc.)
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        
        return "redirect:/reservas/mis-reservas/" + clienteId;
    }
}
```

---

## ⚙️ Configuración de Transacciones

Todos los servicios están anotados con:
```java
@Service
@Transactional
```

### Beneficios de @Transactional:
- ✅ **Atomicidad**: Si falla una operación, se revierten todas
- ✅ **Consistencia**: La base de datos siempre queda en estado válido
- ✅ **Lazy Loading**: Las relaciones JPA se cargan correctamente
- ✅ **Manejo automático de conexiones**: Spring gestiona la conexión a BD

---

## 🎯 Reglas de Negocio Implementadas

### ClienteService:
1. ✅ Nombre y email son obligatorios
2. ✅ Email debe tener formato válido (contiene @)
3. ✅ Email debe ser único
4. ✅ No se puede eliminar cliente con reservas activas

### MesaService:
1. ✅ Número de mesa debe ser mayor a 0
2. ✅ Capacidad debe ser mayor a 0
3. ✅ Número de mesa debe ser único
4. ✅ Mesas nuevas son disponibles por defecto
5. ✅ No se puede eliminar mesa con reservas

### ReservaService:
1. ✅ Cliente y mesa deben existir
2. ✅ Mesa debe estar disponible
3. ✅ Fecha no puede ser pasada
4. ✅ Fecha máxima: 3 meses adelante
5. ✅ Mesa no puede tener otra reserva en la misma fecha/hora
6. ✅ No puede haber reservas cercanas (dentro de 2 horas)
7. ✅ Cliente solo puede cancelar sus propias reservas
8. ✅ No se pueden cancelar reservas pasadas

---

## 🔒 Manejo de Excepciones

### Tipos de Excepciones Lanzadas:

#### IllegalArgumentException:
- Usado para **errores de validación de datos**
- Ejemplos: "Email inválido", "Fecha pasada", "ID no encontrado"

#### IllegalStateException:
- Usado para **errores de estado del sistema**
- Ejemplos: "Mesa ocupada", "Cliente tiene reservas", "No tiene permiso"

### Cómo capturarlas en Controladores:
```java
try {
    servicio.operacion();
} catch (IllegalArgumentException e) {
    // Datos inválidos - warning (amarillo)
    redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
} catch (IllegalStateException e) {
    // Error de estado - danger (rojo)
    redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
} catch (Exception e) {
    // Error inesperado - danger (rojo)
    redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
}
```

---

## 📊 Métodos Estadísticos Útiles

### Para Dashboard de ADMIN:
```java
// Clientes
long totalClientes = clienteService.contarClientes();

// Mesas
long totalMesas = mesaService.contarMesas();
long mesasDisponibles = mesaService.contarMesasDisponibles();
int capacidadTotal = mesaService.obtenerCapacidadTotal();

// Reservas
long totalReservas = reservaService.contarReservas();
long reservasFuturas = reservaService.contarReservasFuturas();
List<Reserva> reservasHoy = reservaService.obtenerReservasDeHoy();
List<Reserva> proximasReservas = reservaService.obtenerProximasReservas();
```

---

## 🚀 Ventajas de esta Arquitectura

### 1. **Controladores Limpios**
Los controladores solo manejan:
- Recibir peticiones HTTP
- Llamar al servicio correspondiente
- Manejar excepciones
- Retornar vistas

### 2. **Lógica Centralizada**
Todas las validaciones y reglas están en servicios, no duplicadas en controladores.

### 3. **Fácil Testing**
```java
@Test
public void testCrearReservaConFechaPasada() {
    // Arrange
    Long clienteId = 1L;
    Long mesaId = 1L;
    LocalDateTime fechaPasada = LocalDateTime.now().minusDays(1);
    
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> {
        reservaService.crearReserva(clienteId, mesaId, fechaPasada);
    });
}
```

### 4. **Reutilización**
El mismo servicio puede usarse desde:
- Controladores web
- APIs REST
- Tareas programadas
- Comandos de consola

---

## 🔄 Próximas Mejoras Sugeridas

1. **Agregar DTOs** (Data Transfer Objects) para separar modelo de presentación
2. **Implementar caché** para consultas frecuentes
3. **Agregar logs** con SLF4J para auditoría
4. **Implementar eventos** (publicar evento cuando se crea reserva)
5. **Agregar validaciones con Bean Validation** (@Valid)
6. **Implementar paginación** en métodos que retornan listas
7. **Agregar búsqueda avanzada** con Specifications
8. **Implementar soft delete** (borrado lógico en lugar de físico)

---

**Fecha de creación**: Noviembre 11, 2025  
**Autor**: Sistema de Gestión de Restaurante  
**Versión**: 1.0
