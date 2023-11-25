package com.example.telegrambot;

import com.example.telegrambot.api.MonitorApi;
import com.example.telegrambot.conversationThreads.ItemManage;
import com.example.telegrambot.conversationThreads.PedidosManage;
import com.example.telegrambot.conversationThreads.UserManage;
import com.fasterxml.jackson.databind.JsonNode;

public class MessageHandler {

    private ItemManage itemManage;
    private PedidosManage pedidosManage;
    private UserManage userManage;

    public MessageHandler() {
        this.itemManage = new ItemManage();
        this.pedidosManage = new PedidosManage();
        this.userManage = new UserManage();
    }

    public String handle(long chatId, String message) {

        System.out.println("Processing message " + message);
        String[] command = message.split(" ");
        System.out.println("Processing command " + command[0]);

        UserData userData = userManage.getOrCreate(chatId);
        String[] commandsParts = command[0].split("_");
        UserState actualState = userData.getState();
        boolean isCommand = message.startsWith("/");

        if (!userData.isLogin()) {
            userData.setState(UserState.LOGOUT);

            if (isCommand) {
                return switch (command[0]) {
                    case "/stats" -> showStats();
                    case "/start" -> userManage.defaultMessage(userData, start());
                    case "/signUp" -> userManage.signUp(userData);
                    case "/logIn" -> userManage.login(userData);
                    case "/verComandos" -> userManage.verComandos(userData);
                    default -> userManage.defaultMessage(userData, "Comando no reconocido.");
                };
            }

            return switch (actualState) {
                case WAITING_USERNAME_SIGNUP -> userManage.waitUsernameSignUp(userData, message);
                case WAITING_EMAIL_SIGNUP -> userManage.waitEmailSignUp(userData, message);
                case WAITING_PASSWORD_SIGNUP -> userManage.waitPasswordSignUp(userData, message);
                case WAITING_ID -> userManage.waitUsername(userData, message);
                case WAITING_PASSWORD -> userManage.waitPassword(userData, message);
                default -> userManage.defaultMessage(userData, "No has iniciado sesion aun.");
            };

        }

        userData.setState(UserState.LOGIN);
        if (isCommand) {
            return switch (commandsParts[0]) {
                case "/stats" -> showStats();
                case "/start" -> userManage.defaultMessage(userData, start());
                case "/logOut" -> userManage.logout(userData);
                case "/signUp" -> userManage.signUp(userData);
                case "/logIn" -> userManage.login(userData);
                case "/verUsuario" -> userManage.verUsuario(userData);
                case "/verPedidos", "/volverAPedidos" -> pedidosManage.verPedidos(userData);
                case "/verPedido" -> pedidosManage.verPedido(userData, commandsParts[1]);
                case "/volverAPedido" -> pedidosManage.verPedido(userData, userData.getPedidoId());
                case "/crearPedido" -> pedidosManage.crearPedido(userData);
                case "/editarItem" -> itemManage.editarItem(userData, commandsParts[1]);
                case "/compartirPedido" -> pedidosManage.compartirPedido(userData);
                case "/cerrarPedido" -> pedidosManage.cerrar(userData);
                case "/abrirPedido" -> pedidosManage.abrir(userData);
                case "/cambiarNombreAPedido" -> pedidosManage.cambiarNombrePedido(userData);
                case "/agregarItemAPedido" -> itemManage.agregarItemAPedido(userData);
                case "/sumarItem" -> itemManage.sumarItem(userData);
                case "/restarItem" -> itemManage.restarItem(userData);
                case "/cambiarDescipcionItem" -> itemManage.cambiarDescipcionItem(userData);
                case "/cambiarUnidadItem" -> itemManage.cambiarUnidadItem(userData);
                case "/verComandos" -> userManage.verComandos(userData);
                case "/borrarPedido" -> pedidosManage.borrarPedido(userData);
                case "/borrarItem" -> itemManage.borrarItem(userData);
                default -> userManage.defaultMessage(userData, "El comando seleccionado no existe.");
            };
        }

        return switch (actualState) {
            case WAITING_ORDER_NAME -> pedidosManage.responseOrderName(userData, message);
            case WAITING_SHARE_ID -> pedidosManage.responseShareId(userData, message);
            case WAITING_ITEM_UNIT -> itemManage.responseItemUnit(userData, message);
            case WAITING_ITEM_QUANTITY -> itemManage.responseItemQuantity(userData, message);
            case WAITING_ITEM_DESCRIPTION -> itemManage.responseItemDescription(userData, message);
            case MOD_SUM_ITEM -> itemManage.responseSumItem(userData, message);
            case MOD_DEC_ITEM -> itemManage.responseDecItem(userData, message);
            case MOD_ITEM_DESC -> itemManage.responseDescItem(userData, message);
            case MOD_ITEM_UNIT -> itemManage.responseUnitItem(userData, message);
            default -> userManage.defaultMessage(userData, "Perdona, no puedo entenderte.");
        };
    }

    private String start() {
        String startMessage = "Bienvenido a Empanadas APP!\nLa mejor aplicacion para hacer organizar tus pedidos.\n\n";
        startMessage += showStats();
        return startMessage;
    }

    public String showStats() {
        JsonNode stats = new MonitorApi().getMonitor();
        String userCount = stats.get("userCount").asText();
        String orderCount = stats.get("orderCount").asText();
        String response = "";
        response += "Usuarios activos: " + userCount + "\n";
        response += "Pedidos creados: " + orderCount + "\n";
        return response;
    }

}
