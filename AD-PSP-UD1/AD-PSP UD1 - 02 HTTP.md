---
tags: [ad-psp, dam, apuntes, http, ud1]
aliases: [Protocolo HTTP, Códigos de estado, Verbos HTTP]
fuente: "Presentación UD1 REST (AD-PSP) + apuntes propios + MDN"
creado: 2026-09-24
---

# Protocolo HTTP

> [!abstract] Leyenda
> Texto normal → tuyo o de las diapositivas · 🆕 añadido · 🔧 corrección · 🧪 reto (respuesta plegada)

Anterior: [[AD-PSP UD1 - 01 Aplicaciones distribuidas y servicios web]] · Siguiente: [[AD-PSP UD1 - 03 REST]] · Índice: [[AD-PSP UD1 - 00 Índice]]

---

## 1. Idea general

- HTTP (*HyperText Transfer Protocol*) es un protocolo de la **capa de aplicación** basado en **petición-respuesta**: el cliente pregunta y el servidor contesta.
- Siempre empieza el cliente; el servidor no te escribe por su cuenta.
- Modelo **cliente / servidor** y **sin estado**.
- Los mensajes viajan como **texto plano** (en HTTP/1.x) sobre TCP. Con **HTTPS** ese mismo contenido va cifrado.

> [!note] 🆕 Matices
> - HTTP/2 y HTTP/3 ya no son texto plano (son binarios) y HTTP/3 va sobre QUIC (UDP). Pero la **semántica** (métodos, cabeceras, códigos) es la misma, así que lo que estudias vale para todas.
> - "El servidor no escribe por su cuenta" es cierto en HTTP puro. Existen técnicas para saltárselo (WebSockets, *Server-Sent Events*), pero ya no es HTTP petición-respuesta clásico.
> - Puertos por defecto: 80 (HTTP) y 443 (HTTPS).

---

## 2. Protocolo sin estado (*stateless*)

Cada petición es **independiente**: el servidor no recuerda nada de la anterior. Si necesitas "memoria" (mantener una sesión iniciada, un carrito...), hay que añadirla **por encima** con mecanismos como:

- **Cookies**: el servidor da un identificador al cliente y el cliente lo devuelve en cada petición.
- **Sesiones** en el servidor (normalmente ligadas a una cookie).
- **Tokens** (por ejemplo JWT), muy habituales en APIs.

> [!tip] 🆕 Por qué importa en REST
> Al no guardar estado en el servidor, **cualquier servidor** de un conjunto puede atender **cualquier petición**, lo que facilita escalar. Por eso en las APIs REST se prefiere que el cliente envíe un token en cada petición (cabecera `Authorization`) en lugar de depender de una sesión en el servidor.

---

## 3. Estructura de los mensajes

Petición y respuesta tienen una estructura "igual": **línea inicial**, **encabezados**, **línea vacía** y **cuerpo**.

| Parte | Petición (cliente → servidor) | Respuesta (servidor → cliente) |
| --- | --- | --- |
| **Primera línea** | Método + ruta (URL) + versión de HTTP | Versión + **código de estado** + texto del estado |
| **Headers** | Metadatos (`Host`, `User-Agent`, `Accept`, `Content-Type`, `Cookie`, `Authorization`...) | Metadatos (`Content-Type`, `Content-Length`, `Set-Cookie`, `Date`...) |
| **Línea en blanco** | Separa headers del cuerpo | Ídem |
| **Cuerpo (body)** | Opcional | Opcional |

**Ejemplos de las diapositivas**
- Petición: método `GET`, ruta `/`, versión `HTTP/1.1`; headers `Host: developer.mozilla.org` y `Accept-Language: fr`.
- Respuesta: `HTTP/1.1`, código `200`, mensaje `OK`; headers `date`, `cache-control` (`public, max-age=3600`) y `content-type: text/html`.

**Detalles a tener claros**
- **Body opcional en ambos.** Un GET normalmente no lleva cuerpo; un POST o PUT normalmente sí. Un `204 No Content` por definición no lo lleva.
- **Headers**: pares nombre-valor. Los más vistos: `Content-Type` (formato del cuerpo) y `Content-Length` (cuánto ocupa).
- La **línea en blanco** marca dónde acaba la cabecera y empieza el cuerpo.

---

## 4. Encabezados

- Metadatos que dan **contexto** al mensaje, en formato `clave: valor`.
- Gran flexibilidad: se pueden inventar propios.
- Deben ser **interpretados** por el navegador, el servidor o un *proxy*.
- Tipos: de **petición**, de **respuesta** y de **petición/respuesta**.

| Header | Va en | Para qué |
| --- | --- | --- |
| `Host` | Petición | Dominio al que va dirigida |
| `User-Agent` | Petición | Qué cliente hace la petición |
| `Accept` | Petición | Qué formatos acepta el cliente |
| `Authorization` | Petición | Credenciales o token |
| `Cookie` | Petición | Devuelve las cookies al servidor |
| `Set-Cookie` | Respuesta | El servidor entrega una cookie |
| `Cache-Control` 🆕 | Respuesta | Cómo se puede cachear |
| `Location` 🆕 | Respuesta | Dónde está el recurso (redirecciones, `201 Created`) |
| `Content-Type` | Ambas | Formato del cuerpo (`application/json`...) |
| `Content-Length` | Ambas | Tamaño del cuerpo |
| `Date` | Ambas | Fecha del mensaje |

