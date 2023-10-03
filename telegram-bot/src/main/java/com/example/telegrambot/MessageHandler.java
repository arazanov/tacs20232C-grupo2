package com.example.telegrambot;

import com.example.telegrambot.api.UserApi;
import com.fasterxml.jackson.databind.JsonNode;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Locale;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;
public class MessageHandler {

    private final SilentSender silentSender;
    private final HashMap<Long,Long> userSessions = new HashMap<>();
    private final HashMap<Long,UserState> userState = new HashMap<>();

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
    }
}
