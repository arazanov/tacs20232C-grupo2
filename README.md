# tacs20232C-grupo2

https://www.tacs-utn.com.ar/
- [Enunciado](https://docs.google.com/document/d/e/2PACX-1vTyCHMPW35IZ2Y-71h1ctMu-Qnf-_A-nDz5uzkm8hOz1xjK01sK6484KS_FuxTGIypyH1Idu5rYbdW1/pub)
- [Planificación](https://docs.google.com/spreadsheets/d/e/2PACX-1vQ5gLUsqSFvnfBfS-eIZmV1CALo9rSC1DcXo6sCcThIILlxLVuIYLbIeHpfmcCAMaFHsrWLHPTGlKjQ/pubhtml?gid=0&single=true)

## Build
```
docker compose up
```

## User Stories Endpoints
Como usuario quiero crear un pedido.  
**POST /orders/{userId}**  

Como usuario quiero agregar un nuevo item a un pedido ya creado.  
**POST /orders/{orderId}/{userId}**  

Body:
```
{
    "description": "someItem",
    "quantity": integer
}
```

Como usuario quiero poder sumar N elementos (+1 por ejemplo) a un item de un pedido.  
**POST /orders/{orderId}/{userId}**  

Body:
```
{
    "description": "someItem",
    "quantity": integer
}
```

Como usuario quiero poder ver los items y cantidades que hay en un pedido.  
**GET /orders/{orderId}/items**  

Como usuario quiero poder cerrar el pedido. Siempre y cuando haya sido creado por mí.  
**PATCH /orders/{orderId}/{userId}**  

Body:
```
{
    "closed": boolean
}
```

A fines de monitoreo (y marketing) se solicita ver un contador con la pedidos creados y usuarios únicos que interactuaron con la plataforma (crear o modificar un pedido)  
**GET /monitor**

Compartir un pedido  
**PATCH /orders/{orderId}**  

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
