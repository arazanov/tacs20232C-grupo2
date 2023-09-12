package com.example.telegrambot.handles;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramHandle extends TelegramLongPollingBot{

    @Override
    public void onUpdateReceived(Update update){

        final String messageReceived = update.getMessage().getText();

        System.out.println("Received message " + messageReceived);



    }
    @Override
    public  String getBotUsername(){
        return "PedidosCompartidosbot";
    }
    @Override
    public  String getBotToken(){
        return "6474434866:AAH4dMSOvG3M-yrquvykVh_pZxKeJASvQqo";
    }

}
