# tacs20232C-grupo2

https://www.tacs-utn.com.ar/
- [Enunciado](https://docs.google.com/document/d/e/2PACX-1vTyCHMPW35IZ2Y-71h1ctMu-Qnf-_A-nDz5uzkm8hOz1xjK01sK6484KS_FuxTGIypyH1Idu5rYbdW1/pub)
- [Planificación](https://docs.google.com/spreadsheets/d/e/2PACX-1vQ5gLUsqSFvnfBfS-eIZmV1CALo9rSC1DcXo6sCcThIILlxLVuIYLbIeHpfmcCAMaFHsrWLHPTGlKjQ/pubhtml?gid=0&single=true)

# Índice

1. [Build](#build)
2. [Deploy en cloud con Azure](#deploy-en-cloud-con-azure)
3. [API Endpoints](#api-endpoints)
   - [Pedidos](#pedidos)
   - [Ítems](#items)
   - [Usuarios](#usuarios)
4. [Diagrama de objetos](#dominio)
5. [Wireframes](#wireframes)
6. [Bot de Telegram](#bot-de-telegram)
   - [Tutorial](#creating-a-new-bot)
   - [Documentación](#telegram-bots)
   - [Modificación de application.properties](#applicationproperties)
   - [Comandos](#algunos-de-los-comandos-disponibles-que-ofrece-nuestro-bot-de-pedidos)

## Build

```
luego de agregar 2 lineas en el archivo telegram-bot.src.main.resources->application.properties:
   bot.token = TU-BOT-TOKEN
   bot.username = TU-BOT-USERNAME
   
docker compose up
```

## Deploy en cloud con Azure

- [Tutorial: Deploy a multi-container group using Docker Compose](https://learn.microsoft.com/en-us/azure/container-instances/tutorial-docker-compose)
- [Link a la app](http://20.242.233.113:80)

## API Endpoints

Tanto los pedidos como los ítems necesitan autorización.

### Pedidos

Crear un pedido

```http request
POST /orders
```

Ver un pedido

```http request
GET /orders/{id}
```

Editar un pedido

```http request
PATCH /orders/{id}
```
Según los campos no nulos, se puede editar la descripción 
(con chequeo de versionado), abrir o cerrar 
o compartir el pedido.

```json
{
  "description": "string",
  "closed": false,
  "user": {}
}
```

Eliminar un pedido

```http request
DELETE /orders/{id}
```

### Items

Crear un ítem

```http request
POST /orders/{id}/items
```

Ver un ítem

```http request
GET /items/{id}
```

Ver todos los ítems de un pedido

```http request
GET /orders/{id}/items
```

Actualizar un ítem

```http request
PUT /items/{id}
```

Al igual que con los pedidos, también se hace un chequeo de versionado
para requests simultáneas.

```json
{
  "description": "string",
  "quantity": 1,
  "unit": "string"
}
```

Eliminar un ítem

```http request
DELETE /items/{id}
```

### Usuarios

Existe un solo tipo de usuario, que solamente tiene permisos sobre su
cuenta y sus pedidos.

Crear un usuario / sign up

```http request
POST /users
```

Login

```http request
POST /
```

El string que se pase por el campo username puede ser tanto el nombre de
usuario como el mail que se usó al momento de registrarse.

```json
{
  "username": "string",
  "password": "string"
}
```

Ver usuario autenticado

```http request
GET /user
```

Ver todos los usuarios con quienes se compartió un pedido

```http request
GET /orders/{id}/users
```

Editar usuario autenticado

```http request
PUT /users
```

```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

Eliminar usuario autenticado

```http request
DELETE /users
```

## Dominio
![diagrama de objetos](/diagramas/objetos.jpg)

## Wireframes
Log In:

![wireframe de login](/diagramas/wireframes/iniciar_sesion.jpg)

Sign Up:

![wireframe de signup](/diagramas/wireframes/crear_usuario.jpg)

Mis Pedidos:

![wireframe de pedidos](/diagramas/wireframes/pedidos.jpg)

Pedido Abierto Admin:

![wireframe de o](/diagramas/wireframes/pedido_admin.jpg)

Pedido Abierto Invitado:

![wireframe de login](/diagramas/wireframes/pedido_no_admin.jpg)

Pedido Cerrado Admin:

![wireframe de login](/diagramas/wireframes/pedido_cerrado_admin.jpg)

## Bot de Telegram

Para utilizar el bot de telegram primero debes crear un bot de telegram.

### Creating a new bot

Use the /newbot command to create a new bot. [@BotFather](https://t.me/botfather) will ask you for a name and username, then generate an authentication token for your new bot.

- The **name** of your bot is displayed in contact details and elsewhere.


- The **username** is a short name, used in search, mentions and t.me links. Usernames are 5-32 characters long and not case-sensitive – but may only include Latin characters, numbers, and underscores. Your bot's username must end in 'bot', like 'tetris_bot' or 'TetrisBot'.


- The **token** is a string, like 110201543:AAHdqTcvCH1vGWJxfSeofSAs0K5PALDsaw, which is required to authorize the bot and send requests to the Bot API. Keep your token secure and store it safely, it can be used by anyone to control your bot.

Unlike the bot’s name, the username cannot be changed later – so choose it carefully.
When sending a request to api.telegram.org, remember to prefix the word ‘bot’ to your token.

### Telegram API

También puedes ver cómo crear un bot de telegram en el siguiente link:

[Como crear y obtener el token de tu bot de telegram](https://core.telegram.org/bots/tutorial#obtain-your-bot-token)

### application.properties

Luego debes modificar el archivo:

telegram-bot/src/main/resources/application.properties

```properties
bot.token = BOT_TOKEN
bot.username = BOT_USERNAME
```

Ahora cuando corras el bot de telegram ya podrás escribirle y recibir respuestas de tu bot creado.

### Algunos de los comandos disponibles que ofrece nuestro bot de pedidos:
```
/logIn (Inicia Sesion)

/signUp (Crea una cuenta)

/start (Ver mensaje de bienvenida)

/stats (Ver estadisticas de la aplicacion)

/verComandos (Lista todos los comandos disponibles)

/logOut (Cierra sesion)

/verUsuario (Ver informacion del usuario actual)

/verPedidos (Lista todos los pedidos a los que tienes acceso)

/crearPedido (Crea nuevo pedido)

/sumarItem (Suma N unidades al item accedido)

/restarItem (Resta N unidades al item accedido)

/cambiarDescipcionItem (Cambia la descripcion del item accedido)

/cambiarUnidadItem (Cambia la unidad del item accedido)

/volverAPedido (Vuelve a la vista del pedido del item accedido)

/cambiarNombreAPedido (Cambiar nombre de pedido accedido)

/agregarItemAPedido (Crea nuevo item en el pedido accedido)
```