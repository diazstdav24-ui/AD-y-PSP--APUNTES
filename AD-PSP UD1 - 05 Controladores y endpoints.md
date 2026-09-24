---
tags: [ad-psp, dam, apuntes, spring-boot, controladores, endpoints, postman, ud1]
aliases: [Controlador REST, Endpoints, Postman, ResponseEntity]
fuente: "Presentación UD1 REST (AD-PSP)"
creado: 2026-09-24
---

# Controladores y endpoints en Spring Boot

> [!info] Nota
> Tus apuntes no cubrían esta parte; viene de las diapositivas, reorganizada. Sin código nuevo: los ejemplos de las diapositivas los describo para que los escribas tú.

> [!abstract] Leyenda
> 🆕 añadido · 🔧 aclaración · 🧪 reto (respuesta plegada)

Anterior: [[AD-PSP UD1 - 04 Spring Boot e inyección de dependencias]] · Índice: [[AD-PSP UD1 - 00 Índice]]

---

## 1. Mi primer endpoint

### Controlador REST
- Es una **clase POJO** (una clase Java normal) anotada con `@RestController` **a nivel de clase**.
- Contiene un **conjunto de endpoints** (métodos).
- 🆕 `@RestController` = `@Controller` + `@ResponseBody`: lo que devuelve cada método **es el cuerpo de la respuesta** (no el nombre de una vista).

### Endpoint
Un endpoint es **identificador de recurso + verbo HTTP**. Ejemplos: `GET /users/1234`, `POST /product/`.

### Verbos HTTP en Spring Boot

| Anotación | Verbo |
| --- | --- |
| `@RequestMapping(method=...)` | El que indiques (forma general) |
| `@GetMapping` | GET |
| `@PostMapping` | POST |
| `@PutMapping`, `@DeleteMapping`, `@PatchMapping` 🆕 | PUT, DELETE, PATCH |

### Estructura de un método (ejemplo de la diapositiva)
Un método con `@GetMapping("/hello")` que devuelve el texto "Hello World":
- **Ruta del endpoint**: lo que va entre paréntesis en la anotación.
- **Argumentos**: no hay ninguno obligatorio.
- **Valor de retorno**: pasa a ser el **cuerpo** de la respuesta.

### Conversión automática (`HttpMessageConverter`)
Spring convierte lo que devuelves (o recibes) al formato adecuado:

| Tipo Java | Conversor por defecto |
| --- | --- |
| `String` | `StringHttpMessageConverter` |
| Objeto Java | `MappingJackson2HttpMessageConverter` (a **JSON**) |
| Formulario HTML | `FormHttpMessageConverter` |
| Otros | XML, Atom, RSS, `byte[]`... |

Ejemplo: un `@GetMapping("/producto")` que devuelve un objeto `Producto` con `id` = 1 y `nombre` = "Nombre del producto". El cliente recibe un **JSON** con esos dos campos. 🆕 Los campos del JSON salen de las propiedades de la clase: el conversor (Jackson) lee sus *getters*, así que si tu clase no los tiene, la respuesta fallará.

---

## 2. Probar con Postman

- **Postman** es una aplicación para lanzar peticiones a tu API y ver la respuesta. Se descarga la app o se usa la versión web.
- Para probar el GET del ejemplo:
  1. Elige el verbo `GET`.
  2. Escribe la URL usando la variable de entorno `{{base_url}}` seguida de `/producto`. 🆕 `base_url` es una variable que defines una vez (por ejemplo, la dirección de tu servidor local) para no repetirla en cada petición. Por defecto, una aplicación Spring Boot local escucha en el puerto 8080.
  3. Pulsa **Send**.
  4. Mira la respuesta: código `200 OK`, tiempo, tamaño y cuerpo en JSON (vista *Pretty*).
- Para una **petición POST**: en tu controlador usas `@PostMapping` y `@RequestBody` (recoge el cuerpo de la petición). En Postman eliges `POST`, vas a **Body → raw → JSON** y escribes el JSON del producto a crear.

> [!tip] 🆕 Otras opciones
> `curl` (terminal), extensiones de VS Code o IntelliJ, y el navegador (este solo sirve para GET).

---

## 3. Organización del controlador

- Anotaciones comunes a nivel de clase: `@RestController` y `@RequestMapping` (🆕 esta última fija el **prefijo de ruta común** a todos los métodos de la clase, por ejemplo `/product`).
- **Buenas prácticas para las rutas**:
  - **Sustantivos**, no verbos.
  - Nombres **claros e intuitivos**.
  - **Minúsculas**.
  - **Sin caracteres especiales**.

> [!note] 🆕 Convenciones extra
> En clase se usa `/product` en singular. Muchos equipos prefieren el **plural** para las colecciones (`/products`). Lo importante es ser **consistente**. Si una ruta necesita varias palabras, lo habitual es separarlas con guion (`/order-items`).

### Métodos básicos: CRUD
Crear un recurso · Obtener todos / uno · Actualizar un recurso · Borrar un recurso.

### Código de respuesta: `ResponseEntity<?>`
- Es el **tipo de retorno** del endpoint cuando quieres controlar la respuesta.
- Permite construir la respuesta completa: **código de estado, encabezados y cuerpo**.

---

## 4. Correspondencia CRUD ↔ HTTP

