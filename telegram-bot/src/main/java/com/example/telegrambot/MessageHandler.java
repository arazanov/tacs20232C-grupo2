package com.example.telegrambot;

import com.example.telegrambot.api.ApiOrders;
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

        switch (userState.get(chatId)){
            case WAITING_USERNAME_SIGNUP: return waitUsernameSignUp(chatId,command[0]);
            case WAITING_EMAIL_SIGNUP: return waitEmailSignUp(chatId,command[0]);
            case WAITING_PASSWORD_SIGNUP: return waitPasswordSignUp(chatId,command[0]);
            case WAITING_ID: return waitUsername(chatId,command[0]);
            case WAITING_PASSWORD: return waitPassword(chatId,command[0]);
        }


        System.out.println(userSessions.get(chatId));
        if(userSessions.get(chatId)==null) return "Por favor, primero iniciar sesion. /login";


        long userId = userSessions.get(chatId);

        String[] commandsParts = command[0].split("_");

        switch (commandsParts[0]){
            case "/verPedidos": return verPedidos(userId,commandsParts);
            case "/verUsuario": return verUsuario(userId,commandsParts);
            case "/verPedido": return verPedido(userId,commandsParts);
            case "/crearPedido": return crearPedido(userId,commandsParts);
            case "/logout": return logout(chatId,commandsParts);
            case "/compartirPedido": return compartirPedido(chatId,commandsParts);
            case "/agregarItemAPedido": return agregarItemAPedido(chatId,commandsParts);
            case "/cerrar": return cerrar(userId,commandsParts);

        }

        switch (userState.get(chatId)){
            case WAITING_SHARE_ID: return responseShareId(chatId,command);
            case WAITING_ITEM_DESCRIPTION: return responseItemDescription(chatId,message);
            case WAITING_ITEM_QUANTITY: return responseItemQuantity(chatId,command[0]);
        }

        return "Por favor ingrese uno de los siguientes comandos: " +
                "\n/verPedidos" +
                "\n/verUsuario" +
                "\n/crearPedido" +
                "\n/logout";
    }

    private String login(long chatId){
        userTokens.remove(chatId);
        System.out.println("Login command found.");
        userState.put(chatId,UserState.WAITING_ID);
        return "Por favor introduzca su username:";
    }


    private String signUp(long chatId){
        userTokens.remove(chatId);
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
        cacheData.remove(chatId);
        userState.remove(chatId);
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
        cacheData.remove(chatId);
        userState.remove(chatId);
        String loginToken = new UserApi().userLogin(username,message);
        if(loginToken!=null){
            userTokens.put(chatId,loginToken);
            return "Login exitoso";
        }
        return "El usuario o contraseña es incorrecto.";
    }



    private String cerrar(long userId,String[] commands){
        try {
            JsonNode order = new ApiOrders().getOrderById(commands[1]);
            if(isAdmin(userId,order)){
                boolean response =new ApiOrders().closeOrderApi(commands[1],String.valueOf(userId),"{\"closed\":true}");
                if(response) return "Pedido cerrado";
                return "No se pudo cerrar el pedido";
            }
            return "No se pudo cerrar el pedido";
        }catch (Exception e){
            return "No se pudo cerrar el pedido";
        }
    }

    private String responseItemQuantity(long chatId,String message){
        int qty;
        try {
             qty = Integer.valueOf(message);
        } catch (Exception e){
            userState.remove(chatId);
            cacheData.remove(chatId);
            return "Esta no es una cantidad posible";
        }
        String pedidoId = cacheData.get(chatId).get("pedidoId");
        String description = cacheData.get(chatId).get("description");

        String jsonBody = "{\"description\":\""+description+"\", \"quantity\":"+String.valueOf(qty)+"}";

        boolean response = new ApiOrders().addItemApi(pedidoId, String.valueOf(userSessions.get(chatId)),jsonBody);

        userState.remove(chatId);
        cacheData.remove(chatId);
        if(response) return "Item agregado correctamente";
        return "El item no pudo ser agregado";
    }


    private String responseItemDescription(long chatId,String message){
        cacheData.get(chatId).put("description",message);
        userState.put(chatId,UserState.WAITING_ITEM_QUANTITY);
        return "Por favor escriba cuantos items de estos desea";
    }

    private String agregarItemAPedido(long chatId,String[] message){
        long userId = userSessions.get(chatId);
        String pedidoId;
        try {
            pedidoId = message[1];
        } catch (Exception e ){
            return "La sintaxis de agregarItemAPedido es: /agregarItemAPedido_PedidoID";
        }
        if(testAcces(userId,pedidoId)==null){
            return "No puedes agregar items a este pedido.";
        }
        cacheData.put(chatId,new HashMap<>());
        cacheData.get(chatId).put("pedidoId",pedidoId);
        userState.put(chatId,UserState.WAITING_ITEM_DESCRIPTION);
        return "Por favor, escriba la descripcion del item.";
    }

    private String logout(long chatId,String[] message){
        userState.remove(chatId);
        userSessions.remove(chatId);
        cacheData.remove(chatId);
        return "Logout realizado.";
    }
    private String responseShareId(long chatId,String[] message){
        userState.remove(chatId);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = new UserApi().getUserById(String.valueOf(message[0]));

        String pedidoId = cacheData.get(chatId).get("pedidoId");
        cacheData.remove(chatId);
        if(jsonNode!=null){
            String requestBody="";
            try {
                requestBody = objectMapper.writeValueAsString(jsonNode);
            }catch (JsonProcessingException e ){
                return "No se a logrado compartir el pedido";
            }
            System.out.println(requestBody);
            System.out.println(pedidoId);
            boolean response = new ApiOrders().mySharePatch(pedidoId,requestBody);
            if(response) return "Se a compartido el pedido con exito";
            return "No se a logrado compartir el pedido";
        }


        return "La orden no pudo ser compartida con ese usuario.";

    }

    private String verUsuario(long userId,String[] message){
        JsonNode jsonNode = new UserApi().getUserById(String.valueOf(userId));

        String mssg="User id: "+jsonNode.get("id").asText()+ "\n User name: "+jsonNode.get("username").asText();

        return mssg;
    }

    private String verPedidos(long userId,String[] message){
        JsonNode jsonNode= new ApiOrders().getOrdersByUserId(String.valueOf(userId));
        int orders_qty = jsonNode.size();
        String mmsg = "Mis Pedidos son";
        for (int i =0;i<orders_qty;i++){
            String id = jsonNode.get(i).get("id").asText();
            mmsg += "\n Orden id: " + id +" /verPedido_"+id;
        }

        return mmsg;
    }
    private String compartirPedido(long chatId,String[] message){
        long userId = userSessions.get(chatId);
        String pedidoId;
        try {
            pedidoId = message[1];
        } catch (Exception e ){
            return "La sintaxis de compartirPedido es: /compartirPedido_PedidoID";
        }
        JsonNode pedido = testAcces(userId,pedidoId);
        if(pedido!=null){
            userState.put(chatId,UserState.WAITING_SHARE_ID);
            cacheData.put(chatId,new HashMap<>());
            cacheData.get(chatId).put("pedidoId",pedidoId);
            return "Introduzca el id del usuario a compartir";
        }
        return "El pedido no puede ser compartido";
    }

    private String verPedido(long userId,String[] message){
        System.out.println("/verPedido command processing");
        String pedidoId;
        try {
            pedidoId = message[1];
        } catch (Exception e ){
            return "La sintaxis de verPedido es: /verPedido_PedidoID";
        }
        JsonNode pedido = testAcces(userId,pedidoId);

        if(pedido!=null){
            String pedidoText = "Pedido id "+pedidoId+":";

            JsonNode items = pedido.get("items");
            for(int j = 0;j<items.size();j++){
                JsonNode item = items.get(j);
                pedidoText += "\n"+item.get("quantity").asText()+" "+item.get("description").asText();
            }
            if (pedido.get("closed").asBoolean()){
                pedidoText+="\nPedido cerrado";
                return pedidoText;
            }
            pedidoText+="\n/compartirPedido_"+pedidoId;
            pedidoText+="\n/agregarItemAPedido_"+pedidoId;
            if(isAdmin(userId,pedido)){
                pedidoText+="\n/cerrar_"+pedidoId;
            }
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

    private String crearPedido(long userId,String[] message){
        boolean response = new ApiOrders().createOrdersByUserId(String.valueOf(userId));
        if(response){
            return "Orden creada con exito.";
        }
        return "La orden no pudo ser creada con exito.";
    }
}
