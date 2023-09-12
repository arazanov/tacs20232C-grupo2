package com.example.telegrambot.services;

import com.example.telegrambot.handles.TelegramHandle;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
public class TelegramBotService {
    public TelegramBotService(){
        try {
            TelegramHandle myHandle = new TelegramHandle();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(myHandle);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
