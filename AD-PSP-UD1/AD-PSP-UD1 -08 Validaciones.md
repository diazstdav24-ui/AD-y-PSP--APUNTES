º

- Para cumplir los requisitos que nosotros establezcamos
- Las validadciones se pueden realizar en cualquier capa, pero aplicarlos en todas las capas es multiplicar trabaja

![alt text](image.png)

Lo suyo es hacer la validacion antes de llegar al servidor

![alt text](image-1.png)

# Dependecias

Para comenzar las validaciones tienes que añadir la dependecnia validation.

Esto hace que hibernate se encargue de validar

El estandar ofrece n juego de validaciones e hibernate ofrece otras.

## Anotaciones tipicas:

![alt text](image-2.png)

**Docs:**

- Hibernates [Hibernate doc](https://docs.hibernate.org/validator/9.2/api/org/hibernate/validator/constraints/package-summary.html)

- Jakarta Validation [Jakarta](https://jakarta.ee/specifications/bean-validation/4.0/apidocs/jakarta.validation/module-summary.html)

# Validaciones

las validaciones pueden combinarse y concretar el error

La validacion de un objeto puede dar un fallo o más de uno. setProperty

![alt text](image-4.png)

Como no vamos a usr el record fuera de esta clase se crea un record local

Aqui por ejemplo dejamos algunos campos null

Esta validacion se aplica a un campo del dto, pero aprenderemos a nivel dto

![alt text](image-5.png)

Este es pa un unico campo.

Los mensajes pueden ser creados en la misma validacion `@NotBlank(message = "Nombre no puede estar en blanco")` o ser mandados desde una clase nueva que los centralice todas, o desde un nuevo fichero llamado errors.properties

Desde este `@Bean`se le especifica el mesnaej que va recibir la properties
![alt text](image-6.png)

Hay un objeto en JAva que se determina locale, identifica la localizacion. Esta conectado con monedas, hora y demás de diferentes paises

`@Valid` no es lo mismo que `@Vaidate`
![alt text](image-7.png)

![alt text](image-8.png)
