# Swagger / OpenAPI
# Swagger / OpenAPI

Swagger es un conjunto de herramientas para **diseñar, documentar y probar APIs REST**. El estándar que hay detrás se llama **OpenAPI**, y "Swagger" se usa como nombre de las herramientas que lo implementan.

## Swagger vs. OpenAPI

- **OpenAPI Specification (OAS):** es la *especificación*, el formato estándar (YAML o JSON) con el que se describe una API: qué endpoints tiene, qué parámetros acepta, qué respuestas devuelve y qué autenticación usa.
- **Swagger:** es el nombre original del proyecto. Cuando se donó a la Linux Foundation pasó a llamarse OpenAPI, y "Swagger" quedó como la marca de las herramientas (Swagger UI, Swagger Editor, Swagger Codegen).

## Herramientas principales

| Herramienta | Qué hace |
|---|---|
| **Swagger Editor** | Editor donde escribes el contrato de la API y ves la documentación en tiempo real. |
| **Swagger UI** | Genera una página web interactiva a partir de la especificación. Permite ver los endpoints y probarlos desde el navegador con el botón "Try it out". |
| **Swagger Codegen** | Genera código cliente (SDKs) o esqueletos de servidor a partir de la especificación. |

## Para qué se usa

1. **Documentar la API automáticamente:** la documentación se mantiene sincronizada con el código y no queda obsoleta.
2. **Probar endpoints sin Postman:** se pueden lanzar peticiones GET, POST, PUT y DELETE desde la propia página.
3. **Contrato entre frontend y backend:** ambos equipos acuerdan cómo será la API antes de implementarla y pueden trabajar en paralelo.
4. **Generar código:** clientes en distintos lenguajes a partir de un único contrato.
5. **Validar y testear:** comprobar que la API cumple lo que promete.

## Enfoques de trabajo

- **Design-first (contrato primero):** se escribe la especificación OpenAPI y después se implementa la API siguiendo ese contrato.
- **Code-first (código primero):** se escribe la API y las anotaciones del código generan la especificación automáticamente.

## En Java / Spring Boot

Lo más habitual es el enfoque code-first con la librería **springdoc-openapi**:

- Se añade la dependencia al proyecto. En Spring Boot 3 es `springdoc-openapi-starter-webmvc-ui`; en Spring Boot 2 era `springdoc-openapi-ui`.
- Se anotan los controladores y los DTO.
- Con la configuración por defecto, la aplicación expone dos rutas (ejemplo con el puerto 8080):
  - `localhost:8080/v3/api-docs`: devuelve la **especificación OpenAPI en formato JSON** con la descripción completa de la API.
  - `localhost:8080/swagger-ui.html`: la interfaz **Swagger UI**, que muestra todos los controladores y endpoints de la API y los **schemas** de los DTO que usan.

Swagger UI no inventa nada: se limita a leer el JSON de `/v3/api-docs` y pintarlo de forma interactiva.

## Configuración desde `application.properties`

Parte del comportamiento se puede cambiar desde el fichero de propiedades, sin tocar el código. Algunas propiedades habituales:

| Propiedad | Para qué sirve |
|---|---|
| `springdoc.api-docs.path` | Cambia la ruta del JSON (por defecto `/v3/api-docs`). |
| `springdoc.swagger-ui.path` | Cambia la ruta de la interfaz Swagger UI. |
| `springdoc.swagger-ui.operations-sorter` | Ordena los endpoints dentro de cada controlador. |
| `springdoc.swagger-ui.tags-sorter` | Ordena los grupos de controladores. |
| `springdoc.packages-to-scan` | Limita qué paquetes se documentan. |
| `springdoc.paths-to-match` | Limita qué rutas se documentan. |

## Anotaciones principales

Todas pertenecen al paquete `io.swagger.v3.oas.annotations`.

| Anotación | Dónde se coloca | Qué hace |
|---|---|---|
| `@OpenAPIDefinition` | Una clase de configuración | Define la **información general de la API**: título, versión, descripción, datos de contacto y licencia. Se rellena con `@Info`, que a su vez contiene `@Contact` y `@License`. |
| `@Tag` | Clase del controlador | Da **nombre y descripción** a un grupo de endpoints. Por ejemplo, name = "Product Controller" y una descripción de lo que gestiona. En Swagger UI aparece como sección agrupada. |
| `@Operation` | Método del endpoint | Pone **título (`summary`) y descripción** a un endpoint concreto. |
| `@ApiResponses` | Método del endpoint | Es el **contenedor**: espera dentro un array de `@ApiResponse`. No documenta nada por sí sola. |
| `@ApiResponse` | Dentro de `@ApiResponses` | Documenta **una respuesta posible** del endpoint (ver detalle abajo). |
| `@Schema` | Clase DTO o sus campos | Describe el modelo de datos: descripción de cada campo, ejemplos, si es obligatorio, etc. |

### Qué hace `@ApiResponse`

Documenta **una posible respuesta HTTP** que puede devolver un endpoint. Cada una describe un resultado distinto, por ejemplo uno correcto y varios de error. Sus atributos más importantes son:

- `responseCode`: el código HTTP, como "200", "201", "400" o "404". Es un texto, no un número.
- `description`: explica en qué situación se produce esa respuesta.
- `content`: indica el tipo de contenido que se devuelve (por ejemplo JSON) y su `@Schema`, es decir, qué DTO viaja en el cuerpo de la respuesta. Se puede omitir cuando no hay cuerpo, como en un 404.

Por eso un endpoint bien documentado suele tener **varias** `@ApiResponse` agrupadas dentro de una única `@ApiResponses`.

## Ideas clave para repasar

- Swagger = herramientas; OpenAPI = estándar.
- `/v3/api-docs` devuelve el JSON y `/swagger-ui.html` lo muestra de forma visual e interactiva.
- Una sola especificación sirve para documentar, probar y generar código.
- `@OpenAPIDefinition` documenta la API completa, `@Tag` el controlador, `@Operation` el endpoint, `@ApiResponse` cada respuesta y `@Schema` cada DTO.
- `@ApiResponses` solo agrupa; la información real está en cada `@ApiResponse`.
- Parte de la configuración se ajusta desde `application.properties`.
