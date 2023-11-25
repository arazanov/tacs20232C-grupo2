package com.example.telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PedidosBot extends TelegramLongPollingBot {

    MessageHandler messageHandler;
    public String BOT_TOKEN;
    public String BOT_USERNAME;

    public PedidosBot(String bot_token, String bot_username) {
        this.BOT_TOKEN = bot_token;
        this.BOT_USERNAME = bot_username;
        this.messageHandler = new MessageHandler();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message mssg = update.getMessage();
            long chatId = mssg.getChatId();
            String message = mssg.getText();
            System.out.println("Message received: " + message);
            String response = messageHandler.handle(chatId, message);
            System.out.println("Message sent: " + response);
            if (response != null && !response.isEmpty()) sendMessage(chatId, response);
        }
    }

    @Override
    public String getBotUsername() {
        return this.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return this.BOT_TOKEN;
    }

    private void sendMessage(long chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}