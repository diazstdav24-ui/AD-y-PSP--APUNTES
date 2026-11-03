# Direccionalidad 

Unidireccional: A conoce a B, B no le conoce 

Bidireccional: A conoce a B y B conoce A 

Lado propietarío, es el que va realmente a la bdd

# Métodos helper (auxiliares) en relaciones bidireccionales JPA/Hibernate

Los métodos helper (o métodos auxiliares) son funciones que desarrollas manualmente en tus entidades para gestionar la persistencia y la coherencia del modelo.

## 1. ¿Para qué sirven?

Sirven para **sincronizar ambos lados de una relación bidireccional** en la memoria de Java.

En Java, si estableces un extremo de una relación (por ejemplo, añades un hijo a la lista del padre), el otro extremo no se actualiza automáticamente. Los métodos helper garantizan que, al realizar una operación (añadir o eliminar), ambos lados del grafo de objetos queden enlazados y en un estado coherente de forma transparente.

## 2. ¿Cuándo se deben colocar?

Siempre que definas una asociación bidireccional, como por ejemplo:

- `@OneToMany` / `@ManyToOne`
- `@OneToOne` bidireccional
- `@ManyToMany` bidireccional

## 3. ¿En qué parte de la asociación se colocan?

Se colocan habitualmente en la entidad del **lado padre / lado inverso** (la entidad que contiene la colección o define el atributo `mappedBy`).

En este patrón, el padre se encarga de llamar al `setter` del hijo para asegurar que el **lado propietario** (el que gestiona la clave foránea) reciba la referencia adecuada.

## 4. ¿Qué pasa si no los pongo?

Si no utilizas métodos helper y olvidas actualizar manualmente ambos lados en tu código de negocio:

1. **Clave foránea a `NULL` o fallo al guardar:** el lado propietario (el `@ManyToOne` que contiene la columna FK) no sabrá a qué padre pertenece. Si solo haces `person.getPhones().add(phone)` pero no `phone.setPerson(person)`, Hibernate insertará la fila con la clave foránea en `NULL`.
2. **Desincronización del contexto en memoria:** un lado verá la relación (el hijo estará en la lista `phones`), pero al navegar desde el hijo hacia el padre (`phone.getPerson()`) obtendrás `null` o un `NullPointerException`.
3. **Ineficiencia o fallos al eliminar:** si la colección y la referencia no se desvinculan correctamente, Hibernate puede no detectar que la entidad ha quedado huérfana, dejando registros obsoletos en memoria o en la base de datos.

---

## Ejemplo completo: `Person` y `Phone`

### Lado propietario (`Phone`)

Es el que tiene la clave foránea (`person_id`). Es igual en los dos casos.

```java
@Entity
public class Phone {

    @Id @GeneratedValue
    private Long id;

    private String number;

    @ManyToOne
    @JoinColumn(name = "person_id")   // aquí vive la FK
    private Person person;

    // getters y setters
    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }
}
```

**Qué hay que entender:** `Phone` es el lado propietario porque tiene `@JoinColumn`. Hibernate solo mira este lado para decidir qué valor lleva la columna `person_id`.

---

### Caso A: SIN métodos helper

```java
@Entity
public class Person {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();

    public List<Phone> getPhones() { return phones; }
}
```

Uso en el servicio:

```java
Person person = new Person();
Phone phone = new Phone();
phone.setNumber("600123456");

person.getPhones().add(phone);   // solo actualizamos el lado padre
personRepository.save(person);
```

**Qué ocurre:**

- Por el `cascade = ALL`, Hibernate guarda también el `Phone`.
- Pero `phone.getPerson()` sigue siendo `null`, y Hibernate lee la FK del lado propietario.
- Resultado en BBDD: la fila de `phone` se inserta con `person_id = NULL`.
- En memoria: `person.getPhones()` contiene el teléfono, pero `phone.getPerson()` devuelve `null`. El modelo está **incoherente**.

Para arreglarlo a mano tendrías que acordarte, en cada sitio del código, de escribir:

```java
person.getPhones().add(phone);
phone.setPerson(person);   // fácil de olvidar
```

---

### Caso B: CON métodos helper

```java
@Entity
public class Person {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();

    // MÉTODOS HELPER
    public void addPhone(Phone phone) {
        this.phones.add(phone);   // lado padre
        phone.setPerson(this);    // lado propietario (hijo)
    }

    public void removePhone(Phone phone) {
        this.phones.remove(phone);   // lado padre
        phone.setPerson(null);       // lado propietario (hijo)
    }
}
```

Uso en el servicio:

```java
Person person = new Person();
Phone phone = new Phone();
phone.setNumber("600123456");

person.addPhone(phone);   // un único método sincroniza los dos lados
personRepository.save(person);
```

**Qué ocurre:**

- `addPhone` añade el teléfono a la lista **y** hace `phone.setPerson(this)`.
- Hibernate inserta la fila con `person_id` correcto.
- En memoria, `phone.getPerson()` devuelve la persona: modelo **coherente**.

Para eliminar:

```java
person.removePhone(phone);   // quita de la lista y pone person = null
```

Gracias a `orphanRemoval = true`, Hibernate detecta que el teléfono ya no pertenece a ninguna persona y lo borra de la base de datos.

---

## Resumen comparativo

| | Sin helpers | Con helpers |
|---|---|---|
| Sincronización de ambos lados | Manual, fácil de olvidar | Automática, en un único método |
| `person_id` al guardar | Puede quedar en `NULL` | Siempre correcto |
| `phone.getPerson()` | Puede ser `null` | Devuelve el padre |
| Eliminación (`orphanRemoval`) | Puede fallar o ser inconsistente | Funciona de forma fiable |
| Riesgo de errores | Alto | Bajo |

# Fetching 

Marcar el momento de recibir los datos

El fetching oneToMany es lazy y las oneToOne son eager

Lazy significa que no se toman todos los datos al rescatar la entidad 

Lazy es el mejor en lo que a optimización se refiere 

# Transaccion 

Es un conjunto de conexiones a la bdd que se tienen que trabajar de forma atomica.