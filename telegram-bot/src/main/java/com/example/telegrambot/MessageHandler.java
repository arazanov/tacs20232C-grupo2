package com.example.telegrambot;

import com.example.telegrambot.api.ApiOrders;
import com.example.telegrambot.api.UserApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
public class MessageHandler {
    private final HashMap<Long,Long> userSessions = new HashMap<>();
    private final HashMap<Long,UserState> userState = new HashMap<>();
    private final HashMap<Long,HashMap<String,String>> cacheData = new HashMap<>();

    public MessageHandler(){}

    public String handle(long chatId,String message){
        System.out.println("Processing message " + message);

        String[] command = message.split(" ");

        System.out.println("Processing command " + command[0]);

        if(command[0].equals("/login")){
            System.out.println("Login command found.");
            userState.put(chatId,UserState.WAITING_ID);
            return "Por favor introduzca su ID:";
        }
        if(userState.get(chatId)==UserState.WAITING_ID) {
            JsonNode user = new UserApi().getUserById(message);
            if(user==null){
                System.out.println("Couldnt find user.");
                userState.remove(chatId);
                userSessions.remove(chatId);
                cacheData.remove(chatId);
                return "No se pudo realizar el login";
            }
            System.out.println("Found user");
            long userId = Long.valueOf(user.get("id").asInt());
            userSessions.put(chatId,userId);
            userState.put(chatId,UserState.LOGGED_IN);
            return "Inicio de sesion realizado.";
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
        }

        switch (userState.get(chatId)){
            case WAITING_SHARE_ID: return responseShareId(chatId,command);
        }

        return "";
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
            return pedidoText;
        }

        return "No se puede acceder al pedido";
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




    /*

    private final SilentSender silentSender;


    public MessageHandler(SilentSender sender){
        this.silentSender = sender;
    }


    private Long getSession(long chatId){
        long userId = userSessions.get(chatId);
        System.out.println(userId);
        return userId;
        //if(userId == null) sendMessage(chatId,"Primero debes iniciar sesion. /login");
    }
    public void viewOrders(MessageContext ctx){

        sendMessage(ctx.chatId(),"AAAAAAAAA ");

        System.out.println("llama a viewOrders");
        long userId = getSession(ctx.chatId());

        userSessions.get(ctx.chatId());

    }
    public void login(MessageContext ctx){
        sendMessage(ctx.chatId(),"Por favor, introduce tu id de usuario: ");
        userState.put(ctx.chatId(),UserState.WAITING_LOGIN_NAME);
    }

    public void sendMessage(long chatId,String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        silentSender.execute(sendMessage);

    }
    public void processLogin(long chatId,Message message){
        try {
            String userId = message.getText();
            JsonNode user = new UserApi().getUserById(userId);
            if(user.get("id").asText().equals(userId)){
                userSessions.put(chatId,Long.valueOf(userId));
                sendMessage(chatId,"Sesion iniciada correctamente.");
                userState.put(chatId,UserState.NO_STATE);
                return;
            }
            sendMessage(chatId,"La sesion no pudo ser establecida");
        }catch (Exception e){
            sendMessage(chatId,"La sesion no pudo ser establecida");
        }
        userState.put(chatId,UserState.NO_STATE);
    }
    public boolean userIsActive(Long chatId) {
        return userState.containsKey(chatId);
    }
    public void replyToButtons(Update upd){
        Message message = upd.getMessage();
        Long chatId = getChatId(upd);
        switch (userState.get(chatId)){
            case WAITING_LOGIN_NAME -> processLogin(chatId,message);
        }
    }*/
}
