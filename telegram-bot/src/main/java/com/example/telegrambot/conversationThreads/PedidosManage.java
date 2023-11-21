package com.example.telegrambot.conversationThreads;

import com.example.telegrambot.UserData;
import com.example.telegrambot.UserState;
import com.example.telegrambot.api.ApiOrders;
import com.example.telegrambot.api.ItemsApi;
import com.fasterxml.jackson.databind.JsonNode;

public class PedidosManage {
    public String verPedido(UserData user, String pedidoId){
        JsonNode pedido =  new ApiOrders().getOrderById(pedidoId,user.getToken());

        if(pedido!=null){
            user.setPedidoId(pedidoId);
            String pedidoText = "Pedido: "+pedido.get("description").asText()+".\n";
            JsonNode items = new ItemsApi().getOrderItems(pedidoId,user.getToken());
            int itemsQty = items.size();
            if (itemsQty!=0){
                pedidoText+="\nItems:";
                for(int j = 0;j<itemsQty;j++){
                    JsonNode item = items.get(j);
                    pedidoText += "\n\t• "+item.get("quantity").asText()+" ";

                    String unit = item.get("unit").asText();
                    if(unit!=null){
                        pedidoText += unit+" de ";
                    }

                    pedidoText+= item.get("description").asText();
                    pedidoText+="\n/editarItem_"+item.get("id").asText();
                }
            }else pedidoText+="\nParece que el pedido esta vacio.";


            if (pedido.get("closed").asBoolean()){
                pedidoText+="\n\nPedido cerrado";
                if(pedido.get("owned").asBoolean()){
                    pedidoText+="\n/abrirPedido";
                }
                pedidoText+="\n/verComandos";
                pedidoText+="\n/volverAPedidos";
                return pedidoText;
            }
            pedidoText+="\n\n/compartirPedido";
            pedidoText+="\n/agregarItemAPedido";
            pedidoText+="\n/cambiarNombreAPedido";
            if(pedido.get("owned").asBoolean()){
                pedidoText+="\n/cerrarPedido";
                pedidoText+="\n/borrarPedido";
            }
            pedidoText+="\n/verComandos";
            pedidoText+="\n/volverAPedidos";
            return pedidoText;
        }

        return "No se puede acceder al pedido";
    }
    public String crearPedido(UserData user){
        JsonNode response = new ApiOrders().createOrdersByUserId(user.getToken());
        if(response!=null){
            user.setState(UserState.WAITING_ORDER_NAME);
            user.setPedidoId(response.get("id").asText());
            return "Orden creada con exito, elija un nombre para su orden:";
        }
        return "La orden no pudo ser creada con exito.";
    }
    public String cambiarNombrePedido(UserData user){

        JsonNode pedido =  new ApiOrders().getOrderById(user.getPedidoId(),user.getToken());

        if (pedido!=null){
            user.setState(UserState.WAITING_ORDER_NAME);
            return "Introduce el nuevo nombre del pedido";
        }
        return "No puedes cambiarle el nombre a este pedido";

    }

    public String responseOrderName(UserData user,String message){
        Boolean response = new ApiOrders().patchOrderName(user.getToken(),user.getPedidoId(),message);
        if(response){
            return messageAndOrder(user,"Nombre actualizado correctamente");
        }
        return "El nombre no pudo ser actualizado.";
    }


    public String verPedidos(UserData user){
        user.setPedidoId(null);
        JsonNode jsonNode= new ApiOrders().getOrdersByUserId(user.getToken());
        int orders_qty = jsonNode.size();

        String mmsg;

        if(orders_qty==0){
            mmsg="Parece que no tienes pedidos...\nPrueba creando uno con /crearPedido .";
        }else {
            mmsg = "Mis Pedidos son";
            for (int i =0;i<orders_qty;i++){
                String nombre = jsonNode.get(i).get("description").asText();
                String id = jsonNode.get(i).get("id").asText();
                mmsg += "\n\t•Pedido: " + nombre +"\n\t /verPedido_"+id;
            }
        }
        mmsg+="\n\n/crearPedido\n/verComandos";

        return mmsg;
    }
    public String compartirPedido(UserData user){
        user.setState(UserState.WAITING_SHARE_ID);
        return "Introduzca el username del usuario a compartir";
    }

    public String responseShareId(UserData user,String message){
        Boolean response = new ApiOrders().shareOrderApi(user.getPedidoId(),user.getToken(),message);
        if(response) return messageAndOrder(user,"Se a compartido el pedido con exito");
        return "No se a logrado compartir el pedido";
    }

    public String cerrar(UserData user){
        boolean response =new ApiOrders().closeOrderApi(user.getPedidoId(),user.getToken());
        if(response) return messageAndOrder(user,"Pedido cerrado");
        return "No se pudo cerrar el pedido";
    }
    public String abrir(UserData user){
        boolean response =new ApiOrders().openOrderApi(user.getPedidoId(),user.getToken());
        if(response) return messageAndOrder(user,"Pedido reabierto");
        return "No se pudo abrir el pedido";
    }


    public String messageAndOrder(UserData user,String message){
        return message+"\n\n"+verPedido(user,user.getPedidoId());
    }

    public String borrarPedido(UserData user){
        boolean response = new ApiOrders().deleteOrder(user.getToken(),user.getPedidoId());
        if(response) return "Pedido borrado correctamente\n\n"+verPedidos(user);
        else return "El pedido no pudo ser borrado";
    }

}
