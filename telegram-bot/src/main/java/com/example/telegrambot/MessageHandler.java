package com.example.telegrambot;

import com.example.telegrambot.api.ApiOrders;
import com.example.telegrambot.api.ItemsApi;
import com.example.telegrambot.api.UserApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
public class MessageHandler {
    private final HashMap<Long,Long> userSessions = new HashMap<>();
    private final HashMap<Long,String> userTokens = new HashMap<>();
    private final HashMap<Long,UserState> userState = new HashMap<>();
    private final HashMap<Long,HashMap<String,String>> cacheData = new HashMap<>();

    public MessageHandler(){}

    public String handle(long chatId,String message){
        System.out.println("Processing message " + message);

        String[] command = message.split(" ");

        System.out.println("Processing command " + command[0]);



        switch (command[0]){
            case "/signUp": return signUp(chatId);
            case "/login": return login(chatId);
        }

        if(userState.get(chatId)!=null){
            switch (userState.get(chatId)){
                case WAITING_USERNAME_SIGNUP: return waitUsernameSignUp(chatId,command[0]);
                case WAITING_EMAIL_SIGNUP: return waitEmailSignUp(chatId,command[0]);
                case WAITING_PASSWORD_SIGNUP: return waitPasswordSignUp(chatId,command[0]);
                case WAITING_ID: return waitUsername(chatId,command[0]);
                case WAITING_PASSWORD: return waitPassword(chatId,command[0]);
            }
        }

        System.out.println(1);

        if(userTokens.get(chatId)==null) return "Por favor haz /login , o si no tienes cuenta /signUp";
        System.out.println(2);
        String userToken=userTokens.get(chatId);
        System.out.println(3);
        String[] commandsParts = command[0].split("_");
        System.out.println(4);
        switch (commandsParts[0]){
            case "/verPedidos": return verPedidos(userToken,commandsParts,chatId);
            case "/verUsuario": return verUsuario(userToken,commandsParts,chatId);
            case "/verPedido": return verPedido(userToken,commandsParts,chatId);
            case "/crearPedido": return crearPedido(userToken,commandsParts,chatId);
            case "/logout": return logout(chatId,commandsParts);
            case "/compartirPedido": return compartirPedido(chatId,commandsParts);//TODO que funcione el compartir
            case "/agregarItemAPedido": return agregarItemAPedido(userToken,commandsParts,chatId);//TODO cantidad y desc iniciales funcionar
            case "/cerrar": return cerrar(userToken,commandsParts,chatId);
            //case "/verItem": return verItem(userToken,commandsParts,chatId);
            //case "/sumarItem": return verItem(userToken,commandsParts,chatId);
            //case "/restarItem": return verItem(userToken,commandsParts,chatId);
            //case "/cambiarNombrePedido": return verItem(userToken,commandsParts,chatId);
            //case "/cambiarDescipcionItem": return verItem(userToken,commandsParts,chatId);
        }
        System.out.println(5);
        if(userState.get(chatId)!=null) {
            switch (userState.get(chatId)) {
                case WAITING_ORDER_NAME: return responseOrderName(userToken,message,chatId);
                case WAITING_ITEM_QUANTITY:return responseItemQuantity(userToken,chatId,message);
                case WAITING_ITEM_DESCRIPTION:return responseItemDescription(userToken,chatId,message);
                case WAITING_SHARE_ID:
                    return responseShareId(userToken, message,chatId);
            }
        }
        System.out.println(6);
        return "Por favor ingrese uno de los siguientes comandos: " +
                "\n/verPedidos" +
                "\n/verUsuario" +
                "\n/crearPedido" +
                "\n/logout";
    }

    private String login(long chatId){
        userTokens.remove(chatId);
        reset(chatId);
        System.out.println("Login command found.");
        userState.put(chatId,UserState.WAITING_ID);
        return "Por favor introduzca su username:";
    }


    private String signUp(long chatId){
        userTokens.remove(chatId);
        reset(chatId);
        System.out.println("SignUp command found.");
        userState.put(chatId,UserState.WAITING_USERNAME_SIGNUP);
        return "Por favor introduzca un username:";
    }




