package com.example.telegrambot;

import com.example.telegrambot.api.ApiOrders;
import com.example.telegrambot.api.ItemsApi;
import com.example.telegrambot.api.UserApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;

import java.util.HashMap;
public class MessageHandler {
    private final HashMap<Long,Long> userSessions = new HashMap<>();
    private final HashMap<Long,String> userTokens = new HashMap<>();
    private final HashMap<Long,UserState> userState = new HashMap<>();

    private final HashMap<Long,UserData> userDatas = new HashMap<>();
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
        UserData userData = userDatas.get(chatId);

        if(userData==null) return "Por favor haz /login , o si no tienes cuenta /signUp (2)";

        UserState actualState = userData.getState();

        userData.setState(UserState.LOGOUT);
        switch (actualState){
            case WAITING_USERNAME_SIGNUP: return waitUsernameSignUp(userData,command[0]);
            case WAITING_EMAIL_SIGNUP: return waitEmailSignUp(userData,command[0]);
            case WAITING_PASSWORD_SIGNUP: return waitPasswordSignUp(userData,command[0]);
            case WAITING_ID: return waitUsername(userData,command[0]);
            case WAITING_PASSWORD: return waitPassword(userData,command[0]);
        }

        if(!actualState.isLogin(actualState)) return "Por favor haz /login , o si no tienes cuenta /signUp (1)";

        String[] commandsParts = command[0].split("_");

        userData.setState(UserState.LOGIN);
        switch (commandsParts[0]){
            case "/verPedidos": return verPedidos(userData,commandsParts);
            case "/verUsuario": return verUsuario(userData,commandsParts);
            case "/verPedido": return verPedido(userData,commandsParts);
            case "/crearPedido": return crearPedido(userData,commandsParts);
            case "/logout": return logout(userData,commandsParts);
            case "/compartirPedido": return compartirPedido(userData,commandsParts);//TODO que funcione el compartir
            case "/agregarItemAPedido": return agregarItemAPedido(userData,commandsParts);//TODO cantidad y desc iniciales funcionar
            case "/cerrar": return cerrar(userData,commandsParts);
            case "/cambiarNombreAPedido": return cambiarNombrePedido(userData,commandsParts);
            case "/editarItem": return editarItem(userData,commandsParts);
            //case "/sumarItem": return verItem(userData,commandsParts);
            //case "/restarItem": return verItem(userData,commandsParts);
            //case "/cambiarDescipcionItem": return verItem(userData,commandsParts);
            //case "/cambiarUnidadItem": return verItem(userData,commandsParts);
        }
        System.out.println(5);
        switch (actualState) {
            case WAITING_ORDER_NAME: return responseOrderName(userData,message);
            case WAITING_ITEM_UNIT: return responseItemUnit(userData,message);
            case WAITING_ITEM_QUANTITY:return responseItemQuantity(userData,message);
            case WAITING_ITEM_DESCRIPTION:return responseItemDescription(userData,message);
            case WAITING_SHARE_ID: return responseShareId(userData,message);
            }

