repasar many to one y one to many 


###  El significado real de `mappedBy`

* Tal como has deducido, **mappedBy** **siempre se coloca en el lado padre (lado inverso)** y su valor debe ser el nombre del atributo en la clase Java de la entidad hija[2][3].
* Literalmente, le indica a Hibernate: *"Yo (Categoría) no gestiono una columna de clave foránea en mi tabla; la relación ya la mapea y controla el atributo* *categoria* *que está dentro de la clase* *Producto* *"*[2][3].
* Es decir, tal como lo has expresado: **mappedBy** **le dice al padre que rastree el atributo de su hija donde reside la clave foráne**