    private String waitUsernameSignUp(long chatId,String message){
        cacheData.put(chatId, new HashMap<>());
        cacheData.get(chatId).put("username", message);
        userState.put(chatId, UserState.WAITING_EMAIL_SIGNUP);
        return "Por favor introduzca un email";
    }

    private String waitEmailSignUp(long chatId,String message){
        cacheData.get(chatId).put("email", message);
        userState.put(chatId, UserState.WAITING_PASSWORD_SIGNUP);
        return "Por favor introduzca una contraseña";
    }

    private String waitPasswordSignUp(long chatId,String message){
        String username = cacheData.get(chatId).get("username");
        String email = cacheData.get(chatId).get("email");
        reset(chatId);
        String loginToken = new UserApi().userSignUp(username,email,message);
        if(loginToken!=null){
            return "SignUp exitoso, ahora puedes iniciar sesion.";
        }
        return "El signUp no se pudo efectuar de forma correcta.";
    }

    private String waitUsername(long chatId,String message){
        cacheData.put(chatId, new HashMap<>());
        cacheData.get(chatId).put("username", message);
        userState.put(chatId, UserState.WAITING_PASSWORD);
        return "Por favor introduzca su contraseña";
    }

    private String waitPassword(long chatId,String message){
        String username = cacheData.get(chatId).get("username");
        reset(chatId);
        String loginToken = new UserApi().userLogin(username,message);
        if(loginToken!=null){
            userTokens.put(chatId,loginToken);
            return "Login exitoso";
        }
        return "El usuario o contraseña es incorrecto.";
    }



    private String cerrar(String token,String[] commands,long chatId){
        reset(chatId);
        boolean response =new ApiOrders().closeOrderApi(commands[1],token);
        if(response) return "Pedido cerrado";
        return "No se pudo cerrar el pedido";
    }

    private String responseItemQuantity(String userToken, long chatId,String message){
        int qty;
        try {
             qty = Integer.valueOf(message);
        } catch (Exception e){
            reset(chatId);
            return "Esta no es una cantidad posible";
        }

        String itemID = cacheData.get(chatId).get("itemId");
        reset(chatId);
        Boolean response = new ItemsApi().patchItemQuantity(userToken,itemID,qty);
        if(response){
            return "Cantidad actualizada correctamente.";
        }
        return "La cantidad no fue modificada.";
    }


    private String responseItemDescription(String userToken, long chatId,String message){

        String itemId = cacheData.get(chatId).get("itemID");
        Boolean response = new ItemsApi().patchItemDescription(userToken,itemId,message);
        if(response){
            userState.put(chatId,UserState.WAITING_ITEM_QUANTITY);
            return "Descripcion actualizada correctamente, por favor escriba cuantos items de estos desea:";
        }
        reset(chatId);
        return "La descripcion no pudo ser actualizada.";

    }

    private String agregarItemAPedido(String token,String[] message,long chatId){
        reset(chatId);
        String pedidoId;
        try {
            pedidoId = message[1];
        } catch (Exception e ){
            return "La sintaxis de agregarItemAPedido es: /agregarItemAPedido_PedidoID";
        }
        JsonNode respuesta= new ItemsApi().addItemApi(pedidoId,token);

        if(respuesta!=null){
            userState.put(chatId,UserState.WAITING_ITEM_DESCRIPTION);
            cacheData.put(chatId, new HashMap<>());
            cacheData.get(chatId).put("itemID",respuesta.get("id").asText());
            return "Se a creado el item de forma correcta, elija una descripcion para el item";
        }
        return "No puedes agregar items a este pedido.";
    }


    private String logout(long chatId,String[] message){
        userSessions.remove(chatId);
        reset(chatId);
        return "Logout realizado.";
    }
    private String responseShareId(String token,String message,long chatId){

        String pedidoId = cacheData.get(chatId).get("pedidoId");
        Boolean response = new ApiOrders().shareOrderApi(pedidoId,token,message);
        reset(chatId);
        if(response) return "Se a compartido el pedido con exito";
        return "No se a logrado compartir el pedido";
    }

    private String verUsuario(String token,String[] message,long chatId){
        reset(chatId);
        JsonNode jsonNode = new UserApi().getUserById(token);

        String mssg="User id: "+jsonNode.get("id").asText()+ "\n User name: "+jsonNode.get("username").asText();

        return mssg;
    }