        System.out.println(6);
        return "Por favor ingrese uno de los siguientes comandos: " +
                "\n/verPedidos" +
                "\n/verUsuario" +
                "\n/crearPedido" +
                "\n/logout";
    }

    private String login(long chatId){
        //userTokens.remove(chatId);
        //reset(chatId);
        System.out.println("Login command found.");
        //userState.put(chatId,UserState.WAITING_ID);
        UserData userData = new UserData(chatId);
        userData.setState(UserState.WAITING_ID);
        userDatas.put(chatId,userData);
        return "Por favor introduzca su username:";
    }


    private String signUp(long chatId){
        //userTokens.remove(chatId);
        //reset(chatId);
        System.out.println("SignUp command found.");
        UserData userData = new UserData(chatId);
        userData.setState(UserState.WAITING_USERNAME_SIGNUP);
        userDatas.put(chatId,userData);
        //userState.put(userDatas,new UserData(chatId));
        return "Por favor introduzca un username:";
    }




    private String waitUsernameSignUp(UserData user,String message){
        /*
        cacheData.put(chatId, new HashMap<>());
        cacheData.get(chatId).put("username", message);
        userState.put(chatId, UserState.WAITING_EMAIL_SIGNUP);

         */
        user.setUsername(message);
        user.setState(UserState.WAITING_EMAIL_SIGNUP);
        return "Por favor introduzca un email";
    }

    private String waitEmailSignUp(UserData user,String message){
        //cacheData.get(chatId).put("email", message);
        //userState.put(chatId, UserState.WAITING_PASSWORD_SIGNUP);
        user.setMail(message);
        user.setState(UserState.WAITING_PASSWORD_SIGNUP);
        return "Por favor introduzca una contraseña";
    }

    private String waitPasswordSignUp(UserData user,String message){
        //String username = cacheData.get(chatId).get("username");
        //String email = cacheData.get(chatId).get("email");
        //reset(chatId);
        String loginToken = new UserApi().userSignUp(user.getUsername(),user.getMail(),message);
        user.setState(UserState.LOGOUT);
        if(loginToken!=null){
            return "SignUp exitoso, ahora puedes iniciar sesion.";
        }
        return "El signUp no se pudo efectuar de forma correcta.";
    }

    private String waitUsername(UserData user,String message){
        //cacheData.put(chatId, new HashMap<>());
        //cacheData.get(chatId).put("username", message);
        //userState.put(chatId, UserState.WAITING_PASSWORD);
        user.setUsername(message);
        user.setState(UserState.WAITING_PASSWORD);
        return "Por favor introduzca su contraseña";
    }

    private String waitPassword(UserData user,String message){
        //String username = cacheData.get(chatId).get("username");
        //reset(chatId);
        String loginToken = new UserApi().userLogin(user.getUsername(),message);
        if((loginToken!=null) && (loginToken!="") && (loginToken!=" ")){
            user.setToken(loginToken);
            user.setState(UserState.LOGIN);
            return "Login exitoso";
        }
        user.setState(UserState.LOGOUT);
        return "El usuario o contraseña es incorrecto.";
    }



    private String cerrar(UserData user,String[] message){
        //reset(chatId);
        boolean response =new ApiOrders().closeOrderApi(user.getPedidoId(),user.getToken());
        if(response) return "Pedido cerrado";
        return "No se pudo cerrar el pedido";
    }

    private String responseItemQuantity(UserData user,String message){
        //String itemID = cacheData.get(chatId).get("itemId");
        //reset(chatId);
        int qty;
        try {
             qty = Integer.valueOf(message);
        } catch (Exception e){

            return "Esta no es una cantidad posible";
        }
        Boolean response = new ItemsApi().putItemQuantity(user.getToken(),user.getItemId(),qty);
        if(response){
            return "Cantidad actualizada correctamente.";
        }
        return "La cantidad no fue modificada.";
    }


    private String responseItemUnit(UserData user,String message){
        //String itemId = cacheData.get(chatId).get("itemID");
        Boolean response = new ItemsApi().putItemUnit(user.getToken(),user.getItemId(),message);
        if(response){
            //userState.put(chatId,UserState.WAITING_ITEM_QUANTITY);
            user.setState(UserState.WAITING_ITEM_QUANTITY);
            return "Unidad actualizada correctamente.\nPor favor escriba la cantidad deseada:";
        }
        return "La unidad no pudo ser actualizada.";
    }

    private String responseItemDescription(UserData user,String message){
        //String itemId = cacheData.get(chatId).get("itemID");
        Boolean response = new ItemsApi().putItemDescription(user.getToken(),user.getItemId(),message);
        if(response){
            //userState.put(chatId,UserState.WAITING_ITEM_UNIT);
            user.setState(UserState.WAITING_ITEM_UNIT);
            return "Descripcion actualizada correctamente.\nPor favor escriba la unidad deseada:";
        }
        //reset(chatId);
        return "La descripcion no pudo ser actualizada.";
    }

    private String agregarItemAPedido(UserData user,String[] message){
        /*reset(chatId);
        String pedidoId;
        try {
            pedidoId = message[1];
        } catch (Exception e ){
            return "La sintaxis de agregarItemAPedido es: /agregarItemAPedido_PedidoID";
        }*/
        JsonNode respuesta= new ItemsApi().addItemApi(user.getPedidoId(),user.getToken());

        if(respuesta!=null){

            user.setItemId(respuesta.get("id").asText());
            user.setState(UserState.WAITING_ITEM_DESCRIPTION);
            //userState.put(chatId,UserState.WAITING_ITEM_DESCRIPTION);
            //cacheData.put(chatId, new HashMap<>());
            //cacheData.get(chatId).put("itemID",respuesta.get("id").asText());
            return "Se a creado el item de forma correcta.\nElija una descripcion para el item:";
        }
        return "No puedes agregar items a este pedido.";
    }


    private String logout(UserData user,String[] message){
        //userSessions.remove(chatId);
        //reset(chatId);
        userDatas.remove(user.getChatId());
        return "Logout realizado.";
    }
    private String responseShareId(UserData user,String message){
        Boolean response = new ApiOrders().shareOrderApi(user.getPedidoId(),user.getToken(),message);
        if(response) return "Se a compartido el pedido con exito";
        return "No se a logrado compartir el pedido";
    }

    private String verUsuario(UserData user,String[] message){
        //reset(chatId);
        JsonNode jsonNode = new UserApi().getUserById(user.getToken());

        String mssg="User id: "+jsonNode.get("id").asText()+ "\n User name: "+jsonNode.get("username").asText();

        return mssg;
    }

    private String verPedidos(UserData user,String[] message){
        //reset(chatId);
        JsonNode jsonNode= new ApiOrders().getOrdersByUserId(user.getToken());
        int orders_qty = jsonNode.size();
        String mmsg = "Mis Pedidos son";
        for (int i =0;i<orders_qty;i++){
            String nombre = jsonNode.get(i).get("description").asText();
            String id = jsonNode.get(i).get("id").asText();
            mmsg += "\n Pedido: " + nombre +"\n/verPedido_"+id;
        }

        return mmsg;
    }
    private String compartirPedido(UserData user,String[] message){
        //reset(chatId);
        /*String pedidoId;
        try {
            pedidoId = message[1];
        } catch (Exception e ){
            return "La sintaxis de compartirPedido es: /compartirPedido_PedidoID";
        }
        userState.put(chatId,UserState.WAITING_SHARE_ID);
        cacheData.put(chatId,new HashMap<>());
        cacheData.get(chatId).put("pedidoId",pedidoId);
        */
        user.setState(UserState.WAITING_SHARE_ID);
        return "Introduzca el username del usuario a compartir";
    }

    private String editarItem(UserData user,String[] message){
        //reset(chatId);
        String itemId;
        try {
            itemId = message[1];
        } catch (Exception e ){
            return "La sintaxis de editarItem es: /editarItem_ItemID";
        }
        JsonNode item = new ItemsApi().getItemById(itemId,user.getToken());
        if(item!=null){
            user.setItemId(itemId);
            String itemText = "Item: "+item.get("description").asText()+".\n";
            itemText+="/cambiarDescipcionItem\n\n";
            itemText+="Cantidad: "+item.get("quantity").asText()+".\n";
            itemText+="/sumarItem\n";
            itemText+="/restarItem\n\n";
            itemText+="Unidad: "+item.get("unit").asText()+".\n";
            itemText+="/cambiarUnidadItem\n\n";
            return itemText;
        }
        return "El item no pudo ser editado.";
    }


    private String verPedido(UserData user,String[] message){
        //reset(chatId);
        System.out.println("/verPedido command processing");
        String pedidoId;
        try {
            pedidoId = message[1];
        } catch (Exception e ){
            return "La sintaxis de verPedido es: /verPedido_PedidoID";
        }
        System.out.println(1);
        JsonNode pedido =  new ApiOrders().getOrderById(pedidoId,user.getToken());
        System.out.println(1);
        if(pedido!=null){
            user.setPedidoId(pedidoId);
            System.out.println(1);
            String pedidoText = "Pedido: "+pedido.get("description").asText()+".";
            System.out.println(1);
            JsonNode items = new ItemsApi().getOrderItems(pedidoId,user.getToken());
            System.out.println(1);
            for(int j = 0;j<items.size();j++){
                System.out.println(1);
                JsonNode item = items.get(j);
                pedidoText += "\n"+item.get("quantity").asText()+" "+item.get("unit").asText()+" de "+item.get("description").asText()+"\n/editarItem_"+item.get("id").asText();
            }
            System.out.println(1);
            if (pedido.get("closed").asBoolean()){
                System.out.println(1);
                pedidoText+="\nPedido cerrado";
                return pedidoText;
            }
            System.out.println(1);
            pedidoText+="\n";
            pedidoText+="\n/compartirPedido";
            pedidoText+="\n/agregarItemAPedido";
            pedidoText+="\n/cambiarNombreAPedido";
            System.out.println(pedido.get("owned").asBoolean());
            if(pedido.get("owned").asBoolean()){
                System.out.println(1);
                pedidoText+="\n/cerrar";
            }
            System.out.println(1);
            return pedidoText;
        }

        return "No se puede acceder al pedido";
    }

    private String crearPedido(UserData user,String[] message){
        //reset(chatId);
        JsonNode response = new ApiOrders().createOrdersByUserId(user.getToken());
        if(response!=null){
            //userState.put(chatId,UserState.WAITING_ORDER_NAME);
            //cacheData.put(chatId, new HashMap<>());
            //cacheData.get(chatId).put("orderID",response.get("id").asText());
            user.setState(UserState.WAITING_ORDER_NAME);
            user.setPedidoId(response.get("id").asText());
            return "Orden creada con exito, elija un nombre para su orden:";
        }
        return "La orden no pudo ser creada con exito.";
    }


    private String cambiarNombrePedido(UserData user,String[] message){
        /*reset(chatId);
        String pedidoId;
        try {
            pedidoId = message[1];
        } catch (Exception e ){
            return "La sintaxis de cambiarNombrePedido es: /cambiarNombrePedido_PedidoID";
        }

         */
        JsonNode pedido =  new ApiOrders().getOrderById(user.getPedidoId(),user.getToken());

        if (pedido!=null){
            //userState.put(chatId,UserState.WAITING_ORDER_NAME);
            //cacheData.put(chatId, new HashMap<>());
            //cacheData.get(chatId).put("orderID",pedidoId);
            user.setState(UserState.WAITING_ORDER_NAME);
            return "Introduce el nuevo nombre del pedido";
        }
        return "No puedes cambiarle el nombre a este pedido";

    }



    private String responseOrderName(UserData user,String message){
        //String orderId = cacheData.get(chatId).get("orderID");
        //reset(chatId);
        Boolean response = new ApiOrders().patchOrderName(user.getToken(),user.getPedidoId(),message);
        if(response){
            return "Nombre actualizado correctamente";
        }
        return "El nombre no pudo ser actualizado.";
    }

}
