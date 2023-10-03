package com.example.telegrambot.services;

import com.example.telegrambot.PedidosBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
public class TelegramBotService {
    public TelegramBotService(){
        try {
            PedidosBot pedidosBot = new PedidosBot();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(pedidosBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
