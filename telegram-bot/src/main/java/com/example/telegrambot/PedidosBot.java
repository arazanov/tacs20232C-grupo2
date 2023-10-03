package com.example.telegrambot;

import org.springframework.core.env.Environment;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

public class PedidosBot extends TelegramLongPollingBot {

    MessageHandler messageHandler;
    public static String BOT_TOKEN = "6474434866:AAH4dMSOvG3M-yrquvykVh_pZxKeJASvQqo";
    public static String BOT_USERNAME  = "PedidosCompartidosbot";

    @Override
    public void onUpdateReceived(Update update){
        if (update.hasMessage()){
            Message mssg = update.getMessage();
            long chatId = mssg.getChatId();
            String message = mssg.getText();
            System.out.println("Message received: "+message);
            String response = messageHandler.handle(chatId,message);
            System.out.println("Message sent: "+response);
            if(response!=null && response!="") sendMessage(chatId,response);
        }
    }
    public PedidosBot(){
        this.messageHandler = new MessageHandler();
    }
    @Override
    public String getBotUsername(){
        return this.BOT_USERNAME;
    }

    @Override
    public String getBotToken(){
        return this.BOT_TOKEN;
    }

    private void sendMessage(long chatId,String message)  {
        try {
            SendMessage sendMessage = new SendMessage(String.valueOf(chatId),message);
            execute(sendMessage);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}