package com.example.telegrambot.conversationThreads;

import com.example.telegrambot.UserData;
import com.example.telegrambot.UserState;
import com.example.telegrambot.api.ApiOrders;
import com.example.telegrambot.api.ItemsApi;
import com.fasterxml.jackson.databind.JsonNode;

public class ItemManage {
    public String responseDescItem(UserData user, String message){
        boolean response = new ItemsApi().putItemDescription(user.getToken(),user.getItemId(),message);
        if (response) return messageAndItem(user,"Descripcion actualizada correctamente.");
        return "La descripcion no pudo ser actualizada.";
    }

    public String responseUnitItem(UserData user,String message){
        boolean response = new ItemsApi().putItemUnit(user.getToken(),user.getItemId(),message);
        if (response) return messageAndItem(user,"Unidad actualizada correctamente.");
        return "La unidad no pudo ser actualizada.";
    }

    public String responseSumItem(UserData user,String message){
        int qty;
        try {
            qty = Integer.valueOf(message);
            if(qty<=0) throw  new Exception();
        } catch (Exception e){
            return "Esta no es una cantidad posible.";
        }
        boolean response = new ItemsApi().putItemModQty(user.getToken(),user.getItemId(),qty);
        if (response) return messageAndItem(user,"Cantidad actualizada correctamente.");
        return "La cantidad no pudo ser actualizada.";
    }

    public String responseDecItem(UserData user,String message){
        int qty;
        try {
            qty = Integer.valueOf(message);
            if(qty<=0) throw  new Exception();
        } catch (Exception e){
            return "Esta no es una cantidad posible.";
        }
        boolean response = new ItemsApi().putItemModQty(user.getToken(),user.getItemId(),-qty);
        if (response) return messageAndItem(user,"Cantidad actualizada correctamente.");
        return "La cantidad no pudo ser actualizada.";
    }

    public String cambiarDescipcionItem(UserData user){
        user.setState(UserState.MOD_ITEM_DESC);
        return "Introduzca el nuevo nombre del item:";
    }

    public String cambiarUnidadItem(UserData user){
        user.setState(UserState.MOD_ITEM_UNIT);
        return "Introduzca la nueva unidad del item:";
    }

    public String sumarItem(UserData user){
        user.setState(UserState.MOD_SUM_ITEM);
        return "Cuantas unidades desea sumar de este item?";
    }

    public String restarItem(UserData user){
        user.setState(UserState.MOD_DEC_ITEM);
        return "Cuantas unidades desea restar de este item?";
    }

    public String responseItemQuantity(UserData user,String message){
        int qty;
        try {
            qty = Integer.valueOf(message);
        } catch (Exception e){

            return "Esta no es una cantidad posible";
        }
        Boolean response = new ItemsApi().putItemQuantity(user.getToken(),user.getItemId(),qty);
        if(response){
            return messageAndItem(user,"Cantidad actualizada correctamente.");
        }
        return "La cantidad no fue modificada.";
    }


    public String responseItemUnit(UserData user,String message){
        Boolean response = new ItemsApi().putItemUnit(user.getToken(),user.getItemId(),message);
        if(response){
            user.setState(UserState.WAITING_ITEM_QUANTITY);
            return "Unidad actualizada correctamente.\nPor favor escriba la cantidad deseada:";
        }
        return "La unidad no pudo ser actualizada.";
    }

    public String responseItemDescription(UserData user,String message){
        Boolean response = new ItemsApi().putItemDescription(user.getToken(),user.getItemId(),message);
        if(response){
            user.setState(UserState.WAITING_ITEM_UNIT);
            return "Descripcion actualizada correctamente.\nPor favor escriba la unidad deseada:";
        }
        return "La descripcion no pudo ser actualizada.";
    }

    public String agregarItemAPedido(UserData user){
        JsonNode respuesta= new ItemsApi().addItemApi(user.getPedidoId(),user.getToken());

        if(respuesta!=null){

            user.setItemId(respuesta.get("id").asText());
            user.setState(UserState.WAITING_ITEM_DESCRIPTION);
            return "Se a creado el item de forma correcta.\nElija una descripcion para el item:";
        }
        return "No puedes agregar items a este pedido.";
    }


    public String messageAndItem(UserData user,String message){
        return message+"\n\n\n"+editarItem(user,user.getItemId());
    }


    public String editarItem(UserData user,String itemId){
        JsonNode item = new ItemsApi().getItemById(itemId,user.getToken());
        if(item!=null){
            user.setItemId(itemId);
            String itemText = "Item: "+item.get("description").asText()+".\n";
            itemText+="/cambiarDescipcionItem\n\n";
            itemText+="Cantidad: "+item.get("quantity").asText()+".\n";
            itemText+="/sumarItem\n";
            itemText+="/restarItem\n\n";
            itemText+="Unidad: "+item.get("unit").asText()+".\n";
            itemText+="/cambiarUnidadItem\n\n\n";
            itemText+="/borrarItem\n";
            itemText+="/verComandos\n";
            itemText+="/volverAPedido";
            return itemText;
        }
        return "El item no pudo ser editado.";
    }

    public String borrarItem(UserData user){
        boolean response = new ItemsApi().deleteItem(user.getToken(),user.getItemId());
        if(response) return "Item borrado correctamente.\n\n"+new PedidosManage().verPedido(user,user.getPedidoId());
        else return "El Item no pudo ser borrado.";
    }

}
