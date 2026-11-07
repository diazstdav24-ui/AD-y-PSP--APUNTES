---
tags: [ad-psp, dam, apuntes, servicios-web, ud1]
aliases: [Intro UD1, Servicios web, GraphQL]
fuente: "Presentación UD1 REST (AD-PSP) + apuntes propios"
creado: 2026-09-24
---

# Aplicaciones distribuidas y servicios web

> [!abstract] Cómo leer estos apuntes
> - Texto normal → tuyo o de las diapositivas, reorganizado.
> - 🆕 → añadido por mí.
> - 🔧 → corrección o aclaración de tus apuntes.
> - 🧪 → reto para pensar tú. Las respuestas están plegadas.

Índice: [[AD-PSP UD1 - 00 Índice]] · Siguiente: [[AD-PSP UD1 - 02 HTTP]]

---

## 1. Evolución de las aplicaciones

| Época | Tipo de aplicación |
| --- | --- |
| Años 80 | Monolíticas |
| Años 90 | Monolitos distribuidos |
| Años 2000 | Conectadas a Internet |
| Años 2010 | "Internet es el sistema" |
| Años 2020 | Inteligentes y conectadas |

Idea de fondo: se pasa de programas aislados a **sistemas formados por piezas que se comunican por red**.

---

## 2. Aplicación distribuida

- La aplicación **no se ejecuta en un único dispositivo**: sus partes corren en máquinas distintas conectadas por red.
- El **middleware** es la capa intermedia que oculta la complejidad de esa comunicación.
- Ejemplos vistos en clase:
  - **CORBA**: el navegador descarga por HTTP un *applet* Java, y ese applet invoca por CORBA métodos de un **objeto remoto** en otro servidor (llamada y retorno).
  - **RMI** (*Remote Method Invocation*): en el ejemplo, una aplicación Java EE por capas: cliente → web → negocio (*Enterprise Beans*) → datos (base de datos).

**Desventajas comunes de estas tecnologías**
- Escalabilidad y evolución complejas.
- Tecnologías propietarias.
- Problemas de comunicación: **latencia** y **cortafuegos**.

> [!note] 🆕 Por qué molestan los cortafuegos
> Estos sistemas usaban sus propios protocolos y puertos, que los cortafuegos suelen bloquear. HTTP usa los puertos 80 y 443, que casi siempre están abiertos. Esto conecta directamente con el siguiente punto.

---

## 3. El auge de la Web

- Plataforma preferida de desarrollo.
- **Microestándares (RFC)**. 🆕 Un RFC (*Request for Comments*) es un documento público que define un estándar de Internet; HTTP se define en varios.
- Enfoque abierto.
- Protocolos más sencillos (**HTTP**).

---

## 4. Aplicación web vs servicio web

| | Aplicación web | Servicio web |
| --- | --- | --- |
| Pensado para | Personas | Otras aplicaciones y servicios |
| Interacción | A través de una interfaz de usuario | De forma **programática** por la red |
| Ejemplo 🆕 | La web de una tienda | La API que consulta esa web para pedir productos |

---

## 5. Características de un servicio web

| Característica | Qué significa (🆕 explicado) |
| --- | --- |
| Interoperabilidad | Sistemas hechos en distintos lenguajes y plataformas pueden comunicarse |
| Protocolo de red | Se apoya en un protocolo estándar (normalmente HTTP) |
| Formato de datos | Acuerdo sobre cómo se representan los datos (JSON, XML...) |
| Independencia | Cliente y servidor no dependen de la tecnología del otro |
| Descubrimiento | Se puede localizar el servicio y conocer su contrato. 🆕 Ej.: WSDL en SOAP, OpenAPI en REST |

---

## 6. Tipos de servicios web

| Tipo | Rasgos clave |
| --- | --- |
| **SOAP** | Basado en XML, reglas estrictas, descrito con WSDL. Complejo y pesado |
| **REST** | Estilo arquitectónico sobre HTTP. Ver [[AD-PSP UD1 - 03 REST]] |
| **gRPC** (*Google Remote Procedure Call*) | Usa Protobuf (*Protocol Buffers*), muy rápido, *streaming* bidireccional. Mayor complejidad. 🆕 Va sobre HTTP/2 |
| **GraphQL** | Más moderno, enfoque declarativo para consultar datos. Mayor complejidad que REST |

REST nació como **alternativa** a SOAP y a COM/COM+, CORBA, RMI...

---

## 7. GraphQL (tus apuntes)

**Qué es**: un lenguaje de consultas para APIs y a la vez un *runtime* en el servidor que las ejecuta. Lo creó Facebook en 2012 (lo liberó en 2015) para resolver un problema de su app móvil.

**Idea central**: el cliente pide **exactamente** los datos que necesita, ni más ni menos, y los recibe **en una sola petición**.

### El problema que resuelve
- **Over-fetching**: pides `/usuarios/5` y te devuelve 30 campos cuando solo necesitabas el nombre.
- **Under-fetching**: para pintar una pantalla necesitas el usuario, sus pedidos y los productos de cada pedido, y acabas haciendo 3 o 4 llamadas a endpoints distintos.

En GraphQL hay **un solo endpoint** (normalmente `/graphql`) y en la petición describes la forma de la respuesta. El servidor devuelve un JSON con esa misma forma.

### Conceptos clave
1. **Schema**: el contrato de la API. Define los tipos (`User`, `Order`...) y las operaciones. Fuertemente tipado.
2. **Query**: leer datos (equivale a un GET).
3. **Mutation**: modificar datos (equivale a POST/PUT/DELETE).
4. **Subscription**: recibir datos en tiempo real (por ejemplo, con WebSockets).
5. **Resolvers**: funciones del servidor que saben obtener los datos de cada campo del schema (de una BD, de otra API...).

### Para qué sirve
- Apps móviles, donde ahorrar datos y peticiones importa.
- Frontends complejos que combinan datos de muchas fuentes.
- Varios clientes (web, móvil, otros servicios) con necesidades distintas sobre los mismos datos.
- Agregar varios microservicios detrás de una única API.

> [!warning] Cuándo NO es tan buena idea
> No es una bala de plata. Para APIs pequeñas y simples, REST suele ser más sencillo. GraphQL añade complejidad: cachear es más difícil, hay que vigilar consultas demasiado profundas o costosas, y la curva de aprendizaje es mayor.

### 🆕 REST vs GraphQL

| | REST | GraphQL |
| --- | --- | --- |
| Endpoints | Muchos (`/users`, `/users/5`...) | Uno (`/graphql`) |
| Forma de la respuesta | La decide el servidor | La decide el cliente |
| Verbos HTTP | GET, POST, PUT, DELETE... con significado | Suele usarse POST para todo |
| Caché HTTP | Natural (URL + GET) | Más difícil |
| Errores | Códigos de estado (404, 500...) | Habitualmente responde `200` con los errores dentro del cuerpo |

> [!question]- 🧪 Necesitas mostrar el nombre de un usuario y el título de sus 3 últimos pedidos. ¿Cuántas peticiones haría un cliente REST típico y cuántas uno GraphQL?
> En REST, normalmente 2 o más: una al usuario y otra a sus pedidos (o una por pedido si el título vive en otro recurso). En GraphQL, **una sola** con una consulta que anide usuario → pedidos → título. Además, en REST recibirías campos que no necesitas (*over-fetching*).
