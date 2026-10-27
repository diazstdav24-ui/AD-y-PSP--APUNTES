º

Añidimos la dependecía de postgres. En lugar de la de H2

El docker compose nos va a permitir orchestar más de un contendor a la vez. 

Definimos cada contenedor como un servicio 

los tags de las imagenes determinan versiones. Se suele utilizar alpine porque pesa menos 

La interconxión por puertos
![alt text](image.png)

server.json es la configuracion que tiene pgAdmin de guardar los servidores a los que se va conectar: 

![alt text](image-1.png)
De esta forma al conectarnos nos entrara directamente



![alt text](image-2.png)
1. Es la cadena de datos que apunta a donde nos vamos a conectar, ese localHost apunta a la bdd qe esta en docker. EL final es el nombre de la bdd 

2. Spring.sql.init.mode=always; cuando no es una bdd embebida, spring te deja decidir que pas con es bdd 