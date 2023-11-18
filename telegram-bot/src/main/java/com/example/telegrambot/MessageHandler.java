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


        UserData userData = getOrCreate(chatId);



        String[] commandsParts = command[0].split("_");

        UserState actualState = userData.getState();

        Boolean isCommand = message.startsWith("/");

        if(!userData.isLogin()){
            userData.setState(UserState.LOGOUT);

            if(isCommand){
                switch (command[0]){
                    case "/signUp": return signUp(userData);
                    case "/login": return login(userData);
                    default: return "Comando no reconocido.\nPuedes intentar con alguno de estos comandos:\n/login\n/signUp";
                }
            }
            switch (actualState){
                case WAITING_USERNAME_SIGNUP: return waitUsernameSignUp(userData,message);
                case WAITING_EMAIL_SIGNUP: return waitEmailSignUp(userData,message);
                case WAITING_PASSWORD_SIGNUP: return waitPasswordSignUp(userData,message);
                case WAITING_ID: return waitUsername(userData,message);
                case WAITING_PASSWORD: return waitPassword(userData,message);
                default: return "Puedes intentar con alguno de estos comandos:\n/login\n/signUp";
            }

        }

        userData.setState(UserState.LOGIN);
        if(isCommand) {
            switch (commandsParts[0]) {
                case "/signUp": return signUp(userData);
                case "/login": return login(userData);
                case "/verPedidos":
                    return verPedidos(userData);
                case "/verUsuario":
                    return verUsuario(userData);
                case "/verPedido":
                    return verPedido(userData, commandsParts[1]);
                case "/crearPedido":
                    return crearPedido(userData);
                case "/logout":
                    return logout(userData);
                case "/compartirPedido":
                    return compartirPedido(userData);
                case "/agregarItemAPedido":
                    return agregarItemAPedido(userData);
                case "/cerrar":
                    return cerrar(userData);
                case "/cambiarNombreAPedido":
                    return cambiarNombrePedido(userData);
                case "/editarItem":
                    return editarItem(userData, commandsParts[1]);
                case "/sumarItem":
                    return sumarItem(userData);
                case "/restarItem":
                    return restarItem(userData);
                case "/cambiarDescipcionItem":
                    return cambiarDescipcionItem(userData);
                case "/cambiarUnidadItem":
                    return cambiarUnidadItem(userData);
                default:
                    return "El comando seleccionado no existe.";
            }
        }
        System.out.println(5);
        switch (actualState) {
            case WAITING_ORDER_NAME: return responseOrderName(userData,message);
            case WAITING_ITEM_UNIT: return responseItemUnit(userData,message);
            case WAITING_ITEM_QUANTITY:return responseItemQuantity(userData,message);
            case WAITING_ITEM_DESCRIPTION:return responseItemDescription(userData,message);
            case WAITING_SHARE_ID: return responseShareId(userData,message);
            case MOD_SUM_ITEM: return responseSumItem(userData,message);
            case MOD_DEC_ITEM: return responseDecItem(userData,message);
            case MOD_ITEM_DESC: return responseDescItem(userData,message);
            case MOD_ITEM_UNIT: return responseUnitItem(userData,message);
            }

        System.out.println(6);
        return "Por favor ingrese uno de los siguientes comandos: " +
                "\n/verPedidos" +
                "\n/verUsuario" +
                "\n/crearPedido" +
                "\n/logout";
    }


    private String responseDescItem(UserData user,String message){
        boolean response = new ItemsApi().putItemDescription(user.getToken(),user.getItemId(),message);
        if (response) return "Descripcion actualizada correctamente.";
        return "La descripcion no pudo ser actualizada.";
    }

    private String responseUnitItem(UserData user,String message){
        boolean response = new ItemsApi().putItemUnit(user.getToken(),user.getItemId(),message);
        if (response) return "Unidad actualizada correctamente.";
        return "La unidad no pudo ser actualizada.";
    }

    private String responseSumItem(UserData user,String message){
        int qty;
        try {
            qty = Integer.valueOf(message);
            if(qty<=0) throw  new Exception();
        } catch (Exception e){
            return "Esta no es una cantidad posible.";
        }
        boolean response = new ItemsApi().putItemModQty(user.getToken(),user.getItemId(),qty);
        if (response) return "Cantidad actualizada correctamente.";
        return "La cantidad no pudo ser actualizada.";
    }

    private String responseDecItem(UserData user,String message){
        int qty;
        try {
            qty = Integer.valueOf(message);
            if(qty<=0) throw  new Exception();
        } catch (Exception e){
            return "Esta no es una cantidad posible.";
        }
        boolean response = new ItemsApi().putItemModQty(user.getToken(),user.getItemId(),-qty);
        if (response) return "Cantidad actualizada correctamente.";
        return "La cantidad no pudo ser actualizada.";
    }

    private String cambiarDescipcionItem(UserData user){
        user.setState(UserState.MOD_ITEM_DESC);
        return "Introduzca el nuevo nombre del item:";
    }

    private String cambiarUnidadItem(UserData user){
        user.setState(UserState.MOD_ITEM_UNIT);
        return "Introduzca la nueva unidad del item:";
    }

    private String sumarItem(UserData user){
        user.setState(UserState.MOD_SUM_ITEM);
        return "Cuantas unidades desea sumar de este item?";
    }

    private String restarItem(UserData user){
        user.setState(UserState.MOD_DEC_ITEM);
        return "Cuantas unidades desea restar de este item?";
    }

    private String login(UserData user){
        System.out.println("Login command found.");
        user.setState(UserState.WAITING_ID);
        return "Por favor introduzca su username:";
    }

    private UserData getOrCreate(long chatId){
        UserData userData = userDatas.get(chatId);
        if(userData==null){
            userData = new UserData(chatId);
            userDatas.put(chatId,userData);
        }

        return userData;
    }


    private String signUp(UserData user){
        System.out.println("SignUp command found.");
        user.setState(UserState.WAITING_USERNAME_SIGNUP);
        return "Por favor introduzca un username:";
    }




    private String waitUsernameSignUp(UserData user,String message){
        user.setUsername(message);
        user.setState(UserState.WAITING_EMAIL_SIGNUP);
        return "Por favor introduzca un email";
    }

    private String waitEmailSignUp(UserData user,String message){
        user.setMail(message);
        user.setState(UserState.WAITING_PASSWORD_SIGNUP);
        return "Por favor introduzca una contraseña";
    }

    private String waitPasswordSignUp(UserData user,String message){
        String loginToken = new UserApi().userSignUp(user.getUsername(),user.getMail(),message);
        user.setState(UserState.LOGOUT);
        if(loginToken!=null){
            return "SignUp exitoso, ahora puedes iniciar sesion.";
        }
        return "El signUp no se pudo efectuar de forma correcta.";
    }

    private String waitUsername(UserData user,String message){
        user.setUsername(message);
        user.setState(UserState.WAITING_PASSWORD);
        return "Por favor introduzca su contraseña";
    }

    private String waitPassword(UserData user,String message){
        String loginToken = new UserApi().userLogin(user.getUsername(),message);
        if((loginToken!=null) && (loginToken!="") && (loginToken!=" ")){
            user.setToken(loginToken);
            user.setState(UserState.LOGIN);
            return "Login exitoso";
        }
        user.setState(UserState.LOGOUT);
        return "El usuario o contraseña es incorrecto.";
    }



    private String cerrar(UserData user){
        boolean response =new ApiOrders().closeOrderApi(user.getPedidoId(),user.getToken());
        if(response) return "Pedido cerrado";
        return "No se pudo cerrar el pedido";
    }

    private String responseItemQuantity(UserData user,String message){
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
        Boolean response = new ItemsApi().putItemUnit(user.getToken(),user.getItemId(),message);
        if(response){
            user.setState(UserState.WAITING_ITEM_QUANTITY);
            return "Unidad actualizada correctamente.\nPor favor escriba la cantidad deseada:";
        }
        return "La unidad no pudo ser actualizada.";
    }

    private String responseItemDescription(UserData user,String message){
        Boolean response = new ItemsApi().putItemDescription(user.getToken(),user.getItemId(),message);
        if(response){
            user.setState(UserState.WAITING_ITEM_UNIT);
            return "Descripcion actualizada correctamente.\nPor favor escriba la unidad deseada:";
        }
        //reset(chatId);
        return "La descripcion no pudo ser actualizada.";
    }

    private String agregarItemAPedido(UserData user){
        JsonNode respuesta= new ItemsApi().addItemApi(user.getPedidoId(),user.getToken());

        if(respuesta!=null){

            user.setItemId(respuesta.get("id").asText());
            user.setState(UserState.WAITING_ITEM_DESCRIPTION);
            return "Se a creado el item de forma correcta.\nElija una descripcion para el item:";
        }
        return "No puedes agregar items a este pedido.";
    }


    private String logout(UserData user){
        userDatas.remove(user.getChatId());
        return "Logout realizado.";
    }
    private String responseShareId(UserData user,String message){
        Boolean response = new ApiOrders().shareOrderApi(user.getPedidoId(),user.getToken(),message);
        if(response) return "Se a compartido el pedido con exito";
        return "No se a logrado compartir el pedido";
    }

    private String verUsuario(UserData user){
        //reset(chatId);
        JsonNode jsonNode = new UserApi().getUserById(user.getToken());

        String mssg="User id: "+jsonNode.get("id").asText()+ "\n User name: "+jsonNode.get("username").asText();

        return mssg;
    }

    private String verPedidos(UserData user){
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
    private String compartirPedido(UserData user){
        user.setState(UserState.WAITING_SHARE_ID);
        return "Introduzca el username del usuario a compartir";
    }

    private String editarItem(UserData user,String itemId){
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


    private String verPedido(UserData user,String pedidoId){
        JsonNode pedido =  new ApiOrders().getOrderById(pedidoId,user.getToken());

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
                pedidoText += "\n"+item.get("quantity").asText()+" ";

                String unit = item.get("unit").asText();
                if(unit!=null){
                    pedidoText += unit+" de ";
                }

                pedidoText+= item.get("description").asText();
                pedidoText+="\n/editarItem_"+item.get("id").asText();
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

    private String crearPedido(UserData user){
        //reset(chatId);
        JsonNode response = new ApiOrders().createOrdersByUserId(user.getToken());
        if(response!=null){
            user.setState(UserState.WAITING_ORDER_NAME);
            user.setPedidoId(response.get("id").asText());
            return "Orden creada con exito, elija un nombre para su orden:";
        }
        return "La orden no pudo ser creada con exito.";
    }


    private String cambiarNombrePedido(UserData user){
        JsonNode pedido =  new ApiOrders().getOrderById(user.getPedidoId(),user.getToken());

        if (pedido!=null){
            user.setState(UserState.WAITING_ORDER_NAME);
            return "Introduce el nuevo nombre del pedido";
        }
        return "No puedes cambiarle el nombre a este pedido";

    }



    private String responseOrderName(UserData user,String message){
        Boolean response = new ApiOrders().patchOrderName(user.getToken(),user.getPedidoId(),message);
        if(response){
            return "Nombre actualizado correctamente";
        }
        return "El nombre no pudo ser actualizado.";
    }



}
