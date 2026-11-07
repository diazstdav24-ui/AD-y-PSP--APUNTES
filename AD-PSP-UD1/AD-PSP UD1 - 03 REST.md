---
tags: [ad-psp, dam, apuntes, rest, ud1]
aliases: [REST, Servicios REST, Richardson]
fuente: "Presentación UD1 REST (AD-PSP) + apuntes propios"
creado: 2026-09-24
---

# REST

> [!abstract] Leyenda
> Texto normal → tuyo o de las diapositivas · 🆕 añadido · 🔧 corrección · 🧪 reto (respuesta plegada)

Anterior: [[AD-PSP UD1 - 02 HTTP]] · Siguiente: [[AD-PSP UD1 - 04 Spring Boot e inyección de dependencias]] · Índice: [[AD-PSP UD1 - 00 Índice]]

---

## 1. Qué es REST

- **Representational State Transfer**.
- Es un **estilo arquitectónico**: un conjunto de principios para **describir y acceder a recursos** en la red. 🆕 No es un protocolo ni un estándar; lo definió Roy Fielding en su tesis doctoral (2000).
- Nace como alternativa a SOAP y a COM/COM+, CORBA, RMI... (ver [[AD-PSP UD1 - 01 Aplicaciones distribuidas y servicios web]]).
- **HTTP es el motor de REST**: REST se apoya en sus verbos, URLs, cabeceras y códigos de estado.

---

## 2. Características de REST

| Característica | Qué significa |
| --- | --- |
| **Basado en HTTP** | Usa el protocolo tal como es. Al ir por los puertos estándar (80/443), suele pasar los cortafuegos sin problemas |
| **Cliente-servidor** | Cliente y servidor están separados y evolucionan de forma independiente |
| **Sin estado** | Cada petición lleva toda la información necesaria; el servidor no recuerda las anteriores |
| **Interfaz uniforme** | Todos los recursos se manejan igual: identificados por URI, con los mismos verbos y formatos |
| **En capas** | Entre cliente y servidor puede haber intermediarios (proxies, balanceadores) sin que el cliente lo note |
| 🆕 **Cacheable** | Las respuestas pueden indicar si se pueden guardar en caché |
| 🆕 **Código bajo demanda** | (Opcional) el servidor puede enviar código ejecutable al cliente |

> [!bug] 🔧 Aclaración de tus apuntes
> Tenías escrito *"No tiene limitaciones de red por el puerte"*. Interpreto que querías decir que, al usar HTTP por puertos estándar, **no tiene problemas con los cortafuegos**, justo lo contrario que CORBA o RMI. Lo he incluido en la primera fila de la tabla; si querías decir otra cosa, corrígelo.

---

## 3. Recursos y URIs

- Un **recurso** es cualquier cosa que se pueda nombrar: un usuario, un producto, un pedido.
- Se **identifica con una URI**: `/users` (colección) y `/users/123` (un elemento concreto).

### 🆕 URL vs URI (el título que dejaste sin contenido)

| Término | Qué es | Ejemplo |
| --- | --- | --- |
| **URI** | Identifica un recurso | `/users/123` |
| **URL** | Es una URI que además dice **dónde y cómo** llegar a él | `https://api.ejemplo.com/users/123` |

Toda URL es una URI, pero no toda URI es una URL. En REST se habla de "URIs" porque lo importante es **identificar** el recurso.

### REST delimita las acciones por recursos
La URI dice **sobre qué** actúas y el verbo dice **qué haces**. Por eso la **misma ruta con distinto verbo** hace cosas distintas:

| Petición | Qué hace |
| --- | --- |
| `GET /productos` | Lista los productos |
| `POST /productos` | Crea un producto nuevo |

Consecuencia: en la URI van **sustantivos**, no verbos (`/productos`, no `/crearProducto`).

---

## 4. Operaciones con verbos HTTP

Cómo se aplican a una colección y a un elemento (🆕 tabla resumen):

| Verbo | `/products` (colección) | `/products/{id}` (elemento) |
| --- | --- | --- |
| `GET` | Listar todos | Obtener uno |
| `POST` | **Crear** uno nuevo | (no se suele usar) |
| `PUT` | (no se suele usar) | **Reemplazar** completo |
| `PATCH` | (no se suele usar) | Modificar parcialmente |
| `DELETE` | (no se suele usar) | Borrar |

Más detalles en [[AD-PSP UD1 - 02 HTTP#5. Métodos de la petición]].

---

## 5. Recurso vs representación

- El **recurso** es la cosa (el producto 123). Su **representación** es la forma en que se te entrega: JSON, XML, HTML...
- Un mismo recurso puede tener **varias representaciones**. El cliente elige con el header `Accept`. Ejemplo de clase: `/product/123` con `Accept: application/json`.
- 🆕 Esto se llama **negociación de contenido**. Si el servidor no puede ofrecer el formato pedido, puede responder `406 Not Acceptable`. Y si **tú** envías un formato que no admite, `415 Unsupported Media Type`.

*(Esto completa la palabra "accept" que dejaste suelta en tus apuntes.)*

---

## 6. Aprovechar los códigos de estado

REST usa los códigos HTTP para comunicar el resultado, en vez de inventar los suyos. Ejemplos de clase: `200 OK`, `201 Created`, `404 Not Found`. Tabla completa en [[AD-PSP UD1 - 02 HTTP#6. Códigos de estado]].

---

## 7. JSON

- Formato de texto ligero para intercambiar datos. Es la representación más habitual en REST.
- Frente a XML (el ejemplo de la diapositiva muestra los mismos datos de una empresa en ambos), JSON es **más compacto y legible**: XML repite cada campo en una etiqueta de apertura y otra de cierre.

> [!info] 🆕 Reglas de JSON
> - Un objeto va entre `{}` con pares `"clave": valor`. Un array va entre `[]`.
> - Las claves **siempre** llevan comillas dobles. No admite comillas simples ni comentarios.
> - Tipos: cadena, número, booleano (`true`/`false`), `null`, objeto y array.
> - Sus tipos no coinciden 1 a 1 con los de Java: por eso hace falta un conversor (lo verás en [[AD-PSP UD1 - 05 Controladores y endpoints]]).

---

## 8. Niveles de madurez de Richardson

Una escala para medir cuánto se acerca una API a REST:

| Nivel | Nombre | Qué tiene |
| --- | --- | --- |
| **0** | POX (*Plain Old XML*) | Un único endpoint y un único verbo; todo se hace por ahí |
| **1** | URIs | **Varias URIs**, una por recurso, pero con un único verbo |
| **2** | Verbos HTTP | Interacción con los recursos usando **verbos distintos** (y códigos de estado) |
| **3** | Hypermedia (HATEOAS) | Las respuestas incluyen **enlaces** a las acciones posibles a continuación |

> [!note] 🆕 Contexto
> La inmensa mayoría de las APIs "REST" reales se quedan en el **nivel 2**. El nivel 3 (HATEOAS) es el más raro. Lo que construirás en esta unidad con Spring Boot es nivel 2.

> [!question]- 🧪 ¿En qué nivel está una API con un único endpoint `/api` al que se manda todo por POST, indicando la acción en el cuerpo?
> En el **nivel 0**: un solo endpoint y un solo verbo.
