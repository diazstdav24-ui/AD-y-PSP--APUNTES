---
tags: [ad-psp, dam, indice, ud1]
aliases: [Índice AD-PSP, UD1 API REST con Spring Boot]
fuente: "Presentación UD1 REST (AD-PSP) + apuntes propios"
creado: 2026-09-24
---

# AD-PSP · UD1 — Desarrollo de una API REST con Spring Boot

| Nota | Contenido | Origen |
| --- | --- | --- |
| [[AD-PSP UD1 - 01 Aplicaciones distribuidas y servicios web]] | Evolución, CORBA/RMI, tipos de servicios web, GraphQL | Diapositivas + tus apuntes (GraphQL) |
| [[AD-PSP UD1 - 02 HTTP]] | Mensajes, headers, verbos, códigos de estado | Tus apuntes reorganizados + diapositivas |
| [[AD-PSP UD1 - 03 REST]] | Principios, URIs, representaciones, JSON, Richardson | Tus apuntes + diapositivas |
| [[AD-PSP UD1 - 04 Spring Boot e inyección de dependencias]] | Qué es Spring Boot, DI, IoC, estereotipos | Diapositivas |
| [[AD-PSP UD1 - 05 Controladores y endpoints]] | Endpoints, CRUD↔HTTP, PathVariable, RequestParam, Postman | Diapositivas |

> [!abstract] Leyenda
> 🆕 añadido · 🔧 corrección o aclaración de tus apuntes · 🧪 reto para probar tú mismo

## El hilo conductor de la unidad

1. Las aplicaciones se **distribuyen** y sus piezas se hablan por red.
2. Las tecnologías antiguas (CORBA, RMI) daban problemas; la **Web** ofrecía un protocolo sencillo: **HTTP**.
3. Sobre HTTP se define **REST**: recursos identificados por URIs, manipulados con verbos y respondidos con códigos de estado.
4. **Spring Boot** permite construir esa API rápido, apoyándose en la **inyección de dependencias**.
5. En un **controlador** cada método es un endpoint (recurso + verbo), y se prueba con **Postman**.

## Lo que más se suele confundir

- **401 vs 403** y **400 vs 422** → [[AD-PSP UD1 - 02 HTTP#6. Códigos de estado]]
- **Accept vs Content-Type** → [[AD-PSP UD1 - 02 HTTP#4. Encabezados|Encabezados]]
- **PUT vs PATCH** e idempotencia → [[AD-PSP UD1 - 02 HTTP#5. Métodos de la petición]]
- **PathVariable vs RequestParam** → [[AD-PSP UD1 - 05 Controladores y endpoints#5. Más formas de recibir datos]]

## Organización sugerida en Obsidian
Guarda estas notas en una carpeta `AD-PSP/` y las de Python en `Python/`. Los enlaces `[[ ]]` siguen funcionando aunque muevas los ficheros dentro del vault.
