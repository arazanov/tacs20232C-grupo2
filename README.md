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

## Build

```
docker compose up
```

## Deploy en cloud con Azure

- [Tutorial: Deploy a multi-container group using Docker Compose](https://learn.microsoft.com/en-us/azure/container-instances/tutorial-docker-compose)
- [Link a la app](http://20.124.62.23:80)

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
  "description": string,
  "closed": boolean,
  "user": User
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
  "description": string,
  "quantity": int,
  "unit": string
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
  "username": string,
  "password": string
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
  "username": string,
  "email": string,
  "password": string
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
