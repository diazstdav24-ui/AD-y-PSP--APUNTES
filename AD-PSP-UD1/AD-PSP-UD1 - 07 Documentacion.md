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

### 1. Nivel API

| Anotación | Nivel | Descripción | Atributos clave |
|---|---|---|---|
| `@OpenAPIDefinition` | API | Punto de entrada de la documentación global. Se coloca en una clase de configuración. | `info`<br>`servers`<br>`tags`<br>`externalDocs` |
| `@Info` | API | Metadatos generales de la API. | `title`<br>`version`<br>`description`<br>`contact`<br>`license` |
| `@Contact` | API | Datos de contacto del responsable. | `name`<br>`email`<br>`url` |
| `@License` | API | Licencia bajo la que se publica la API. | `name`<br>`url` |
| `@Server` | API | Servidores donde está desplegada la API. | `url`<br>`description` |

### Ejemplo

```java
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de Productos",
        version = "1.0",
        description = "API REST para la gestión del catálogo de productos",
        contact = @Contact(name = "Davi", email = "davi@ejemplo.com"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
    ),
    servers = @Server(url = "http://localhost:8080", description = "Servidor local")
)
public class OpenApiConfig {
}
```

> **Resultado en Swagger UI:** el título, la versión y la descripción aparecen en la cabecera de la página, y el servidor en el desplegable *Servers*.

### 2.Nivel Controlador: 

| Anotación | Nivel | Descripción | Atributos clave |
|---|---|---|---|
| `@Tag` | Controlador | 	Agrupa los endpoints bajo una sección en Swagger UI | `name`<br>`description`|
| `@ApiResponses` | Controlador | Si la pones en la clase, aplica a todos los endpoints. Sirve para errores comunes como un 500. | `@value`|
| `@Hidden` | Controlador o endpoint |Oculta algo de la documentación | sin atributos |
### Ejemplo

```java
@RestController
@RequestMapping("/products")
@Tag(name = "Product Controller", description = "Operaciones para la gestión del catálogo de productos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Endpoints (ver nivel 3)
}
```

> **Resultado en Swagger UI:** todos los endpoints de esta clase aparecen agrupados en una sección desplegable llamada *Product Controller*, con su descripción.

---

## 3. Nivel endpoint

| Anotación | Nivel | Descripción | Atributos clave |
|---|---|---|---|
| `@Operation` | Endpoint | Título y descripción de la operación. | `summary`<br>`description`<br>`tags`<br>`deprecated` |
| `@ApiResponses` | Endpoint | Contenedor que agrupa las posibles respuestas. | `value` |
| `@ApiResponse` | Endpoint | Una respuesta HTTP posible del endpoint. | `responseCode`<br>`description`<br>`content` |
| `@Content` | Dentro de `@ApiResponse` | Tipo de contenido y qué viaja en el cuerpo. | `mediaType`<br>`schema`<br>`examples` |
| `@ExampleObject` | Dentro de `@Content` | Ejemplo literal del JSON. | `name`<br>`summary`<br>`value` |
| `@Parameter` | Parámetro del método | Documenta un `@PathVariable` o `@RequestParam`. | `name`<br>`description`<br>`required`<br>`example` |
| `@RequestBody` (Swagger) | Parámetro del método | Documenta el cuerpo de la petición en un POST o PUT. | `description`<br>`required`<br>`content` |

### Ejemplo

```java
@Operation(
    summary = "Obtener un producto por su ID",
    description = "Devuelve los datos de un producto concreto del catálogo"
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Producto encontrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProductDTO.class),
            examples = @ExampleObject(
                name = "Producto de ejemplo",
                value = """
                    {
                      "id": 1,
                      "name": "Teclado mecánico",
                      "price": 59.99,
                      "category": "PERIFERICOS",
                      "tags": ["gaming", "rgb"]
                    }
                    """
            )
        )
    ),
    @ApiResponse(responseCode = "404", description = "No existe ningún producto con ese ID")
})
@GetMapping("/{id}")
public ResponseEntity<ProductDTO> findById(
        @Parameter(description = "ID del producto a buscar", required = true, example = "1")
        @PathVariable Long id) {
    return ResponseEntity.ok(productService.findById(id));
}

@Operation(summary = "Crear un producto nuevo")
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "201",
        description = "Producto creado correctamente",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = ProductDTO.class))
    ),
    @ApiResponse(responseCode = "400", description = "Los datos enviados no son válidos")
})
@PostMapping
public ResponseEntity<ProductDTO> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del producto a crear",
            required = true
        )
        @RequestBody ProductDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto));
}
```

> **Resultado en Swagger UI:** cada endpoint muestra su título, el parámetro `id` con su ejemplo ya relleno, y la lista de respuestas (200/404 y 201/400) con el JSON de ejemplo y el modelo del DTO.

---

## 4. Nivel DTO

| Anotación | Nivel | Descripción | Atributos clave |
|---|---|---|---|
| `@Schema` (clase) | DTO | Describe el modelo de datos completo. | `name`<br>`description` |
| `@Schema` (campo) | Atributo | Describe un campo concreto del DTO. | `description`<br>`example`<br>`requiredMode`<br>`accessMode`<br>`minLength` / `maxLength`<br>`minimum` / `maximum`<br>`allowableValues` |
| `@ArraySchema` | Atributo | Describe un campo que es una lista. | `schema`<br>`arraySchema`<br>`minItems` / `maxItems` |

### Ejemplo

```java
@Schema(description = "Representa un producto del catálogo")
public record ProductDTO(

    @Schema(description = "Identificador único del producto",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "Nombre comercial del producto",
            example = "Teclado mecánico",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3, maxLength = 100)
    String name,

    @Schema(description = "Precio en euros",
            example = "59.99",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0")
    BigDecimal price,

    @Schema(description = "Categoría del producto",
            example = "PERIFERICOS",
            allowableValues = {"PERIFERICOS", "COMPONENTES", "ALMACENAMIENTO"})
    String category,

    @ArraySchema(
        arraySchema = @Schema(description = "Etiquetas asociadas al producto"),
        schema = @Schema(example = "gaming"),
        minItems = 0)
    List<String> tags
) {}
```

> **Resultado en Swagger UI:** en la sección *Schemas* aparece el modelo con cada campo descrito, los obligatorios marcados con asterisco y los valores permitidos de `category`. El `id` sale en las respuestas pero no se pide en el POST.

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
