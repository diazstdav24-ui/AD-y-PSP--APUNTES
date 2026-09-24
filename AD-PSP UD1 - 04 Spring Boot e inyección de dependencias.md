---
tags: [ad-psp, dam, apuntes, spring-boot, inyeccion-dependencias, ud1]
aliases: [Spring Boot, DI, IoC, Inyección de dependencias]
fuente: "Presentación UD1 REST (AD-PSP)"
creado: 2026-09-24
---

# Spring Boot e inyección de dependencias

> [!info] Nota
> Tus apuntes no cubrían esta parte, así que gran parte es material de las diapositivas reorganizado, con lo que añado marcado como 🆕. Siguiendo tu regla, **no incluyo código**: describo los ejemplos de las diapositivas para que los escribas tú.

> [!abstract] Leyenda
> 🆕 añadido · 🧪 reto (respuesta plegada)

Anterior: [[AD-PSP UD1 - 03 REST]] · Siguiente: [[AD-PSP UD1 - 05 Controladores y endpoints]] · Índice: [[AD-PSP UD1 - 00 Índice]]

---

## 1. Qué es Spring Boot

- Un framework **opinionado** del ecosistema Spring.
- **Simplifica** la creación de aplicaciones **listas para producción**.
- Filosofía: **convención sobre configuración**.

> [!note] 🆕 Qué significa "opinionado"
> Spring Boot ya viene con **decisiones tomadas por defecto**: qué versiones de las librerías son compatibles entre sí, qué servidor usar, cómo convertir objetos a JSON... Tú solo configuras lo que quieras cambiar. Es la idea de "convención sobre configuración": si sigues las convenciones, no escribes configuración.

**Sin Spring Boot** (la diapositiva lo ilustra con un desarrollador desesperado) montar una aplicación Spring implicaba configurar todo a mano: ficheros XML, elegir a mano versiones compatibles de cada dependencia y desplegar en un servidor de aplicaciones externo. 🆕

---

## 2. Características

| Característica | Qué aporta |
| --- | --- |
| Aplicaciones **autónomas** | Se ejecutan por sí solas, sin desplegarlas en un servidor externo |
| Servidor **incrustado** | El servidor web va dentro de la aplicación. 🆕 Por defecto, Tomcat |
| Dependencias **predefinidas** | 🆕 Se traen con "starters": un paquete que arrastra todo lo necesario para un tipo de aplicación |
| Configuración **automática** | Spring Boot detecta qué librerías tienes y configura lo típico |
| Lista para **producción** | Trae herramientas de monitorización y arranque |
| Sin generación de código ni **XML** | Se configura con anotaciones y ficheros de propiedades |
| **Spring Initializr** | Generador de proyectos (ver abajo) |

**Tipos de aplicaciones que puedes crear**: cualquier aplicación Java (también Kotlin o Groovy): línea de comandos, web, **API REST**, microservicios...

**Beneficios**: rapidez de desarrollo, menos código *boilerplate*, facilidad de despliegue y escalabilidad.

### 🆕 Spring Initializr
Herramienta web (start.spring.io) que genera el esqueleto del proyecto. Vas eligiendo: gestor de construcción (Maven o Gradle), lenguaje, versión de Spring Boot, empaquetado, versión de Java y **dependencias**. Para una API REST, la dependencia básica es la de **Spring Web**.

---

## 3. Inyección de dependencias (DI)

### La idea
- Una clase declara una **lista de requisitos** (otros objetos) necesarios para hacer su trabajo.
- Esa clase **no es responsable de crear** las instancias de los objetos de los que depende.
- Esos objetos se **proporcionan desde fuera** de la clase.

### Ejemplo de las diapositivas: `Car` y `Engine`
- **Sin DI**: `Car` crea su propio `Engine` con `new` dentro de su constructor. `Car` queda **atado** a esa implementación concreta y es difícil de probar por separado.
- **Con DI**: `Car` **recibe** el `Engine` como parámetro de su constructor. Quien crea el `Car` decide qué `Engine` le pasa.

> [!tip] 🆕 Por qué merece la pena
> - **Desacoplamiento**: `Car` no sabe (ni le importa) cómo se construye el motor.
> - **Testeable**: en las pruebas puedes pasarle un motor falso (*mock*).
> - **Cambiar piezas** sin tocar la clase que las usa.

### El contenedor IoC
- **IoC** = *Inversion of Control* (inversión de control): en lugar de que tu código cree sus objetos, **el framework toma el control** de crearlos y conectarlos.
- El **contenedor de Spring** (IoC container) crea los objetos gestionados, llamados **beans**, y los **inyecta entre sí**. En la diapositiva: un `ReportGen` que necesita un `Query`, y un `Repo` y un `Mapper` que viven en el contenedor.

### Configuración basada en anotaciones: estereotipos
Se marcan las clases (a nivel de clase) para que Spring las registre como beans:

| Anotación | Rol típico 🆕 |
| --- | --- |
| `@Component` | Genérica: "esto es un bean" |
| `@Service` | Lógica de negocio |
| `@Repository` | Acceso a datos |
| `@Controller` | Capa web |
| `@RestController` | Capa web para APIs REST (ver [[AD-PSP UD1 - 05 Controladores y endpoints]]) |

`@Service`, `@Repository` y `@Controller` son especializaciones de `@Component` (por eso en la diapositiva cuelgan de él).

### Inyección automática (*autowiring*)
Cuando una clase necesita una dependencia, Spring **busca candidatos en el contenedor** (beans del tipo requerido) y se lo inyecta automáticamente.

> [!info] 🆕 Buenas prácticas y errores típicos
> - Se recomienda inyectar **por constructor**, como en el ejemplo de `Car`.
> - Si Spring encuentra **varios** beans del tipo pedido, no sabe cuál elegir y falla. Si no encuentra **ninguno**, también.
>   - Varios candidatos → `NoUniqueBeanDefinitionException` (se resuelve indicando cuál usar).
>   - Ninguno → `NoSuchBeanDefinitionException` (suele ser que falta la anotación de estereotipo).
> - Arquitectura típica en capas: **Controller → Service → Repository**. Cada capa recibe la siguiente por inyección.

> [!question]- 🧪 Tu clase `PedidoService` necesita un `PedidoRepository`. Pero al arrancar da `NoSuchBeanDefinitionException`. ¿Qué es lo primero que revisarías?
> Que `PedidoRepository` esté **registrada como bean**, es decir, que tenga su anotación de estereotipo (`@Repository`) y que esté en un paquete que Spring escanee (dentro del paquete de la clase principal o de sus subpaquetes).