> [!question]- 🧪 ¿Qué diferencia hay entre `Accept` y `Content-Type`?
> `Accept` dice **qué formato quiero recibir**. `Content-Type` dice **qué formato tiene el cuerpo que estoy enviando** (o que recibes). Al hacer un POST con JSON, mandas `Content-Type: application/json`; si además quieres la respuesta en JSON, añades `Accept: application/json`.

---

## 5. Métodos de la petición

| Método | Para qué | Seguro 🆕 | Idempotente 🆕 |
| --- | --- | --- | --- |
| `GET` | Pedir/leer un recurso | Sí | Sí |
| `POST` | Crear un recurso o enviar datos | No | **No** |
| `PUT` | **Reemplazar** un recurso completo | No | Sí |
| `PATCH` | Modificar **parte** de un recurso | No | No necesariamente |
| `DELETE` | Borrar un recurso | No | Sí |

> [!info] 🆕 Definiciones
> - **Seguro**: no modifica nada en el servidor (solo lee).
> - **Idempotente**: repetir la misma petición varias veces deja el servidor **igual que si la hicieras una sola vez**.
> - En las diapositivas solo aparecen GET, POST, PUT y DELETE. `PATCH` viene de tus apuntes. Existen además `HEAD` y `OPTIONS`.

> [!question]- 🧪 ¿Por qué `POST` no es idempotente pero `PUT` sí?
> Si repites un `POST /products`, creas **otro** producto cada vez. Si repites un `PUT /products/5` con los mismos datos, el producto 5 acaba igual que la primera vez.

---

## 6. Códigos de estado

El servidor **siempre** responde con un código numérico. La primera cifra indica la familia:

| Familia | Significado |
| --- | --- |
| **1xx** | Informativos 🆕 |
| **2xx** | Éxito |
| **3xx** | Redirección 🆕 |
| **4xx** | Error del **cliente** (lo hemos hecho mal nosotros) |
| **5xx** | Error del **servidor** (el servidor ha tenido problemas) |

Referencia completa: https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status

### Los que tienes que dominar

| Código | Nombre | Cuándo |
| --- | --- | --- |
| **200** | OK | Todo fue bien |
| **201** | Created | Se **ha creado** un recurso en el servidor a partir de lo que enviaste en el cuerpo |
| **204** | No Content | Petición correcta y **sin cuerpo** en la respuesta |
| 301 🆕 | Moved Permanently | El recurso cambió de URL para siempre |
| 304 🆕 | Not Modified | Lo que tienes en caché sigue valiendo |
| **400** | Bad Request | Petición errónea; es el código general cuando no se sabe el error |
| **401** | Unauthorized | No estás **autenticado** |
| **403** | Forbidden | Estás identificado (o no), pero **no tienes permiso** para esa zona |
| **404** | Not Found | No se encuentra el recurso |
| **405** | Method Not Allowed | Verbo equivocado para esa ruta |
| 415 🆕 | Unsupported Media Type | Envías un formato que el servidor no admite |
| **422** | Unprocessable Content | La petición está bien formada pero **no se puede procesar** |
| **429** | Too Many Requests | Demasiadas peticiones |
| **500** | Internal Server Error | Error del servidor |
| **501** | Not Implemented | Algo que el servidor no implementa |
| 502 🆕 | Bad Gateway | Un servidor intermedio recibió una respuesta inválida (sale como ejemplo en clase) |
| 503 🆕 | Service Unavailable | Servidor caído o saturado temporalmente |

> [!bug] 🔧 Matices sobre tus apuntes
> - **401 vs 403**: el nombre engaña. `401 Unauthorized` en realidad significa "**no sé quién eres**" (falta autenticación o es inválida). `403 Forbidden` significa "**sé quién eres, pero no puedes**" (falta autorización). Regla mnemotécnica: 401 = "identifícate", 403 = "aquí no".
> - **422**: no es exclusivo de POST. Se usa en cualquier petición con cuerpo (POST, PUT, PATCH) cuando el formato es correcto pero los datos no tienen sentido (por ejemplo, un precio negativo). En la tabla de clase, para esos casos aparece `400`; ambos se usan, lo importante es ser coherente.
> - **201**: más que "enviamos algo", significa "**se creó un recurso**". 🆕 Lo habitual es acompañarlo de un header `Location` con la URL del recurso nuevo.

> [!question]- 🧪 ¿Qué código devolverías en cada caso?
> 1. Piden `/product/999` y no existe → **404**.
> 2. Un usuario sin iniciar sesión intenta ver su perfil → **401**.
> 3. Un usuario normal intenta borrar algo que solo puede borrar un administrador → **403**.
> 4. Se crea un producto correctamente → **201**.
> 5. Se borra un producto correctamente → **204**.
> 6. Llega un JSON con el precio en negativo → **400** o **422**.
> 7. Se lanza una excepción no controlada en tu código → **500**.