| Acción | Método | Ruta | Respuesta correcta | Respuesta errónea |
| --- | --- | --- | --- | --- |
| Obtener (todos) | GET | `/product/` | 200 OK | 404 Not Found |
| Obtener (uno) | GET | `/product/{id}` | 200 OK | 404 Not Found |
| Crear | POST | `/product/` | 201 Created | 400 Bad Request |
| Actualizar | PUT | `/product/{id}` | 200 OK | 400 Bad Request, 404 Not Found |
| Borrar | DELETE | `/product/{id}` | 204 No Content | 404 Not Found |

**Otros códigos de error comunes**: 401 y 403 (autenticación / control de acceso), 405 (método no permitido), 415 (tipo no soportado). Detalle en [[AD-PSP UD1 - 02 HTTP#6. Códigos de estado]].

> [!note] 🆕 Matices del mundo real
> - **GET de una colección**: en la práctica, si no hay elementos se suele devolver `200` con una **lista vacía**, y el `404` se reserva para un elemento concreto que no existe (`/product/{id}`). Si te lo piden, sigue la tabla de clase; pero conviene que conozcas este criterio.
> - **POST**: además de `201`, es buena práctica devolver el header `Location` con la URL del recurso creado.
> - **DELETE**: en clase aparece la duda "¿404?". Como DELETE es idempotente, hay quien devuelve `204` aunque ya no existiera y quien devuelve `404`. Ambas posturas son defendibles; elige una y sé coherente.

---

## 5. Más formas de recibir datos

### `@PathVariable`
- Inyecta una **variable de la ruta** como argumento del método.
- Ejemplos de rutas: `/product/{id}` y `/post/{postId}/comment/{commentId}` (varias variables).
- En la diapositiva: el `{id}` de `@GetMapping("/{id}")` llega al argumento `Long id` del método.
- **Expresiones complejas** con `PathPattern`: `{var:regex}`, que se recoge con `@PathVariable("var")`. Ejemplo: `/product/{id:[0-9]+}` solo acepta `id` numérico.

### `@RequestParam`
La **query** de una URL va a continuación de `?`, con pares `clave=valor` separados por `&`. Ejemplos: `/?sort=date`, `/?city=Sevilla&date=April`, `/product/?maxPrice=23.45&sort=desc`.

`@RequestParam` rescata un parámetro de la query:
- `required=false` → **no obligatorio**.
- `defaultValue=valor` → **valor por defecto** si no se envía.
- Con `Map<String, String>` o `MultiValueMap<String, String>` y **sin** indicar `value`, recoges **todos** los parámetros de golpe.

> [!tip] 🆕 ¿Cuál uso?
> - **`@PathVariable`** → para **identificar** un recurso concreto (`/product/42`).
> - **`@RequestParam`** → para **filtrar, ordenar o paginar** (`/product?maxPrice=20&sort=desc`).
> - **`@RequestBody`** → para el **contenido** que se crea o actualiza (POST/PUT/PATCH).

### Valores de retorno
- `ResponseEntity`: controlas el código, headers y cuerpo.
- **Clase POJO**: Spring responde con **`200 OK`** por defecto.

---

## 6. 🆕 Errores típicos al probar

| Lo que ves en Postman | Causa probable |
| --- | --- |
| `404 Not Found` | Ruta mal escrita, o falta el prefijo del `@RequestMapping` |
| `405 Method Not Allowed` | Existe la ruta, pero con otro verbo |
| `415 Unsupported Media Type` | Mandas un POST con cuerpo sin indicar `Content-Type: application/json` |
| `400 Bad Request` | JSON mal formado (coma de más, comillas simples...) o tipo incorrecto |
| `500 Internal Server Error` | Excepción no controlada en tu código: **mira la consola** de la aplicación |

---

## 7. 🧪 Retos para practicar (sin solución, a propósito)

1. Crea un proyecto en Spring Initializr con la dependencia **Spring Web**, y haz que `GET /hello` devuelva un texto. Compruébalo en Postman. ¿Qué `Content-Type` devuelve la respuesta?
2. Haz que otro endpoint devuelva un **objeto** con dos atributos. ¿Qué cambia en el `Content-Type`? ¿Qué conversor ha actuado?
3. Diseña **en papel** la tabla de endpoints de un recurso `libro` (acción, método, ruta, código correcto, código de error).
4. Añade un `POST` que reciba un libro en el cuerpo y devuelva `201`. Pruébalo con Postman y provoca un `415` y un `400` a propósito.
5. Añade un `GET /libro/{id}` con `@PathVariable`, y un `GET /libro` que admita un parámetro de búsqueda **opcional**.

Cuando los tengas, me los pasas y los revisamos juntos.

---

## ✅ Checklist antes del examen

- [ ] Sé explicar qué es un endpoint y por qué la URI lleva sustantivos.
- [ ] Sé qué anotación va en cada verbo y qué hace `@RequestBody`.
- [ ] Distingo `@PathVariable` de `@RequestParam` y sé cuándo usar cada uno.
- [ ] Sé qué código devolver en cada operación CRUD (éxito y error).
- [ ] Distingo 401 de 403, y 400 de 422.
- [ ] Sé qué hace `ResponseEntity` y qué devuelve Spring si solo retorno un POJO.
- [ ] Sé explicar la inyección de dependencias con el ejemplo del coche.
- [ ] Sé probar todo con Postman, incluido un POST con JSON.
