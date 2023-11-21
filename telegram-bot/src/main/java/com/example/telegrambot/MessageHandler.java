package com.example.telegrambot;

import com.example.telegrambot.api.ApiOrders;
import com.example.telegrambot.api.ItemsApi;
import com.example.telegrambot.api.MonitorApi;
import com.example.telegrambot.api.UserApi;
import com.example.telegrambot.conversationThreads.ItemManage;
import com.example.telegrambot.conversationThreads.PedidosManage;
import com.example.telegrambot.conversationThreads.UserManage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;

import java.util.HashMap;
public class MessageHandler {

    private ItemManage itemManage;
    private PedidosManage pedidosManage;
    private UserManage userManage;

    public MessageHandler(){
        this.itemManage = new ItemManage();
        this.pedidosManage = new PedidosManage();
        this.userManage = new UserManage();
    }

    public String handle(long chatId,String message){
        System.out.println("Processing message " + message);

        String[] command = message.split(" ");

        System.out.println("Processing command " + command[0]);

        UserData userData = userManage.getOrCreate(chatId);

        String[] commandsParts = command[0].split("_");

        UserState actualState = userData.getState();

        Boolean isCommand = message.startsWith("/");

        if(!userData.isLogin()){
            userData.setState(UserState.LOGOUT);

            if(isCommand){
                switch (command[0]){
                    case "/stats": return showStats();
                    case "/start": return userManage.defaultMessage(userData,start());
                    case "/signUp": return userManage.signUp(userData);
                    case "/logIn": return userManage.login(userData);
                    case "/verComandos": return  userManage.verComandos(userData);
                    default: return userManage.defaultMessage(userData,"Comando no reconocido.");
                }
            }
            switch (actualState){
                case WAITING_USERNAME_SIGNUP: return userManage.waitUsernameSignUp(userData,message);
                case WAITING_EMAIL_SIGNUP: return userManage.waitEmailSignUp(userData,message);
                case WAITING_PASSWORD_SIGNUP: return userManage.waitPasswordSignUp(userData,message);
                case WAITING_ID: return userManage.waitUsername(userData,message);
                case WAITING_PASSWORD: return userManage.waitPassword(userData,message);
                default: return userManage.defaultMessage(userData,"No has iniciado sesion aun.");
            }

        }

        userData.setState(UserState.LOGIN);
        if(isCommand) {
            switch (commandsParts[0]) {
                case "/stats":
                    return showStats();
                case "/start":
                    return userManage.defaultMessage(userData,start());
                case "/logOut":
                    return userManage.logout(userData);
                case "/signUp":
                    return userManage.signUp(userData);
                case "/logIn":
                    return userManage.login(userData);
                case "/verUsuario":
                    return userManage.verUsuario(userData);
                case "/verPedidos","/volverAPedidos":
                    return pedidosManage.verPedidos(userData);
                case "/verPedido":
                    return pedidosManage.verPedido(userData, commandsParts[1]);
                case "/volverAPedido":
                    return pedidosManage.verPedido(userData, userData.getPedidoId());
                case "/crearPedido":
                    return pedidosManage.crearPedido(userData);
                case "/editarItem":
                    return itemManage.editarItem(userData, commandsParts[1]);
                case "/compartirPedido":
                    return pedidosManage.compartirPedido(userData);
                case "/cerrarPedido":
                    return pedidosManage.cerrar(userData);
                case "/abrirPedido":
                    return pedidosManage.abrir(userData);
                case "/cambiarNombreAPedido":
                    return pedidosManage.cambiarNombrePedido(userData);
                case "/agregarItemAPedido":
                    return itemManage.agregarItemAPedido(userData);
                case "/sumarItem":
                    return itemManage.sumarItem(userData);
                case "/restarItem":
                    return itemManage.restarItem(userData);
                case "/cambiarDescipcionItem":
                    return itemManage.cambiarDescipcionItem(userData);
                case "/cambiarUnidadItem":
                    return itemManage.cambiarUnidadItem(userData);
                case "/verComandos":
                    return userManage.verComandos(userData);
                case "/borrarPedido":
                    return pedidosManage.borrarPedido(userData);
                case "/borrarItem":
                    return itemManage.borrarItem(userData);
                default:
                    return userManage.defaultMessage(userData,"El comando seleccionado no existe.");
            }
        }


        switch (actualState) {
            case WAITING_ORDER_NAME:
                return pedidosManage.responseOrderName(userData,message);
            case WAITING_SHARE_ID:
                return pedidosManage.responseShareId(userData,message);
            case WAITING_ITEM_UNIT:
                return itemManage.responseItemUnit(userData,message);
            case WAITING_ITEM_QUANTITY:
                return itemManage.responseItemQuantity(userData,message);
            case WAITING_ITEM_DESCRIPTION:
                return itemManage.responseItemDescription(userData,message);
            case MOD_SUM_ITEM:
                return itemManage.responseSumItem(userData,message);
            case MOD_DEC_ITEM:
                return itemManage.responseDecItem(userData,message);
            case MOD_ITEM_DESC:
                return itemManage.responseDescItem(userData,message);
            case MOD_ITEM_UNIT:
                return itemManage.responseUnitItem(userData,message);
            default:
                return userManage.defaultMessage(userData,"Perdona, no puedo entenderte.");
            }
    }

    private String start(){
        String startMessage = "Bienvenido a Empanadas APP!\nLa mejor aplicacion para hacer organizar tus pedidos.\n\n";
        startMessage+=showStats();
        return startMessage;
    }

    public String showStats(){
        JsonNode stats = new MonitorApi().getMonitor();
        String userCount = stats.get("userCount").asText();
        String orderCount = stats.get("orderCount").asText();
        String response = "";
        response+="Usuarios activos: "+userCount+"\n";
        response+="Pedidos creados: "+orderCount+"\n";
        return response;
    }





}
