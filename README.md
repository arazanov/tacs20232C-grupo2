# tacs20232C-grupo2

## Build
En tp-tacs
```
docker build .
docker run -p 8080:8080 [image]
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
**POST /webapp/orders/{orderId}/addItems?description=string&quantity=number**  

Body:
```
{
    "username": "someUsername"
}
```


Como usuario quiero poder sumar N elementos (+1 por ejemplo) a un item de un pedido.  
**POST /webapp/orders/{orderId}/addItems?description=string&quantity=number**  

Body:
```
{
    "username": "someUsername"
}
```


Como usuario quiero poder ver los items y cantidades que hay en un pedido.  
**GET /webapp/orders/{orderId}/items**  

Como usuario quiero poder cerrar el pedido. Siempre y cuando haya sido creado por mí.  
**PATCH /webapp/orders/{orderId}/close**  

Body:
```
{
    "username": "someUsername"
}
```

A fines de monitoreo (y marketing) se solicita ver un contador con la pedidos creados y usuarios únicos que interactuaron con la plataforma (crear o modificar un pedido)  
**GET /webapp/monitor**

Compartir un pedido
**POST /webapp/orders/{orderId}/share**  

Body:
```
[
    {
        "username": "user1"
    },
    {
        "username": "user2"
    }
]
```

## Dominio  
![diagrama de objetos](/diagramas/objetos.jpg)