    private String verPedidos(String token,String[] message,long chatId){
        reset(chatId);
        JsonNode jsonNode= new ApiOrders().getOrdersByUserId(token);
        int orders_qty = jsonNode.size();
        String mmsg = "Mis Pedidos son";
        for (int i =0;i<orders_qty;i++){
            String id = jsonNode.get(i).get("id").asText();
            mmsg += "\n Pedido: " + id +" /verPedido_"+id;
        }

        return mmsg;
    }
    private String compartirPedido(long chatId,String[] message){
        reset(chatId);
        String pedidoId;
        try {
            pedidoId = message[1];
        } catch (Exception e ){
            return "La sintaxis de compartirPedido es: /compartirPedido_PedidoID";
        }
        userState.put(chatId,UserState.WAITING_SHARE_ID);
        cacheData.put(chatId,new HashMap<>());
        cacheData.get(chatId).put("pedidoId",pedidoId);
        return "Introduzca el username del usuario a compartir";
    }

    private String verPedido(String token,String[] message,long chatId){
        reset(chatId);
        System.out.println("/verPedido command processing");
        String pedidoId;
        try {
            pedidoId = message[1];
        } catch (Exception e ){
            return "La sintaxis de verPedido es: /verPedido_PedidoID";
        }
        System.out.println(1);
        JsonNode pedido =  new ApiOrders().getOrderById(pedidoId,token);
        System.out.println(1);
        if(pedido!=null){
            System.out.println(1);
            String pedidoText = "Pedido:"+pedido.get("description").asText()+":";
            System.out.println(1);
            JsonNode items = new ItemsApi().getOrderItems(pedidoId,token);
            System.out.println(1);
            for(int j = 0;j<items.size();j++){
                System.out.println(1);
                JsonNode item = items.get(j);
                pedidoText += "\n"+item.get("quantity").asText()+" "+item.get("description").asText()+"/verItem_"+item.get("id").asText();
            }
            System.out.println(1);
            if (pedido.get("closed").asBoolean()){
                System.out.println(1);
                pedidoText+="\nPedido cerrado";
                return pedidoText;
            }
            System.out.println(1);
            pedidoText+="\n/compartirPedido_"+pedidoId;
            pedidoText+="\n/agregarItemAPedido_"+pedidoId;
            System.out.println(pedido.get("owned").asBoolean());
            if(pedido.get("owned").asBoolean()){
                System.out.println(1);
                pedidoText+="\n/cerrar_"+pedidoId;
            }
            System.out.println(1);
            return pedidoText;
        }

        return "No se puede acceder al pedido";
    }

    private boolean isAdmin(long userId,JsonNode pedido){
        return Long.valueOf(pedido.get("user").get("id").asInt()) == userId;
    }

    private JsonNode testAcces(long userId,String pedidoId){
        JsonNode jsonNode= new ApiOrders().getOrdersByUserId(String.valueOf(userId));
        int orders_qty = jsonNode.size();
        for (int i =0;i<orders_qty;i++){
            JsonNode pedido = jsonNode.get(i);
            String id = pedido.get("id").asText();
            if (id.equals(pedidoId)){
                return pedido;
            }
        }
        return null;
    }

    private String crearPedido(String userToken,String[] message,long chatId){
        reset(chatId);
        JsonNode response = new ApiOrders().createOrdersByUserId(userToken);
        if(response!=null){
            userState.put(chatId,UserState.WAITING_ORDER_NAME);
            cacheData.put(chatId, new HashMap<>());
            cacheData.get(chatId).put("orderID",response.get("id").asText());
            return "Orden creada con exito, elija un nombre para su orden:";
        }
        return "La orden no pudo ser creada con exito.";
    }




    private String responseOrderName(String userToken,String message,long chatId){
        String orderId = cacheData.get(chatId).get("orderID");
        reset(chatId);
        Boolean response = new ApiOrders().patchOrderName(userToken,orderId,message);
        if(response){
            return "Nombre actualizado correctamente";
        }
        return "El nombre no pudo ser actualizado.";
    }



    private void reset(long chatId){
        userState.remove(chatId);
        cacheData.remove(chatId);
    }

}
