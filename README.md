# tacs20232C-grupo2

## Build
```
docker build .
docker-compose up
```

## User Stories Endpoints
Como usuario quiero crear un pedido.  
**POST /webapp/orders**  

Body:
```
{
    "username": "someUsername"
}
```

Como usuario quiero agregar un nuevo item a un pedido ya creado.  
**POST /webapp/orders/{orderId}/addItems/{userId}**  

Body:
```
{
    "description": "someItem",
    "quantity": number
}
```


Como usuario quiero poder sumar N elementos (+1 por ejemplo) a un item de un pedido.  
**POST /webapp/orders/{orderId}/addItems/{userId}**  

Body:
```
{
    "description": "someItem",
    "quantity": number
}
```


Como usuario quiero poder ver los items y cantidades que hay en un pedido.  
**GET /webapp/orders/{orderId}/items**  

Como usuario quiero poder cerrar el pedido. Siempre y cuando haya sido creado por mí.  
**PATCH /webapp/orders/{orderId}/close/{userId}**  


A fines de monitoreo (y marketing) se solicita ver un contador con la pedidos creados y usuarios únicos que interactuaron con la plataforma (crear o modificar un pedido)  
**GET /webapp/monitor**

Compartir un pedido  
**POST /webapp/orders/{orderId}/share**  

Body:
```
{
    "username": "someUsername"
}
```

## Dominio  
![diagrama de objetos](/diagramas/objetos.jpg)

## Wireframes  
Log In:

![wireframe de login](/wireframes/iniciar_sesion.jpg)

Sign Up:

![wireframe de signup](/wireframes/crear_usuario.jpg)

Mis Pedidos:

![wireframe de pedidos](/wireframes/pedidos.jpg)

Pedido Abierto Admin:

![wireframe de o](/wireframes/pedido_admin.jpg)

Pedido Abierto Invitado:

![wireframe de login](/wireframes/pedido_no_admin.jpg)

Pedido Cerrado Admin:

![wireframe de login](/wireframes/pedido_cerrado_admin.jpg)
